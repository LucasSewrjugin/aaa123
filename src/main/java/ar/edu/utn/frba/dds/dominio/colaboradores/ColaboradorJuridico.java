package ar.edu.utn.frba.dds.dominio.colaboradores;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * Instancia de colaborador jurídico.
 */
@Entity
public class ColaboradorJuridico /* implements */ extends Colaborador {
  // @Id
  // @GeneratedValue
  // private Long id;
  @Column
  private String razonSocial;
  @Enumerated(EnumType.STRING)
  private TipoJuridico tipo;
  @Column
  private String rubro;
  @OneToMany
  @JoinColumn(name = "Heladera")
  private List<Heladera> heladeras = new ArrayList<>();
  //////////////////////////
  ///// CONSTRUCTTOR //////
  ////////////////////////
  
  /**
   * Constructor por defecto necesario para Hibernate y la herencia.
   */
  public ColaboradorJuridico() {
  }
  
  /**
   * Constructor por parámetros.
   *
   * @param unaOrg      Organización.
   * @param email       Email (si tiene).
   * @param telefono    Teléfono (si tiene).
   * @param whatsapp    WhatsApp (si tiene).
   * @param direccion   Domicilio.
   * @param razonSocial Razón social de la empresa.
   * @param tipo        Tipo jurídico (SA, SRL, etc).
   * @param rubro       Rubro al que se dedica.
   */
  public ColaboradorJuridico(Organizacion unaOrg, String email,
      Integer telefono, Integer whatsapp, Ubicacion direccion,
      String razonSocial, TipoJuridico tipo, String rubro) {
    super(unaOrg, email, telefono, whatsapp, direccion);
    if (razonSocial.isEmpty() || tipo == null || rubro.isEmpty()) {
      throw new IllegalArgumentException(
          "La razon social,  tipo, contacto y rubro son datos obligatorios");
    }
    this.razonSocial = razonSocial;
    this.tipo = tipo;
    this.rubro = rubro;
  }
  /////////////////////////
  /// COMPORTAMIENTO ///
  ///////////////////////
  
  /**
   * Un colaborador jurídico se hace cargo de una heladera.
   *
   * @param unaHeladera Heladera que asume.
   */
  public void hacerseCargoDe(Heladera unaHeladera) {
    heladeras.add(unaHeladera);
  }
  
  /**
   * Sólo para el asesor de colaboraciones.
   */
  public Boolean soyHumano() {
    return false;
  }
  /*
   * public void colaborar(Colaboracion colaboracion) { if
   * (!colaboracionesPosibles.contains(colaboracion)) throw new
   * IllegalArgumentException("La colaboracion no es valida para esta clase");
   * super(colaboracion); }
   */
  //////////////////////////
  ///// SETTERS ///////
  ////////////////////////
  
  public void setNombre(String razonSocial) {
    this.razonSocial = razonSocial;
  }
  
  public void setTipo(TipoJuridico nuevoTipo) {
    this.tipo = nuevoTipo;
  }
  
  /**
   * Setter de contacto.
   *
   * @param email    Email.
   * @param telefono Teléfono.
   * @param whatsapp WhatsApp.
   */
  public void setContacto(String email, Integer telefono, Integer whatsapp) {
    this.email = email;
    this.telefono = telefono;
    this.whatsapp = whatsapp;
  }
  
  public void setDireccion(Ubicacion nuevaDireccion) {
    this.direccion = nuevaDireccion;
  }
  
  public String getRubro() {
    return rubro;
  }
  //////////////////////////
  ///// GETTERS ///////
  ////////////////////////
  
  public void setRubro(String nuevoRubro) {
    this.rubro = nuevoRubro;
  }
  
  public List<Heladera> getHeladeras() {
    return this.heladeras;
  }
  
  public String getRazonSocial() {
    return this.razonSocial;
  }
  
  @Override
  public Double puntaje() {
    Double puntaje = this.getDineroDonado().doubleValue() * 0.5
        + this.heladerasActivasPorMesesPorUsos() * 5;
    return puntaje;
  }
  
  private Double heladerasActivasPorMesesPorUsos() {
    Double res = 0.0;
    for (Heladera heladera : heladeras) {
      Period tiempoActiva = Period.between(LocalDate.now(),
          heladera.getFechaEstreno());
      int mesesActiva = tiempoActiva.getMonths();
      int usos = heladera.usosTotales();
      res += mesesActiva * usos;
    }
    return res;
  }
}