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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @DisplayName("Test para listar todos los empleados")
    @Test
    public void testListarEmpleados() throws Exception {
        // given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("oscar").apellido("ochoa").email("ochoscar@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("pedro").apellido("sanchez").email("pp@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("almibar").apellido("eutanasio").email("eta@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/listarEmpleados"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));
    }

    @DisplayName("Test para listar un empleado por ID")
    @Test
    public void testListarEmpleadoPorId() throws Exception {
        // given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId))
                .willReturn(Optional.of(empleado));

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/obtenerEmpleadoPorId/{id}", empleadoId));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.emial", is(empleado.getEmail())));
    }

    @DisplayName("Test para listar un empleado no encontrado")
    @Test
    public void testListarEmpleadoNoEncontrado() throws Exception {
        // given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId))
                .willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/empleados/obtenerEmpleadoPorId/{id}", empleadoId));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    public void testActualizarEmpleado() throws Exception {
        // given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .id(1L)
                .nombre("oscar andres")
                .apellido("ochoa v")
                .email("ochoscar@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId))
                .willReturn(Optional.of(empleado));
        given(empleadoService.updateEmpleado(ArgumentMatchers.any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/api/empleados/actualizarEmpleado/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.emial", is(empleadoActualizado.getEmail())));
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    public void eliminarEmpleado() throws Exception {
        // given
        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        // when
        ResultActions response = mockMvc.perform(delete("/api/empleados/eliminarEmpleado/{id}", empleadoId));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }


}
