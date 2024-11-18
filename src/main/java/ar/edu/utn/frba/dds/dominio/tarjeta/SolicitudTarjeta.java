package ar.edu.utn.frba.dds.dominio.tarjeta;

import ar.edu.utn.frba.dds.dominio.colaboradores.ColaboradorHumana;

/**
 * Registro de solicitud de la tarjeta.
 */
public record SolicitudTarjeta(ColaboradorHumana colaborador, String direccion) {
}
