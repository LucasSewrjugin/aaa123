package ar.edu.utn.frba.dds.dominio.suscripcion;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Suscripción.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Suscripcion {
  @Id
  @GeneratedValue
  private Long id;
  
  /**
   * Notifica sobre una heladera.
   *
   * @param heladera Heladera donde se realizarán las notificaciones.
   */
  public abstract void notificar(Heladera heladera);
}