package ar.edu.utn.frba.dds.dominio.tarjeta;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Instancia de tarjeta.
 */
@Entity
@Table(name = "Tarjetas")
public class Tarjeta {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String codigo;
  @Column
  @OneToMany
  @JoinColumn(name = "UsosDeTarjeta")
  private List<UsoDeTarjeta> usos = new ArrayList<>();
  @Column
  private LocalDate fechaDeCreacion = LocalDate.now();
  
  /**
   * Constructor principal.
   *
   * @param codigo Código de la tarjeta.
   */
  public Tarjeta(String codigo) {
    this.codigo = codigo;
  }
  //////////////////////////
  // COMPORTAMIENTO ///
  ////////////////////////
  
  /**
   * Utiliza la tarjeta en una heladera para retirar una vianda.
   *
   * @param heladera     Heladera donde opera el beneficiario.
   * @param beneficiario Beneficiario que utiliza la heladera.
   */
  public void utilizarEn(Heladera heladera, Beneficiario beneficiario) {
    if (!meQuedanUsos(beneficiario)) {
      throw new RuntimeException("No quedan mas usos en la tarjeta, espere a otro dia");
    }
    UsoDeTarjeta usoNuevo = new UsoDeTarjeta(heladera, beneficiario, LocalDate.now());
    this.agregarUso(usoNuevo);
  }
  
  /**
   * Registra un nuevo uso de la tarjeta.
   *
   * @param usoNuevo Uso a registrar.
   */
  public void agregarUso(UsoDeTarjeta usoNuevo) {
    usos.add(usoNuevo);
  }
  
  /**
   * Informa cuántos usos diarios quedan disponibles.
   *
   * @param beneficiario Beneficiario que intenta utilizar la tarjeta.
   * @return Devuelve la cantidad de usos que aún puede realizar.
   */
  public int usosDeHoyRestantes(Beneficiario beneficiario) {
    return usosMaximos(beneficiario) - cantidadDeUsosDeHoy();
  }
  
  /**
   * Obtiene los usos que se realizaron.
   *
   * @return Devuelve el informe listado.
   */
  public List<UsoDeTarjeta> obtenerUsosDeHoy() {
    return this.obtenerUsosDe(LocalDate.now());
  }
  
  /**
   * Obtiene los usos de una fecha específica.
   *
   * @param fecha Fecha a evaluar.
   * @return Devuelve el informe listado.
   */
  public List<UsoDeTarjeta> obtenerUsosDe(LocalDate fecha) {
    return usos.stream().filter(uso -> uso.soyDeFecha(fecha)).toList();
  }
  
  /**
   * Obtiene los usos de hoy.
   *
   * @return Devuelve la cantidad de veces que se usó en el día de hoy.
   */
  public int cantidadDeUsosDeHoy() {
    return this.obtenerUsosDeHoy().size();
  }
  
  /**
   * Determina la cantidad de veces que se puede usar la tarjeta antes de requerir un reemplazo.
   *
   * @param beneficiario Beneficiario dueño de la tarjeta.
   * @return Devuelve la cantidad especificada.
   */
  public int usosMaximos(Beneficiario beneficiario) {
    return beneficiario.getMenoresAcargo() * 2 + 4;
  }
  
  public Month getMesDeCreacion() {
    return fechaDeCreacion.getMonth();
  }
  //////////////////////////
  ///// GETTERS ///////
  ////////////////////////
  
  public String getCodigo() {
    return codigo;
  }
  
  /**
   * Queremos conocer hace cuántos meses está activa la tarjeta.
   *
   * @return Devuelve la cantidad en meses.
   */
  public int mesesActiva() {
    LocalDate ahora = LocalDate.now();
    Period periodo = Period.between(fechaDeCreacion, ahora);
    return periodo.getYears() * 12 + periodo.getMonths();
  }
  
  public int getUsosTotales() {
    return usos.size();
  }
  /////////////////////////
  ///// CONSULTAS //////
  ///////////////////////
  
  /**
   * Determina si aún se puede utilizar la tarjeta en el día de hoy.
   *
   * @param beneficiario Beneficiario dueño de la tarjeta.
   * @return V o F según corresponda.
   */
  public boolean meQuedanUsos(Beneficiario beneficiario) {
    return usosDeHoyRestantes(beneficiario) > 0;
  }
}
