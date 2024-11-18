package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.registro.RepositorioUsuarios;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de la sesión.
 */
public class SessionController {
  /**
   * Nos retorna la vista de login.
   *
   * @param context Contexto.
   */
  public void show(Context context) {
    Map<String, Object> modelo = new HashMap<>();
    if ("true".equals((context.queryParam("error")))) {
      modelo.put("error", "usuario o contraseña invalidas");
    }
    if (context.sessionAttribute("user_id") != null) {
      context.redirect("/home");
    }
    context.render("login.hbs", modelo);
  }
  
  /**
   * Crea la sesión. Si fue exitosa, redirige hacia la vista de colaboración. Caso contrario,
   * devuelve al login con error.
   *
   * @param context Contexto del cual se obtienen las credenciales de acceso.
   */
  public void create(Context context) {
    try {
      var usuario = RepositorioUsuarios.getInstance()
          .buscarColaboradorHumana(context.formParam("nombre"), context.formParam("password"));
      context.sessionAttribute("user_id", usuario.getId());
      context.redirect("/colaborar");
    } catch (Exception e) {
      e.printStackTrace();
      context.redirect("/login?error=true");
    }
  }

  /**
   * Método para desloguearse del sistema.
   *
   * @param context Contexto recibido.
   */
  public void logout(Context context) {
    context.req().getSession().invalidate(); // Invalida la sesión actual
    context.redirect("/home"); // Redirige a la página de inicio
  }
}