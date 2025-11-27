package com.pasteleria.dto;

import com.pasteleria.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String run;
    private String dv;
    private String nombre;
    private String apellidos;
    private String correo;
    private LocalDate fechaNacimiento;
    private String codigoDescuento;
    private Role tipoUsuario;
    private String regionId;
    private String regionNombre;
    private String comuna;
    private String direccion;
    private String avatarUrl;
    private Boolean activo;
}
