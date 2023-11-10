package ru.vtb.asaf.sfera.mapper;

import ru.vtb.asaf.sfera.dto.TaskDto;
import ru.vtb.asaf.sfera.dto.TaskReportDto;

import java.util.stream.Collectors;

public final class TaskReportMapper {

    private static final String STREAM_CONSUMER = "streamConsumer";
    private static final String STREAM_EXECUTOR = "streamExecutor";

    public static TaskReportDto toTaskReport(
            TaskDto task,
            String statusHistory,
            String projectConsumer,
            String epicNumber
    ) {
        String label = getLabel(task);
        String component = getComponent(task);
        String assignee = getAssignee(task);
        String owner = getOwner(task);

        return TaskReportDto.builder()
                .number(getValueNotNull(task.getNumber()))
                .status(getValueNotNull(task.getStatus()))
                .label(label)
                .component(component)
                .assignee(assignee)
                .owner(owner)
                .streamConsumer(getValueNotNull(task.getCustomFieldsValues()
                        .stream()
                        .filter(f -> STREAM_CONSUMER.equals(f.getCode()))
                        .map(TaskDto.CustomFieldsValues::getValue)
                        .findFirst()
                        .orElse(null)))
                .streamExecutor(getValueNotNull(task.getCustomFieldsValues()
                        .stream()
                        .filter(f -> STREAM_EXECUTOR.equals(f.getCode()))
                        .map(TaskDto.CustomFieldsValues::getValue)
                        .findFirst()
                        .orElse(null)))
                .projectConsumer(projectConsumer)
                .createDate(getValueNotNull(task.getCreateDate()))
                .updateDate(getValueNotNull(task.getUpdateDate()))
                .dueDate(getValueNotNull(task.getDueDate()))
                .statusHistory(statusHistory)
                .type(getValueNotNull(task.getType()))
                .name(getValueNotNull(task.getName()))
                .epic(epicNumber)
                .build();
    }

    private static String getLabel(TaskDto task) {
        if (task.getLabel() == null) {
            return "";
        } else {
            return task.getLabel()
                    .stream()
                    .map(TaskDto.Label::getName)
                    .collect(Collectors.toList())
                    .toString();
        }
    }

    private static String getComponent(TaskDto task) {
        if (task.getComponent() == null) {
            return "";
        } else {
            return task.getComponent()
                    .stream()
                    .map(TaskDto.Label::getName)
                    .collect(Collectors.toList())
                    .toString();
        }
    }

    private static String getAssignee(TaskDto task) {
        String assignee;
        if (task.getAssignee() == null) {
            assignee = "";
        } else {
            assignee = String.format("%s %s %s",
                    getValueNotNull(task.getAssignee().getLastName()),
                    getValueNotNull(task.getAssignee().getFirstName()),
                    getValueNotNull(task.getAssignee().getPatronymic()));
        }
        return assignee;
    }

    private static String getOwner(TaskDto task) {
        String owner;
        if (task.getOwner() == null) {
            owner = "";
        } else {
            owner = String.format("%s %s %s",
                    getValueNotNull(task.getOwner().getLastName()),
                    getValueNotNull(task.getOwner().getFirstName()),
                    getValueNotNull(task.getOwner().getPatronymic()));
        }
        return owner;
    }

    private static String getValueNotNull(String value) {
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
}
