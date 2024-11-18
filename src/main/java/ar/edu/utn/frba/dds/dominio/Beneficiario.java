package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.colaboradores.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.tarjeta.Tarjeta;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Instancia de beneficiario.
 */
@Entity
public class Beneficiario {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String nombre;
  @Column
  private LocalDate nacimiento;
  @Column
  private LocalDate fechaRegistro = LocalDate.now();
  @Embedded
  private Ubicacion domicilio;
  @Column
  private Integer documento;
  @Column
  private Integer menoresAcargo;
  @OneToOne
  private Tarjeta tarjeta;
  @Enumerated(EnumType.STRING)
  private TipoDocumento tipoDocumento;
  ///////////////////////
  //// CONSTRUCTOR ////
  /////////////////////
  
  /**
   * Costructor principal.
   *
   * @param nombre Nombre del beneficiario.
   * @param nacimiento Fecha de nacimiento.
   * @param unaUbicacion Ubicación del mismo.
   * @param documento Documento del beneficiario.
   * @param tipoDocumento Tipo de documento.
   * @param menoresAcargo Cantidad de hijos.
   * @param tarjeta Tarjeta de acceso a viandas.
   */
  public Beneficiario(String nombre, LocalDate nacimiento, Ubicacion unaUbicacion,
      Integer documento, TipoDocumento tipoDocumento, Integer menoresAcargo,
      Tarjeta tarjeta) {
    if (nombre.isEmpty() || nacimiento == null) {
      throw new IllegalArgumentException(
          "Debe ingresar obligatoriamente nombre y nacimiento");
    }
    this.nombre = nombre;
    this.nacimiento = nacimiento;
    this.domicilio = unaUbicacion;
    this.documento = documento;
    this.tipoDocumento = tipoDocumento;
    this.menoresAcargo = menoresAcargo;
    this.tarjeta = tarjeta;
  }
  ///////////////////////
  //// COMPORTAMIENTO //
  /////////////////////
  
  /**
   * Método para que un beneficiario reciba una tarjeta de acceso a las heladeras.
   *
   * @param tarjeta Tarjeta que se registra a nombre del beneficiario.
   */
  public void recibirTarjeta(Tarjeta tarjeta) {
    this.tarjeta = tarjeta;
  }
  
  /**
   * Método para que un beneficiario tome una vianda desde una heladera.
   *
   * @param heladera Heladera desde donde se retira la vianda.
   * @param beneficiario Beneficiario que retira.
   */
  public void tomarViandaDe(Heladera heladera, Beneficiario beneficiario) {
    tarjeta.utilizarEn(heladera, beneficiario);
    heladera.quitarUnaVianda();
  }
  ///////////////////////
  //// GETTERS ////
  /////////////////////
  
  public Integer getMenoresAcargo() {
    return this.menoresAcargo;
  }
  
  public String getNombre() {
    return this.nombre;
  }
  ///////////////////////
  //// SETTERS ////
  /////////////////////
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public void setNacimiento(LocalDate fechaNacimiento) {
    this.nacimiento = fechaNacimiento;
  }
}
