package ar.edu.utn.frba.dds.dominio.mensajeria;

import ar.edu.utn.frba.dds.dominio.serviciosexternos.InstantMessageApp;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.InstantMessageSender;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

/**
 * Mensajería por teléfono.
 */
@Entity
public class MensajeriaTelefono extends MedioComunicacion {
  @Transient
  // NO NOS INTERESA PERSISTIRLO PORQUE ES EXTERNO Y SE CREA CUANDO INICIALIZO
  // EL PROGRAMA
  private InstantMessageSender messageSender;
  @Enumerated(EnumType.STRING)
  private InstantMessageApp servicio;
  @Column
  private String telefono;
  
  /**
   * Constructor principal.
   *
   * @param messageSender Mensaje a enviar.
   * @param servicio Servicio de mensajería.
   * @param telefono Teléfono destinatario.
   */
  public MensajeriaTelefono(InstantMessageSender messageSender,
      InstantMessageApp servicio, String telefono) {
    this.messageSender = messageSender;
    this.servicio = servicio;
    this.telefono = telefono;
  }
  
  /**
   * Método para enviar mensaje.
   */
  public void enviarMensaje(String mensaje) {
    messageSender.sendMessage(servicio, telefono, mensaje);
  }
}