package com.mvillalba.msvc_usuarios.dto;

import lombok.Data;

import java.sql.Timestamp;

public class ErrorDTO {
    private Timestamp timestamp;
    private Integer codigo;
    private String detail;

    public ErrorDTO(Timestamp timestamp, Integer codigo, String detail) {
        this.timestamp = timestamp;
        this.codigo = codigo;
        this.detail = detail;
    }
}
