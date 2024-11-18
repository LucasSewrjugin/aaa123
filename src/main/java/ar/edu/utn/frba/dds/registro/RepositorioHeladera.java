package ar.edu.utn.frba.dds.registro;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Repositorio de heladeras.
 */
public class RepositorioHeladera implements WithSimplePersistenceUnit {
  private static RepositorioHeladera instance;
  
  /**
   * Singleton para el repositorio.
   *
   * @return Devuelve la única instancia.
   */
  public static RepositorioHeladera getInstance() {
    if (instance == null) {
      instance = new RepositorioHeladera();
    }
    return instance;
  }

  /**
   * Registra una heladera.
   *
   * @param heladera Heladera a registrar.
   */
  public void registrarHeladera(Heladera heladera) {
    entityManager().persist(heladera);
  }
  
  /**
   * Busca una heladera a partir de un ID.
   *
   * @param id ID especificado.
   * @return Devuelve la heladera encontrada.
   */
  public Heladera buscarHeladeraId(long id) {
    return entityManager().find(Heladera.class, id);
  }
  
  /**
   * Buscamos la heladera a las que el colaborador puede acceder.
   *
   * @param  usuario  Usuario.
   * @param  password Contraseña.
   * @return          Devuelve las heladeras.
   */
  public Colaborador buscarHeladera(String usuario, String password) {
    return entityManager()
        .createQuery(
            "from Colaborador where username = :usuario and password = :password",
            Colaborador.class)
        .setParameter("nombre", usuario)
        .setParameter("password", DigestUtils.sha256Hex(password))
        .getResultList()
        .get(0);
  }
}
