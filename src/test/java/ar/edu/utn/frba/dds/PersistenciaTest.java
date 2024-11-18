package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.RepositorioPersistencia;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.ControladorDeAccesoAdapter;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePesoConWsensor;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDeTemperaturaConTsensor;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoJuridico;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Reading;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Tsensor;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Wsensor;
import ar.edu.utn.frba.dds.registro.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitario de persistencia.
 */
public class PersistenciaTest implements SimplePersistenceTest {
  RepositorioPersistencia repositorio = new RepositorioPersistencia();
  ControladorDeAccesoAdapter controlador;
  private Organizacion unaOrg;
  private Ubicacion unaUbicacionJ;
  private Heladera heladera;
  private List<Vianda> viandas = new ArrayList<>();
  private ColaboradorHumana colaboradorH;
  // private ColaboradorJuridico colaboradorJ;
  private Ubicacion unaUbicacionH;
  // private Ubicacion unaUbicacionJ;
  
  /**
   * Inicializamos variables.
   */
  @BeforeEach
  public void inicializarVariables() {
    unaOrg = new Organizacion();
    Wsensor wsensor = mock(Wsensor.class);
    when(wsensor.getWeight(anyString())).thenReturn(new Reading(50, "Kg"));
    controlador = mock(ControladorDeAccesoAdapter.class);
    unaUbicacionJ = new Ubicacion("Corrientes 654", 5.6, 7.8);
    unaUbicacionH = new Ubicacion("Medrano 951", 1.2, 3.4);
    // unaUbicacionJ = new Ubicacion("Corrientes 654", 5.6, 7.8);
    LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
    Tsensor tsensor = mock(Tsensor.class);
    colaboradorH =
        new ColaboradorHumana(unaOrg, "jperez@frba.utn.edu.ar", 12348765, 1143218765,
            unaUbicacionH, "Juan", "Perez", fechaNacimiento);
    colaboradorH.setTarjetaCodigo("12345");
    // colaboradorJ = new ColaboradorJuridico(unaOrg,
    // "contacto@libreriasaturno.com", 11230123, 1198761234,
    // unaUbicacionJ, "Libreria Saturno", empresa, "Articulos escolares");
    LocalDate fechaDeEstreno = LocalDate.of(2024, 4, 30);
    heladera = new Heladera(unaOrg, "Refrigerios", viandas, unaUbicacionJ, 1000.50,
        fechaDeEstreno,
        new SensorDePesoConWsensor(wsensor), new SensorDeTemperaturaConTsensor(tsensor),
        controlador);
  }
  
  @Test // TODO: descomentar, lo hice para levantar el azure
  public void testPersistirHeladera() {
    repositorio.registrarHeladera(heladera);
    // Heladera otraHeladera = repositorio.buscarHeladera(1);
    assertEquals(1, repositorio.buscarHeladera(heladera.getId()).getId());
    assertEquals("Refrigerios", repositorio.buscarHeladera(1).getNombre());
  }
  
  @Test
  public void testPersistenciaUsuario() {
    repositorio.registrarOrg(unaOrg);
    colaboradorH.setUsername("juan");
    colaboradorH.setPassword("PaNN15!");
    RepositorioUsuarios.getInstance().registrarColaboradorHumana(colaboradorH);
    colaboradorH.setUsername("Pepe");
    colaboradorH.setUsername("juan");
    assertEquals(colaboradorH.getNombre(),
        RepositorioUsuarios.getInstance().buscarPorNombre("juan").getNombre());
    assertEquals(colaboradorH.getNombre(),
        RepositorioUsuarios.getInstance().buscarColaboradorHumana("juan", "PaNN15!")
            .getNombre());
  }
}