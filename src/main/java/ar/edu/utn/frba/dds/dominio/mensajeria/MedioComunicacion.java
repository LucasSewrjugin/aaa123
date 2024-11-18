package ar.edu.utn.frba.dds.dominio.mensajeria;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Medio de comunicación para suscribirse.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MedioComunicacion {

  @Id
  @GeneratedValue
  private Long id;

  /**
   * Método para enviar mensaje.
   *
   * @param mensaje Mensaje a enviar.
   */
  public abstract void enviarMensaje(String mensaje);
}