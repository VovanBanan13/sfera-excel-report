package ru.vtb.asaf.sfera.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vtb.asaf.sfera.dto.AuthRes;
import ru.vtb.asaf.sfera.dto.GlobalTaskDto;
import ru.vtb.asaf.sfera.dto.QueryDto;
import ru.vtb.asaf.sfera.dto.TaskDto;
import ru.vtb.asaf.sfera.dto.TaskReportDto;
import ru.vtb.asaf.sfera.mapper.TaskReportMapper;
import ru.vtb.asaf.sfera.util.Constant;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TaskService {

    RestTemplate restTemplate = new RestTemplate();
    QueryService queryService = new QueryService();


    public List<TaskReportDto> getTasksReport(AuthRes authRes) throws URISyntaxException {
        HttpEntity<String> requestEntity = getRequestEntity(authRes);

        return getTasksReport(requestEntity, getTasksByFilter(requestEntity));
    }

    private HttpEntity<String> getRequestEntity(AuthRes authRes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String cookie = String.format("ACCESS_TOKEN=%s; CHECK_AUTH=true; REFRESH_TOKEN=%s", authRes.getAccess_token(), authRes.getRefresh_token());
        headers.set("Cookie", cookie);
        return new HttpEntity<>(headers);
    }

//    private List<String> getTasksByFilter(HttpEntity<String> requestEntity) throws URISyntaxException {
//        List<String> taskNameList = new ArrayList<>();
//        QueryDto queryDto = queryService.createQuery();
//        String sizePage = String.format("&size=%d", queryDto.getSize());
//        String finalUrl = Constant.TASKS_MANUAL_FILTER_GET_URL + queryDto.getQuery() + sizePage + Constant.URL_PAGEABLE;
//        ResponseEntity<GlobalTaskDto> responseEntity = restTemplate.exchange(new URI(finalUrl), HttpMethod.GET, requestEntity, GlobalTaskDto.class);
//
//        if (responseEntity.getBody() != null) {
////            System.out.print(responseEntity.getBody());
//            responseEntity.getBody().getContent().forEach(task -> taskNameList.add(task.getNumber()));
//        }
//        System.out.println("Количество задач всего: " + responseEntity.getBody().getTotalElements());
//        return taskNameList;
//    }

    private List<String> getTasksByFilter(HttpEntity<String> requestEntity) throws URISyntaxException {
        List<String> taskNameList = new ArrayList<>();
        QueryDto queryDto = queryService.createQuery();

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(Constant.TASKS_GET_URL)
                .queryParam("query", queryDto.getQuery())
                .queryParam("size", queryDto.getSize())
                .queryParam("page","0")
                .queryParam("attributesToReturn","number,name,actualSprint,priority,status,estimation,spent,assignee,owner,dueDate,updateDate,createDate")
                .build();

        ResponseEntity<GlobalTaskDto> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, GlobalTaskDto.class);

        if (responseEntity.getBody() != null) {
            responseEntity.getBody().getContent().forEach(task -> taskNameList.add(task.getNumber()));
        }
        System.out.println("Количество задач всего: " + responseEntity.getBody().getTotalElements());
        return taskNameList;
    }

    private List<TaskReportDto> getTasksReport(HttpEntity<String> requestEntity, List<String> taskNameList) throws URISyntaxException {
        List<TaskReportDto> taskReportDtoList = new ArrayList<>();
        for (String taskName : taskNameList) {
            taskReportDtoList.add(getTaskInfo(requestEntity, taskName));
        }
        return taskReportDtoList;
    }

    private TaskReportDto getTaskInfo(HttpEntity<String> requestEntity, String taskName) throws URISyntaxException {
        ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(new URI(Constant.TASK_GET_URL+taskName), HttpMethod.GET, requestEntity, TaskDto.class);
//        TaskDto responseEntity = restTemplate.getForObject(new URI(Constant.TASK_GET_URL+taskName), TaskDto.class);
//        System.out.println(responseEntity.getBody());
        if (responseEntity.getBody() != null) {
//            System.out.println(TaskReportMapper.toTaskReport(responseEntity.getBody()));
            return TaskReportMapper.toTaskReport(responseEntity.getBody());
        }
        return null;
    }
}
