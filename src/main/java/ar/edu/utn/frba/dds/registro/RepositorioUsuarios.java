package ar.edu.utn.frba.dds.registro;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Repositorio de usuarios.
 */
public class RepositorioUsuarios implements WithSimplePersistenceUnit {
  private static RepositorioUsuarios instance;
  
  /**
   * Singleton para repositorio de usuarios.
   *
   * @return Devuelve la única instancia.
   */
  public static RepositorioUsuarios getInstance() {
    if (instance == null) {
      instance = new RepositorioUsuarios();
    }
    return instance;
  }
  
  /**
   * Registra la organización.
   *
   * @param organizacion Organización a registrar.
   */
  public void registrarOrg(Organizacion organizacion) {
    entityManager().persist(organizacion);
  }
  
  /**
   * Registra un colaborador.
   *
   * @param colaborador Colaborador a registrar.
   */
  public void registrarColaborador(Colaborador colaborador) {
    entityManager().persist(colaborador);
  }
  
  /**
   * Busca un colaborador a partir de un ID.
   *
   * @param id ID especficado.
   * @return Devuelve el colaborador encontrado.
   */
  public Colaborador buscarColaboradorId(long id) {
    return entityManager().find(Colaborador.class, id);
  }
  
  /**
   * Busca un colaborador a partir de un usuario y contraseña.
   *
   * @param  usuario  Usuario.
   * @param  password Contraseña.
   * @return          Devuelve el colaborador si lo encuentra.
   */
  public Colaborador buscarColaborador(String usuario, String password) {
    return entityManager()
        .createQuery(
            "from Colaborador where username = :usuario and password = :password",
            Colaborador.class)
        .setParameter("nombre", usuario)
        .setParameter("password", DigestUtils.sha256Hex(password))
        .getResultList().get(0);
  }
  
  /**
   * Registra un colaborador humano.
   *
   * @param colaborador Colaborador a registrar.
   */
  public void registrarColaboradorHumana(ColaboradorHumana colaborador) {
    entityManager().persist(colaborador);
  }
  
  /**
   * Busca un colaborador humano a partir de un ID.
   *
   * @param id ID especificado.
   * @return Devuelve el colaborador encontrado.
   */
  public ColaboradorHumana buscarColaboradorHumanaId(long id) {
    return entityManager().find(ColaboradorHumana.class, id);
  }
  
  /**
   * Busca un colaborador por su nombre.
   *
   * @param  nombre Nombre del colaborador.
   * @return        Devuelve el colaborador si lo encuentra.
   */
  public ColaboradorHumana buscarPorNombre(String nombre) {
    /*
     * return entityManager()
     * .createQuery("from ColaboradorHumana ch INNER JOIN ch.colaborador c" +
     * " where c.username = :nombre" , ColaboradorHumana.class)
     * .setParameter("nombre", nombre) //.setParameter("hashPassorwd",
     * DigestUtils.sha256Hex(contrasenia)) .getResultList() .get(0);
     */
    CriteriaBuilder cb = entityManager().getCriteriaBuilder();
    CriteriaQuery<ColaboradorHumana> query = cb.createQuery(ColaboradorHumana.class);
    Root<ColaboradorHumana> root = query.from(ColaboradorHumana.class);
    // Aquí accedemos a los atributos de la clase A a través de la clase B
    Predicate condiciones = cb.and(cb.equal(root.get("username"), nombre)
    // cb.equal(root.get("password"), password)
    );
    query.select(root).where(condiciones);
    return entityManager().createQuery(query).getSingleResult();
  }
  
  /**
   * Busca el colaborador a través de sus credenciales de acceso.
   *
   * @param  nombre   Usuario.
   * @param  password Contraseña.
   * @return          Devuelve el colaborador si lo encuentra.
   */
  public ColaboradorHumana buscarColaboradorHumana(String nombre, String password) {
    CriteriaBuilder cb = entityManager().getCriteriaBuilder();
    CriteriaQuery<ColaboradorHumana> query = cb.createQuery(ColaboradorHumana.class);
    Root<ColaboradorHumana> root = query.from(ColaboradorHumana.class);
    Predicate condiciones = cb.and(cb.equal(root.get("username"), nombre),
        cb.equal(root.get("password"), DigestUtils.sha256Hex(password)));
    query.select(root).where(condiciones);
    return entityManager().createQuery(query).getSingleResult();
  }
}
