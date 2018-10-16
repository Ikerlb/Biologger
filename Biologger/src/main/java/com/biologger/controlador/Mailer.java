import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class Mailer{
    /**
     * agrs[0] remitente - direccion de la cual se va a enviar el correo
     * agrs[1] contraseña de remitente
     * agrs[2] destinatario - dirreccion a la que se enviara el correo
     * agrs[3] servidor de correo remitente mail.grudis.com smtp.gmail.com para grudis y google respectivamente
     * agrs[4] puerto de salida remitente 2525 587 para grudis y google respectivamente 
     * agrs[5] asunto
     * agrs[6] cuerpo
     * ejemplo de uso: java -cp javax.activation-1.1.0.v201105071233.jar;javax.mail.jar Mailer biologger2019.1@gmail.com Biologger1234 martinortega@ciencias.unam.mx smtp.gmail.com 587 AsuntoDeCorreo "CuerpoDeCorreo(acepta html segun recuerdo)"
     */
    public static void main(String[] args){
        try{
	    String correoCiencias="martinortega@ciencias.unam.mx";
	    String correoComunidad="jmom_y_dgc@comunidad.unam.mx";
	    String correoGrudis="martin.ortega@grudis.com";
            // Propiedades de la conexión
            Properties props = new Properties();
	    props.setProperty("mail.smtp.host", args[3]);
            props.setProperty("mail.smtp.starttls.enable", "true");
	    props.setProperty("mail.smtp.port", args[4]);
	    props.setProperty("mail.smtp.user", args[0]);
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(args[0]));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(args[2]));
            message.setSubject(args[5]);
            message.setText(args[6]);

            // Lo enviamos.
	    Transport t = session.getTransport("smtp");
            t.connect(correoGrudis, args[1]);
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
