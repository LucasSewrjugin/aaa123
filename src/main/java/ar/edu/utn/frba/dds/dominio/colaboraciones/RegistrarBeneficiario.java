package ar.edu.utn.frba.dds.dominio.colaboraciones;

import ar.edu.utn.frba.dds.dominio.Beneficiario;
import ar.edu.utn.frba.dds.dominio.Organizacion;
import ar.edu.utn.frba.dds.dominio.tarjeta.Tarjeta;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class RegistrarBeneficiario extends Colaboracion {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne // ???
  private Organizacion organizacion; // Me genera dudas que esta colaboracion conozca a la organización

  @OneToOne
  private Tarjeta tarjeta;

  @OneToOne
  private Beneficiario beneficiario;

  public RegistrarBeneficiario(LocalDate unaFecha, Organizacion organizacion, Tarjeta tarjeta, Beneficiario beneficiario) {
    this.organizacion = organizacion;
    this.tarjeta = tarjeta;
    this.beneficiario = beneficiario;
    this.fecha = unaFecha;
    this.tipoColaboracion = TipoColaboracion.REGISTRO_PERSONAS_SITUACION_VULNERABLE;
  }

  @Override
  public void contribuir() {
    beneficiario.recibirTarjeta(tarjeta);
    organizacion.altaBeneficiario(beneficiario);
  }

  public Tarjeta getTarjeta() {
    return this.tarjeta;
  }

  public LocalDate getFecha() {
    return fecha;
  }
}
