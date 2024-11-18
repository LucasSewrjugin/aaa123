package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.scripts.Bootstrap;
import ar.edu.utn.frba.dds.server.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.server.templates.JavalinRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

/**
 * Inicialización de front.
 */
public class Server {
  /**
   * Inicialización de Javalin.
   */
  public void start() {
    var app = Javalin.create(config -> {
      initializeStaticFiles(config);
      config.staticFiles.add("/assets");
      initializeTemplating(config);
    });
    new Router().configure(app);

    int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));

    app.start(port);
  }



  private void initializeTemplating(JavalinConfig config) {
    Handlebars handlebars = new Handlebars();
    ObjectMapper objectMapper = new ObjectMapper();
    handlebars.registerHelper("json", (context, options) -> {
      try {
        return objectMapper.writeValueAsString(context);
      } catch (Exception e) {
        throw new RuntimeException("Error al convertir a JSON", e);
      }
    });
    config.fileRenderer(
        new JavalinRenderer().register("hbs", new JavalinHandlebars()));
  }
  
  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/assets";
      staticFileConfig.directory = "/assets";
    });
  }
}