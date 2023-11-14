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
import java.util.List;
import java.util.Map;
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
                try {
                    beforeDate = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                String afterDate = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                resultList.add(String.format("%s -> %s", beforeDate, afterDate));
            }
        }
        return resultList.toString();
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
                try {
                    beforeAssignee = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
                } catch (NullPointerException ignored) {}
                String afterAssignee = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                resultList.add(String.format("%s -> %s", beforeAssignee, afterAssignee));
            }
        }
        return resultList.toString();
    }

    public Map<String, String> getAllChangeStatus(TaskHistoryDto history) {
        Map<String, String> resultList = new HashMap<>();
        var result = history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "status".equalsIgnoreCase(content.getChanges().get(0).getCode()))
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
        return getChangeStatus(result, resultList, null, timestamp);
    }

    private Map<String, String> getChangeStatus(List<TaskHistoryDto.Content> contentList, Map<String, String> resultList, String status, String timestamp) {
        if (contentList.isEmpty()) {
            resultList.put("", "");
            return resultList;
        } else {
            for (TaskHistoryDto.Content content : contentList) {
                if (status == null) {
                    status = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
                }
                if (status.equalsIgnoreCase(content.getChanges().get(0).getBefore().getValues().get(0).getValue())) {
                    String timestampContent = content.getTimestamp();
                    String afterStatus = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                    content.getChanges().get(0).getBefore().getValues().get(0).setValue("");
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
