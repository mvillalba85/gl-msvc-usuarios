package com.mvillalba.msvc_usuarios.dto.response;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ResponseError extends Response{

    List<ErrorDetails> errores = new ArrayList<>();

    public ResponseError() {
    }

    public ResponseError(ErrorDetails error) {
        this.errores.add(error);
    }

    public ResponseError(Timestamp now, int value, String message) {
        ErrorDetails error = new ErrorDetails(now, value, message);
        errores.add(error);
    }

    public void setErrores(ErrorDetails error) {
        this.errores.add(error);
    }

    public void addError(ErrorDetails error){
        errores.add(error);
    }
}
