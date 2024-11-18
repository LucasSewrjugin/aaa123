package ar.edu.utn.frba.dds.dominio.incidente;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Tipos mapeados de incidentes.
 */
@Entity
@Table(name = "TipoIncidente")
public record TipoIncidente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id, Boolean esAlerta,
    @OneToOne Colaborador cobaborador,
    @Column String descripcion, @Column String urlFoto, @Column String mensaje) {
  /**
   * Constructor principal.
   *
   * @param esAlerta Determina si es o no un aviso de falla.
   * @param colaborador Colaborador que realiza el reporte.
   * @param descripcion Descripción del incidente.
   * @param urlFoto Foto linkeada por URL.
   * @param mensaje Mensaje del colaborador.
   * @return Devuelve la instancia.
   */
  public static TipoIncidente crearSinId(Boolean esAlerta, Colaborador colaborador,
      String descripcion, String urlFoto, String mensaje) {
    return new TipoIncidente(null, esAlerta, colaborador, descripcion, urlFoto, mensaje);
  }
}