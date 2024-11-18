package ar.edu.utn.frba.dds.dominio.apisparainyectar;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;

/**
 * Sensor de peso para las heladeras.
 */
public interface SensorDePeso {
  /**
   * Método que devuelve el peso de la heladera.
   *
   * @param heladera Heladera a analizar.
   * @return Devuelve el valor del peso en la unidad especificada.
   */
  public Double getPeso(Heladera heladera);
}
