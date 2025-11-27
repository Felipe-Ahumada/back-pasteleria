package com.pasteleria.service;

import com.pasteleria.model.Comuna;
import com.pasteleria.model.Region;
import com.pasteleria.repository.ComunaRepository;
import com.pasteleria.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    public List<Region> getAllRegiones() {
        return regionRepository.findAll();
    }

    public List<Comuna> getComunasByRegionCodigo(String regionCodigo) {
        return comunaRepository.findByRegionCodigo(regionCodigo);
    }

    @Transactional
    public void seedData() {
        if (regionRepository.count() > 0) {
            return; // Data already seeded
        }

        // Hardcoded data from region_comuna.json
        createRegionWithComunas("15", "Arica y Parinacota", List.of("Arica", "Camarones", "Putre", "General Lagos"));
        createRegionWithComunas("01", "Tarapacá",
                List.of("Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"));
        createRegionWithComunas("02", "Antofagasta", List.of("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal",
                "Calama", "Ollagüe", "San Pedro de Atacama", "Tocopilla", "María Elena"));
        createRegionWithComunas("03", "Atacama", List.of("Copiapó", "Caldera", "Tierra Amarilla", "Chañaral",
                "Diego de Almagro", "Vallenar", "Alto del Carmen", "Freirina", "Huasco"));
        createRegionWithComunas("04", "Coquimbo",
                List.of("La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paihuano", "Vicuña", "Illapel", "Canela",
                        "Los Vilos", "Salamanca", "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"));
        createRegionWithComunas("05", "Valparaíso",
                List.of("Valparaíso", "Viña del Mar", "Concón", "Quilpué", "Villa Alemana", "Quillota", "Calera",
                        "Hijuelas", "La Cruz", "Nogales", "San Antonio", "Algarrobo", "Cartagena", "El Quisco",
                        "El Tabo", "Santo Domingo", "Los Andes", "Calle Larga", "Rinconada", "San Esteban",
                        "San Felipe", "Catemu", "Llay Llay", "Panquehue", "Putaendo", "Santa María", "La Ligua",
                        "Cabildo", "Papudo", "Petorca", "Zapallar", "Isla de Pascua", "Juan Fernández"));
        createRegionWithComunas("06", "O'Higgins",
                List.of("Rancagua", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "Las Cabras", "Machalí",
                        "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua", "Quinta de Tilcoco", "Rengo", "Requínoa",
                        "San Vicente", "Pichilemu", "La Estrella", "Litueche", "Marchihue", "Navidad", "Paredones",
                        "San Fernando", "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla", "Peralillo",
                        "Placilla", "Pumanque", "Santa Cruz"));
        createRegionWithComunas("07", "Maule",
                List.of("Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco", "Pencahue", "Río Claro",
                        "San Clemente", "San Rafael", "Cauquenes", "Chanco", "Pelluhue", "Curicó", "Hualañé",
                        "Licantén", "Molina", "Rauco", "Romeral", "Sagrada Familia", "Teno", "Vichuquén", "Linares",
                        "Colbún", "Longaví", "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"));
        createRegionWithComunas("08", "Biobío",
                List.of("Concepción", "Coronel", "Chiguayante", "Florida", "Hualqui", "Lota", "Penco",
                        "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Hualpén", "Lebu", "Arauco",
                        "Cañete", "Contulmo", "Curanilahue", "Los Álamos", "Tirúa", "Los Ángeles", "Antuco", "Cabrero",
                        "Laja", "Mulchén", "Nacimiento", "Negrete", "Quilaco", "Quilleco", "San Rosendo",
                        "Santa Bárbara", "Tucapel", "Yumbel", "Alto Biobío"));
        createRegionWithComunas("09", "La Araucanía",
                List.of("Temuco", "Carahue", "Cunco", "Curarrehue", "Freire", "Galvarino", "Gorbea", "Lautaro",
                        "Loncoche", "Melipeuco", "Nueva Imperial", "Padre Las Casas", "Perquenco", "Pitrufquén",
                        "Pucón", "Saavedra", "Teodoro Schmidt", "Toltén", "Vilcún", "Villarrica", "Cholchol", "Angol",
                        "Collipulli", "Curacautín", "Ercilla", "Lonquimay", "Los Sauces", "Lumaco", "Purén", "Renaico",
                        "Traiguén", "Victoria"));
        createRegionWithComunas("14", "Los Ríos", List.of("Valdivia", "Corral", "Lanco", "Los Lagos", "Máfil",
                "Mariquina", "Paillaco", "Panguipulli", "La Unión", "Futrono", "Lago Ranco", "Río Bueno"));
        createRegionWithComunas("10", "Los Lagos",
                List.of("Puerto Montt", "Calbuco", "Cochamó", "Fresia", "Frutillar", "Los Muermos", "Llanquihue",
                        "Maullín", "Puerto Varas", "Castro", "Ancud", "Chonchi", "Curaco de Vélez", "Dalcahue",
                        "Puqueldón", "Queilén", "Quellón", "Quemchi", "Quinchao", "Osorno", "Puerto Octay", "Purranque",
                        "Puyehue", "Río Negro", "San Juan de la Costa", "San Pablo", "Chaitén", "Futaleufú",
                        "Hualaihué", "Palena"));
        createRegionWithComunas("11", "Aysén", List.of("Coyhaique", "Lago Verde", "Aysén", "Cisnes", "Guaitecas",
                "Cochrane", "O'Higgins", "Tortel", "Chile Chico", "Río Ibáñez"));
        createRegionWithComunas("12", "Magallanes",
                List.of("Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio", "Cabo de Hornos", "Antártica",
                        "Porvenir", "Primavera", "Timaukel", "Natales", "Torres del Paine"));
        createRegionWithComunas("13", "Metropolitana",
                List.of("Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central",
                        "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana",
                        "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa",
                        "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura", "Quinta Normal",
                        "Recoleta", "Renca", "San Joaquín", "San Miguel", "San Ramón", "Vitacura", "Puente Alto",
                        "Pirque", "San José de Maipo", "Colina", "Lampa", "Tiltil", "San Bernardo", "Buin",
                        "Calera de Tango", "Paine", "Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro",
                        "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"));
    }

    private void createRegionWithComunas(String codigo, String nombre, List<String> comunasNombres) {
        Region region = new Region();
        region.setCodigo(codigo);
        region.setNombre(nombre);
        region = regionRepository.save(region);

        for (String comunaNombre : comunasNombres) {
            Comuna comuna = new Comuna();
            comuna.setNombre(comunaNombre);
            comuna.setRegion(region);
            comunaRepository.save(comuna);
        }
    }
}
