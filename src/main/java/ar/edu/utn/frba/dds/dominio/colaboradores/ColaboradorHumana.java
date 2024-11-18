package ar.edu.utn.frba.dds.dominio.colaboradores;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.dominio.colaboraciones.RegistrarBeneficiario;
import ar.edu.utn.frba.dds.dominio.colaboraciones.TipoColaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.mensajeria.MedioComunicacion;
import ar.edu.utn.frba.dds.dominio.suscripcion.SuscripcionAheladeraSufreIncidente;
import ar.edu.utn.frba.dds.dominio.suscripcion.SuscripcionAquedanNviandas;
import ar.edu.utn.frba.dds.dominio.tarjeta.RepositorioDeTarjetas;
import ar.edu.utn.frba.dds.dominio.tarjeta.SolicitudTarjeta;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Instancia del colaborador humano.
 */
@Entity
public class ColaboradorHumana extends Colaborador {
  @Column
  private String nombre;
  @Column
  private String apellido;
  @Column
  private LocalDate fechaDeNacimiento; // opcional
  @Enumerated(EnumType.STRING)
  private TipoDocumento tipoDocumento;
  @Column
  private Integer documento;
  @Column
  private String tarjetaCodigo;
  //////////////////////////
  ///// CONSTRUCTOR ///////
  ////////////////////////
  
  /**
   * Constructor por defecto, necesario para Hibernate y la herencia.
   */
  public ColaboradorHumana() {
  }
  
  /**
   * Constructor principal.
   *
   * @param unaOrg            Organización a la que pertenece.
   * @param email             Email del colaborador (si posee).
   * @param telefono          Celular (si posee).
   * @param whatsapp          Ídem anterior.
   * @param direccion         Domicilio.
   * @param nombre            Nombre.
   * @param apellido          Apellido.
   * @param fechaDeNacimiento Fecha de nacimiento.
   */
  public ColaboradorHumana(Organizacion unaOrg, String email, Integer telefono,
      Integer whatsapp, Ubicacion direccion, String nombre, String apellido,
      LocalDate fechaDeNacimiento) {
    super(unaOrg, email, telefono, whatsapp, direccion);
    if (nombre.isEmpty() || apellido.isEmpty()) {
      throw new RuntimeException("El nombre y apellido son datos obligatorios");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaDeNacimiento = fechaDeNacimiento;
    this.direccion = direccion;
  }
  /////////////////////////
  //// COMPORTAMIENTO ////
  ///////////////////////
  
  /**
   * Método sobreescrito.
   */
  public Boolean soyHumano() {
    return true;
  }
  
  @Override
  public void agregarColaboracion(Colaboracion unaColaboracion) {
    // Valida si necesita una tarjeta
    if (unaColaboracion.necesitaTarjeta()) {
      RepositorioDeTarjetas operador = RepositorioDeTarjetas.getInstance();
      SolicitudTarjeta solicitud = new SolicitudTarjeta(this,
          direccion.getDomicilio());
      operador.agregarSolicitud(solicitud);
    }
    // Valida si el tipo corresponde
    super.agregarColaboracion(unaColaboracion);
  }
  
  /**
   * Registra una colaboración.
   *
   * @param colaboracion Colaboración realizada.
   */
  public void registrarAccion(Colaboracion colaboracion) {
    colaboracion.registrarAccion(tarjetaCodigo);
  }
  //////////////////////////
  ///// SETTERS ///////
  ////////////////////////
  
  public void setFechaNacimiento(LocalDate nuevaFechaDeNacimiento) {
    this.fechaDeNacimiento = nuevaFechaDeNacimiento;
  }
  
  //////////////////////////
  ///// GETTERS ///////
  ////////////////////////
  public int getDocumento() {
    return this.documento;
  }
  
  public TipoDocumento getTipoDocumento() {
    return this.tipoDocumento;
  }
  
  public String getNombre() {
    return this.nombre;
  }
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public String getTarjetaCodigo() {
    return this.tarjetaCodigo;
  }
  
  public void setTarjetaCodigo(String tarjetaCodigo) {
    this.tarjetaCodigo = tarjetaCodigo;
  }
  
  @Override
  public Double puntaje() {
    BigDecimal dineroDonado = this.getDineroDonado();
    // TODO: Reemplazar el this.getColaboraciones() por
    // this.colaboracionesAPartirDe(fechaDeseada)
    // en todos los metodos usados abajo
    Double puntaje = dineroDonado.doubleValue() * 0.5
        + this.viandasDistribuidas()
        + this.viandasDonadasPorTiempoFresco() * 1.5
        + this.tarjetasRepartidasPorMesesPorUsos() * 2;
    return puntaje;
  }
  
  /**
   * Viandas que fueron distribuidas.
   *
   * @return Devuelve la cantidad de viandas distribuidas.
   */
  public Double viandasDistribuidas() {
    long count = filtrarYmapearColaboraciones(DonacionVianda.class).size();
    return (double) count;
  }
  
  /**
   * Viandas donadas tomando en cuenta el tiempo fresco.
   *
   * @return Devuelve la cantidad de viandas frescas.
   */
  public Double viandasDonadasPorTiempoFresco() {
    List<Vianda> viandasDonadas = this.misViandasDonadas();
    Double res = 0.0;
    for (Vianda vianda : viandasDonadas) {
      Duration tiempoFresca = Duration.between(vianda.getFechaDonacion(),
          vianda.getFechaCaducidad());
      long semanasFresca = tiempoFresca.toDays() / 7;
      res += semanasFresca;
    }
    return res;
  }
  
  /**
   * Viandas que dona el colaborador.
   *
   * @return Devuelve la lista de viandas donadas.
   */
  public List<Vianda> misViandasDonadas() {
    List<DonacionVianda> donacionesDeViandas = this
        .misColaboracionesDeViandas();
    List<Vianda> viandasDonadas = donacionesDeViandas.stream()
        .map(donacionDeVianda -> donacionDeVianda.getVianda()).toList();
    return viandasDonadas;
  }
  
  private <T> List<T> filtrarYmapearColaboraciones(Class<T> tipo) {
    return this.getColaboraciones().stream()
        .filter(colaboracion -> tipo.isInstance(colaboracion))
        .map(colaboracion -> tipo.cast(colaboracion))
        .collect(Collectors.toList());
  }
  
  /**
   * Colaboraciones (sólo de viandas).
   *
   * @return Devuelve la lista de viandas donadas.
   */
  public List<DonacionVianda> misColaboracionesDeViandas() {
    List<DonacionVianda> donacionesDeViandas = filtrarYmapearColaboraciones(
        DonacionVianda.class);
    return donacionesDeViandas;
  }
  
  /**
   * Tarjetas repartidas.
   *
   * @return Tarjetas repartidas.
   */
  public Double tarjetasRepartidasPorMesesPorUsos() {
    List<RegistrarBeneficiario> registrosDeBeneficiario = filtrarYmapearColaboraciones(
        RegistrarBeneficiario.class);
    Double res = 0.0;
    for (RegistrarBeneficiario reg : registrosDeBeneficiario) {
      res += reg.getTarjeta().mesesActiva() * reg.getTarjeta().getUsosTotales();
    }
    return res;
  }
  
  public String getApellido() {
    return this.apellido;
  }
  
  public void setApellido(String apellido) {
    this.apellido = apellido;
  }
  
  /**
   * Cantidad de colaboraciones según tipo de colaboración.
   *
   * @param  tipoColaboracion Tipo de la colaboración.
   * @return                  Devuelve la cantidad (tamaño de la lista).
   */
  public int cantidadDeColaboracionesDe(TipoColaboracion tipoColaboracion) {
    List<Colaboracion> colaboracionesFiltradas = colaboraciones.stream().filter(
        colaboracion -> colaboracion.getTipoColaboracion() == tipoColaboracion)
        .toList();
    return colaboracionesFiltradas.size();
  }
  
  /**
   * Determina si el colaborador ya tiene ese documento.
   *
   * @param documentoNuevo Documento nuevo para el colaborador.
   * @param tipoDocumentoNuevo Tipo de documento.
   * @return V o F según corresponda.
   */
  public Boolean tengoDocumentoDe(int documentoNuevo,
      TipoDocumento tipoDocumentoNuevo) {
    return documento == documentoNuevo && tipoDocumentoNuevo == tipoDocumento;
  }
  /*
   * public void registrarAccion(Heladera unaHeladera, ) {
   *
   * }
   */
  
  // ESTOS 2 METODOS NO SE SI VAN A QUEDAR AL FINAL, CAPAZ LOS SACAMOS Y HACEMOS
  // LA SUSCRIPCION
  // DIRECTAMENTE EN LA HELADERA CON LOS DATOS INGRESADOS POR UI
  /**
   * Suscripción al informe de cuántas viandas quedan en una heladera.
   *
   * @param heladera Heladera donde el colaborador se suscribe.
   * @param contacto Contacto por donde llega la notificación.
   * @param viandasParaElAviso Viandas que se informan.
   */
  public void suscribirseAquedanNviandasEnHeladera(Heladera heladera,
      MedioComunicacion contacto, int viandasParaElAviso) {
    heladera.suscribirseAquedanNviandas(
        new SuscripcionAquedanNviandas(this, contacto, viandasParaElAviso));
  }
  
  /**
   * Suscripción al informe de fallas en una heladera.
   *
   * @param heladera Heladera donde el colaborador se suscribe.
   * @param contacto Contacto por donde llega la notificación.
   */
  public void suscribirseAfallaEnHeladera(Heladera heladera,
      MedioComunicacion contacto) {
    heladera.suscribirseAfallaEnHeladera(
        new SuscripcionAheladeraSufreIncidente(this, contacto));
  }
}
