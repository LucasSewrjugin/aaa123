package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.registro.RepositorioHeladera;
import ar.edu.utn.frba.dds.registro.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Controlador backend de vista Home y asociados.
 */
public class HomeController implements WithSimplePersistenceUnit, TransactionalOps {
  /**
   * Vista principal.
   *
   * @param  ctx Contexto devuelto.
   * @return     Devuelve la información de las heladeras con las que el
   *             colaborador puede interactuar.
   */
  public Map<String, Object> index(Context ctx) {
    // Obtén el id del colaborador desde la sesión
    Long idColaborador = ctx.sessionAttribute("user_id");
    if (idColaborador != null) {
      ColaboradorHumana colaborador = RepositorioUsuarios.getInstance()
          .buscarColaboradorHumanaId(idColaborador);
      if (colaborador != null) {
        Organizacion organizacion = colaborador.getOrganizacion();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", organizacion.getHeladeras());
        List<Heladera> heladeras = organizacion.getHeladeras();
        List<String> heladerasJson = heladeras.stream().map(heladera -> {
          try {
            return heladera.toJson();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }).toList();
        model.put("userName", colaborador.getNombre());
        model.put("heladerasJson", heladerasJson);
        return model;
      }
    }
    return Collections.emptyMap();
  }
  
  /**
   * Muestra los detalles de la heladera.
   *
   * @param  ctx Contexto del cual recibe los datos de sesión.
   * @return     Devuelve la información de la heladera a partir del id obtenido
   *             de la misma.
   */
  public Map<String, Object> mostrarDetallesHeladera(Context ctx) {
    // Supongamos que obtienes el ID de la heladera desde la URL
    String idParam = ctx.pathParam("id");
    Long id = Long.parseLong(idParam);
    // Convertir a Long si la ID es de este
    // tipo en la DB
    // Busca la heladera en tu repositorio o servicio
    Heladera heladera = RepositorioHeladera.getInstance().buscarHeladeraId(id);
    // Prepara los datos para Handlebars
    Map<String, Object> model = new HashMap<>();
    model.put("nombre", heladera.getNombre());
    model.put("fechaEstreno", heladera.getFechaEstreno().toString());
    model.put("temperatura", heladera.getTemperatura());
    model.put("capacidad", heladera.getCapacidad());
    return model;
  }
  
  /**
   * Muestra la vista principal.
   *
   * @param  ctx Contexto del cual recibe los datos de sesión.
   * @return     Devuelve el modelo.
   */
  public Map<String, Object> mostrarLanding(Context ctx) {
    // Obtén el id del colaborador desde la sesión
    Long idColaborador = ctx.sessionAttribute("user_id");
    Map<String, Object> model = new HashMap<>();
    if (idColaborador != null) {
      ColaboradorHumana colaborador = RepositorioUsuarios.getInstance()
          .buscarColaboradorHumanaId(idColaborador);
      if (colaborador != null) {
        model.put("userName", colaborador.getNombre());
      }
    }
    return model;
  }
  
  /**
   * Instancia una donación de dinero.
   *
   * @param  ctx Contexto recibido.
   * @return     Devuelve lo solicitado.
   */
  public Map<String, Object> crearDonarDinero(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    try {
      String montoStr = ctx.formParam("monto");
      String frecuenciaStr = ctx.formParam("frecuencia");
      LocalDate fecha = LocalDate.now();
      BigDecimal monto = new BigDecimal(montoStr);
      Integer frecuencia = Integer.parseInt(frecuenciaStr);
      DonacionDinero donacion = new DonacionDinero(fecha, monto, frecuencia);
      // Añadir la instancia al modelo para usarla en la vista o para debug
      model.put("donacion", donacion);
      model.put("success", true);
    } catch (Exception e) {
      model.put("error", "Datos inválidos o incompletos: " + e.getMessage());
      model.put("success", false);
    }
    return model;
  }
  
  /**
   * Método para realizar una donación de vianda desde la interfaz del sistema.
   *
   * @param  ctx Contexto recibido.
   * @return Devuelve la donación mapeada.
   */
  public Map<String, Object> crearDonarVianda(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    return model;
  }
  
  /**
   * Método para registrar un beneficiario desde la interfaz del sistema.
   *
   * @param  ctx Contexto recibido.
   * @return Devuelve el registro mapeado.
   */
  public Map<String, Object> crearRegistrarBeneficiario(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    return model;
  }
  
  /**
   * Método para realizar una distribución de viandas desde la interfaz del sistema.
   *
   * @param  ctx Contexto recibido.
   * @return Devuelve la donación mapeada.
   */
  public Map<String, Object> crearDistribucionDeViandas(@NotNull Context ctx) {
    Map<String, Object> model = new HashMap<>();
    return model;
  }
  
}
