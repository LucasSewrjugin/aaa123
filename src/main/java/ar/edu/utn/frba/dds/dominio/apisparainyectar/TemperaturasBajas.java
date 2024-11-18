package ar.edu.utn.frba.dds.dominio.apisparainyectar;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.incidente.Incidente;
import ar.edu.utn.frba.dds.dominio.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.dominio.serviciosexternos.Action;
import java.time.LocalDateTime;

/**
 * Implementación de acción para temperaturas bajas.
 */
public class TemperaturasBajas implements Action {
  private Heladera heladera;

  /**
   * Constructor principal.
   *
   * @param heladera Heladera en cuestión.
   */
  public TemperaturasBajas(Heladera heladera) {
    this.heladera = heladera;
  }
  
  @Override
  public void executeForTemperature(double temperature) {
    heladera.detectarTemperaturaIimperfecta();
    TipoIncidente alerta = TipoIncidente.crearSinId(true, null,
        "Temperatura irregular detectada", null, null);
    // TODO: Corregir lo de long ID que lo pide al momento de crear
    Incidente nuevoIncidente = new Incidente(null, alerta, LocalDateTime.now(), heladera);
    heladera.agregarIncidente(nuevoIncidente);
  }
}
