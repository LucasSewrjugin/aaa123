package ar.edu.utn.frba.dds.dominio.colaboraciones;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class DonacionDinero extends Colaboracion {

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private BigDecimal monto;

  @Column
  private Integer frecuencia;

  public DonacionDinero(LocalDate unaFecha, BigDecimal unMonto, Integer unaFrecuencia) {
    this.fecha = unaFecha;
    this.monto = unMonto;
    this.frecuencia = unaFrecuencia;
    this.tipoColaboracion = TipoColaboracion.DONAR_DINERO;
  }

  @Override
  public void contribuir() {
    // TODO: El dinero no se agrega a ningun lado

  }

  public BigDecimal getMonto() {
    return this.monto;
  }
}
