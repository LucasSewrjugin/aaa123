package ar.edu.utn.frba.dds.dominio.colaboraciones;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Colaboracion {
  @Id
  @GeneratedValue
  protected long id;

  @Enumerated(EnumType.STRING)
  protected TipoColaboracion tipoColaboracion;

  @Column
  protected LocalDate fecha;

  public abstract void contribuir();

  public LocalDate getFecha() {
    return this.fecha;
  }

  public TipoColaboracion getTipoColaboracion() {
    return this.tipoColaboracion;
  }

  public boolean necesitaTarjeta() {
    return false;
  }

  public void registrarAccion(String tarjetaCodigo) {
  }
}
