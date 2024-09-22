package com.mvillalba.msvc_usuarios.mapper.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public interface UtilMapConverter {

    default String localDateTimeToString(LocalDateTime fecha){
        String formato = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return Optional.ofNullable(fecha).map(d -> fecha.format(formatter)).orElse("");
    }

}
