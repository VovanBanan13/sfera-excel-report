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
        createCell(row, 0, "Ключ РДС", style);
        createCell(row, 1, "Статус", style);
        createCell(row, 2, "Метки", style);
        createCell(row, 3, "Компоненты", style);
        createCell(row, 4, "Исполнитель", style);
        createCell(row, 5, "Владелец", style);
        createCell(row, 6, "Стрим-заказчик", style);
        createCell(row, 7, "Стрим-исполнитель", style);
        createCell(row, 8, "Проект-заказчик", style);
        createCell(row, 9, "Создано", style);
        createCell(row, 10, "Срок исполнения", style);
        createCell(row, 11, "Обновлено", style);
        createCell(row, 12, "Смена статуса", style);
        createCell(row, 13, "Тип", style);
        createCell(row, 14, "Название", style);
        createCell(row, 15, "Эпик", style);
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
            createCell(row, columnCount++, record.getOwner(), style);
            createCell(row, columnCount++, record.getStreamConsumer(), style);
            createCell(row, columnCount++, record.getStreamExecutor(), style);
            createCell(row, columnCount++, record.getProjectConsumer(), style);
            createCell(row, columnCount++, record.getCreateDate(), style);
            createCell(row, columnCount++, record.getDueDate(), style);
            createCell(row, columnCount++, record.getUpdateDate(), style);
            createCell(row, columnCount++, record.getStatusHistory(), style);
            createCell(row, columnCount++, record.getType(), style);
            createCell(row, columnCount++, record.getName(), style);
            createCell(row, columnCount++, record.getEpic(), style);
            if (rowCount%50 == 0) {
                System.out.printf("Обработано %d из %d записей%n", rowCount, taskList.size());
            }
        }
    }

}
