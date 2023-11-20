package ru.vtb.asaf.sfera.service;

import lombok.var;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vtb.asaf.sfera.dto.TaskHistoryDto;
import ru.vtb.asaf.sfera.util.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TaskHistoryService {

    RestTemplate restTemplate = new RestTemplate();

    public TaskHistoryDto getHistoryInfo(HttpEntity<String> requestEntity, String taskName) {

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.HISTORY_GET_URL)
                .queryParam("size", "100")
                .queryParam("page","0")
                .queryParam("sort","createDate,asc")
                .build();
        Map<String, String> variables = new HashMap<>();
        variables.put("taskName", taskName);
        ResponseEntity<TaskHistoryDto> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, requestEntity, TaskHistoryDto.class, variables);

//        System.out.println(taskName);
        return responseEntity.getBody();
    }

    public String getAllChangeDueDate(TaskHistoryDto history) {
        List<String> resultList = new ArrayList<>();
        var result = history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "dueDate".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            resultList.add("");
            return resultList.toString();
        } else {
            for (TaskHistoryDto.Content content : result) {
                String beforeDate = "null";
                String afterDate = "null";
                try {
                    beforeDate = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                try {
                    afterDate = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                resultList.add(String.format("%s -> %s", beforeDate, afterDate));
            }
        }
        return resultList.toString();
    }

    public String getEndDate(TaskHistoryDto history) {
        return history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "status".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .filter(content -> "closed".equalsIgnoreCase(content.getChanges().get(0).getAfter().getValues().get(0).getValue()))
                .map(TaskHistoryDto.Content::getTimestamp)
                .findFirst()
                .orElse("");
    }

    public String getAllChangeAssignee(TaskHistoryDto history) {
        List<String> resultList = new ArrayList<>();
        var result = history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "assignee".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            resultList.add("");
            return resultList.toString();
        } else {
            for (TaskHistoryDto.Content content : result) {
                String beforeAssignee = "null";
                String afterAssignee = "null";
                try {
                    beforeAssignee = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                try {
                    afterAssignee = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                resultList.add(String.format("%s -> %s", beforeAssignee, afterAssignee));
            }
        }
        return resultList.toString();
    }

    public Map<String, String> getAllChangeStatus(TaskHistoryDto history, String currentStatus) {
        Map<String, String> resultList = new HashMap<>();
        Set<String> contentFilter = new HashSet<>();
        contentFilter.add("status");
        contentFilter.add("resolution");
        var result = history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> contentFilter.contains(content.getChanges().get(0).getCode()))
//                .filter(content -> "status".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
//        System.out.println(result);
        List<TaskHistoryDto.Content> contentList = history.getContent().stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
        String timestamp = null;
        if (!contentList.isEmpty()) {
            timestamp = contentList.get(0).getTimestamp();
        }

        Map<String, String> resultMap = getChangeStatus(result, resultList, null, timestamp);
        return getFinalStatus(result, resultMap, currentStatus);
    }

    private Map<String, String> getFinalStatus(List<TaskHistoryDto.Content> contentList,
                                               Map<String, String> resultList,
                                               String currentStatus) {
        if (contentList.isEmpty()) {
            resultList.put("", "");
            return resultList;
        } else {
            for (int i = contentList.size() - 1; i >=0; i--) {
                if (currentStatus.equalsIgnoreCase(contentList.get(i).getChanges()
                        .stream()
                        .filter(change -> "status".equalsIgnoreCase(change.getCode()))
                        .map(change -> change.getAfter().getValues().get(0).getValue())
                        .findFirst().orElse(""))) {
                    String timestampContent = contentList.get(contentList.size() - 1).getTimestamp();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date now = new Date();
                    String currentDate = dateFormat.format(now);
                    resultList.put(currentStatus, String.format("%s -> %s за %s дней", currentStatus, "now", getCompareDate(currentDate, timestampContent)));
                    break;
                }
            }
        }
        return resultList;
    }

    private Map<String, String> getChangeStatus(List<TaskHistoryDto.Content> contentList,
                                                Map<String, String> resultList,
                                                String status,
                                                String timestamp) {
        if (contentList.isEmpty()) {
            resultList.put("", "");
            return resultList;
        } else {
            for (TaskHistoryDto.Content content : contentList) {
                if (status == null) {
                    status = content.getChanges().stream()
                            .filter(change -> "status".equalsIgnoreCase(change.getCode()))
                            .map(change -> change.getBefore().getValues().get(0).getValue())
                            .findFirst().orElse("");
                }
                if (status.equalsIgnoreCase(content.getChanges()
                        .stream()
                        .filter(change -> "status".equalsIgnoreCase(change.getCode()))
                        .map(change -> change.getBefore().getValues().get(0).getValue())
                        .findFirst().orElse(""))) {
                    String timestampContent = content.getTimestamp();
                    String afterStatus = content.getChanges()
                            .stream()
                            .filter(change -> "status".equalsIgnoreCase(change.getCode()))
                            .map(change -> change.getAfter().getValues().get(0).getValue())
                            .findFirst().orElse("unknown");
                    content.getChanges()
                            .stream()
                            .filter(change -> "status".equalsIgnoreCase(change.getCode()))
                            .forEach(change -> change.getBefore().getValues().get(0).setValue(""));
                    resultList.put(status, String.format("%s -> %s за %s дней", status, afterStatus, getCompareDate(timestampContent, timestamp)));
//                    System.out.printf("%s -> %s за %s дней\n", status, afterStatus, getCompareDate(timestampContent, timestamp));
                    getChangeStatus(contentList, resultList, afterStatus, timestampContent);
                }
            }
        }
        return resultList;
    }

    private long getCompareDate(String dateNowStr, String dateOldStr){
        if (dateOldStr == null) {
            return 0L;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dateNow = dateFormat.parse(dateNowStr);
            Date dateOld = dateFormat.parse(dateOldStr);
            long diff = dateNow.getTime() - dateOld.getTime();
//            System.out.println(String.format("Now date: %s | Old date %s", dateNow, dateOld));
//            System.out.println(TimeUnit.MILLISECONDS.toDays(diff));
            return TimeUnit.MILLISECONDS.toDays(diff);
        } catch (ParseException e) {
            System.err.println("Не удалось распарсить дату");
        }
        return 0L;
    }

}
