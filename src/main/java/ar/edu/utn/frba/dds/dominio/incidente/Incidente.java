package ar.edu.utn.frba.dds.dominio.incidente;

import ar.edu.utn.frba.dds.dominio.heladera.Heladera;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Récord de incidente.
 */
@Entity
@Table(name = "Incidente")
public record Incidente(
    @Id @GeneratedValue Long id,
    @ManyToOne TipoIncidente tipo,
    @Column LocalDateTime fecha,
    @ManyToOne Heladera heladera) {
}