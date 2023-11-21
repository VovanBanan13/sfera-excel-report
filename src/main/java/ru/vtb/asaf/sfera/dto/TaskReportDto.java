package ru.vtb.asaf.sfera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReportDto {
    private String number;
    private String status;
    private String label;
    private String component;
    private String assignee;
    private String assigneeHistory;
    private String owner;
    private String streamConsumer;
    private String streamExecutor;
    private String projectConsumer;
    private String createDate;
    private String updateDate;
    private String endDate;
    private String implementationEndDate;
    private String dueDate;
    private String dueDateHistory;
    private String statusHistory;
    private Integer statusCreated;
    private Integer statusInProgress;
    private Integer statusAnalyze;
    private Integer statusTesting;
    private Integer statusWaiting;
    private Integer statusOnTheQueue;
    private Integer statusDone;
    private Integer statusClosed;
    private Integer estimation;
    private Integer worklogSpent;
    private String type;
    private String name;
    private String epic;
    private String epicProjectConsumer;
    private String taskInEpic;
    private String relatedEntities;
}
