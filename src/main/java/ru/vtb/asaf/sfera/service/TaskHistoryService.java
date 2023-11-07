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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskHistoryService {

    RestTemplate restTemplate = new RestTemplate();

    public TaskHistoryDto getHistoryInfo(HttpEntity<String> requestEntity, String taskName) throws URISyntaxException {

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.HISTORY_GET_URL)
                .queryParam("size", "100")
                .queryParam("page","0")
                .build();
        Map<String, String> variables = new HashMap<>();
        variables.put("taskName", taskName);
        ResponseEntity<TaskHistoryDto> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, requestEntity, TaskHistoryDto.class, variables);

        System.out.println(taskName);
        return responseEntity.getBody();
    }

    public void getAllChangeStatus(TaskHistoryDto history) {
        var result = history.getContent()
                .stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "status".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
        System.out.println(result);
        List<TaskHistoryDto.Content> contentList = history.getContent().stream()
                .filter(content -> !content.getChanges().isEmpty())
                .filter(content -> "".equalsIgnoreCase(content.getChanges().get(0).getCode()))
                .collect(Collectors.toList());
        String timestamp = null;
        if (!contentList.isEmpty()) {
            timestamp = contentList.get(0).getTimestamp();
        }
        getChangeStatus(result, "created", timestamp);
    }

    public void getChangeStatus(List<TaskHistoryDto.Content> contentList, String status, String timestamp) {
        for (TaskHistoryDto.Content content : contentList) {

            if (status.equalsIgnoreCase(content.getChanges().get(0).getBefore().getValues().get(0).getValue())) {
                String timestampContent = content.getTimestamp();
                String afterStatus = content.getChanges().get(0).getAfter().getValues().get(0).getValue();
                content.getChanges().get(0).getBefore().getValues().get(0).setValue("");
                System.out.printf("%s -> %s за %s - %s\n", status, afterStatus, timestampContent, timestamp);
                getChangeStatus(contentList, afterStatus, timestampContent);
            }
        }

//        getChangeStatus(contentList, contentList.get(0).getChanges().get(0).getBefore().getValues().get(0).getValue(), null);

    }

    private void getTimeStatus(TaskHistoryDto.Content content, String status) {
        String beforeStatus = content.getChanges().get(0).getBefore().getValues().get(0).getValue();
    }
}
