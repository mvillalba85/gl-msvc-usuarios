package com.mvillalba.msvc_usuarios.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private boolean success;
    private Object data;
    private List<ErrorDTO> errores = new ArrayList<>();

    public ResponseDTO(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
}
