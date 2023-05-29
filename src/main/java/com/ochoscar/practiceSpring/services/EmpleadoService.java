package com.ochoscar.practiceSpring.services;

import com.ochoscar.practiceSpring.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleados();

    Optional<Empleado> getEmpleadoById(long id);

    Empleado updateEmpleado(Empleado empleadoActualizado);

    void deleteEmpleado(long id);

}
