package ar.edu.utn.frba.dds.registro;

import java.util.function.Consumer;

/**
 * Validador de contraseñas.
 */
public class ValidacionContrasenia {
  private Consumer<String> prueba;

  /**
   * Construcor principal.
   *
   * @param prueba Contraseña a evaluar.
   */
  public ValidacionContrasenia(Consumer<String> prueba) {
    this.prueba = prueba;
  }
  
  /**
   * Validador.
   *
   * @param password Contraseña a evaluar.
   */
  public void validar(String password) {
    prueba.accept(password);
  }
}
