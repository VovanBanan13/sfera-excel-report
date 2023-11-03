package ru.vtb.asaf.sfera.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import ru.vtb.asaf.sfera.dto.QueryDto;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Component
public class QueryService {

    private final String SIZE_PAGE = "1000";

    public QueryDto createQuery() {
        String query = "";
        try {
            File file = new File("./query.txt");
            query = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            System.err.println("Не удалось прочесть файл");
        }

//        Scanner console = new Scanner(System.in);
//
//        System.out.print("Введи количество задач: ");
//        String size = console.nextLine();

        return QueryDto.builder()
                .query(query)
                .size(SIZE_PAGE)
                .build();
    }
}
