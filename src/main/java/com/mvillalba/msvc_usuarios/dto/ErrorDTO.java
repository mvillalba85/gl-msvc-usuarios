package com.mvillalba.msvc_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorDTO {
    private LocalDateTime timestamp;
    private Integer codigo;
    private String detail;

}
