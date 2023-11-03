package ru.vtb.asaf.sfera.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vtb.asaf.sfera.dto.TaskHistoryDto;
import ru.vtb.asaf.sfera.util.Constant;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskHistoryService {

    RestTemplate restTemplate = new RestTemplate();

    public void getHistoryInfo(HttpEntity<String> requestEntity, String taskName) throws URISyntaxException {

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.HISTORY_GET_URL)
                .queryParam("size", "100")
                .queryParam("page","0")
                .build();
        Map<String, String> variables = new HashMap<>();
        variables.put("taskName", taskName);
        ResponseEntity<TaskHistoryDto> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, TaskHistoryDto.class, variables);
    }
}
