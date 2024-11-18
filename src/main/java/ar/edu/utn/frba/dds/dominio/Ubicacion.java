package ar.edu.utn.frba.dds.dominio;

import javax.persistence.Embeddable;
//import javax.persistence.Entity;

/**
 * Instancias de ubicación.
 */
@Embeddable
public class Ubicacion {
  private String domicilio;
  private double latitud;
  private double longitud;
  
  /**
   * Constructor por defecto (requerido por JPA).
   */
  public Ubicacion() {
    // Constructor sin argumentos requerido por JPA
  }
  
  /**
   * Constructor principal.
   *
   * @param domicilio Domicilio.
   * @param latitud   Latitud.
   * @param longitud  Longitud.
   */
  public Ubicacion(String domicilio, double latitud, double longitud) {
    if (domicilio.isEmpty()) {
      throw new IllegalArgumentException("Direccion vacia");
    }
    this.domicilio = domicilio;
    this.latitud = latitud;
    this.longitud = longitud;
  }
  //////////////////////
  //// GETTERS //////
  /////////////////////
  
  public String getDomicilio() {
    return domicilio;
  }
  
  public double getLongitud() {
    return longitud;
  }
  
  public double getLatitud() {
    return latitud;
  }
  
  /**
   * Distancia entre una ubicación destino y mi ubicación actual.
   *
   * @param  ubicacion Ubicación destino.
   * @return           Devuelve la distancia.
   */
  public double distanciaHasta(Ubicacion ubicacion) {
    double distanciaLatitud = Math.abs(this.latitud - ubicacion.getLatitud());
    double distanciaLongitud = Math.abs(this.longitud - ubicacion.getLongitud());
    return distanciaLongitud + distanciaLatitud;
  }
}
