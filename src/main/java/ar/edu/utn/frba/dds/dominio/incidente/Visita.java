package ar.edu.utn.frba.dds.dominio.incidente;

import ar.edu.utn.frba.dds.dominio.heladera.Tecnico;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Visita del técnico.
 */
@Entity
@Table(name = "Visitas")
public record Visita(
    @Id @GeneratedValue Long id,
    @Column LocalDateTime fechaDeRevision,
    @OneToOne Tecnico tecnico,
    @OneToOne Incidente motivoVisita,
    @Column String trabajoRealizado,
    @Column String urlFoto,
    @Column Boolean resuelto
) {
}