package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * Acción a realizar.
 */
public interface Action {
  /**
   * Realiza una acción a partir de la medición de una temperatura reportada.
   *
   * @param temperature Ante dicha temperatura se toma una acción.
   */
  void executeForTemperature(double temperature);
}
