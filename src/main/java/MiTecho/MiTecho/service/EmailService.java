package MiTecho.MiTecho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import MiTecho.MiTecho.model.Orden;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void enviarCorreoBienvenida(String destinatario, String nombreUsuario) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            Context contexto = new Context();
            contexto.setVariable("nombreUsuario", nombreUsuario);

            String contenidoHtml = templateEngine.process("correos/bienvenida", contexto);

            helper.setTo(destinatario);
            helper.setSubject("¡Bienvenido a MiTecho!");
            helper.setText(contenidoHtml, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void enviarCorreoConfirmacionOrden(String destinatario, String nombreUsuario, Orden orden) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            Context contexto = new Context();
            contexto.setVariable("nombreUsuario", nombreUsuario);
            contexto.setVariable("numeroOrden", orden.getNumero());
            contexto.setVariable("totalOrden", orden.getTotal());
            contexto.setVariable("fechaCreacion", orden.getFechaCreacion());

            String contenidoHtml = templateEngine.process("correos/confirmacion_orden", contexto);

            helper.setTo(destinatario);
            helper.setSubject("Confirmación de tu orden #" + orden.getNumero());
            helper.setText(contenidoHtml, true);

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
