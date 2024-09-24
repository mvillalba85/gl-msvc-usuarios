package com.mvillalba.msvc_usuarios.dto.response;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class ResponseError extends Response{

    List<ErrorDTO> errores = new ArrayList<>();

    public ResponseError() {
    }

    public ResponseError(ErrorDTO error) {
        this.errores.add(error);
    }

    public void setErrores(ErrorDTO error) {
        this.errores.add(error);
    }
}
