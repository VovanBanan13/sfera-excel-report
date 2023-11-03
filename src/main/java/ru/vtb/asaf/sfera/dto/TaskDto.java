package ru.vtb.asaf.sfera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String number;
    private String status;
//    private List<String> label; //TODO: не список String
    private Person assignee;
    private Person owner;
    private List<CustomFieldsValues> customFieldsValues; //TODO Фильтровать по 'Стрим-заказчик' и 'Стрим-исполнитель'
    private String createDate;
    private String updateDate;
    private String dueDate;

    private String type;
    private String name;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Person {
        private String login;
        private String firstName;
        private String lastName;
        private String patronymic;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomFieldsValues {
        private String code;
        private String name;
        private String value;
    }
}
