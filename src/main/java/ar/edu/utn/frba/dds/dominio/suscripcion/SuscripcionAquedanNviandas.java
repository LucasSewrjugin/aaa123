package ar.edu.utn.frba.dds.dominio.suscripcion;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.mensajeria.MedioComunicacion;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Suscripción correspondiente a cuántas viandas quedan en la heladera.
 */
@Entity
public class SuscripcionAquedanNviandas extends Suscripcion {
  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  private Colaborador suscriptor;
  @Transient
  private MedioComunicacion contacto;
  @Column
  private int viandasParaElAviso;
  
  /**
   * Constructor principal.
   *
   * @param suscriptor         Colaborador que se suscribe.
   * @param contacto           Contacto proporcionado por el colaborador.
   * @param viandasParaElAviso Cantidad de viandas que se informa.
   */
  public SuscripcionAquedanNviandas(Colaborador suscriptor, MedioComunicacion contacto,
      int viandasParaElAviso) {
    this.suscriptor = suscriptor;
    this.contacto = contacto;
    this.viandasParaElAviso = viandasParaElAviso;
  }
  
  /**
   * Revisa si la cantidad a notificar es menor o igual a las viandas que se informan.
   *
   * @param heladera Heladera en cuestión.
   * @return V o F según corresponda.
   */
  private Boolean quedanSoloNviandas(Heladera heladera) {
    return heladera.cantidadDeViandas() <= viandasParaElAviso;
  }
  
  /**
   * Notifica.
   */
  public void notificar(Heladera heladera) {
    if (quedanSoloNviandas(heladera)) {
      contacto.enviarMensaje("Quedan " + viandasParaElAviso + " viandas en la heladera "
          + heladera.getNombre());
    }
  }
}