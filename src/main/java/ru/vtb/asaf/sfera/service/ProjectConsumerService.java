package ru.vtb.asaf.sfera.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vtb.asaf.sfera.dto.ProjectConsumerDto;
import ru.vtb.asaf.sfera.dto.TaskDto;
import ru.vtb.asaf.sfera.util.Constant;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectConsumerService {

    RestTemplate restTemplate = new RestTemplate();

    private static final String PROJECT_CONSUMER = "projectConsumer";

    public String getProjectConsumerName(HttpEntity<String> requestEntity, TaskDto task) {
        String projectConsumerId = getValueNotNull(task.getCustomFieldsValues()
                .stream()
                .filter(f -> PROJECT_CONSUMER.equals(f.getCode()))
                .map(TaskDto.CustomFieldsValues::getValue)
                .findFirst()
                .orElse(null));
        if ("".equalsIgnoreCase(projectConsumerId)) {
            return "";
        }
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.PROJECT_CONSUMER_GET_URL)
                .queryParam("type","short")
                .build();
        Map<String, String> variables = new HashMap<>();
        variables.put("projectConsumer", projectConsumerId);
        ResponseEntity<ProjectConsumerDto> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, requestEntity, ProjectConsumerDto.class, variables);
        if (responseEntity.getBody() != null) {
            return getValueNotNull(responseEntity.getBody().getCode()) + getValueNotNull(responseEntity.getBody().getName());
        } else {
            return "";
        }
    }

    public String getProjectConsumerName(HttpEntity<String> requestEntity, String taskName) throws URISyntaxException {
        if (!taskName.isEmpty()) {
            ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(new URI(Constant.TASK_GET_URL+taskName), HttpMethod.GET, requestEntity, TaskDto.class);
            if (responseEntity.getBody() != null) {
                return getProjectConsumerName(requestEntity, responseEntity.getBody());
            }
        }
        return "";
    }

    private static String getValueNotNull(String value) {
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }

}
