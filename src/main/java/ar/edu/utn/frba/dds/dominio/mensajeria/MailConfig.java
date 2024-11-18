package ar.edu.utn.frba.dds.dominio.mensajeria;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;

/**
 * Configuración de la suscripción por mail.
 */
public class MailConfig {
  private final Properties properties;
  private final Session session;
  
  /**
   * Constructor principal.
   *
   * @param host Host.
   * @param port Puerto.
   * @param username Usuario.
   * @param password Password.
   */
  public MailConfig(String host, String port, String username, String password) {
    properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);
    properties.put("mail.smtp.ssl.trust", host);
    session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    // Optional: Enable debug mode
    session.setDebug(true);
  }
  
  public Session getSession() {
    return session;
  }
}
