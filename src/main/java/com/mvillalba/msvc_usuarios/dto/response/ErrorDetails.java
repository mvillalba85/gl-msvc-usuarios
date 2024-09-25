package com.mvillalba.msvc_usuarios.dto.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ErrorDetails {
    private Timestamp timestamp;
    private Integer codigo;
    private String detail;

    public ErrorDetails(Timestamp timestamp, Integer codigo, String detail) {
        this.timestamp = timestamp;
        this.codigo = codigo;
        this.detail = detail;
    }
}
