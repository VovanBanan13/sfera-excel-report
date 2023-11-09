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
    private String owner;
    private String streamConsumer;
    private String streamExecutor;
    private String projectConsumer;
    private String createDate;
    private String updateDate;
    private String dueDate;
    private String statusHistory;
    private String type;
    private String name;

}
