package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SessionController;
import ar.edu.utn.frba.dds.scripts.Bootstrap;
import com.github.jknack.handlebars.Context;
import io.javalin.Javalin;
import java.util.Map;

/**
 * Rutas del sistema.
 */
public class Router {
  /**
   * Configuración.
   *
   * @param app parámetro de configuración.
   */
  public void configure(Javalin app) {
    HomeController controller = new HomeController();
    var session = new SessionController();
    app.get("/", ctx -> {
      ctx.redirect("/home");
    });
    app.get("/home", ctx -> {
      ctx.render("home.hbs", controller.mostrarLanding(ctx));
    });
    app.get("/colaborar", ctx -> {
      ctx.render("colaborar.hbs", controller.index(ctx));
    });
    app.get("/donar_dinero", ctx -> {
      ctx.render("donar_dinero.hbs", controller.index(ctx));
    });
    app.get("/donar_vianda", ctx -> {
      ctx.render("donar_vianda.hbs", controller.crearDonarVianda(ctx));
    });
    app.get("/registrar_beneficiario", ctx -> {
      ctx.render("registrar_beneficiario.hbs");
    });
    app.get("/distribuir_viandas", ctx -> {
      ctx.render("distribuir_viandas.hbs");
    });
    app.post("/donar_dinero", ctx -> {
      ctx.render("donar_dinero.hbs", controller.crearDonarDinero(ctx));
    });
    app.post("/donar_vianda", ctx -> {
      ctx.render("donar_vianda.hbs", controller.crearDonarVianda(ctx));
    });
    app.post("/registrar_beneficiario", ctx -> {
      ctx.render("registrar_beneficiario.hbs",
          controller.crearRegistrarBeneficiario(ctx));
    });
    app.post("/distribuir_viandas", ctx -> {
      ctx.render("distribuir_viandas.hbs", controller.crearDistribucionDeViandas(ctx));
    });
    app.get("/heladeras", ctx -> {
      ctx.render("heladeras.hbs", controller.index(ctx));
    });
    app.get("/carga_csv", ctx -> {
      ctx.render("carga_csv.hbs");
    });
    app.get("/reportes", ctx -> {
      ctx.render("reportes.hbs");
    });
    app.get("/nueva_heladera", ctx -> {
      ctx.render("nueva_heladera.hbs");
    });
    // Esto es temporal hasta que pongamos las rutas dinamicas con el ID
    // heladera
    app.get("/heladeras/{id}", ctx -> {
      Map<String, Object> model = new HomeController().mostrarDetallesHeladera(ctx);
      ctx.render("heladera_x.hbs", model);
      // Usa el nombre del archivo que
      // contiene la plantilla
    });
    app.get("/heladera_x_reportar_falla", ctx -> {
      ctx.render("reportar_falla_heladera.hbs");
    });
    app.get("/login", session::show);
    app.post("/login", session::create);
    app.get("/logout", session::logout);
  }
  
  private static boolean isUserLoggedIn(Context ctx) {
    // return ctx.sessionAttribute("user") != null;
    return ctx.get("user") != null;
  }
}
/*
 * app.put("/heladeras", ctx -> { // Obtener parámetros desde el formulario
 * String nombreHeladera = ctx.formParam("nombre"); String capacidadStr =
 * ctx.formParam("capacidad"); int capacidad = Integer.parseInt(capacidadStr);
 *
 * //TODO: ver como hacemos para ingresar la ubicacion
 *
 * // Crear una nueva heladera Heladera nuevaHeladera = new
 * Heladera(organizacion, nombreHeladera, null, null, null, capacidad,
 * LocalDate.now(), null, null); nuevaHeladera.setNombre(nombreHeladera);
 * nuevaHeladera.setCapacidad(capacidad);
 *
 * // Lógica para guardar la nueva heladera en la base de datos
 * organizacion.guardarHeladera(nuevaHeladera); // Debes implementar este método
 * en tu clase organizacion
 *
 * ctx.status(201).result("Heladera creada exitosamente."); });
 *
 *
 * app.get("/heladeras/{id}", ctx -> { long id =
 * Long.parseLong(ctx.pathParam("id")); Optional<Heladera> heladera =
 * organizacion.buscarHeladeraPorId(id);
 *
 * if(heladera == null){ ctx.status(404).result("No se encontró la heladera.");
 * return; }
 *
 * ctx.render("detalle_heladera.hbs", Map.of("heladera", heladera.get())); });
 *
 * app.get("/heladeras/{id}/incidentes", ctx -> { long id =
 * Long.parseLong(ctx.pathParam("id")); Optional<Heladera> heladera =
 * organizacion.buscarHeladeraPorId(id);
 *
 * if(heladera == null){ ctx.status(404).result("No se encontró la heladera.");
 * return; } List<Incidente> incidentes =
 * heladera.get().getRegistroIncidentes();
 *
 * ctx.render("detalle_heladera.hbs", Map.of( "heladera", heladera.get(),
 * "incidentes", incidentes )); });
 *
 * app.post("/heladeras/{id}/incidentes", ctx -> {
 *
 * long id = Long.parseLong(ctx.pathParam("id")); Optional<Heladera>
 * heladera_opcional = organizacion.buscarHeladeraPorId(id);
 *
 * if (heladera_opcional.isEmpty()) {
 * ctx.status(404).result("No se encontró la heladera."); return; }
 *
 *
 *
 * String descripcion = ctx.formParam("descripcion"); LocalDateTime fecha =
 * LocalDateTime.now();
 *
 * TipoIncidente tipoIncidente = new TipoIncidente(false, /* Lo sacamos del la
 * sesion
 *//* , descripcion, /*Foto de la heladera URL *//*
                                                  * , null)
                                                  *
                                                  * Heladera heladera =
                                                  * heladera_opcional.get();
                                                  *
                                                  * Incidente nuevoIncidente =
                                                  * new
                                                  * Incidente(null,tipoIncidente
                                                  * ,fecha, heladera);
                                                  *
                                                  * });
                                                  */