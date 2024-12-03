package com.mvillalba.msvc_usuarios.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
//@ExtendWith(MockitoExtension.class)
//@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BindingResult bindingResult;


    @BeforeEach
    public void setup() {
        FieldError fieldError = new FieldError("user", "password", "La contraseña no puede estar vacía");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
    }

    @Test
    public void testHandleValidationExceptions() throws Exception {
        RuntimeException exception = new RuntimeException("Runtime exception occurred");

        mockMvc.perform(post("/some-endpoint")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .requestAttr("javax.servlet.error.exception", exception))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("La contraseña no puede estar vacía"));

    }

    @Test
    void handleRuntimeException() {
    }
}