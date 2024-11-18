package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * Sender de mensajería instantánea.
 */
public interface InstantMessageSender {
  /**
   * Envía un mensaje.
   *
   * @param  provider                        Proveedor de mensajería
   *                                         instantánea.
   * @param  telephone                       Teléfono celular.
   * @param  message                         Mensaje a enviar.
   * @throws InvalidTelephoneNumberException Si el teléfono no corresponde a un
   *                                         abonado en servicio, se lanza
   *                                         error.
   */
  void sendMessage(InstantMessageApp provider, String telephone, String message)
      throws InvalidTelephoneNumberException;
}
