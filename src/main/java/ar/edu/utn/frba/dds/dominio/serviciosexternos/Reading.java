package ar.edu.utn.frba.dds.dominio.serviciosexternos;

/**
 * Lector de valor para sensor de peso.
 */
public class Reading {
  public double value;
  public String unit;
  
  /**
   * Constructor principal.
   *
   * @param val Valor medido.
   * @param unit Unidad de medición.
   */
  public Reading(double val, String unit) {
    this.value = val;
    this.unit = unit;
  }
}
