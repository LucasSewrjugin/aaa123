package ar.edu.utn.frba.dds.dominio.heladera;

import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.incidente.Incidente;
import ar.edu.utn.frba.dds.dominio.incidente.Visita;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entidad técnico.
 */
@Entity
public class Tecnico {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String nombre;
  @Embedded
  private Ubicacion ubicacion;
  @OneToMany
  private List<Incidente> heladerasArevisar;
  
  /**
   * Constructor principal.
   *
   * @param nombre Nombre del técnico.
   * @param ubicacion Ubicación.
   */
  public Tecnico(String nombre, Ubicacion ubicacion) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
  }
  
  public Ubicacion getUbicacion() {
    return ubicacion;
  }
  
  /**
   * Notifica al técnico por un incidente.
   *
   * @param incidente Incidente a reportar.
   */
  public void notify(Incidente incidente) {
    heladerasArevisar.add(incidente);
  }
  
  /**
   * Método.
   *
   * @param incidente Incidente.
   */
  public void revisar(Incidente incidente) {
    if (!heladerasArevisar.contains(incidente)) {
      throw new RuntimeException("No tiene incidente el tecnico");
    }
    Heladera heladera = incidente.heladera();
    // El tecnico (persona física) ingresa los datos correspondientes para
    // generar
    // la visita que realizó
    /* DATOS INGRESADOS POR EL TECNICO */
    heladera.registrarVisita(new Visita(null, null, this, null, nombre, nombre,
        null)); /*
                 * en la funcion del lado de la heladera revisar la visita
                 * registrada, si el atributo resuelto es true, cambiar el
                 * estado de la heladera a "activa"
                 */
    heladerasArevisar.remove(incidente);
  }
}