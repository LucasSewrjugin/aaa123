package ar.edu.utn.frba.dds.dominio.incidente;

import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DistribucionViandas;
import ar.edu.utn.frba.dds.dominio.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.dominio.colaboraciones.TipoColaboracion;
import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;
import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Instancia de reporte.
 */
public class Reporte {
  private Organizacion organizacion;
  private List<Incidente> incidentes;
  //////////////////////////
  ///// CONSTRUCTOR ///////
  ////////////////////////
  /////////////////////////
  // REPORTES HELADERA //
  ///////////////////////
  
  /**
   * Queremos saber si hubo fallas en los últimos N días.
   *
   * @param  incidentes Lista de incidentes.
   * @param  dias       Días a contar.
   * @return            Devuelve la cantidad de fallas reportadas.
   */
  public int fallasEnLosUltimosNdias(List<Incidente> incidentes, int dias) {
    return incidentes.stream()
        .filter(
            incidente -> incidente.fecha().isAfter(LocalDateTime.now().minusDays(dias)))
        .toList().size();
  }
  
  /**
   * Genera un reporte de fallas.
   *
   * @return Devuelve la lista de reportes.
   */
  public List<String> generarReporteDeFallasPorHeladera() {
    List<String> fallasHeladeras = new ArrayList<>();
    organizacion.getHeladeras()
        .forEach(heladera -> fallasHeladeras.add(heladera.getNombre() + " tuvo "
            + fallasEnLosUltimosNdias(heladera.getRegistroIncidentes(), 7)
            + " fallas la ultima semana"));
    return fallasHeladeras;
  }
  
  /**
   * Genera reportes de viandas por cada heladera.
   *
   * @return Devuelve la lista de reportes.
   */
  public List<String> generarReporteViandasPorHeladera() {
    List<String> viandasAgregadasYretiradas = new ArrayList<>();
    // extraer de los logs de las heladeras los de la ultima semana y contar
    // ingresos y retiros x separado
    return viandasAgregadasYretiradas;
  }
  
  /**
   * Genera reporte de viandas a partir del colaborador.
   *
   * @return Devuelve la lista de viandas.
   */
  public List<String> generarReporteViandasPorColaborador() {
    List<String> viandasDonadasPorColaborador = new ArrayList<>();
    donacionesDeViandaDeLosUltimos(7);
    return viandasDonadasPorColaborador;
  }
  /////////////////////////
  //// COMPORTAMIENTO ////
  ///////////////////////
  
  /**
   * Genera un reporte completo de fallas.
   *
   * @return Devuelve la lista de reportes.
   */
  public List<String> generarReporteDeFallas() {
    List<Incidente> incidentesSemana = incidentes.stream()
        .filter(incidente -> incidente.fecha().isAfter(LocalDateTime.now().minusDays(7)))
        .toList();
    Map<Object, Long> nombreContador = incidentesSemana.stream()
        .collect(Collectors.groupingBy(incidente -> incidente.heladera().getNombre(),
            Collectors.counting()));
    return nombreContador.entrySet().stream()
        .map(entry -> "la heladera " + entry.getKey() + " tiene " + entry.getValue()
            + " fallas")
        .collect(Collectors.toList());
  }
  
  /**
   * Queremos conocer las viandas ingresadas a una heladera en los últimos N
   * días.
   *
   * @param  cantidadDeDias Cantidad de días a evaluar.
   * @return                Devuelve la lista de viandas que se ingresaron.
   */
  public List<String> viandasIngresadasPorHeladeraEnLosUltimos(int cantidadDeDias) {
    List<Map.Entry<Heladera, Integer>> viandasIngresadasPorHeladera =
        viandasRecibidasPorHeladeraEnLosUltimos(cantidadDeDias);
    Map<Heladera, Integer> sumasPorHeladera =
        agruparYsumarViandas(viandasIngresadasPorHeladera);
    List<String> resultado = pasarAtextoLasViandasIngresadasPorHeladera(sumasPorHeladera);
    return resultado;
  }
  
  private Map<Heladera, Integer> agruparYsumarViandas(
      List<Map.Entry<Heladera, Integer>> viandasIngresadasPorHeladera) {
    return viandasIngresadasPorHeladera.stream()
        .collect(Collectors.groupingBy(
            Map.Entry::getKey,
            Collectors.summingInt(Map.Entry::getValue)));
  }
  
  private List<Map.Entry<Heladera, Integer>> heladerasQueRecibieronViandasPorDonacionEnLosUltimos(
      int dias) {
    List<DonacionVianda> donaciones = donacionesDeViandaDeLosUltimos(dias);
    List<Map.Entry<Heladera, Integer>> heladeras = donaciones.stream()
        .map(donacion -> Map.entry(donacion.getHeladera(), 1))
        .collect(Collectors.toList());
    return heladeras;
  }
  
  private List<String> pasarAtextoLasViandasExtraidasPorHeladera(
      Map<Heladera, Integer> sumasPorHeladera) {
    return sumasPorHeladera.entrySet().stream()
        .map(entry -> "De la heladera " + entry.getKey().getNombre() + " se retiraron "
            + entry.getValue() + " viandas")
        .collect(Collectors.toList());
  }
  
  private List<String> pasarAtextoLasViandasIngresadasPorHeladera(
      Map<Heladera, Integer> sumasPorHeladera) {
    return sumasPorHeladera.entrySet().stream()
        .map(entry -> "La heladera " + entry.getKey().getNombre() + " recibió "
            + entry.getValue() + " viandas")
        .collect(Collectors.toList());
  }
  
  private Map<ColaboradorHumana, Integer> agruparYsumarViandasPorColaborador(
      List<Map.Entry<ColaboradorHumana, Integer>> viandasPorColaborador) {
    return viandasPorColaborador.stream()
        .collect(Collectors.groupingBy(
            Map.Entry::getKey,
            Collectors.summingInt(Map.Entry::getValue)));
  }
  
  private List<Map.Entry<Heladera, Integer>> 
      heladerasQueRecibieronViandasPorDistribucionEnLosUltimos(
      int dias) {
    List<DistribucionViandas> distribuciones = distribucionDeViandaDeLosUltimos(dias);
    List<Map.Entry<Heladera, Integer>> heladerasConCantidadDeViandas =
        viandasRecibidasPorHeladeraEnCadaDistribucion(distribuciones);
    return heladerasConCantidadDeViandas;
  }
  
  private List<Map.Entry<Heladera, Integer>> viandasRecibidasPorHeladeraEnCadaDistribucion(
      List<DistribucionViandas> distribuciones) {
    List<Map.Entry<Heladera, Integer>> heladerasConCantidadDeViandas =
        distribuciones.stream()
            .map(
                dist -> Map.entry(dist.getHeladeraDestino(), dist.getCantidadDeViandas()))
            .collect(Collectors.toList());
    return heladerasConCantidadDeViandas;
  }
  
  private List<Map.Entry<Heladera, Integer>> viandasExtraidasPorHeladeraEnCadaDistribucion(
      List<DistribucionViandas> distribuciones) {
    List<Map.Entry<Heladera, Integer>> heladerasConCantidadDeViandas =
        distribuciones.stream()
            .map(dist -> Map.entry(dist.getHeladeraOrigen(), dist.getCantidadDeViandas()))
            .collect(Collectors.toList());
    return heladerasConCantidadDeViandas;
  }
  
  private List<Map.Entry<Heladera, Integer>> viandasExtraidasPorHeladeraEnLosUltimos(
      int dias) {
    List<DistribucionViandas> distribuciones = distribucionDeViandaDeLosUltimos(dias);
    List<Map.Entry<Heladera, Integer>> heladerasConCantidadDeViandasExtraidas =
        viandasExtraidasPorHeladeraEnCadaDistribucion(distribuciones);
    return heladerasConCantidadDeViandasExtraidas;
  }
  
  private List<Map.Entry<Heladera, Integer>> viandasRecibidasPorHeladeraEnLosUltimos(
      int cantidadDeDias) {
    List<Map.Entry<Heladera, Integer>> recibieronPorDistribucion =
        heladerasQueRecibieronViandasPorDistribucionEnLosUltimos(cantidadDeDias);
    List<Map.Entry<Heladera, Integer>> recibieronPorDonacion =
        heladerasQueRecibieronViandasPorDonacionEnLosUltimos(cantidadDeDias);
    List<Map.Entry<Heladera, Integer>> heladerasTotales =
        new ArrayList<>(recibieronPorDistribucion);
    heladerasTotales.addAll(recibieronPorDonacion);
    return heladerasTotales;
  }
  /////////////////
  // COMPARTIDOS //
  /////////////////
  
  private List<Colaboracion> colaboracionesDeLosUltimos(int cantidadDeDias) {
    return organizacion.getUsuarios().stream()
        .flatMap(colaborador -> colaborador
            .colaboracionesApartirDe(LocalDate.now().minusDays(cantidadDeDias)).stream())
        .collect(Collectors.toList());
  }
  
  private List<DistribucionViandas> distribucionDeViandaDeLosUltimos(int dias) {
    List<DistribucionViandas> distribuciones =
        this.colaboracionesDeLosUltimos(dias).stream()
            .filter(colaboracion -> colaboracion.getTipoColaboracion()
                .equals(TipoColaboracion.DISTRIBUIR_VIANDA))
            .map(colaboracion -> (DistribucionViandas) colaboracion)
            .collect(Collectors.toList());
    return distribuciones;
  }
  
  private List<DonacionVianda> donacionesDeViandaDeLosUltimos(int dias) {
    List<DonacionVianda> donaciones = this.colaboracionesDeLosUltimos(dias).stream()
        .filter(colaboracion -> colaboracion.getTipoColaboracion()
            .equals(TipoColaboracion.DONAR_VIANDA))
        .map(colaboracion -> (DonacionVianda) colaboracion).collect(Collectors.toList());
    return donaciones;
  }
  /////////////////////////////
  // VIANDAS POR COLABORADOR //
  /////////////////////////////
  
  private List<Map.Entry<ColaboradorHumana, Integer>> viandasDistribuidasPorColaborador(
      List<DistribucionViandas> distribuciones) {
    List<Map.Entry<ColaboradorHumana, Integer>> colaboradorConViandas =
        distribuciones.stream()
            .map(dist -> Map.entry(dist.getColaborador(), dist.getCantidadDeViandas()))
            .collect(Collectors.toList());
    return colaboradorConViandas;
  }
  
  private List<Map.Entry<ColaboradorHumana, Integer>> viandasDonadasPorColaborador(
      List<DonacionVianda> donaciones) {
    List<Map.Entry<ColaboradorHumana, Integer>> colaboradorConViandas =
        donaciones.stream()
            .map(dist -> Map.entry(dist.getColaborador(), 1))
            .collect(Collectors.toList());
    return colaboradorConViandas;
  }
  
  private List<Map.Entry<ColaboradorHumana, Integer>> viandasPorColaboradorEnLosUltimos(
      int dias) {
    List<DistribucionViandas> distribuciones = distribucionDeViandaDeLosUltimos(dias);
    List<DonacionVianda> donaciones = donacionesDeViandaDeLosUltimos(dias);
    List<Map.Entry<ColaboradorHumana, Integer>> porDistribucion =
        viandasDistribuidasPorColaborador(distribuciones);
    List<Map.Entry<ColaboradorHumana, Integer>> porDonacion =
        viandasDonadasPorColaborador(donaciones);
    List<Map.Entry<ColaboradorHumana, Integer>> colaboracionesTotales =
        new ArrayList<>(porDistribucion);
    colaboracionesTotales.addAll(porDonacion);
    return colaboracionesTotales;
  }
  
  /**
   * Genera un informe de cuántas viandas donó o distribuyó un colaborador.
   *
   * @param  viandaPorColaborador Vianda a mapear,
   * @return                      Devuelve el informe.
   */
  public List<String> pasarAtextoLasViandasPorColaborador(
      Map<ColaboradorHumana, Integer> viandaPorColaborador) {
    return viandaPorColaborador.entrySet().stream()
        .map(entry -> "El Colaborador " + entry.getKey().getNombre() + " Dono/Distribuyo "
            + entry.getValue() + " viandas")
        .collect(Collectors.toList());
  }
  
  /**
   * Reporte a partir de un colaborador en los últimos N días.
   *
   * @param  cantidadDeDias Dias a evaluar.
   * @return                Devuelve el informe.
   */
  public List<String> reportePorColaboradorEnLosUltimos(int cantidadDeDias) {
    List<Map.Entry<ColaboradorHumana, Integer>> viandasPorColaborador =
        viandasPorColaboradorEnLosUltimos(cantidadDeDias);
    Map<ColaboradorHumana, Integer> viandaPorColaborador =
        agruparYsumarViandasPorColaborador(viandasPorColaborador);
    List<String> resultado = pasarAtextoLasViandasPorColaborador(viandaPorColaborador);
    return resultado;
  }
}
