package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * Excepción lanzada al enviar un mensaje a un teléfono no válido.
 */
public class InvalidTelephoneNumberException extends RuntimeException {
  /**
   * Excepción de número no existente.
   *
   * @param mensaje Mensaje a enviar.
   */
  public InvalidTelephoneNumberException(String mensaje) {
    super(mensaje);
  }
}
