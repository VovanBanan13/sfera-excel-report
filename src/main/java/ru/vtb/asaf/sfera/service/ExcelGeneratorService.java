package ru.vtb.asaf.sfera.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.vtb.asaf.sfera.dto.TaskReportDto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelGeneratorService {

    List<TaskReportDto> taskList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGeneratorService(List<TaskReportDto> taskList) {
        this.taskList = taskList;
        workbook = new XSSFWorkbook();
    }

    public void generateExcelFile(String filename) throws IOException {
        System.out.println("\nИдёт запись в файл: " + filename);
        writeHeader();
        write(taskList);
        FileOutputStream file = new FileOutputStream(filename);
        workbook.write(file);
        workbook.close();
        file.close();
        System.out.println("\nОтчёт записан в файл: " + filename);
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Sfera_Report");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Номер задачи", style);
        createCell(row, 1, "Статус", style);
        createCell(row, 2, "Метки", style);
        createCell(row, 3, "Компоненты", style);
        createCell(row, 4, "Исполнитель", style);
        createCell(row, 5, "Смена исполнителя", style);
        createCell(row, 6, "Владелец", style);
        createCell(row, 7, "Стрим-заказчик", style);
        createCell(row, 8, "Стрим-исполнитель", style);
        createCell(row, 9, "Проект-заказчик", style);
        createCell(row, 10, "Создано", style);
        createCell(row, 11, "Срок исполнения", style);
        createCell(row, 12, "Смена срока исполнения", style);
        createCell(row, 13, "Обновлено", style);
        createCell(row, 14, "Дата завершения", style);
        createCell(row, 15, "Дата завершения работ", style);
        createCell(row, 16, "Смена статуса", style);
        createCell(row, 17, "Создано", style);
        createCell(row, 18, "В работе", style);
        createCell(row, 19, "Анализ", style);
        createCell(row, 20, "Тестирование", style);
        createCell(row, 21, "В ожидании", style);
        createCell(row, 22, "В очереди", style);
        createCell(row, 23, "Выполнено", style);
        createCell(row, 24, "Закрыто", style);
        createCell(row, 25, "Прогресс задачи (план)", style);
        createCell(row, 26, "Прогресс задачи (факт)", style);
        createCell(row, 27, "Тип", style);
        createCell(row, 28, "Название", style);
        createCell(row, 29, "Эпик", style);
        createCell(row, 30, "Проект эпика", style);
        createCell(row, 31, "RDS эпика", style);
        createCell(row, 32, "Связи задачи", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write(List<TaskReportDto> taskList) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (TaskReportDto record: taskList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getNumber(), style);
            createCell(row, columnCount++, record.getStatus(), style);
            createCell(row, columnCount++, record.getLabel(), style);
            createCell(row, columnCount++, record.getComponent(), style);
            createCell(row, columnCount++, record.getAssignee(), style);
            createCell(row, columnCount++, record.getAssigneeHistory(), style);
            createCell(row, columnCount++, record.getOwner(), style);
            createCell(row, columnCount++, record.getStreamConsumer(), style);
            createCell(row, columnCount++, record.getStreamExecutor(), style);
            createCell(row, columnCount++, record.getProjectConsumer(), style);
            createCell(row, columnCount++, record.getCreateDate(), style);
            createCell(row, columnCount++, record.getDueDate(), style);
            createCell(row, columnCount++, record.getDueDateHistory(), style);
            createCell(row, columnCount++, record.getUpdateDate(), style);
            createCell(row, columnCount++, record.getEndDate(), style);
            createCell(row, columnCount++, record.getImplementationEndDate(), style);
            createCell(row, columnCount++, record.getStatusHistory(), style);
            createCell(row, columnCount++, record.getStatusCreated(), style);
            createCell(row, columnCount++, record.getStatusInProgress(), style);
            createCell(row, columnCount++, record.getStatusAnalyze(), style);
            createCell(row, columnCount++, record.getStatusTesting(), style);
            createCell(row, columnCount++, record.getStatusWaiting(), style);
            createCell(row, columnCount++, record.getStatusOnTheQueue(), style);
            createCell(row, columnCount++, record.getStatusDone(), style);
            createCell(row, columnCount++, record.getStatusClosed(), style);
            createCell(row, columnCount++, record.getEstimation(), style);
            createCell(row, columnCount++, record.getWorklogSpent(), style);
            createCell(row, columnCount++, record.getType(), style);
            createCell(row, columnCount++, record.getName(), style);
            createCell(row, columnCount++, record.getEpic(), style);
            createCell(row, columnCount++, record.getEpicProjectConsumer(), style);
            createCell(row, columnCount++, record.getTaskInEpic(), style);
            createCell(row, columnCount++, record.getRelatedEntities(), style);
            if (rowCount%10 == 0) {
                System.out.printf("Обработано %d из %d записей%n", rowCount, taskList.size());
            }
        }
    }

}
