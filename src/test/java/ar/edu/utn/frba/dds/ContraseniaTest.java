package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.registro.ValidadorCredenciales;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test unitario de contraseñas.
 */
public class ContraseniaTest {
  private ValidadorCredenciales validador;
  
  /**
   * Inicializamos las variables.
   */
  @BeforeEach
  public void inicializarVariables() {
    /*
     * El classLoader nos permite obtener la ruta del archivo sin necesidad de
     * pasarle la ruta absoluta, lo que lo hace más portable.
     */
    ClassLoader classLoader = getClass().getClassLoader();
    String path = Objects
        .requireNonNull(classLoader.getResource("contraseniasDebiles.txt")).getPath();
    validador = ValidadorCredenciales.getInstance(path);
  }
  
  @Test
  public void unaContraseniaEsDebilSiSeEncuentraEnElArchivo() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> validador.validarContrasenia("password"));
  }
  
  @Test
  public void unaContraseniaNoEsDebilSiNoSeEncuentraEnElArchivo() {
    Assertions.assertEquals("!%BuenaPassword14!%%76",
        validador.validarContrasenia("!%BuenaPassword14!%%76"));
  }
}
