package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * Interfaz para T sensor.
 */
public interface Tsensor {
  /**
   * Conecta el sensor.
   *
   * @param serialNumber Número de serie del sensor.
   */
  void connect(String serialNumber);
  
  /**
   * Ante un cambio de temperatura, reporta.
   *
   * @param action Acción a realizar.
   */
  void onTemperatureChange(Action action);
}
