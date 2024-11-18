package ar.edu.utn.frba.dds.dominio.apisparainyectar;

/**
 * Sensor externo.
 */
public interface Lsensor {
  /**
   * L sensor.
   *
   * @param action Acción.
   */
  void onLunchBoxChanged(LunchBoxAction action);
}
