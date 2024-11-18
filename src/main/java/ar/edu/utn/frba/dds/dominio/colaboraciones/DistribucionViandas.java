package ar.edu.utn.frba.dds.dominio.colaboraciones;

import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Colaboración extendida a la acción de distribuir las viandas.
 */
@Entity
public class DistribucionViandas extends Colaboracion {
  @OneToOne
  private final Heladera heladeraOrigen;
  @OneToOne
  private final Heladera heladeraDestino;
  @Column
  private final Integer cantidadViandas;
  @Enumerated(EnumType.STRING)
  private final MotivoDistribucionVianda motivo;
  @OneToOne
  private final ColaboradorHumana colaborador;
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String tarjetaCodigo;
  
  /**
   * Constructor principal.
   *
   * @param colaborador     Colaborador que realiza la acción.
   * @param heladeraOrigen  Heladera de donde se sacan las viandas (origen).
   * @param heladeraDestino Heladera donde se colocarán (destino).
   * @param cantidadViandas Cantidad de viandas que se trasladan.
   * @param motivo          Motivo por el cual se realiza la colaboración.
   * @param unaFecha        Fecha de la colaboración.
   */
  public DistribucionViandas(ColaboradorHumana colaborador, Heladera heladeraOrigen,
      Heladera heladeraDestino,
      Integer cantidadViandas, MotivoDistribucionVianda motivo, LocalDate unaFecha) {
    if (cantidadViandas <= 0) {
      throw new IllegalArgumentException(
          "La cantidad de viandas a distribuir debe ser una cantidad positiva");
    }
    // if (cantidadViandas > heladeraOrigen.getViandas().size())
    // throw new IllegalArgumentException(
    // "La cantidad de viandas a distribuir no debe superar el total de viandas
    // de la heladera origen");
    this.colaborador = colaborador;
    this.heladeraOrigen = heladeraOrigen;
    this.heladeraDestino = heladeraDestino;
    this.cantidadViandas = cantidadViandas;
    this.motivo = motivo;
    this.fecha = unaFecha;
    this.tipoColaboracion = TipoColaboracion.DISTRIBUIR_VIANDA;
  }
  
  /**
   * Forma de contribuir.
   */
  public void contribuir() {
    heladeraOrigen.notificarSolicitudApertura(tarjetaCodigo);
    heladeraDestino.notificarSolicitudApertura(tarjetaCodigo);
    for (int i = 0; i < cantidadViandas; i++) { // revisar si es < o <=
      Vianda viandaAuxiliar = heladeraOrigen.retirarPrimeraVianda();
      heladeraDestino.ingresarVianda(viandaAuxiliar);
    } 
    heladeraOrigen.registrarLog(tarjetaCodigo,
        "Apertura de heladera para distribuir vianda");
    heladeraDestino.registrarLog(tarjetaCodigo,
        "Apertura de heladera para distribuir vianda");
  }
  
  @Override
  public boolean necesitaTarjeta() {
    return true;
  }
  
  @Override
  public void registrarAccion(String tarjetaCodigo) {
    this.tarjetaCodigo = tarjetaCodigo;
    heladeraOrigen.registrarLog(tarjetaCodigo,
        "Solicitud de apertura de heladera para retirar una vianda para distribucion");
    heladeraDestino.registrarLog(tarjetaCodigo,
        "Solicitud de apertura de heladera para ingresar una vianda distribuida");
  }
  
  public Heladera getHeladeraDestino() {
    return heladeraDestino;
  }
  
  public Heladera getHeladeraOrigen() {
    return heladeraOrigen;
  }
  
  public Integer getCantidadDeViandas() {
    return cantidadViandas;
  }
  
  public ColaboradorHumana getColaborador() {
    return colaborador;
  }
}