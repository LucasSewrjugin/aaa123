package ar.edu.utn.frba.dds.dominio.colaboraciones;

import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Herencia correspondiente a colocar una heladera.
 */
@Entity
@Table
public class ColocarHeladera extends Colaboracion {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  private Heladera heladera;

  @OneToOne
  private ColaboradorJuridico colaborador;

  /**
   * Constructor principal.
   *
   * @param unaHeladera Heladera que se va a colocar.
   * @param colaborador Colaborador que realiza la acción.
   */
  public ColocarHeladera(Heladera unaHeladera, ColaboradorJuridico colaborador) {
    this.colaborador = colaborador;
    this.heladera = unaHeladera;
    this.tipoColaboracion = TipoColaboracion.COLOCAR_HELADERA;
  }

  /**
   * Forma de contribución.
   */
  public void contribuir() {
    colaborador.hacerseCargoDe(heladera);
  }

}
