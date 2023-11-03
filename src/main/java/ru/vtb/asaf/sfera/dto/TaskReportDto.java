package ru.vtb.asaf.sfera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReportDto {
    private String number;
    private String status;
//    private List<String> label; //TODO: не список String
    private String assignee;
    private String owner;
    private String streamConsumer;
    private String streamExecutor;
//    private List<CustomFieldsValues> customFieldsValues; //TODO Фильтровать по 'Стрим-заказчик' и 'Стрим-исполнитель'
    private String createDate;
    private String updateDate;
    private String dueDate;

    private String type;
    private String name;

}
