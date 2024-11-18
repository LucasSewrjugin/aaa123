package ar.edu.utn.frba.dds.dominio.colaboradores;

import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.TipoColaboracion;
import java.util.Arrays;
import java.util.List;


// NO nos interesa persistirlo o hacerlo tabla
public class AsesorDeColaboraciones {
  private static AsesorDeColaboraciones instance = new AsesorDeColaboraciones();
  private List<TipoColaboracion> colaboracionesHumanas = Arrays.asList(TipoColaboracion.DISTRIBUIR_VIANDA, TipoColaboracion.DONAR_DINERO, TipoColaboracion.DONAR_VIANDA, TipoColaboracion.REGISTRO_PERSONAS_SITUACION_VULNERABLE);
  private List<TipoColaboracion> colaboracionesJuridicas = Arrays.asList(TipoColaboracion.COLOCAR_HELADERA, TipoColaboracion.DONAR_DINERO);

  private AsesorDeColaboraciones() {
  }

  public static AsesorDeColaboraciones getInstance() {
    return instance;
  }

  public void validarColaboracion(Colaboracion colaboracion, Colaborador colaborador) {
    if (colaborador.soyHumano() && !esDeTipoHumano(colaboracion.getTipoColaboracion())) {
      throw new RuntimeException("Error: Es un colaborador humano y esta haciendo colaboracion de otro tipo");
    }
    if (!colaborador.soyHumano() && !esDeTipoJuridico(colaboracion.getTipoColaboracion())) {
      throw new RuntimeException("Error: Es un colaborador juridico y esta haciendo colaboracion de otro tipo");
    }
  }

  public Boolean esDeTipoHumano(TipoColaboracion tipo) {
    return colaboracionesHumanas.contains(tipo);
  }

  public Boolean esDeTipoJuridico(TipoColaboracion tipo) {
    return colaboracionesJuridicas.contains(tipo);
  }
}
