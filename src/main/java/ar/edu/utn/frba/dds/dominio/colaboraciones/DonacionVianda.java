package ar.edu.utn.frba.dds.dominio.colaboraciones;

import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Colaboración extendida a la acción de donación de viandas.
 */
@Entity
public class DonacionVianda extends Colaboracion {

  @OneToOne
  private final Heladera heladera;
  @OneToOne
  private final Vianda vianda;
  @OneToOne
  private final ColaboradorHumana colaborador;
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private boolean entregada;
  @Column
  private String tarjetaCodigo;

  /**
   * Constructor principal.
   *
   * @param colaborador Colaborador que realiza la acción.
   * @param unaFecha Fecha de realización de la acción.
   * @param unaHeladera Heladera donde se colocará la vianda.
   * @param unaVianda Vianda a colocar.
   * @param entregado Resultado de la operación.
   */
  public DonacionVianda(ColaboradorHumana colaborador, LocalDate unaFecha, Heladera unaHeladera,
                        Vianda unaVianda, boolean entregado) {
    this.colaborador = colaborador;
    this.heladera = unaHeladera;
    this.vianda = unaVianda;
    this.entregada = entregado;
    this.tipoColaboracion = TipoColaboracion.DONAR_VIANDA;
    this.fecha = unaFecha;
  }

  /////////////////////
  // COMPORTAMIENTO //
  ///////////////////

  @Override
  public void contribuir() {
    if (entregada) {
      throw new RuntimeException("La vianda ya fue entregada");
    }
    heladera.notificarSolicitudApertura(tarjetaCodigo);
    heladera.ingresarVianda(vianda);
    heladera.registrarLog(tarjetaCodigo,
        "Apertura de heladera para donacion de vianda");
  }

  @Override
  public void registrarAccion(String tarjetaCodigo) {
    this.tarjetaCodigo = tarjetaCodigo;
    heladera.registrarLog(tarjetaCodigo,
        "Solicitud de apertura de heladera para donacion de vianda");
  }

  @Override
  public boolean necesitaTarjeta() {
    return true;
  }


  ////////////////
  /// SETTERS ///
  ///////////////

  public void setEntregada(boolean entregada) {
    this.entregada = entregada;
  }

  ////////////////
  /// GETTERS ///
  //////////////

  public Vianda getVianda() {
    return vianda;
  }

  public Heladera getHeladera() {
    return heladera;
  }

  public ColaboradorHumana getColaborador() {
    return colaborador;
  }
}
