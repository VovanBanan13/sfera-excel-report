package ru.vtb.asaf.sfera.service;

import org.springframework.stereotype.Component;
import ru.vtb.asaf.sfera.dto.QueryDto;

import java.util.Scanner;

@Component
public class QueryService {

    public QueryDto createQuery() {
        Scanner console = new Scanner(System.in);

        System.out.print("Введи фильтр: ");
        String query = console.nextLine();
        System.out.print("Введи количество задач: ");
        String size = console.nextLine();

        return QueryDto.builder()
                .query(query)
                .size(size)
                .build();
    }
}
