package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.scripts.Bootstrap;

/**
 * Aplicación principal.
 */
public class App {
  /**
   * Método main donde se inicializa el sistema.
   *
   * @param args Argumentos recibidos.
   */
  public static void main(String[] args) {
    new Bootstrap().init();
    new Server().start();
  }
}
