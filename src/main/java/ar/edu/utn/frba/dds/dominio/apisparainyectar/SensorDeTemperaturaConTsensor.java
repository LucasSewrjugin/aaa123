package ar.edu.utn.frba.dds.dominio.apisparainyectar;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Action;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Tsensor;

/**
 * Implementación de sensor de temperatura con T Sensor.
 */
public class SensorDeTemperaturaConTsensor implements SensorDeTemperatura {
  private final Tsensor tsensor;

  /**
   * T sensor de temperatura.
   *
   * @param tsensor Tsensor.
   */
  public SensorDeTemperaturaConTsensor(Tsensor tsensor) {
    this.tsensor = tsensor;
  }
  
  @Override
  public void conectar(Heladera heladera) {
    tsensor.connect(heladera.getNombre());
  }
  
  @Override
  public void realizarAccionEnTemperatura(Action accion) {
    tsensor.onTemperatureChange(accion);
  }
}
