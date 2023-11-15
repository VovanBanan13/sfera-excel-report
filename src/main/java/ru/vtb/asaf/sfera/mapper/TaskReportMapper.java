package ru.vtb.asaf.sfera.mapper;

import ru.vtb.asaf.sfera.dto.TaskDto;
import ru.vtb.asaf.sfera.dto.TaskReportDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class TaskReportMapper {

    private static final String STREAM_CONSUMER = "streamConsumer";
    private static final String STREAM_EXECUTOR = "streamExecutor";

    public static TaskReportDto toTaskReport(
            TaskDto task,
            Map<String, String> statusHistory,
            String dueDateHistory,
            String endDate,
            String assigneeHistory,
            String projectConsumer,
            String epicNumber,
            String taskInEpic
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
                .assigneeHistory(assigneeHistory)
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
                .endDate(endDate)
                .dueDate(getValueNotNull(task.getDueDate()))
                .dueDateHistory(dueDateHistory)
                .statusHistory(getDateTimeAfterCreate(statusHistory.toString(), getValueNotNull(task.getCreateDate())))
                .statusCreated(getNumber(getValueNotNull(statusHistory.get("created"))))
                .statusInProgress(getNumber(getValueNotNull(statusHistory.get("inProgress"))))
                .statusAnalyze(getNumber(getValueNotNull(statusHistory.get("analysis"))))
                .statusTesting(getNumber(getValueNotNull(statusHistory.get("testing"))))
                .type(getValueNotNull(task.getType()))
                .name(getValueNotNull(task.getName()))
                .epic(epicNumber)
                .taskInEpic(taskInEpic)
                .build();
    }

    private static String getDateTimeAfterCreate(String statusHistory, String createDate) {
        if (!"[]".equals(statusHistory)) {
            return statusHistory;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date now = new Date();
            String currentDate = dateFormat.format(now);
            return String.format("создано %s дней назад", getCompareDate(currentDate, createDate));
        }
    }

    private static long getCompareDate(String dateNowStr, String dateOldStr){
        if (dateOldStr == null) {
            return 0L;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dateNow = dateFormat.parse(dateNowStr);
            Date dateOld = dateFormat.parse(dateOldStr);
            long diff = dateNow.getTime() - dateOld.getTime();
            return TimeUnit.MILLISECONDS.toDays(diff);
        } catch (ParseException e) {
            System.err.println("Не удалось распарсить дату");
        }
        return 0L;
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
                    .map(TaskDto.Component::getName)
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

    private static String getNumber(String value) {
        return value.replaceAll("\\D", "");
    }
}
