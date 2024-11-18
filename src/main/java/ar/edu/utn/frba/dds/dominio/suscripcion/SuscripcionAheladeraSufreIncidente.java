package ar.edu.utn.frba.dds.dominio.suscripcion;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.mensajeria.MedioComunicacion;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Suscripción correspondiente a los incidentes que sufre la heladera.
 */
@Entity
public class SuscripcionAheladeraSufreIncidente extends Suscripcion {
  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  private Colaborador suscriptor;
  @OneToOne
  private MedioComunicacion contacto;
  
  /**
   * Constructor principal.
   *
   * @param suscriptor Colaborador que se suscribe.
   * @param contacto   Contacto del colaborador.
   */
  public SuscripcionAheladeraSufreIncidente(Colaborador suscriptor,
      MedioComunicacion contacto) {
    this.suscriptor = suscriptor;
    this.contacto = contacto;
  }
  
  /**
   * Notifica al colaborador.
   */
  public void notificar(Heladera heladera) {
    contacto.enviarMensaje(
        "La heladera " + heladera.getNombre() + " sufrió una falla y se desactivo");
    // metodo.enviarMensaje(/* OFRECER OPCIONES AL COLABORADOR PARA QUE MUEVA
    // LAS VIANDAS */);
  }
}