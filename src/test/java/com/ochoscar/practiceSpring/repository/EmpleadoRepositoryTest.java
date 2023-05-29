package com.ochoscar.practiceSpring.repository;

import com.ochoscar.practiceSpring.model.Empleado;
import org.checkerframework.checker.signature.qual.DotSeparatedIdentifiers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


/// given, when, then

//@DataJpaTest
@SpringBootTest
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

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

    @DisplayName("Test para guardar un empleado")
    @Test
    public void testGuardarEmpleado() {
        // Given

        Empleado empleado1 = Empleado.builder()
                .nombre("pepe")
                .apellido("lopez")
                .email("p12@gmail.com")
                .build();

        // when
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        // then
        Assert.assertNotEquals(null, empleado1);
        Assert.assertTrue(empleado1.getId().compareTo(0L) > 0);
    }

    @DisplayName("Test para listar los empleados")
    @Test
    public void testListarEmpleados() {
        // given
        empleadoRepository.deleteAll();
        Empleado empleado1 = Empleado.builder()
                .nombre("oscar")
                .apellido("ochoa")
                .email("ochsocar@gmail.com")
                .build();
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        // when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        // then
        Assert.assertNotEquals(null, listaEmpleados);
        Assert.assertEquals(2, listaEmpleados.size());
    }

    @DisplayName("Test para obtener un empleado por id")
    @Test
    public void testObtenerERmpleadoPorId() {
        // given
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        //then
        Assert.assertNotEquals(null, empleadoBD);
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    public void testActualizarEmpleado() {
        // given
        empleadoRepository.save(empleado);

        // when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("test@gmail.com");
        empleadoGuardado.setNombre("testname");
        empleadoGuardado.setApellido("testapellido");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //then
        Assert.assertEquals("test@gmail.com", empleadoActualizado.getEmail());
        Assert.assertEquals("testname", empleadoActualizado.getNombre());
        Assert.assertEquals("testapellido", empleadoActualizado.getApellido());
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    public void testEliminarEmpleado() {
        //given
        empleadoRepository.save(empleado);

        // when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        //then
        Assert.assertTrue(empleadoOptional.isEmpty());
    }

}
