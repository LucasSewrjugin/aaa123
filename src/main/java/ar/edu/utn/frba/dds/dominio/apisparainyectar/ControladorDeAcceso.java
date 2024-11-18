package ar.edu.utn.frba.dds.dominio.apisparainyectar;

/**
 * Controlador de acceso externo.
 */
public interface ControladorDeAcceso {
  /**
   * Notifica a la heladera que una tarjeta ha sido autorizada a abrir la
   * heladera para ingresar una vianda en las próximas 3 horas.
   */
  void notificarTarjetaColaboradoraHabilitada(String numerosDeTarjeta);
}