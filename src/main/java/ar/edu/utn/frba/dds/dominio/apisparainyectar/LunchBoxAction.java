package ar.edu.utn.frba.dds.dominio.apisparainyectar;

/**
 * Interfaz de acción de vianda.
 */
public interface LunchBoxAction {
  /**
   * Cambio.
   *
   * @param newQuantity Nueva cantidad.
   */
  void onLunchBoxChanged(int newQuantity);

}
