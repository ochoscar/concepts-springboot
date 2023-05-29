package com.ochoscar.practiceSpring.service;

import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

import com.ochoscar.practiceSpring.exception.ResourceNotFoundException;
import com.ochoscar.practiceSpring.model.Empleado;
import com.ochoscar.practiceSpring.repository.EmpleadoRepository;
import com.ochoscar.practiceSpring.services.EmpleadoService;
import com.ochoscar.practiceSpring.services.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setup() {
        System.out.println("Inicializando el setup de las pruebas");
        empleado = Empleado.builder()
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar el empleado")
    @Test
    void testGuardarEmpleado() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado))
                .willReturn(empleado);
        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);
        //then
        assertNotEquals(null, empleadoGuardado);
    }

    @DisplayName("Test para guardar el empleado con Throw Exception")
    @Test
    void testGuardarEmpleadoConThrowException() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));
        //when
        assertThrows(ResourceNotFoundException.class, () -> {
            empleadoService.saveEmpleado(empleado);
        });
        //then
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @DisplayName("Test para listar los empleados")
    @Test
    public void testListarEmpleados() {
        //given
        Empleado empleado1 = Empleado.builder()
                .id(2L)
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochoscar@gmail.com")
                .build();
        given(empleadoRepository.findAll())
                .willReturn(List.of(empleado, empleado1));

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();

        //then
        assertNotEquals(null, empleados);
        assertEquals(2, empleados.size());
    }

    @DisplayName("Test para retornar una lista vacia")
    @Test
    void testListarColleccionEmpleadosVacia() {
        //given
        given(empleadoRepository.findAll())
                .willReturn(Collections.emptyList());

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();

        //then
        assertNotEquals(null, empleados);
        assertEquals(0, empleados.size());
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    public void obtenerEmpleadoPorId() {
        // given
        empleado.setId(1L);
        given(empleadoRepository.findById(1L))
                .willReturn(Optional.of(empleado));
        // when
        Empleado empleadoGuardado = empleadoService.getEmpleadoById(1L).get();
        // then
        assertNotEquals(null, empleadoGuardado);
        assertEquals(Optional.of(1L), Optional.ofNullable((Long) empleadoGuardado.getId()));
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado() {
        //given
        given(empleadoRepository.save(empleado))
                .willReturn(empleado);
        empleado.setEmail("test@gmail.com");
        empleado.setNombre("testnombre");
        //when
        Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);

        //then
        assertEquals("test@gmail.com", empleadoActualizado.getEmail());
        assertEquals("testnombre", empleadoActualizado.getNombre());
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado() {
        // given
        long empleadoId = 1;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
        // when
        empleadoService.deleteEmpleado(empleadoId);
        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }

}
