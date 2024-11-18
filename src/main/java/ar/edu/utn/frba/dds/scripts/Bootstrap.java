package ar.edu.utn.frba.dds.scripts;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.RepositorioPersistencia;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePeso;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePesoConWsensor;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.registro.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bootstrap.
 */
public class Bootstrap implements WithSimplePersistenceUnit {
  /**
   * Inicializamos todo el sistema (momentáneamnete).
   */
  public void init() {
    withTransaction(() -> {
      Organizacion unaOrg = new Organizacion();
      Ubicacion unaUbicacionH = new Ubicacion("Medrano 951", -34.598643, -58.419974);
      LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
      ColaboradorHumana colaboradorH =
          new ColaboradorHumana(unaOrg, "jperez@frba.utn.edu.ar", 12348765, 1143218765,
              unaUbicacionH, "Juan", "Perez", fechaNacimiento);
      colaboradorH.setTarjetaCodigo("12345");
      colaboradorH.setUsername("qwe");
      colaboradorH.setPassword("asd");
      ColaboradorHumana colaboradorH2 =
          new ColaboradorHumana(unaOrg, "ctizi@frba.utn.edu.ar", 12348765, 1143218765,
              unaUbicacionH, "Carlos", "Tizi", fechaNacimiento);
      colaboradorH2.setTarjetaCodigo("123456");
      colaboradorH2.setUsername("qwer");
      colaboradorH2.setPassword("asdf");
      RepositorioUsuarios.getInstance().registrarOrg(unaOrg);
      RepositorioUsuarios.getInstance().registrarColaboradorHumana(colaboradorH);
      RepositorioUsuarios.getInstance().registrarColaboradorHumana(colaboradorH2);
      Vianda arrozConPollo = new Vianda("Arroz con Pollo",
          LocalDateTime.now().plusDays(30), LocalDateTime.now(), colaboradorH, 150, 50.0);
      Vianda salchichaConPure = new Vianda("Salchichas con puré",
          LocalDateTime.now().plusDays(45), LocalDateTime.now(), colaboradorH, 150, 50.0);
      List<Vianda> listaViandas1 = new ArrayList<>();
      listaViandas1.add(salchichaConPure);
      listaViandas1.add(arrozConPollo);
      List<Vianda> listaViandas2 = new ArrayList<>();
      listaViandas2.add(salchichaConPure);
      listaViandas2.add(arrozConPollo);
      listaViandas2.add(arrozConPollo);
      LocalDate fechaEstreno = LocalDate.of(2012, 2, 1);
      Ubicacion otraUbicacion = new Ubicacion("Mozart 2300", -34.659713, -58.468426);
      Heladera heladera = new Heladera(unaOrg, "Heladera Sede Campus", listaViandas1,
          otraUbicacion, 10.0, fechaEstreno, null, null, null);
      LocalDate otraFechaEstreno = LocalDate.of(2013, 2, 1);
      Heladera otraHeladera = new Heladera(unaOrg, "Heladera Sede Medrano", listaViandas2,
          unaUbicacionH, 10.0, otraFechaEstreno, null, null, null);
      /*
       * repositorio.registrarHeladera(heladera);
       * repositorio.registrarHeladera(otraHeladera);
       */
      unaOrg.altaHeladera(heladera);
      unaOrg.altaHeladera(otraHeladera);
      /*
       * public Heladera(Organizacion organizacion, String nombre, List<Vianda>
       * viandas, Ubicacion ubicacion, Double capacidad, LocalDate
       * unaFechaEstreno, SensorDePeso sensorDePeso, SensorDeTemperatura
       * sensorDeTemperatura, ControladorDeAccesoAdapter controlador)
       */
    });
  }
}
