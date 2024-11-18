package ar.edu.utn.frba.dds.registro;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Validador de credenciales.
 */
public class ValidadorCredenciales {
  private static ValidadorCredenciales instance;
  private String path;
  private List<ValidacionContrasenia> validaciones;
  
  private ValidadorCredenciales(String unPath) {
    this.path = unPath;
    this.validaciones = new ArrayList<>();
  }
  
  /**
   * Singleton para validador.
   *
   * @param  unPath Ruta del archivo de datos que contiene las claves más
   *                usadas.
   * @return        Devuelve la única instancia.
   */
  public static ValidadorCredenciales getInstance(String unPath) {
    if (instance == null) {
      instance = new ValidadorCredenciales(unPath);
    }
    instance.agregarValidacion(instance.esContrasenaDebil());
    instance.agregarValidacion(instance.noTieneTamanioAdecuado());
    return instance;
  }
  
  /**
   * Agrega una contraseña que fue validada.
   *
   * @param validacion Validación a añadir.
   */
  public void agregarValidacion(ValidacionContrasenia validacion) {
    validaciones.add(validacion);
  }
  
  // PATH de txt:
  // src/main/java/ar/edu/utn/frba/dds/resources/contraseniasDebiles.txt
  private void cargarContraseniasDebiles(Set<String> contraseniasDebiles) {
    File archivo = new File(this.path);
    try (Scanner scanner = new Scanner(archivo)) {
      while (scanner.hasNextLine()) {
        String linea = scanner.nextLine().trim();
        if (!linea.isEmpty()) { // Ignorar líneas vacías
          contraseniasDebiles.add(linea);
        }
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("No se pudo encontrar el archivo");
      // System.err.println("No se pudo encontrar el archivo: " +
      // e.getMessage());
    }
  }
  
  /**
   * Validador de usuario disponible para registro.
   *
   * @param  username      Usuario.
   * @param  colaboradores Lista de colaboradores a evaluar.
   * @return               Devuelve el usuario a registrar en caso de que no
   *                       exista.
   */
  public String validarUsername(String username, List<Colaborador> colaboradores) {
    if (username.isEmpty()) {
      throw new IllegalArgumentException("Por favor, ingrese un nombre de usuario");
    }
    if (esCredencialDefault(username)) {
      throw new IllegalArgumentException(
          "Las credenciales por defecto no estan autorizadas");
    }
    if (usuarioYaExistente(username, colaboradores)) {
      throw new IllegalArgumentException("Este usuario ya esta registrado");
    }
    return username;
  }
  
  /**
   * Validador de la contraseña.
   *
   * @param  password Password a evaluar.
   * @return          Devuelve la contraseña en caso de éxito.
   */
  public String validarContrasenia(String password) {
    // TODO Al mensaje de requisitos agregarle cuales son dichos requsitos
    // (Leerlos en la pagina web especificada)
    for (ValidacionContrasenia validacion : validaciones) {
      validacion.validar(password);
    }
    return password;
  }
  
  /**
   * Nos dice si la contraseña es débil.
   *
   * @return Devuelve la instancia de contraseña.
   */
  public ValidacionContrasenia esContrasenaDebil() {
    Set<String> contraseniasDebiles = new HashSet<>();
    cargarContraseniasDebiles(contraseniasDebiles);
    return new ValidacionContrasenia(password -> {
      if (contraseniasDebiles.contains(password)) {
        throw new IllegalArgumentException("Contraseña muy debil");
      }
    });
  }
  
  private ValidacionContrasenia noTieneTamanioAdecuado() {
    return new ValidacionContrasenia(password -> {
      if (password.length() < 8 || password.length() > 64) {
        throw new IllegalArgumentException("Contrasenia no cumple la longitud esperada");
      }
    });
  }
  
  /**
   * Determina si una contraseña es por defecto.
   *
   * @param username Usuario en cuestión.
   * @return V o F según corresponda.
   */
  public boolean esCredencialDefault(String username) {
    return username.toLowerCase().equals("admin");
  }
  
  /**
   * Nos dice si un usuario ya existe.
   *
   * @param username Usuario que quiere registrarse.
   * @param usuarios Lista de usuarios que puede contener el username en cuestión.
   * @return Devuelve V o F según corresponda.
   */
  public boolean usuarioYaExistente(String username, List<Colaborador> usuarios) {
    Set<String> usernamesExistentes = new HashSet<>();
    for (Colaborador user : usuarios) {
      usernamesExistentes.add(user.getUsername());
    }
    return usernamesExistentes.contains(username);
  }
  
  /**
   * Calcula el cifrado de la contraseña.
   *
   * @param input Contraseña a cifrar.
   * @return Devuelve el cifrado en SHA256.
   */
  public String calcularHash(String input) {
    return DigestUtils.sha256Hex(input);
    // return input.hashCode();
  }
}
/*
 * A continuación, te describo algunas de las recomendaciones clave de la
 * sección 5.1.1.2 del NIST 800-63 que podrías considerar al alinear tu política
 * de contraseñas:
 * 
 * Longitud de la Contraseña: Las contraseñas deben tener al menos 8 caracteres
 * de longitud cuando las crea el usuario.HECHO
 * 
 * Deben tener al menos 6 caracteres cuando sean generadas automáticamente por
 * el sistema y sean temporales.HECHO
 * 
 * Se recomienda permitir una longitud máxima de al menos 64 caracteres para
 * permitir el uso de frases de contraseña.HECHO
 * 
 * Complejidad: Se debe permitir el uso de una amplia gama de caracteres (letras
 * mayúsculas y minúsculas, números, símbolos y espacios). HECHO
 * 
 * Las contraseñas no deben imponer artificialmente la necesidad de incluir
 * ciertos tipos de caracteres (como al menos una mayúscula, etc.). HECHO En
 * cambio, se deben permitir pero no requerir tipos de caracteres mixtos.
 * 
 * Se desalienta el uso de preguntas de seguridad que permiten respuestas
 * fácilmente adivinables. HECHO
 * 
 * Rotación de Contraseñas: Se aconseja no forzar la rotación de contraseñas
 * periódicamente sin motivo, pues esto puede llevar a una menor seguridad si
 * los usuarios eligen contraseñas más débiles y más fáciles de recordar debido
 * a la fatiga de tener que cambiarlas constantemente.
 * 
 * La rotación de contraseñas solo se debe requerir si hay evidencia de su
 * compromiso. HECHO
 * 
 * Almacenamiento de Contraseñas: Las contraseñas deben almacenarse en forma de
 * hash utilizando un algoritmo de hashing resistente que incluya un factor de
 * sal (un valor aleatorio añadido a la contraseña antes de aplicar el hash) y
 * un factor de trabajo (que hace computacionalmente costoso calcular el hash).
 * 
 * Nunca se deben almacenar contraseñas en texto plano.
 */