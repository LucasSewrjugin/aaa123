package ar.edu.utn.frba.dds.dominio.mensajeria;

/**
 * Excepción ocurrida en la suscripción por mail.
 */
public class MailException extends RuntimeException {
  /**
   * Constructor principal.
   *
   * @param mensaje Mensaje de informe del error.
   */
  public MailException(String mensaje) {
    super(mensaje);
  }
}