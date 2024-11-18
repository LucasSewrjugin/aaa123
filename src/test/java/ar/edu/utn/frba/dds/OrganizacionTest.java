package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.ControladorDeAccesoAdapter;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePesoConWsensor;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDeTemperaturaConTsensor;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoJuridico;
import ar.edu.utn.frba.dds.dominio.heladera.AtencionRequerida;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.heladera.NivelDeLlenado;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Reading;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Tsensor;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Wsensor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitario de la organización.
 */
public class OrganizacionTest {
  private static final Map<String, Object> datosOrg = new HashMap<>();
  private final TipoJuridico empresa = TipoJuridico.Empresa;
  // REQUERIMIENTO 2
  ControladorDeAccesoAdapter controlador;
  // REQUERIMIENTO 1
  private Organizacion unaOrg;
  private ColaboradorHumana colaboradorH;
  private ColaboradorJuridico colaboradorJ;
  private Ubicacion unaUbicacionH;
  private Ubicacion unaUbicacionJ;
  private Beneficiario beneficiario;
  // REQUERIMIENTO 5
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
    unaUbicacionJ = new Ubicacion("Corrientes 654", 5.6, 7.8);
    LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
    colaboradorH =
        new ColaboradorHumana(unaOrg, "jperez@frba.utn.edu.ar", 12348765, 1143218765,
            unaUbicacionH, "Juan", "Perez", fechaNacimiento);
    colaboradorJ = new ColaboradorJuridico(unaOrg, "contacto@libreriasaturno.com",
        11230123, 1198761234,
        unaUbicacionJ, "Libreria Saturno", empresa, "Articulos escolares");
    beneficiario =
        new Beneficiario("Maria Gomez", fechaNacimiento, null, null, null, null, null);
    Wsensor wsensor = mock(Wsensor.class);
    when(wsensor.getWeight(anyString())).thenReturn(new Reading(50, "Kg"));
    Tsensor tsensor = mock(Tsensor.class);
    controlador = mock(ControladorDeAccesoAdapter.class);
    LocalDate fechaDeEstreno = LocalDate.of(2024, 4, 30);
    heladera = new Heladera(unaOrg, "Refrigerios", viandas, unaUbicacionJ, 1000.50,
        fechaDeEstreno,
        new SensorDePesoConWsensor(wsensor), new SensorDeTemperaturaConTsensor(tsensor),
        controlador);
  }
  // REQUERIMIENTO 1 - Alta, baja y modificación de colaboradores
  // ALTA
  
  @Test
  void laOrganizacionRealizaElAltaDeUnColaboradorHumano() {
    unaOrg.altaColaborador(colaboradorH);
    List<Colaborador> colaboradores = unaOrg.getUsuarios();
    assertTrue(colaboradores.contains(colaboradorH));
  }
  
  @Test
  void laOrganizacionRealizaElAltaDeUnColaboradorJuridico() {
    unaOrg.altaColaborador(colaboradorJ);
    List<Colaborador> colaboradores = unaOrg.getUsuarios();
    assertTrue(colaboradores.contains(colaboradorJ));
  }
  
  @Test
  void laOrganizacionRealizaLaModificacionDeUnColaboradorJuridico() {
    unaOrg.altaColaborador(colaboradorJ);
    colaboradorJ.setRubro("Articulos de libreria");
    assertEquals("Articulos de libreria", colaboradorJ.getRubro());
  }
  // BAJA
  
  @Test
  void laOrganizacionRealizaLaBajaDeUnColaboradorHumano() {
    unaOrg.altaColaborador(colaboradorH);
    unaOrg.bajaColaborador(colaboradorH);
    List<Colaborador> colaboradores = unaOrg.getUsuarios();
    assertFalse(colaboradores.contains(colaboradorH));
  }
  
  @Test
  void laOrganizacionRealizaLaBajaDeUnColaboradorJuridico() {
    unaOrg.altaColaborador(colaboradorJ);
    unaOrg.bajaColaborador(colaboradorJ);
    List<Colaborador> colaboradores = unaOrg.getUsuarios();
    assertFalse(colaboradores.contains(colaboradorJ));
  }
  // REQUERIMIENTO 4 - Alta, baja y modificación de Beneficiarios
  // ALTA
  
  @Test
  void laOrganizacionRealizaElAltaDeUnBeneficiario() {
    unaOrg.altaBeneficiario(beneficiario);
    List<Beneficiario> beneficiarios = unaOrg.getBeneficiarios();
    assertTrue(beneficiarios.contains(beneficiario));
  }
  // BAJA
  
  @Test
  void laOrganizacionRealizaLaBajaDeUnBeneficiario() {
    unaOrg.altaBeneficiario(beneficiario);
    unaOrg.bajaBeneficiario(beneficiario);
    List<Beneficiario> beneficiarios = unaOrg.getBeneficiarios();
    assertFalse(beneficiarios.contains(beneficiario));
  }
  // REQUERIMIENTO 5 - Alta, baja y modificación de Heladeras
  // ALTA
  
  @Test
  void laOrganizacionRealizaElAltaDeUnaHeladera() {
    unaOrg.altaHeladera(heladera);
    List<Heladera> heladeras = unaOrg.getHeladeras();
    assertTrue(heladeras.contains(heladera));
  }
  // //BAJA
  
  @Test
  void laOrganizacionRealizaLaBajaDeUnaHeladera() {
    unaOrg.altaHeladera(heladera);
    unaOrg.bajaHeladera(heladera);
    List<Heladera> heladeras = unaOrg.getHeladeras();
    assertFalse(heladeras.contains(heladera));
  }
  
  @Test
  void seConoceElEstadoDeLaHeladera() {
    heladera.revisarLlenado();
    heladera.revisarTemperatura();
    assertTrue(heladera.getAtencionRequerida() == AtencionRequerida.BUENA_TEMPERATURA);
    assertTrue(heladera.getNivelDeLlenado() == NivelDeLlenado.BAJO);
  }
}