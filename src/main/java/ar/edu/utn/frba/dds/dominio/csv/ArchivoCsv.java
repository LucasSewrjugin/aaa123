package ar.edu.utn.frba.dds.dominio.csv;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.Ubicacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DistribucionViandas;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.dominio.colaboraciones.RegistrarBeneficiario;
import ar.edu.utn.frba.dds.dominio.colaboraciones.TipoColaboracion;
import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.colaboradores.TipoDocumento;
import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Instancia del archivo CSV para realizar la carga.
 */
public class ArchivoCsv {
  private Organizacion org;
  // private List<ColaboradorHumana> colaboradores; No sabemos si va
  
  /**
   * Constructor principal.
   *
   * @param unaOrg Organización que otorga el archivo para la carga de datos.
   */
  public ArchivoCsv(Organizacion unaOrg) {
    this.org = unaOrg;
  }
  
  // 
  /**
   * Constructor por defecto, necesario para JPA. Podrian ser mas organizacion pero KISS por ahora.
   */
  public ArchivoCsv(/* List<ColaboradorHumana> colaboradores,Organizacion org */) {
    // this.org = org;
    // this.colaboradores = colaboradores;
  }
  
  /**
   * Obtiene un registro de colaboraciones.
   *
   * @param colaborador Colaborador.
   * @return Devuelve la lista de colaboraciones de ese colaborador.
   */
  public List<RegistroColaboracion> obtenerRegistroDeColaboracionApartirDeUnColaborador(
      ColaboradorHumana colaborador) {
    List<Colaboracion> colaboraciones = colaborador.getColaboraciones();
    List<RegistroColaboracion> registrosDeColaboraciones = colaboraciones.stream()
        .map(colaboracion -> new RegistroColaboracion(colaborador, colaboracion))
        .toList();
    return registrosDeColaboraciones;
  }
  
  /**
   * Leer un CSV.
   *
   * @param path Ruta del CSV.
   * @param org  Organización que aporta los datos.
   */
  public void leerCsv(String path, Organizacion org) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // lo uso para sacar la fecha del csv
    try (Reader reader = new FileReader(path);
        CSVParser csvParser = new CSVParser(reader,
            CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());) {
      for (CSVRecord record : csvParser) {
        TipoDocumento tipoDocumento = parsearTipoDocumento(record.get("Tipo Doc"));
        int documento = Integer.parseInt(record.get("Documento"));
        String nombre = record.get("Nombre");
        String apellido = record.get("Apellido");
        String mail = record.get("Mail");
        LocalDate fechaDeColaboracion =
            LocalDate.parse(record.get("Fecha de colaboración"), formatter);
        // Asumo que tengo una entrada por cada colaboracion, es decir, si un
        // colaborador dono dinero y una vianda, aparece 2 veces en el .csv
        TipoColaboracion colaboracion =
            parsearColaboracion(record.get("Forma de colaboración"));
        // Empiezo
        // viendo
        // que
        // onda
        // con una
        int cantidad = Integer.parseInt(record.get("Cantidad"));
        ColaboradorHumana aux =
            new ColaboradorHumana(org, mail, null, null, null, "null", apellido, null);
        Colaboracion nuevaColaboracion =
            registrarColaboracion(aux, fechaDeColaboracion, colaboracion, cantidad);
        RegistroColaboracion fila = new RegistroColaboracion(aux, nuevaColaboracion);
        if (fechaAntesDe(fechaDeColaboracion, org.getFechaDeRegistros())) {
          // TODO: Hay que registrar todo, sólo filtro cuando hago el cálculo de
          // los puntos.
          registrarFila(fila);
        }
        // else{
        // throw new RuntimeException("Error al leer fila del archivo CSV");
        // }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al abrir archivo CSV");
    }
  }
  
  private boolean fechaAntesDe(LocalDate fechaAvalidar, LocalDate fechaLimite) {
    return fechaAvalidar.isBefore(fechaLimite);
  }
  
  private TipoDocumento parsearTipoDocumento(String tipoDocumento) {
    return TipoDocumento.valueOf(tipoDocumento.toUpperCase());
  }
  
  private TipoColaboracion parsearColaboracion(String colaboraciones) {
    switch (colaboraciones.toUpperCase()) {
      case "DINERO":
        return TipoColaboracion.valueOf("DONAR_DINERO");
      case "DONACION_VIANDAS":
        return TipoColaboracion.valueOf("DONAR_VIANDA");
      case "REDISTRIBUCION_VIANDAS":
        return TipoColaboracion.valueOf("DISTRIBUIR_VIANDA");
      case "ENTREGA_TARJETAS":
        return TipoColaboracion.valueOf("REGISTRO_PERSONAS_SITUACION_VULNERABLE");
      default:
        throw new RuntimeException("No existe el tipo de colaboracion");
    }
  }
  // private void registrarFilaNoAniadida(FilaNoAniadida fila) {
  // String motivo = registrarMotivo();
  // filasNoAniadidas.add(new FilaNoAniadida(fila, motivo));
  // }
  // // TODO registrar el motivo por el que no se añadio dicha fila
  // private String registrarMotivo() {
  // return "";
  // }
  
  // private void registrarFila(TipoDocumento unTipoDocumento, int documento,
  // String nombre, String apellido, String mail, LocalDate fechaDeColaboracion,
  // TipoColaboracion unaColaboracion, int cantidad) {
  private void registrarFila(RegistroColaboracion fila) {
    // TODO: si existe lo actualizo, sino lo creo
    Colaborador unColaborador;
    if (existeColaborador(fila.getTipoDocumento(), fila.getDocumento(), org)) {
      unColaborador =
          obtenerColaborador(fila.getTipoDocumento(), fila.getDocumento(), org);
      registrarColaboracion(unColaborador, fila.getFechaDeColaboracion(),
          fila.getTipoColaboracion(), fila.getCantidad());
    } else {
      // TODO: validar datos minimos de colaborador, si es que no se hace en la
      // lectura del csv
      Ubicacion unaUbicacion = new Ubicacion("Nulo", 0, 0);
      LocalDate fechaNacimiento = LocalDate.now();
      unColaborador = new ColaboradorHumana(org, fila.getMail(), 0, 0, null,
          fila.getNombre(), fila.getApellido(), fechaNacimiento);
      // TODO: Completar para crear colaborador.
      registrarColaboracion(unColaborador, fila.getFechaDeColaboracion(),
          fila.getTipoColaboracion(), fila.getCantidad());
    }
  }
  
  private Colaborador obtenerColaborador(TipoDocumento unTipoDocumento, int documento,
      Organizacion org) {
    return org.colaboradorQueTiene(documento, unTipoDocumento);
  }
  
  private boolean existeColaborador(TipoDocumento unTipoDocumento, int documento,
      Organizacion org) {
    return org.tengoColaboradorCon(documento, unTipoDocumento);
  }
  
  // Hay que llevarlo a una lista de colaboraciones
  private Colaboracion registrarColaboracion(Colaborador unColaborador,
      LocalDate fechaDeColaboracion, TipoColaboracion unTipoDeColaboracion,
      int cantidad) {
    switch (unTipoDeColaboracion) {
      case DONAR_DINERO -> {
        DonacionDinero unaDonacionDeDinero =
            crearColaboracionDonarDinero(fechaDeColaboracion);
        iterarAniadirColaboracion(unColaborador, unaDonacionDeDinero, cantidad);
        return unaDonacionDeDinero;
      }
      case DONAR_VIANDA -> {
        DonacionVianda unaDonacionDeVianda =
            crearColaboracionDonarVianda(fechaDeColaboracion);
        iterarAniadirColaboracion(unColaborador, unaDonacionDeVianda, cantidad);
        return unaDonacionDeVianda;
      }
      case DISTRIBUIR_VIANDA -> {
        DistribucionViandas unaDistribucion =
            crearColaboracionDistribucionVianda(fechaDeColaboracion);
        iterarAniadirColaboracion(unColaborador, unaDistribucion, cantidad);
        return unaDistribucion;
      }
      case REGISTRO_PERSONAS_SITUACION_VULNERABLE -> {
        RegistrarBeneficiario unRegistroBeneficiario =
            crearColaboracionEntregaDeTarjetas(fechaDeColaboracion);
        iterarAniadirColaboracion(unColaborador, unRegistroBeneficiario, cantidad);
        return unRegistroBeneficiario;
      }
      default -> {
        throw new RuntimeException("No existe este tipo de colaboracion");
      }
    }
  }
  
  private void iterarAniadirColaboracion(Colaborador colaborador,
      Colaboracion colaboracion, int cantidad) {
    for (int i = 0; i < cantidad; i++) {
      colaborador.aniadirColaboracion(colaboracion);
    }
  }
  
  private DonacionDinero crearColaboracionDonarDinero(LocalDate fechaDeColaboracion) {
    return new DonacionDinero(fechaDeColaboracion, null, 0);
  }
  
  private DonacionVianda crearColaboracionDonarVianda(LocalDate fechaDeColaboracion) {
    return new DonacionVianda(null, fechaDeColaboracion, null, null, true);
  }
  
  private DistribucionViandas crearColaboracionDistribucionVianda(
      LocalDate fechaDeColaboracion) {
    return new DistribucionViandas(null, null, null, null, null, fechaDeColaboracion);
  }
  
  private RegistrarBeneficiario crearColaboracionEntregaDeTarjetas(
      LocalDate fechaDeColaboracion) {
    return new RegistrarBeneficiario(fechaDeColaboracion, null, null, null);
  }
}
/*
 * Donación Dinero: No tenemos la cantidad de pesos donados. Distribución de
 * viandas: No tenemos la cantidad de viandas distribuidas. Viandas donadas: No
 * tenemos el vencimiento de la vianda. Distribución de tarjetas: No tenemos
 * datos de la persona en situación de riesgo para darle el alta y ver cuantas
 * veces uso la tarjeta. Administración de heladeras: No tenemos datos de la
 * heladera para darle el alta, por lo que nunca van a estar activas.
 */
