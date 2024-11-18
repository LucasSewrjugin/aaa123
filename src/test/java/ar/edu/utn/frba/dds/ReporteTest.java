package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoJuridico;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitario de reportes.
 */
public class ReporteTest {
  private static final Map<String, Object> datosOrg = new HashMap<>();
  private final TipoJuridico empresa = TipoJuridico.Empresa;
  // REQUERIMIENTO 1
  private Organizacion unaOrg;
  private ColaboradorHumana colaboradorH;
  private Ubicacion unaUbicacionH;
  private Heladera heladera;
  private List<Vianda> viandas = new ArrayList<>();
  
  /**
   * Inicializamos variables.
   */
  @BeforeEach
  public void inicializarVariables() {
    unaOrg = new Organizacion();
    datosOrg.put("Sueldo", 3000);
    unaUbicacionH = new Ubicacion("Medrano 951", 1.2, 3.4);
    LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
    colaboradorH = new ColaboradorHumana(unaOrg, "jperez@frba.utn.edu.ar", 12348765,
        1143218765, unaUbicacionH, "Juan", "Perez", fechaNacimiento);
  }
  
  @Test
  void laCantidadDeColaboracionesDeUnColaboradorCorrespondeConLasRealizadas() {
  }
}
