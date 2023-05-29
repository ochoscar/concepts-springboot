package com.ochoscar.practiceSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ochoscar.practiceSpring.model.Empleado;
import com.ochoscar.practiceSpring.services.EmpleadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

//@SpringBootTest
@WebMvcTest
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Test guardar empleado con throws")
    @Test
    void testGuardarEmpleado() throws Exception {
        // given
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();
        given(empleadoService.saveEmpleado(ArgumentMatchers.any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/listarEmpleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));
        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.emial", is(empleado.getEmail())));
    }

}
