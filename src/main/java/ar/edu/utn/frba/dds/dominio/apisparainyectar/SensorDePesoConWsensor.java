package ar.edu.utn.frba.dds.dominio.apisparainyectar;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Reading;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Wsensor;

/**
 * Sensor de peso con WSensor.
 */
public class SensorDePesoConWsensor implements SensorDePeso {
  private final Wsensor wsensor;

  /**
   * Constructor principal.
   *
   * @param wsensor Sensor.
   */
  public SensorDePesoConWsensor(Wsensor wsensor) {
    this.wsensor = wsensor;
  }
  
  @Override
  public Double getPeso(Heladera heladera) {
    Reading reading = wsensor.getWeight(heladera.getNombre());
    return this.estaEnkg(reading) ? reading.value : this.pasarAkg(reading.value);
  }
  
  /**
   * Sólo para determinar si el peso está expresado en kilogramos.
   *
   * @param reading Lectura.
   * @return Devuelve V o F según corresponda.
   */
  public boolean estaEnkg(Reading reading) {
    return reading.unit.toUpperCase() == "KG";
  }
  
  /**
   * Convierte de libras a kilogramos.
   *
   * @param libras Peso en libras.
   * @return Devuelve el peso expresado en kilogramos.
   */
  public Double pasarAkg(Double libras) {
    return libras * 0.453592; // 1lbs ---------- 0.453592kg
  }
}
