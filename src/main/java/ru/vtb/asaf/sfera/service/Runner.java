package ru.vtb.asaf.sfera.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vtb.asaf.sfera.dto.AuthRes;
import ru.vtb.asaf.sfera.dto.TaskReportDto;

import java.time.Instant;
import java.util.Date;
import java.util.List;


@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n\n*** Начало работы приложения ***\n");

        AuthorizationService authorizationService = new AuthorizationService();
        TaskService taskService = new TaskService();

        Date dateTime = Date.from(Instant.now());
        String fileName = String.format("report_%s.xlsx", dateTime.toString().replaceAll("[ :]", ""));


        AuthRes authRes = authorizationService.authorization();

        long startTime = System.currentTimeMillis();

        List<TaskReportDto> list = taskService.getTasksReport(authRes);
        ExcelGeneratorService excelGeneratorService = new ExcelGeneratorService(list);
        excelGeneratorService.generateExcelFile(fileName);

        long endTime = System.currentTimeMillis();
        System.out.println("\nПроцесс занял " + Math.round((endTime - startTime)/60000) + " минут");

        System.out.println("\n\n*** Завершение работы приложения ***\n");
        System.exit(0);
    }

}
