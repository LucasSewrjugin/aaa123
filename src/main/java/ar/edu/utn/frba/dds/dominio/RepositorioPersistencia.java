package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

/**
 * Repositorio de persistencia.
 */
public class RepositorioPersistencia implements WithSimplePersistenceUnit {
  /**
   * Método para registrar una heladera.
   *
   * @param heladera Heladera a registrar.
   */
  public void registrarHeladera(Heladera heladera) {
    entityManager().persist(heladera);
  }
  
  /**
   * Busca una heladera a partir de su ID.
   *
   * @param id ID de la heladera en cuestión.
   * @return Devuelve la heladera si la encuentra o null en caso contrario.
   */
  public Heladera buscarHeladera(long id) {
    return entityManager().find(Heladera.class, id);
  }
  
  /**
   * Lista todas las heladeras.
   *
   * @return Devuelve la lista.
   */
  @SuppressWarnings("unchecked")
  public List<Heladera> todasLasHeladeraes() {
    return entityManager()
        .createQuery("from Heladera")
        .getResultList();
  }
  
  /**
   * Filtra las heladeras según su numbre.
   *
   * @param  nombre Nombre de la heladera buscada.
   * @return        Devuelve la heladera en cuestión.
   */
  @SuppressWarnings("unchecked")
  public List<Heladera> filtrarHeladeraPorNombre(String nombre) {
    return entityManager()
        .createQuery("from Heladera where nombre = :nombre")
        .setParameter("nombre", nombre)
        .getResultList();
  }
  
  /**
   * Registra una organización.
   *
   * @param org Organización a registrar.
   */
  public void registrarOrg(Organizacion org) {
    entityManager().persist(org);
  }
  
  /**
   * Busca una organización a partir de su ID.
   *
   * @param id ID de la org.
   * @return Devuelve la organización.
   */
  public Organizacion buscarOrg(long id) {
    return entityManager().find(Organizacion.class, id);
  }
}