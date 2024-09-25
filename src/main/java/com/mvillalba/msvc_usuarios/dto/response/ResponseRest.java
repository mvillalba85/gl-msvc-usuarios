package com.mvillalba.msvc_usuarios.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mvillalba.msvc_usuarios.dto.ErrorDTO;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRest {

    List<ErrorDTO> errores;

    public List<ErrorDTO> getErrores() {
        return errores;
    }

    public void setErrores(List<ErrorDTO> errores) {
        this.errores = errores;
    }

    public void addError(ErrorDTO error){
        if(errores == null){
            errores = new ArrayList<>();
        }
        errores.add(error);
    }

}
