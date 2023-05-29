package com.ochoscar.practiceSpring.controller;

import com.ochoscar.practiceSpring.model.Empleado;
import com.ochoscar.practiceSpring.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping("/guardarEmpleado")
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
        return empleadoService.saveEmpleado(empleado);
    }

    @GetMapping("/listarEmpleados")
    @ResponseStatus(HttpStatus.OK)
    public List<Empleado> listarEmpleados() {
        return empleadoService.getAllEmpleados();
    }

    @GetMapping("/obtenerEmpleadoPorId/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable("id") long empleadoId) {
        return empleadoService.getEmpleadoById(empleadoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizarEmpleado/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable("id") long id, @RequestBody Empleado empleado) {
        return empleadoService.getEmpleadoById(id)
                .map(empleadoGuardado -> {
                    empleadoGuardado.setNombre(empleado.getNombre());
                    empleadoGuardado.setApellido(empleado.getApellido());
                    empleadoGuardado.setEmail(empleado.getEmail());

                    Empleado empleadoActualizado = empleadoService.updateEmpleado(empleadoGuardado);
                    return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminarEmpleado/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable("id") long empleadoId) {
        empleadoService.deleteEmpleado(empleadoId);
        return new ResponseEntity<>("Empleado eliminado exitosamente", HttpStatus.OK);
    }

}
