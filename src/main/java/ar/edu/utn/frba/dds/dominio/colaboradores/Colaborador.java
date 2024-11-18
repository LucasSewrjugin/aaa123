package ar.edu.utn.frba.dds.dominio.colaboradores;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.registro.ValidadorCredenciales;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Instancia padre de la clase Colaborador.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Colaborador {
  @Id
  @GeneratedValue
  private long id;
  @Column
  protected String username;
  @Column
  protected String password;
  @OneToOne
  protected Organizacion org;
  @Embedded
  protected Ubicacion direccion;
  @Column
  protected String email;
  @Column
  protected Integer whatsapp;
  @Column
  protected Integer telefono;
  @OneToMany
  @JoinColumn(name = "Colaboraciones")
  protected List<Colaboracion> colaboraciones = new ArrayList<>();
  //////////////////////////
  ///// CONSTRUCTOR ///////
  ////////////////////////
  
  /**
   * Constructor por defecto necesario para Hibernate y la herencia.
   */
  public Colaborador() {
  }
  
  /**
   * Constructor principal.
   *
   * @param unaOrg    Organización.
   * @param email     Email.
   * @param telefono  Teléfono (si tiene).
   * @param whatsapp  Ídem anterior.
   * @param direccion Domicilio.
   */
  public Colaborador(Organizacion unaOrg, String email, Integer telefono,
      Integer whatsapp, Ubicacion direccion) {
    this.org = unaOrg;
    validarContacto(email, telefono, whatsapp);
    this.direccion = direccion;
    this.email = email;
    this.whatsapp = whatsapp;
  }
  /////////////////////////
  //// COMPORTAMIENTO ////
  ///////////////////////
  
  /**
   * Puntaje del colaborador.
   *
   * @return Devuelve el puntaje obtenido.
   */
  public abstract Double puntaje();
  
  /**
   * Agrega una colaboración. Se separo contribuir de agregerColaboracion para
   * tener cambios de estado y acciones en el mismo método.
   *
   * @param colaboracion Colaboración a agregar.
   */
  public void agregarColaboracion(Colaboracion colaboracion) {
    AsesorDeColaboraciones asesorDeColaboraciones = AsesorDeColaboraciones
        .getInstance();
    asesorDeColaboraciones.validarColaboracion(colaboracion, this);
    this.aniadirColaboracion(colaboracion);
  }
  
  /**
   * Contribuir con una donación.
   *
   * @param colaboracion Colaboración con la que se contribuye.
   */
  public void contribuir(Colaboracion colaboracion) {
    AsesorDeColaboraciones.getInstance().validarColaboracion(colaboracion,
        this);
    this.colaboraciones.add(colaboracion);
    colaboracion.contribuir();
  }
  
  /**
   * Añade una colaboración.
   *
   * @param colaboracion Colaboración a añadir.
   */
  public void aniadirColaboracion(Colaboracion colaboracion) {
    colaboraciones.add(colaboracion);
  }
  
  /**
   * Determina si un colaborador es humano.
   *
   * @return V o F según corresponda.
   */
  public abstract Boolean soyHumano();
  ///////////////////
  //// GETTTERS ////
  /////////////////
  
  // Método para tomar solo las colaboraciones aptas para el puntaje. TODO: Ver
  // de
  // donde sacamos la
  // fecha
  /**
   * Colaboraciones desde una fecha.
   *
   * @param  fecha Fecha a determinar.
   * @return       Devuelve la lista de colaboraciones realizadas desde ese día.
   */
  public List<Colaboracion> colaboracionesApartirDe(LocalDate fecha) {
    return colaboraciones.stream()
        .filter(colaboracion -> colaboracion.getFecha().isAfter(fecha))
        .toList();
  }
  
  /**
   * Dinero que se donó.
   *
   * @return Devuelve el valor buscado.
   */
  public BigDecimal getDineroDonado() {
    // TODO: Reemplazar el this.getColaboraciones() por
    // this.colaboracionesApartirDe(fechaDeseada)
    return this.getColaboraciones().stream()
        .filter(colaboracion -> colaboracion instanceof DonacionDinero)
        .map(colaboracion -> ((DonacionDinero) colaboracion).getMonto())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public long getId() {
    return id;
  }
  
  public Organizacion getOrganizacion() {
    return org;
  }
  
  public Integer getCantidadColaboraciones() {
    return colaboraciones.size();
  }
  
  public List<Colaboracion> getColaboraciones() {
    return this.colaboraciones;
  }
  
  public String getMail() {
    return this.email;
  }
  
  public Ubicacion getDireccion() {
    return this.direccion;
  }
  //////////////////
  //// SETTTERS ////
  //////////////////
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  /**
   * Setea una contraseña para su usuario.
   *
   * @param password Password a setear.
   */
  public void setPassword(String password) {
    ClassLoader classLoader = getClass().getClassLoader();
    String path = Objects
        .requireNonNull(classLoader.getResource("contraseniasDebiles.txt"))
        .getPath();
    this.password = ValidadorCredenciales.getInstance(path)
        .calcularHash(password);
  }
  
  public void setDireccion(Ubicacion direccionNueva) {
    this.direccion = direccionNueva;
  }
  
  public void setWhatsapp(Integer whatsappNuevo) {
    this.whatsapp = whatsappNuevo;
  }
  
  public void setTelefono(Integer telefonoNuevo) {
    this.telefono = telefonoNuevo;
  }
  
  public void setEmail(String emailNuevo) {
    this.email = emailNuevo;
  }
  
  public void setOrg(Organizacion unaOrg) {
    this.org = unaOrg;
  }
  //////////////////////
  //// VALIDACIONES ////
  //////////////////////
  
  /**
   * Validación del contacto.
   *
   * @param email    Email a validar (si tiene).
   * @param telefono Ídem anterior.
   * @param whatsApp Idem anterior.
   */
  public void validarContacto(String email, Integer telefono,
      Integer whatsApp) {
    if (email.isEmpty() && telefono == null && whatsApp == null) {
      throw new IllegalArgumentException(
          "Debe tener por lo menos un medio de contacto");
    }
  }
}