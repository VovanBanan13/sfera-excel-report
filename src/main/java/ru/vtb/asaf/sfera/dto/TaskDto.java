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
    private List<Label> label;
    private List<Component> component;
    private Person assignee;
    private Person owner;
    private List<CustomFieldsValues> customFieldsValues;
    private String createDate;
    private String updateDate;
    private String dueDate;
    private Integer estimation;
    private List<Worklog> worklog;
    private List<RelatedEntity> relatedEntities;
    private String type;
    private String name;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Label {
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Component {
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedEntity {
        private Entity entity;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Entity {
            private String number;
            private String type;
        }
    }

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Worklog {
        private Integer spent;
    }
}
