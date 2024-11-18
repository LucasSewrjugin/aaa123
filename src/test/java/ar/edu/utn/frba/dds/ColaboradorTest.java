package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.ControladorDeAccesoAdapter;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePesoConWsensor;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDeTemperaturaConTsensor;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DistribucionViandas;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.dominio.colaboraciones.MotivoDistribucionVianda;
import ar.edu.utn.frba.dds.dominio.colaboraciones.RegistrarBeneficiario;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
//import ar.edu.utn.frba.dds.dominio.colaboraciones.*;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoJuridico;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.tarjeta.Tarjeta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test unitario de colaborador.
 */
public class ColaboradorTest {
  private static Map<String, Object> datosOrg = new HashMap<>();
  // REQUERIMIENTO 1
  private Organizacion unaOrg;
  private ColaboradorHumana colaboradorH;
  private ColaboradorJuridico colaboradorJ;
  private Ubicacion unaUbicacionH;
  private Ubicacion unaUbicacionJ;
  private TipoJuridico empresa = TipoJuridico.Empresa;
  private Heladera heladera;
  private Heladera heladeraOrigen;
  private Heladera heladeraDestino;
  private Vianda unaVianda;
  private Vianda otraVianda;
  private DonacionVianda unaDonacionVianda;
  private DistribucionViandas unaDistribucionViandas;
  private Ubicacion ubicacionOrigen;
  private Ubicacion ubicacionDestino;
  private MotivoDistribucionVianda motivoDistribucion;
  private List<Vianda> viandas = new ArrayList<>();
  private List<Vianda> otrasViandas = new ArrayList<>();
  
  /**
   * Inicializamos todas las variables de testeo.
   */
  @BeforeEach
  public void inicializarVariables() {
    SensorDePesoConWsensor sensorDePeso = mock(SensorDePesoConWsensor.class);
    when(sensorDePeso.getPeso(any(Heladera.class))).thenReturn(100.5);
    unaOrg = new Organizacion();
    datosOrg.put("Sueldo", 3000);
    unaUbicacionH = new Ubicacion("Medrano 951", 1.2, 3.4);
    unaUbicacionJ = new Ubicacion("Corrientes 654", 5.6, 7.8);
    LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
    colaboradorH =
        new ColaboradorHumana(unaOrg, "jperez@frba.utn.edu.ar", 12348765, 1143218765,
            unaUbicacionH, "Juan", "Perez", fechaNacimiento);
    colaboradorH.setTarjetaCodigo("12345");
    colaboradorJ = new ColaboradorJuridico(unaOrg, "contacto@libreriasaturno.com",
        11230123, 1198761234,
        unaUbicacionJ, "Libreria Saturno", empresa, "Articulos escolares");
    ubicacionOrigen = new Ubicacion("Cabildo 654", 5.6, 7.8);
    ubicacionDestino = new Ubicacion("Libertador 654", 5.6, 7.8);
    LocalDate fechaDeEstreno = LocalDate.of(2024, 4, 30);
    SensorDeTemperaturaConTsensor sensorDeTemperatura =
        mock(SensorDeTemperaturaConTsensor.class);
    ControladorDeAccesoAdapter controlador = mock(ControladorDeAccesoAdapter.class);
    heladera = new Heladera(unaOrg, "HeladeraDePrueba", viandas, ubicacionDestino, 1000.5,
        fechaDeEstreno,
        sensorDePeso, sensorDeTemperatura, controlador);
    LocalDateTime fechaCaducidad = LocalDateTime.of(2024, 5, 30, 15, 30);
    LocalDateTime fechaDeDonacion = LocalDateTime.of(2024, 5, 15, 9, 20);
    unaVianda = new Vianda("Hamburguesa", fechaCaducidad, fechaDeDonacion, colaboradorH,
        500, 400.0);
    otraVianda =
        new Vianda("Milanesa", fechaCaducidad, fechaDeDonacion, colaboradorH, 350, 150.0);
    viandas.add(unaVianda);
    otrasViandas.add(otraVianda);
    unaDonacionVianda =
        new DonacionVianda(colaboradorH, LocalDate.now(), heladera, unaVianda, false);
    heladeraOrigen = new Heladera(unaOrg, "Heladera de Cabildo", viandas, ubicacionOrigen,
        1000.50, fechaDeEstreno,
        sensorDePeso, sensorDeTemperatura, controlador);
    heladeraDestino = new Heladera(unaOrg, "Heladera de Libertador", otrasViandas,
        ubicacionDestino, 1000.50,
        fechaDeEstreno, sensorDePeso, sensorDeTemperatura, controlador);
    motivoDistribucion = MotivoDistribucionVianda.FALTA_VIANDAS;
    unaDistribucionViandas =
        new DistribucionViandas(colaboradorH, heladeraOrigen, heladeraDestino, 1,
            motivoDistribucion, LocalDate.now());
  }
  // REQUERIMIENTO 2 - Realizar Colaboraciones
  
  // Colaboradores Humanos
  @Test
  @Disabled
  void elColaboradorHumanoRealizaDonacionDeDinero() {
    // Por el momento no hay un uso definido para esta forma de colaboración
  }
  
  @Test
  void elColaboradorHumanoRealizaDistrucionDeVianda() {
    colaboradorH.agregarColaboracion(unaDistribucionViandas);
    List<Colaboracion> colaboraciones = colaboradorH.getColaboraciones();
    assertTrue(colaboraciones.contains(unaDistribucionViandas));
  }
  
  @Test
  void elColaboradorHumanoRealizaRegistroDeBeneficiario() {
    Tarjeta unaTarjeta = new Tarjeta("ABCDE123456");
    Beneficiario unBeneficiario =
        new Beneficiario("Pepe", LocalDate.now(), unaUbicacionH, 12345678,
            TipoDocumento.DNI, 0, unaTarjeta);
    RegistrarBeneficiario unRegistroDeBeneficiario =
        new RegistrarBeneficiario(LocalDate.now(), unaOrg, unaTarjeta,
            unBeneficiario);
    colaboradorH.agregarColaboracion(unRegistroDeBeneficiario);
    assertTrue(colaboradorH.getColaboraciones().contains(unRegistroDeBeneficiario));
  }
  
  // Colaboradores Juridicos
  @Test
  void elColaboradorJuridicoRealizaDonacionDeDinero() {
    // Por el momento no hay un uso definido para esta forma de colaboración
  }
  
  @Test
  void elColaboradorJuridicoRealizaColocarHeladera() {
    colaboradorJ.hacerseCargoDe(heladera);
    List<Heladera> heladeras = colaboradorJ.getHeladeras();
    assertTrue(heladeras.contains(heladera));
  }
  
  @Test
  void elColaboradorJuridicoNoRealizaDistrucionDeVianda() {
    Throwable exception = assertThrows(RuntimeException.class, () -> {
      colaboradorJ.agregarColaboracion(unaDistribucionViandas);
    });
    String mensajeEsperado =
        "Error: Es un colaborador juridico y esta haciendo colaboracion de otro tipo";
    String mensajeLanzado = exception.getMessage();
    assertEquals(mensajeEsperado, mensajeLanzado);
  }
  // REQUERIMIENTO 3 - Ingresar viandas a la heladera
  
  // Colaboradores Humanos
  // DonacionVianda(LocalDate unaFecha,Heladera unaHeladera, Vianda unaVianda,
  // Boolean entregado)
  @Test
  void elColaboradorHumanoRealizaDonacionDeVianda() {
    Colaboracion unaDonacionVianda =
        new DonacionVianda(colaboradorH, LocalDate.now(), heladera, unaVianda, false);
    colaboradorH.agregarColaboracion(unaDonacionVianda);
    List<Colaboracion> colaboraciones = colaboradorH.getColaboraciones();
    assertTrue(colaboraciones.contains(unaDonacionVianda));
  }
  
  @Test
  void noPuedeRealizarseUnaDonacionDeViandaYaEntregada() {
    unaDonacionVianda.setEntregada(true);
    // Throwable exception = assertThrows(RuntimeException.class, () -> {
    // colaboradorH.agregarColaboracion(unaDonacionVianda);
    // colaboradorH.contribuir(unaDonacionVianda);
    // });
    // String mensajeEsperado = "La vianda ya fue entregada";
    // String mensajeLanzado = exception.getMessage();
    // assertEquals(mensajeEsperado, mensajeLanzado);
    assertThrows(RuntimeException.class, () -> {
      colaboradorH.agregarColaboracion(unaDonacionVianda);
      colaboradorH.contribuir(unaDonacionVianda);
    });
  }
  // Colaboradores Juridicos
  
  @Test
  void elColaboradorJuridicoNoRealizaDonacionDeVianda() {
    Throwable exception = assertThrows(RuntimeException.class, () -> {
      colaboradorJ.agregarColaboracion(unaDonacionVianda);
    });
    String mensajeEsperado =
        "Error: Es un colaborador juridico y esta haciendo colaboracion de otro tipo";
    String mensajeLanzado = exception.getMessage();
    assertEquals(mensajeEsperado, mensajeLanzado);
  }
  // PRUEBAS DE PUNTAJES
  
  @Test
  @Disabled
  void puntajeCorrectoColaboradorHumano() {
    // TODO
  }
  
  @Test
  @Disabled
  void puntajeCorrectoColaboradorJuridico() {
    // TODO
  }
  
  // TEST DE REGISTRARACCION EN COLABORADORH
  @Test
  void registrarUnaSolicitudDeDistribucionVianda() {
    int cantidadLogsHeladeraOrigen = heladeraOrigen.getLogs().size();
    int cantidadLogsHeladeraDestino = heladeraDestino.getLogs().size();
    colaboradorH.registrarAccion(unaDistribucionViandas);
    assertEquals(cantidadLogsHeladeraOrigen + 1, heladeraOrigen.getLogs().size());
    assertEquals(cantidadLogsHeladeraDestino + 1, heladeraOrigen.getLogs().size());
  }
  
  @Test
  void registrarUnaSolicitudDeDonacionVianda() {
    int cantidadLogsHeladera = heladera.getLogs().size();
    colaboradorH.registrarAccion(unaDonacionVianda);
    assertEquals(cantidadLogsHeladera + 1, heladera.getLogs().size());
  }
}
// public class ColaboradorTest {
// private static Organizacion unaOrg;
// private static ColaboradorHumana colaboradorHumano;
// private static Vianda vianda;
// private static DonacionVianda donacionVianda;
// private static Map<String, Object> datosOrg = new HashMap<>();
//
// @BeforeAll
// static void iniciarVariables(){
// unaOrg = new Organizacion();
// unaOrg.agregarCampoRequerido("Sueldo", Integer.class);
// datosOrg.put("Sueldo", 3000);
//
// Ubicacion direccion = new Ubicacion("Mozart", 5.5, 6.5);
// Heladera heladera = new Heladera("Helados", direccion, 10, LocalDate.now());
// donacionVianda = new DonacionVianda(heladera, null, null);
// colaboradorHumano = new ColaboradorHumana(unaOrg,"hola@frba.com", 011551355,
// null, datosOrg,
// direccion, "Juan", "Perez", LocalDate.now());
// //colaboradorHumano.setOrg(unaOrg);
// vianda = new Vianda("Papas fritas", LocalDate.now(), LocalDate.now(),
// colaboradorHumano, 0, 0,
// heladera);
// //colaboradorHumano.agregarColaboracion(donacionVianda);
//
// }
//
// @Test
// void elColaboradorHumanoPuedeRealizarDonacionVianda() {
// colaboradorHumano.agregarColaboracion(donacionVianda);
// Assertions.assertEquals(1, colaboradorHumano.getCantidadColaboraciones());
// }
// }
