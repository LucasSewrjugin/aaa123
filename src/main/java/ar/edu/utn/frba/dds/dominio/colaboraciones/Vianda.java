package ar.edu.utn.frba.dds.dominio.colaboraciones;

import ar.edu.utn.frba.dds.dominio.colaboradores.Colaborador;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Vianda {

  @Id
  @GeneratedValue
  private long id;

  @Column
  private String comida;

  @Column
  private LocalDateTime fechaDeCaducidad;

  @Column
  private LocalDateTime fechaDonacion;

  @Column
  private Integer calorias;

  @Column
  private Double peso; // Como peso estandar es en gramos

  @Column
  private Boolean entregada;

  @ManyToOne
  private Colaborador colaborador;

  ///////////////////////
  //// CONSTRUCTOR  ////
  /////////////////////

  public Vianda(String unaComida, LocalDateTime unaFechaDeCaducidad, LocalDateTime unaFechaDeDonacion, Colaborador unColaborador, int calorias, Double peso) {
    if (unaComida.isEmpty() || unaFechaDeCaducidad == null || unaFechaDeDonacion == null || unColaborador == null) {
      throw new IllegalArgumentException("La comida, fecha de caducidad, fecha de donacion, colaborador y la heladera son datos obligatorios");
    }
    this.comida = unaComida;
    this.fechaDeCaducidad = unaFechaDeCaducidad;
    this.fechaDonacion = unaFechaDeDonacion;
    this.colaborador = unColaborador;
    this.calorias = calorias; //Opcional
    this.peso = peso; //Opcional
    this.entregada = false;
  }


  ///////////////////////
  // COMPORTAMIENTO  ///
  /////////////////////

  public void confirmarEntrega() {
    this.entregada = true;
  }

  public Boolean fueEntregada() {
    return this.entregada;
  }

  public Boolean esFresca() {
    return LocalDateTime.now().isBefore(fechaDeCaducidad);
  }

  //////////////////////////
  /////   GETTERS   ///////
  ////////////////////////

  public Double getPeso() {
    return peso;
  }

  public LocalDateTime getFechaDonacion() {
    return fechaDonacion;
  }

  public LocalDateTime getFechaCaducidad() {
    return fechaDeCaducidad;
  }
  //////////////////////////
  /////   SETTERS   ///////
  ////////////////////////


}