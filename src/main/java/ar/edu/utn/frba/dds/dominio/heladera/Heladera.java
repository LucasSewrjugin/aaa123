package ar.edu.utn.frba.dds.dominio.heladera;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.ControladorDeAccesoAdapter;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDePeso;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.SensorDeTemperatura;
import ar.edu.utn.frba.dds.dominio.apisparainyectar.TemperaturasBajas;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Vianda;
import ar.edu.utn.frba.dds.dominio.incidente.Incidente;
import ar.edu.utn.frba.dds.dominio.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.dominio.incidente.Visita;
import ar.edu.utn.frba.dds.dominio.suscripcion.Suscripcion;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Entidad heladera.
 */
@Entity
public class Heladera {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private Organizacion organizacion;
  @Column
  private String nombre;
  @Embedded
  private Ubicacion ubicacion;
  @Column
  private Double capacidad;
  @Column
  private LocalDate fechaEstreno;
  @OneToMany
  private List<Vianda> viandas;
  @Column
  private int usosTotales;
  @Column
  private double temperaturaActual;
  @Column
  private double temperaturaMin;
  @Column
  private double temperaturaMax;
  @Column
  private Boolean activa;
  @Enumerated(EnumType.STRING)
  private NivelDeLlenado nivelDeLlenado = NivelDeLlenado.BAJO;
  @Enumerated(EnumType.STRING)
  private AtencionRequerida atencionRequerida = AtencionRequerida.BUENA_TEMPERATURA;
  @Column
  private int temperaturasDebajoDelMinimo;
  @OneToMany
  private List<Suscripcion> suscripcionesAquedanNviandas;
  @OneToMany
  private List<Suscripcion> suscripcionesAfallaDeHeladera;
  @OneToMany
  @JoinColumn(name = "Visitas")
  private List<Visita> visitas;
  // Es externo, ergo, no podemos hacerlo clase abstracta
  @Transient
  private ControladorDeAccesoAdapter controladorDeAcceso;
  @Transient
  private SensorDePeso sensorDePeso;
  @Transient
  private SensorDeTemperatura sensorDeTemperatura;
  @ElementCollection
  private List<String> log;
  @OneToMany
  @JoinColumn(name = "Incidentes")
  private List<Incidente> registroIncidentes;
  //////////////////////
  //// CONSTRUCTOR /////
  //////////////////////

  /**
   * Constructor vacío para la persistencia de Hibernate.
   */
  public Heladera() {

  }
  
  /**
   * Constructor de la heladera.
   *
   * @param organizacion        Organización.
   * @param nombre              Nombre.
   * @param viandas             Viandas.
   * @param ubicacion           Ubicación.
   * @param capacidad           Capacidad máxima.
   * @param unaFechaEstreno     Fecha de estreno.
   * @param sensorDePeso        Sensor de peso.
   * @param sensorDeTemperatura Sensor de temperatura.
   * @param controlador         Controlador de tarjetas.
   */
  public Heladera(Organizacion organizacion, String nombre,
      List<Vianda> viandas, Ubicacion ubicacion, Double capacidad,
      LocalDate unaFechaEstreno, SensorDePeso sensorDePeso,
      SensorDeTemperatura sensorDeTemperatura,
      ControladorDeAccesoAdapter controlador) {
    if (nombre.isEmpty() || ubicacion == null || capacidad <= 0 /*
                                                                 * ||
                                                                 * sensorDePeso
                                                                 * == null ||
                                                                 * sensorDeTemperatura
                                                                 * == null
                                                                 */) {
      throw new RuntimeException("Porfavor, Ingrese datos validos");
    }
    this.organizacion = organizacion;
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.capacidad = capacidad;
    this.fechaEstreno = unaFechaEstreno;
    this.viandas = viandas;
    this.sensorDePeso = sensorDePeso;
    this.sensorDeTemperatura = sensorDeTemperatura;
    this.activa = true;
    this.usosTotales = 0;
    this.temperaturasDebajoDelMinimo = 0;
    this.viandas = new ArrayList<>(); // Cuando creo una heladera, la creo vacía;
    this.suscripcionesAfallaDeHeladera = new ArrayList<>();
    // Cuando creo la heladera, aún no tiene suscriptores.
    this.suscripcionesAquedanNviandas = new ArrayList<>();
    this.visitas = new ArrayList<>();
    this.registroIncidentes = new ArrayList<>();
    this.controladorDeAcceso = controlador;
    this.log = new ArrayList<>();
  }
  /////////////////////////
  // COMPORTAMIENTO ///
  ///////////////////////
  //////////// INCIDENTES //////////
  
  /**
   * Ubica al técnico más cercano para que atienda incidente.
   *
   * @return Devuelve la instancia de técnico que corresponda.
   */
  public Tecnico ubicarTecnicoCercano() {
    Tecnico tecCercano = organizacion.getTecnicos().get(0);
    for (Tecnico aux : organizacion.getTecnicos()) {
      if (this.getUbicacion().distanciaHasta(aux.getUbicacion()) < this
          .getUbicacion().distanciaHasta(tecCercano.getUbicacion())) {
        tecCercano = aux;
      }
    }
    return tecCercano;
  }
  
  /**
   * Notifica a un técnico acerca de un incidente.
   *
   * @param tecnico Técnico a notificar.
   * @param incidente Detalles del incidente que se reporta.
   */
  public void notificarTecnico(Tecnico tecnico, Incidente incidente) {
    tecnico.notify(incidente);
  }
  
  /**
   * Avisa a la mesa de técnicos sobre un incidente reportado.
   *
   * @param unIncidente Incidente a reportar.
   */
  public void llamarTecnico(Incidente unIncidente) {
    Tecnico tecnicoAllamar = ubicarTecnicoCercano();
    notificarTecnico(tecnicoAllamar, unIncidente); 
  }
  
  /**
   * Agrega un incidente reportado.
   *
   * @param unIncidente Incidente que se reporta.
   */
  public void agregarIncidente(Incidente unIncidente) {
    this.desactivar();
    this.llamarTecnico(unIncidente);
    this.notificarFallaHeladera();
    registroIncidentes.add(unIncidente);
  }
  //////////// VIANDAS ///////////////
  
  /**
   * Agrega una vianda a la heladera.
   *
   * @param vianda Vianda que se va a agregar.
   */
  public void ingresarVianda(Vianda vianda) {
    if (this.excedeCapacidadSiAniade(vianda)) {
      throw new RuntimeException("No hay espacio para agregar otra vianda");
    }
    viandas.add(vianda);
    vianda.confirmarEntrega();
    this.revisarLlenado();
  }
  
  /**
   * Quita una vianda de la heladera.
   */
  public void quitarUnaVianda() {
    viandas.remove(0);
    notificarNviandas();
  }
  
  /**
   * Retira la primera vianda disponible.
   *
   * @return Devuelve la instancia de vianda correspondiente.
   */
  public Vianda retirarPrimeraVianda() {
    Vianda retorno = viandas.remove(0);
    notificarNviandas();
    return retorno;
  }
  
  /**
   * Queremos conocer cuántas viandas hay en la heladera.
   *
   * @return Devuelve la cantidad de viandas que hay en la heladera.
   */
  public Integer cantidadDeViandas() {
    return viandas.size();
  }
  ////////// CAPACIDAD DE LA HELADERA ///////
  
  /**
   * Revisa si la heladera admite más viandas.
   */
  public void revisarLlenado() {
    if (laCapacidadActualCubreElPorcentualTotalDe(0.70)) {
      this.setNivelDeLlenado(NivelDeLlenado.ALTO);
    } else if (laCapacidadActualCubreElPorcentualTotalDe(0.30)) {
      this.setNivelDeLlenado(NivelDeLlenado.MEDIO);
    } else {
      this.setNivelDeLlenado(NivelDeLlenado.BAJO);
    }
  }
  
  /**
   * Revisa si se excede la capacidad al añadir una vianda.
   *
   * @param  vianda Vianda que se va a agregar provisoriamente para evaluar.
   * @return        V o F según corresponda.
   */
  public boolean excedeCapacidadSiAniade(Vianda vianda) { 
    List<Vianda> viandasNuevas = viandas;
    viandasNuevas.add(vianda);
    Heladera heladeraAux = new Heladera(organizacion, "heladeraAux",
        viandasNuevas, new Ubicacion("ubicacionAux", 0.0, 0.0), capacidad, null,
        this.sensorDePeso, this.sensorDeTemperatura, controladorDeAcceso);
    return heladeraAux.excedeCapacidad();
  }
  
  /**
   * Revisa si se excede en peso la capacidad de la heladera.
   *
   * @return V o F según corresponda.
   */
  public boolean excedeCapacidad() {
    return this.getPeso() >= this.getCapacidad();
  }
  
  /**
   * Evalúa si la capacidad actual cubre un porcentaje del peso.
   *
   * @param porcentaje Porcentaje a evaluar con la capacidad.
   * @return V o F según corresponda.
   */
  public boolean laCapacidadActualCubreElPorcentualTotalDe(Double porcentaje) {
    return this.getPeso() >= this.capacidad * porcentaje;
  }
  //////////// TEMPERATURA DE LA HELADERA ///////////////
  
  /**
   * Sensor de temperatura.
   */
  public void revisarTemperatura() {
    sensorDeTemperatura.conectar(this);
    TemperaturasBajas temperaturaBaja = new TemperaturasBajas(this);
    sensorDeTemperatura.realizarAccionEnTemperatura(temperaturaBaja);
  }
  
  private boolean tengoTresoMasTemperaturasMinimas() {
    return temperaturasDebajoDelMinimo >= 3;
  }
  
  /**
   * Revisa si la temperatura está en el rango aceptado.
   */
  public void detectarTemperaturaIimperfecta() {
    temperaturasDebajoDelMinimo++;
    if (tengoTresoMasTemperaturasMinimas()) {
      this.averiarse();
    }
  }
  
  /**
   * Evalúa si la temperatura no se midió.
   */
  public Boolean laTemperaturaNoSeMidioTresVeces() {
    return true;
  }
  
  /**
   * En testeo.
   *
   * @return V o F según corresponda.
   */
  public Boolean detectarTresNullDeTemperatura() {
    // FALLA EN LA CONEXION, no se recibieron lecturas por más de 15 minutos, es
    // decir, 3 veces la
    // temperatura fue Null
    return this.laTemperaturaNoSeMidioTresVeces();
  }
  
  /**
   * Evalúa si la temperatura da por debajo del mínimo establecido.
   */
  public Boolean dioPorDebajoDelMinimo() {
    return this.temperaturaActual < this.temperaturaMin;
  }
  
  /**
   * Evalúa si la temperatura excede el máximo establecido.
   */
  public Boolean dioPorEncimaDelMaximo() {
    return this.temperaturaActual > this.temperaturaMax;
  }
  
  /**
   * Evalúa si hubo un error al medir el peso.
   */
  public Boolean seDetectoExcepcionEnElSensorDePeso() {
    return true;
  }
  
  /**
   * Revisa si hay un error en el sensor de peso.
   */
  public void detectarExcepcionDelSensorDePeso() {
    if (seDetectoExcepcionEnElSensorDePeso()) {
      this.alertaDetectada();
    }
  }
  
  /**
   * Reporta una alerta.
   */
  public void alertaDetectada() {
    // Se crea una alerta que avisa al técnico
    // Alerta alerta = new Alerta("Se ha detectado una alerta");
    // TipoIncidente alerta = new TipoIncidente(true, null, "Se ha detectado una
    // alerta", null,
    // null);
    TipoIncidente alerta = TipoIncidente.crearSinId(true, null,
        "Se ha detectado una alerta", null, null);
    // Se desactiva la heladera
    this.desactivar();
  }
  //////////// ACCIONES A REALIZAR SOBRE LA HELADERA ///////////////
  
  /**
   * Activa la heladera.
   */
  public void activar() {
    this.activa = true;
  }
  
  /**
   * Desactiva la heladera.
   */
  public void desactivar() {
    this.activa = false;
  }
  
  private void averiarse() {
    this.atencionRequerida = AtencionRequerida.NECESITA_ATENCION;
  }
  
  /**
   * Registra la visita del técnico.
   *
   * @param visita Informe de la visita.
   */
  public void registrarVisita(Visita visita) {
    visitas.add(visita);
    if (visita.resuelto()) {
      this.activar(); 
    }
  }
  //////////////////////////
  /// SUSCRIPCIONES /////
  ////////////////////////
  
  /**
   * Suscripción para saber cuántas viandas quedan en la heladera.
   *
   * @param suscripcion Suscripción a añadir.
   */
  public void suscribirseAquedanNviandas(Suscripcion suscripcion) {
    suscripcionesAquedanNviandas.add(suscripcion);
  }
  
  /**
   * Suscripción para saber si la heladera tuvo fallas.
   *
   * @param suscripcion Suscripción a añadir.
   */
  public void suscribirseAfallaEnHeladera(Suscripcion suscripcion) {
    suscripcionesAfallaDeHeladera.add(suscripcion);
  }
  
  private void notificarNviandas() {
    suscripcionesAquedanNviandas
        .forEach(suscripcion -> suscripcion.notificar(this));
  }
  
  private void notificarFallaHeladera() {
    suscripcionesAfallaDeHeladera
        .forEach(suscripcion -> suscripcion.notificar(this));
  }
  // private boolean laHeladeraTuvoUnIncidente() {
  // return false;
  // }
  //////////////////////////
  ///// SETTERS ///////
  ////////////////////////
  
  public void setTemperaturaActual(Double temperaturaActual) {
    this.temperaturaActual = temperaturaActual;
  }
  
  public String getNombre() {
    return nombre;
  }
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public Ubicacion getUbicacion() {
    return this.ubicacion;
  }
  
  public void setUbicacion(Ubicacion ubicacion) {
    this.ubicacion = ubicacion;
  }
  
  public LocalDate getFechaEstreno() {
    return this.fechaEstreno;
  }
  
  public void setFechaEstreno(LocalDate unaFechaDeEstreno) {
    this.fechaEstreno = unaFechaDeEstreno;
  }
  
  public Double getPeso() {
    return sensorDePeso.getPeso(this);
  }
  /////////////////////////
  ///// GETTERS //////
  ///////////////////////
  
  public Double getCapacidad() {
    return capacidad;
  }
  
  public void setCapacidad(Double capacidad) {
    this.capacidad = capacidad;
  }
  
  public NivelDeLlenado getNivelDeLlenado() {
    return nivelDeLlenado;
  }
  
  public void setNivelDeLlenado(NivelDeLlenado nivelDeLlenado) {
    this.nivelDeLlenado = nivelDeLlenado;
  }
  
  public AtencionRequerida getAtencionRequerida() {
    return atencionRequerida;
  }
  
  public Double getTemperaturaMax() {
    return temperaturaMax;
  }
  
  public void setTemperaturaMax(Double tempMaxNueva) {
    this.temperaturaMax = tempMaxNueva;
  }
  
  public Double getTemperaturaMin() {
    return temperaturaMin;
  }
  
  public void setTemperaturaMin(Double tempMinNueva) {
    this.temperaturaMin = tempMinNueva;
  }
  
  public Double getTemperatura() {
    return temperaturaActual;
  }
  
  /**
   * Getter de temperaturas debajo del mínimo.
   *
   * @return Devuelve las temperaturas debajo del mínimo.
   */
  public int temperaturasDebajoDelMinimo() {
    return this.temperaturasDebajoDelMinimo;
  }
  
  /**
   * Getter de usos totales de la heladera.
   *
   * @return Devuelve la cantidad de veces que se usó esta heladera.
   */
  public int usosTotales() {
    return usosTotales;
  }
  
  public List<Vianda> getViandas() {
    return viandas;
  }
  
  public List<Incidente> getRegistroIncidentes() {
    return registroIncidentes;
  }
  
  /**
   * Notifica un pedido de apertura con una tarjeta.
   *
   * @param tarjeta Tarjeta que solicita el acceso.
   */
  public void notificarSolicitudApertura(String tarjeta) {
    controladorDeAcceso.notificarTarjetaColaboradoraHabilitada(tarjeta);
  }
  
  /**
   * Registra acceso.
   *
   * @param tarjetaCodigo Tarjeta que solicitó acceso.
   * @param mensaje       Mensaje correspondiente al acceso.
   */
  public void registrarLog(String tarjetaCodigo, String mensaje) {
    LocalDateTime tiempo = LocalDateTime.now();
    String strTiempo = tiempo
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    log.add(strTiempo + " " + tarjetaCodigo + " " + mensaje);
  }
  
  public List<String> getLogs() {
    return log;
  }
  
  public long getId() {
    return id;
  }
  
  /**
   * Convierte cadena en formato Json.
   *
   * @return           Devuelve el resultado en formato Json.
   * @throws Exception Devuelve la excepción lanzada.
   */
  public String toJson() throws Exception {
    Map<String, Object> jsonMap = new HashMap();
    jsonMap.put("id", id);
    jsonMap.put("nombre", nombre);
    if (ubicacion != null) {
      jsonMap.put("direccion", ubicacion.getDomicilio());
      jsonMap.put("latitud", ubicacion.getLatitud());
      jsonMap.put("longitud", ubicacion.getLongitud());
    } else {
      jsonMap.put("direccion", null);
      jsonMap.put("latitud", null);
      jsonMap.put("longitud", null);
    }
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(jsonMap);
  }
}
