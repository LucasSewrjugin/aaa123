package ar.edu.utn.frba.dds.dominio.mensajeria;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;

/**
 * Sender de emails.
 */
public class MailSender {
  private final Session session;
  
  /**
   * Constructor principal.
   *
   * @param session Sesión para realizar el envío de correo.
   */
  public MailSender(Session session) {
    this.session = session;
  }
  
  /**
   * Envía un email.
   *
   * @param from Remitente.
   * @param to Destinatario.
   * @param subject Asunto.
   * @param text Cuerpo del correo.
   * @throws MessagingException Excepción ocurrida.
   */
  public void sendEmail(String from, List<String> to, String subject, String text)
      throws MessagingException {
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    for (String recipient : to) {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
    message.setSubject(subject);
    message.setText(text);
    Transport.send(message);
    System.out.println("Correo enviado con éxito");
  }
  
  /**
   * Envía un correo con un adjunto.
   *
   * @param from Remitente.
   * @param to Destinatario.
   * @param subject Asunto.
   * @param text Cuerpo del correo.
   * @param filePath Ruta de los adjunos.
   * @throws Exception Excepción ocurrida.
   */
  public void sendEmailWithAttachment(String from, List<String> to, String subject,
      String text, String filePath) throws Exception {
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    for (String recipient : to) {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
    message.setSubject(subject);
    // Create the message part
    MimeBodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setText(text);
    // Create the attachment part
    MimeBodyPart attachmentPart = new MimeBodyPart();
    attachmentPart.attachFile(new File(filePath));
    // Create the multipart
    MimeMultipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);
    multipart.addBodyPart(attachmentPart);
    // Set the content
    message.setContent(multipart);
    Transport.send(message);
    System.out.println("Correo con archivo adjunto enviado con éxito");
  }
}
