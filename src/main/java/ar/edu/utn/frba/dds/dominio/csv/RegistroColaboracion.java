package ar.edu.utn.frba.dds.dominio.csv;

import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.TipoColaboracion;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoDocumento;
import java.time.LocalDate;

/**
 * Registra las colaboraciones.
 */
public class RegistroColaboracion {
  private TipoDocumento tipoDocumento;
  private int documento;
  private String nombre;
  private String apellido;
  private String mail;
  private LocalDate fechaDeColaboracion;
  private TipoColaboracion tipoColaboracion;
  private int cantidad;
  
  /**
   * Constructor principal.
   *
   * @param colaborador Colaborador.
   * @param colaboracion Colaboración que realiza.
   */
  public RegistroColaboracion(ColaboradorHumana colaborador, Colaboracion colaboracion) {
    this.tipoDocumento = colaborador.getTipoDocumento();
    this.documento = colaborador.getDocumento();
    this.nombre = colaborador.getNombre();
    this.apellido = colaborador.getApellido();
    this.mail = colaborador.getMail();
    this.fechaDeColaboracion = colaboracion.getFecha();
    this.tipoColaboracion = colaboracion.getTipoColaboracion();
    this.cantidad = colaborador.cantidadDeColaboracionesDe(tipoColaboracion);
  }
  
  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }
  
  public int getDocumento() {
    return documento;
  }
  
  public String getNombre() {
    return nombre;
  }
  
  public String getApellido() {
    return apellido;
  }
  
  public String getMail() {
    return mail;
  }
  
  public int getCantidad() {
    return cantidad;
  }
  
  public TipoColaboracion getTipoColaboracion() {
    return tipoColaboracion;
  }
  
  public LocalDate getFechaDeColaboracion() {
    return fechaDeColaboracion;
  }
}