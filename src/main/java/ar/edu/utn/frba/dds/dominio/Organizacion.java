package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.csv.ArchivoCsv;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import ar.edu.utn.frba.dds.dominio.heladera.Tecnico;
import ar.edu.utn.frba.dds.dominio.heladera.TuplaHeladera;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * Entidad de la organización.
 */
@Entity
public class Organizacion {
  @Id
  @GeneratedValue
  private Long id;
  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Heladera> heladeras;
  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Beneficiario> beneficiarios;
  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Colaborador> colaboradores;
  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Tecnico> tecnicos;
  @Column
  private LocalDate fechaDeRegistros;
  
  /**
   * Constructor principal de la organización.
   */
  public Organizacion() {
    heladeras = new ArrayList<>();
    beneficiarios = new ArrayList<>();
    colaboradores = new ArrayList<>();
    tecnicos = new ArrayList<>();
  }
  ///////////////////////
  //// CONSTRUCTOR ////
  /////////////////////
  
  private void cargarDatosViejos(ArchivoCsv importador, String path) {
    importador.leerCsv(path, this);
  }
  
  public LocalDate getFechaDeRegistros() {
    return this.fechaDeRegistros;
  }
  
  public List<Colaboracion> getColaboraciones() {
    return colaboradores.stream().flatMap(colaborador -> colaborador.getColaboraciones().stream())
        .collect(Collectors.toList());
  }
  ////////////////////////
  //// BENEFICIARIOS ////
  //////////////////////
  
  /**
   * Alta de beneficiario.
   *
   * @param nuevoBeneficiario Beneficiario.
   */
  public void altaBeneficiario(Beneficiario nuevoBeneficiario) {
    beneficiarios.add(nuevoBeneficiario);
  }
  
  /**
   * Baja de beneficiario.
   *
   * @param beneficiario Beneficiario.
   */
  public void bajaBeneficiario(Beneficiario beneficiario) {
    beneficiarios.remove(beneficiario);
  }
  
  public List<Beneficiario> getBeneficiarios() {
    return this.beneficiarios;
  }
  ////////////////////////
  //// COLABORADORES ////
  //////////////////////
  
  /**
   * Alta de colaborador.
   *
   * @param colaborador Colaborador.
   */
  public void altaColaborador(Colaborador colaborador) {
    colaboradores.add(colaborador);
  }
  
  /**
   * Baja de colaborador.
   *
   * @param colaborador Colaborador.
   */
  public void bajaColaborador(Colaborador colaborador) {
    colaboradores.remove(colaborador);
  }
  
  public List<Colaborador> getUsuarios() {
    return this.colaboradores;
  }
  
  // <(Pepito,50),(Moria Casan,60),...>
  /**
   * Puntajes generales.
   *
   * @return Devuelve la lista de puntajes.
   */
  public HashMap<String, Double> puntajeDeColaboradores() {
    Map<String, Double> nombreConPuntajeColaboradores = colaboradores.stream()
        .collect(Collectors.toMap(Colaborador::getUsername, Colaborador::puntaje));
    return new HashMap<>(nombreConPuntajeColaboradores);
  }
  
  // El findFirst segun la documentacion de Java y el findAny no deja colocar
  // predicados :/
  /**
   * Colaborador que tiene un determinado documento.
   *
   * @param documento Documento con el que se filtra.
   * @param tipoDocumento Tipo de documento.
   * @return Devuelve el colaborador si lo encuentra, o null en caso contrario.
   */
  public Colaborador colaboradorQueTiene(int documento, TipoDocumento tipoDocumento) {
    List<ColaboradorHumana> colaboradoresHumanos = this.misColaboradoresHumanos();
    return colaboradoresHumanos.stream()
        .filter(colaborador -> colaborador.tengoDocumentoDe(documento, tipoDocumento)).findFirst()
        .orElse(null);
  }
  
  /**
   * Parecido al método anterior pero que matchea (?).
   *
   * @param documento Documento.
   * @param tipoDocumento Tipo de documento.
   * @return Devuelve true o false según corresponda.
   */
  public Boolean tengoColaboradorCon(int documento, TipoDocumento tipoDocumento) {
    List<ColaboradorHumana> colaboradoresHumanos = this.misColaboradoresHumanos();
    return colaboradoresHumanos.stream()
        .anyMatch(colaborador -> colaborador.tengoDocumentoDe(documento, tipoDocumento));
  }
  
  // Esto tiene unn code smell fuerte, pero decidi dejarlo porque no se me ocurre
  // otra forma. Atte: Mati
  /**
   * Lista de los colaboradores humanos.
   *
   * @return Devuelve la lista correspondiente.
   */
  public List<ColaboradorHumana> misColaboradoresHumanos() {
    return colaboradores.stream().filter(colaborador -> colaborador instanceof ColaboradorHumana)
        .map(colaborador -> (ColaboradorHumana) colaborador).toList();
  }
  ///////////////////////
  //// HELADERAS /////
  /////////////////////
  
  /**
   * Alta de heladera.
   *
   * @param heladera Heladera a agregar.
   */
  public void altaHeladera(Heladera heladera) {
    heladeras.add(heladera);
  }
  
  /**
   * Baja de heladera.
   *
   * @param heladera Heladera a quitar.
   */
  public void bajaHeladera(Heladera heladera) {
    if (!heladeras.contains(heladera)) {
      throw new RuntimeException("No existe la heladera");
    }
    heladeras.remove(heladera);
  }
  
  public List<Heladera> getHeladeras() {
    return this.heladeras;
  }
  
  /**
   * Lista la ubicación de las heladeras.
   *
   * @return Devuelve las ubicaciones.
   */
  public List<Ubicacion> listarUbicacionHeladeras() {
    List<Ubicacion> ubicaciones = new ArrayList<>();
    for (Heladera heladera : heladeras) {
      ubicaciones.add(heladera.getUbicacion());
    }
    return ubicaciones;
  }
  
  /**
   * Mapa de heladeras.
   *
   * @return Devuelve lista de tuplas.
   */
  public List<TuplaHeladera> mapaDeHeladeras() {
    return heladeras
        .stream().map(heladera -> new TuplaHeladera(heladera.getNombre(),
            heladera.getNivelDeLlenado(), heladera.getAtencionRequerida()))
        .collect(Collectors.toList());
  }
  
  public List<Tecnico> getTecnicos() {
    return tecnicos;
  }
}
