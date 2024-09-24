package com.mvillalba.msvc_usuarios.dto;

import com.mvillalba.msvc_usuarios.dto.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorDTO extends Response {
    private LocalDateTime timestamp;
    private Integer codigo;
    private String detail;

}
