package ar.edu.utn.frba.dds.dominio.mensajeria;

import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Mensajería por mail.
 */
@Entity
public class MensajeriaMail extends MedioComunicacion {
  @Transient
  // NO NOS INTERESA PERSISTIRLO PORQUE ES EXTERNO Y SE CREA CUANDO INICIALIZO
  // EL PROGRAMA
  private final MailSender mailSender;
  @Column
  private final String remitenteMail;
  @Column
  private String userMail;
  
  /**
   * Constructor principal.
   *
   * @param userMail Correo.
   */
  public MensajeriaMail(String userMail) {
    this.userMail = userMail;
    this.remitenteMail = "remitente.noreply@gmail.com"; 
    String host = "sandbox.smtp.mailtrap.io";
    String port = "25";
    String username = "your_username";
    String password = "your_password";
    MailConfig mailConfig = new MailConfig(host, port, username, password);
    this.mailSender = new MailSender(mailConfig.getSession());
  }
  
  /**
   * Envía un mensaje.
   */
  public void enviarMensaje(String mensaje) {
    try {
      mailSender.sendEmail(remitenteMail, Arrays.asList(userMail),
          "Notificacion Heladera", mensaje);
    } catch (Exception e) {
      throw new MailException("Se produjo un problema al enviar el mensaje");
    }
  }
}