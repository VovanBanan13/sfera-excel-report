package ru.vtb.asaf.sfera.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthReq {
    private String username;
    private String password;
}
