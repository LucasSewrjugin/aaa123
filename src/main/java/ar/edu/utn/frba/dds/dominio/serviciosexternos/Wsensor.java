package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * W sensor. Api externa.
 */
public interface Wsensor {
  /**
   * Sensor de peso.
   *
   * @param serialNumber Número de serie del sensor.
   * @return Devuelve la lectura realizada.
   */
  Reading getWeight(String serialNumber);
} 
    