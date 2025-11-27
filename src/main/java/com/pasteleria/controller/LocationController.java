package com.pasteleria.controller;

import com.pasteleria.model.Comuna;
import com.pasteleria.model.Region;
import com.pasteleria.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Ubicaciones", description = "API de regiones y comunas")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/regions")
    @Operation(summary = "Listar regiones", description = "Obtiene todas las regiones")
    public List<Region> listarRegiones() {
        return locationService.getAllRegiones();
    }

    @GetMapping("/regions/{codigo}/comunas")
    @Operation(summary = "Listar comunas por región", description = "Obtiene todas las comunas de una región específica")
    public List<Comuna> listarComunasPorRegion(@PathVariable String codigo) {
        return locationService.getComunasByRegionCodigo(codigo);
    }
}
