package ar.edu.utn.frba.dds.dominio.tarjeta;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// Justificacion: Es menos engorroso utilizar un Hashh map con los registros
// nos resulta mas facil hacer un Data object que cosifique el registro

/**
 * Registro de usos de la tarjeta.
 */
@Entity
@Table(name = "Usos de tarjetas")
public class UsoDeTarjeta {
  @Id
  @GeneratedValue
  private Long id;
  @OneToOne
  private Heladera heladera;
  @OneToOne
  private Beneficiario beneficiario;
  @Column
  private LocalDate fechaTransaccion;
  
  /**
   * Constructor principal.
   *
   * @param heladera Heladera donde se utiliza la tarjeta.
   * @param beneficiario Beneficiario dueño de la tarjeta.
   * @param fechaTransaccion Fecha en la que se usó la tarjeta.
   */
  public UsoDeTarjeta(Heladera heladera, Beneficiario beneficiario,
      LocalDate fechaTransaccion) {
    this.heladera = heladera;
    this.beneficiario = beneficiario;
    this.fechaTransaccion = fechaTransaccion;
  }
  
  /**
   * Determina si el uso informado se realizó en la fecha especificada.
   *
   * @param fecha Fecha a evaluar.
   * @return V o F según corresponda.
   */
  public Boolean soyDeFecha(LocalDate fecha) {
    return fechaTransaccion == fecha;
  }
}
