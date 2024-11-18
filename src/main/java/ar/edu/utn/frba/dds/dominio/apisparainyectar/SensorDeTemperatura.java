package ar.edu.utn.frba.dds.dominio.apisparainyectar;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Action;

/**
 * Sensor de temperatura.
 */
public interface SensorDeTemperatura {
  /**
   * Conecta el sensor a una heladera.
   *
   * @param heladera Heladera.
   */
  public void conectar(Heladera heladera);
  
  /**
   * Realiza una acción determinada.
   *
   * @param accion Acción a ejecutar.
   */
  public void realizarAccionEnTemperatura(Action accion);
}
