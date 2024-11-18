package ar.edu.utn.frba.dds.dominio.heladera;

// DATA OBJECT PARA MOSTRAR EN ORGANIZACION
/**
 * Tupla para heladeras.
 */
public record TuplaHeladera(String nombre, NivelDeLlenado nivelDeLlenado,
    AtencionRequerida atencionRequerida) {
}
