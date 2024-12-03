package com.mvillalba.msvc_usuarios.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mvillalba.msvc_usuarios.dto.ErrorDTO;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRest {

    List<ErrorDTO> error;

    public List<ErrorDTO> getError() {
        return error;
    }

    public void addError(ErrorDTO error){
        if(this.error == null){
            this.error = new ArrayList<>();
        }
        this.error.add(error);
    }

}
