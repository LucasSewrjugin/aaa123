package ar.edu.utn.frba.dds.dominio.apisparainyectar;

/**
 * Adaptador del controlador externo.
 */
public class ControladorDeAccesoAdapter {
  private final ControladorDeAcceso controladorDeAcceso;

  /**
   * Constructor del adapter.
   *
   * @param controladorDeAcceso Controlador a adaptar.
   */
  public ControladorDeAccesoAdapter(ControladorDeAcceso controladorDeAcceso) {
    this.controladorDeAcceso = controladorDeAcceso;
  }

  /**
   * Notifica si la tarjeta está habilitada para operar.
   *
   * @param numerosDeTarjeta Números identificatorios de la tarjeta.
   */
  public void notificarTarjetaColaboradoraHabilitada(String numerosDeTarjeta) {
    controladorDeAcceso.notificarTarjetaColaboradoraHabilitada(numerosDeTarjeta);
  }
}
