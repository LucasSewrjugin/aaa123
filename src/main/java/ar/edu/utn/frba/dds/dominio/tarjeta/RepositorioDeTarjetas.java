package ar.edu.utn.frba.dds.dominio.tarjeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de tarjetas.
 */
public class RepositorioDeTarjetas {
  private static RepositorioDeTarjetas instance;
  private final List<SolicitudTarjeta> listaDeSolicitudes = new ArrayList<>();
  
  private RepositorioDeTarjetas() {
  }
  
  /**
   * Singleton para el repositorio de tarjetas.
   *
   * @return Devuelve la instancia.
   */
  public static RepositorioDeTarjetas getInstance() {
    if (instance == null) {
      instance = new RepositorioDeTarjetas();
    }
    return instance;
  }
  
  /**
   * Agrega una solicitud de tarjeta.
   *
   * @param unaSolicitud Solicitud recibida.
   */
  public void agregarSolicitud(SolicitudTarjeta unaSolicitud) {
    if (!yaExisteSolicitud(unaSolicitud)) {
      listaDeSolicitudes.add(unaSolicitud);
    }
  }
  
  private boolean yaExisteSolicitud(SolicitudTarjeta unaSolicitud) {
    return listaDeSolicitudes.stream()
        .map(SolicitudTarjeta::colaborador)
        .toList()
        .contains(unaSolicitud.colaborador());
  }
  
  public List<SolicitudTarjeta> getListaDeSolicitudes() {
    return listaDeSolicitudes;
  }
}
/*
 * El operador se encarga de ver el listado de solicitudes de tarjetas
 * pendientes. Este operador solo debe saber quien está esperando por una
 * tarjeta y dónde se encuentra.
 *
 * Unica tarjeta por colaborador
 */