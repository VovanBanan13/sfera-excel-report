package ru.vtb.asaf.sfera.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.vtb.asaf.sfera.dto.AuthReq;
import ru.vtb.asaf.sfera.dto.AuthRes;
import ru.vtb.asaf.sfera.util.Constant;

import java.util.Scanner;

@Slf4j
@Service
public class AuthorizationService {
    RestTemplate restTemplate = new RestTemplate();
    public AuthRes authorization() {
        Scanner console = new Scanner(System.in);

        System.out.println("\nАвторизация в 'СФЕРА'\n");
        System.out.print("Логин: ");
        String username = console.nextLine();
        System.out.print("Пароль: ");
        String password = console.nextLine();
        AuthReq authReq = AuthReq.builder().username(username).password(password).build();

        try {
            ResponseEntity<AuthRes> response = restTemplate.postForEntity(Constant.LOGIN_POST_URL, authReq, AuthRes.class);
            System.out.println("OK Authorized");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Invalid login or password");
            return authorization();
        }
    }
}
