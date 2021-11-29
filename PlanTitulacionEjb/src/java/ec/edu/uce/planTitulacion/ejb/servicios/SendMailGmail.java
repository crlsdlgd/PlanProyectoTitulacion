package ec.edu.uce.planTitulacion.ejb.servicios;

import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.impl.UsuarioDaoImpl;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.ejb.EJB;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailGmail {

    private String correoEnvia = "gestiontitulacionuce@gmail.com"; //correo emisor
    private String claveCorreo = "Pass_1992";//contraseña de correo emisor
    private Properties properties = new Properties();

    private String factorAutenticacion = "";
    private Usuario user;

    @EJB
    UsuarioDaoImpl dao;

    //metodo para configuracion del correo
    public SendMailGmail() {
        user = new Usuario();
        //user.setEmail("estudiantetitulacion@gmail.com");
        //user.setNombre("Estudiante");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);
    }

    //metodo para enviar correo
    public void send(String host, String port, final String userName, final String password, String toAddress,
            String subject, String htmlBody, Map<String, String> mapInlineImages)
            throws AddressException, MessagingException {
        // sets SMTP server properties

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlBody, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();

            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);

                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(imagePart);
            }
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void enviarTemaARevisionMail(Plan plan) throws Exception {
        try {
//            List<Usuario> lista = dao.listarUserByPlan(plan);
            UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
            List<Usuario> lista = usuarioDaoImpl.listarUserByPlan(plan);
            for (int i = 0; i < lista.size(); i++) {
                enviarTemaARevisionMailUsuario(lista.get(i), plan);
            }
        } catch (Exception e) {

        }
    }

    public void enviarQuinceDiasMail(Plan plan) throws Exception {
        UsuarioDaoImpl dao = new UsuarioDaoImpl();
        try {
            List<Usuario> lista = dao.listarUserByPlan(plan);
            for (int i = 0; i < lista.size(); i++) {
                enviarQuinceDiasMailUsuario(lista.get(i), plan);
            }
        } catch (Exception e) {

        }

    }

    public void enviarCincoMesesrMail(Plan plan) throws Exception {

        UsuarioDaoImpl dao = new UsuarioDaoImpl();
        try {
            List<Usuario> lista = dao.listarUserByPlan(plan);
            for (int i = 0; i < lista.size(); i++) {
                enviarCincoMesesMailUsuario(lista.get(i), plan);
            }
        } catch (Exception e) {

        }

    }

    public void enviarTemaAprobadoMail(Plan plan) throws Exception {
        try {
//            List<Usuario> lista = dao.listarUserByPlan(plan);
            UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
            List<Usuario> lista = usuarioDaoImpl.listarUserByPlan(plan);
            for (int i = 0; i < lista.size(); i++) {
                enviarTemaAprobadoMailUsuario(lista.get(i), plan);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void enviarTemaNoAprobadoMail(Plan plan) throws Exception {
        try {
//            List<Usuario> lista = dao.listarUserByPlan(plan);
            UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
            List<Usuario> lista = usuarioDaoImpl.listarUserByPlan(plan);
            for (int i = 0; i < lista.size(); i++) {
                enviarTemaNoAprobadoMailUsuario(lista.get(i), plan);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //metodo base
//    public void enviarMail() {
//        // SMTP info
//        String host = properties.getProperty("mail.smtp.host");
//        String port = properties.getProperty("mail.smtp.port");
//
//        // message info
//        //String mailTo = user.getEmail();
//        String mailTo = "estudiantetitulacion@gmail.com";
//        String subject = "RECORDATORIO DE TEMA DE TESIS";
//        StringBuffer body = new StringBuffer("<html><br>");
//        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
//        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Estimado Estudiate.<br><br>");
//        //body.append(user.getNombre() + "<br>");
//        body.append("Le recordamos que usted consta en el sistema de titulación<br><br>");
//        body.append("A la Fecha: " + new Date() + "<br>");
//        body.append("Con el tutor: " + new Date() + "<br>");
//        body.append("Con el tema de: " + factorAutenticacion + " <br><br><br>");
//        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
//        body.append("Atentamente,<br>");
//        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
//        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
//        body.append("</html>");
//
//        // inline images
//        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie
//
//        try {
//            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
//            System.out.println("Email sent.");
//        } catch (Exception ex) {
//            System.out.println("Could not send email.");
//            ex.printStackTrace();
//        }
//    }
    // este metodo envia un correo informando a al usuario de un plan que el plan se ha enviado a consejo de carrera
    public void enviarTemaARevisionMailUsuario(Usuario user, Plan plan) throws Exception {
        // SMTP info
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");

//        Usuario tutor =dao.findTutorByPlan(plan);
        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);

        // message info
        //String mailTo = user.getEmail();
        String mailTo = user.getUsrPersona().getPrsMailPersonal();//para pruebas, se encuentra con gmail
        String subject = "TEMA DE TESIS EN REVISIÓN";
        StringBuffer body = new StringBuffer("<html><br>");
        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Un cordial Saludo.<br><br>");
        //body.append(user.getNombre() + "<br>");
        body.append("El día de hoy en el sistema de titulación el tema: " + plan.getPlnTema() + " <br><br>");
        body.append("Con el tutor: " + tutor.getUsrPersona().getPrsNombres() + " " + tutor.getUsrPersona().getPrsPrimerApellido() + "<br>");//tutor aqui
        body.append("Ha sido enviado a Consejo de Carrera para ser revisado.<br>");
        body.append("Se le informará si este proyecto ha sido aprobado como tema de tesis o rechazado junto con observaciones del mismo.<br>");
        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
        body.append("Atentamente,<br>");
        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera localhost
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie localhost
        
        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera DigitalOcean
        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie cabecera DigitalOcean
        try {
            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

    // este metodo envia un correo informando a al usuario de un plan que han pasado 15 dias desde la aprobacion del tema
    public void enviarQuinceDiasMailUsuario(Usuario user, Plan plan) throws Exception {
        // SMTP info

        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");

        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);

        // message info
        String mailTo = user.getUsrPersona().getPrsMailPersonal();//para pruebas, se encuentra con gmail
        String subject = "RECORDATORIO DE TEMA DE TESIS";
        StringBuffer body = new StringBuffer("<html><br>");
        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Estimado Estudiate.<br><br>");
        //body.append(user.getNombre() + "<br>");
        body.append("Le recordamos que el tema de tesis: " + plan.getPlnTema() + "<br><br>");
        body.append("A la Fecha: " + plan.getPlnFecha() + "<br>");
        body.append("Con el tutor: " + tutor.getUsrPersona().getPrsNombres() + " " + tutor.getUsrPersona().getPrsPrimerApellido() + "<br>");
        body.append("Tiene seis meses para concluir el proyecto desde la fecha de aprobacion en el sistema de titulación.<br>");
        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
        body.append("Atentamente,<br>");
        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera localhost
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie localhost
        
        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera DigitalOcean
        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie cabecera DigitalOcean

        try {
            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

    // este metodo envia un correo informando a al usuario de un plan que han pasado 5 meses desde la aprobacion del tema
    public void enviarCincoMesesMailUsuario(Usuario user, Plan plan) throws Exception {
        // SMTP info
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");

        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);

        // message info
        //String mailTo = user.getEmail();
        String mailTo = user.getUsrPersona().getPrsMailPersonal();//para pruebas, se encuentra con gmail
        String subject = "RECORDATORIO DE TEMA DE TESIS";
        StringBuffer body = new StringBuffer("<html><br>");
        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Estimado Estudiate.<br><br>");
        //body.append(user.getNombre() + "<br>");
        body.append("Le recordamos que el tema de tesis: " + plan.getPlnTema() + "<br><br>");
        body.append("A la Fecha: " + plan.getPlnFecha() + "<br>");
        body.append("Con el tutor: " + tutor.getUsrPersona().getPrsNombres() + " " + tutor.getUsrPersona().getPrsPrimerApellido() + "<br>");
        body.append("Recuerde ya han transcurrido cinco meses desde el registro de su tema, le queda un mes para el control antiplagio. <br>");
        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
        body.append("Atentamente,<br>");
        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera localhost
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie localhost
        
        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera DigitalOcean
        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie cabecera DigitalOcean

        try {
            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

    // este metodo envia un correo informando a al usuario de un plan que el plan se ha aprobado por consejo de carrera
    private void enviarTemaAprobadoMailUsuario(Usuario user, Plan plan) throws Exception {
        // SMTP info
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");

//        Usuario tutor =dao.findTutorByPlan(plan);
        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);

        // message info
        //String mailTo = user.getEmail();
        String mailTo = user.getUsrPersona().getPrsMailPersonal();//para pruebas, se encuentra con gmail
        String subject = "APROBACIÓN DE TEMA DE TESIS";
        StringBuffer body = new StringBuffer("<html><br>");
        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Un cordial Saludo.<br><br>");
        //body.append(user.getNombre() + "<br>");
        body.append("El día de hoy se ha registrado en el sistema de titulación el tema: " + plan.getPlnTema() + " <br><br>");
        body.append("Con el tutor: " + tutor.getUsrPersona().getPrsNombres() + " " + tutor.getUsrPersona().getPrsPrimerApellido() + "<br>");//tutor aqui
        body.append("Este plan de tesis ha sido revisado por Consejo de Carrera y fue APROBADO.<br>");
        body.append("Recuerde que tiene seis meses para terminar este tema.<br>");
        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
        body.append("Atentamente,<br>");
        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera localhost
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie localhost
        
        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera DigitalOcean
        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie cabecera DigitalOcean

        try {
            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

    // este metodo envia un correo informando a al usuario de un plan que el plan se sido rechazado por consejo de carrera
    private void enviarTemaNoAprobadoMailUsuario(Usuario user, Plan plan) throws Exception {
        // SMTP info
        String host = properties.getProperty("mail.smtp.host");
        String port = properties.getProperty("mail.smtp.port");

//        Usuario tutor =dao.findTutorByPlan(plan);
        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);

        // message info
        //String mailTo = user.getEmail();
        String mailTo = user.getUsrPersona().getPrsMailPersonal();//para pruebas, se encuentra con gmail
        String subject = "APROBACIÓN DE TEMA DE TESIS";
        StringBuffer body = new StringBuffer("<html><br>");
        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Un cordial Saludo.<br><br>");
        //body.append(user.getNombre() + "<br>");
        body.append("El día de hoy se ha dado respuesta en el sistema de titulación al tema: " + plan.getPlnTema() + " <br><br>");
        body.append("Con el tutor: " + tutor.getUsrPersona().getPrsNombres() + " " + tutor.getUsrPersona().getPrsPrimerApellido() + "<br>");//tutor aqui
        body.append("Este plan de tesis ha sido revisado por Consejo de Carrera y fue RECHAZADO.<br>");
        body.append("Por el motivo de :" + plan.getPlnObservaciones() + ".<br>");
        body.append("Gracias por utilizar nuestros servicios.<br><br><br>");
        body.append("Atentamente,<br>");
        body.append("Facultad de Ingeniería Ciencias Físicas y Matemática, Dirección de Carrera de Ingeniería Informática.</form>");
        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "D:/Eilfil/Imágenes/plantillaCabecera.png");//Imagen de cabecera localhost
//        inlineImages.put("image2", "D:/Eilfil/Imágenes/plantillaPie.png");//Imagen de pie localhost
        
        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera DigitalOcean
        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie cabecera DigitalOcean

        try {
            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

//    metodo de prueba 
//    public void metodoPrueba() throws Exception {
//        // SMTP info
//        String host = properties.getProperty("mail.smtp.host");
//        String port = properties.getProperty("mail.smtp.port");
//
////        Usuario tutor =dao.findTutorByPlan(plan);
////        UsuarioDaoImpl usuarioDaoImpl = new UsuarioDaoImpl();
////        Usuario tutor = usuarioDaoImpl.findTutorByPlan(plan);
//        // message info
//        //String mailTo = user.getEmail();
//        String mailTo = "estudiantetitulacion@gmail.com";//para pruebas, se encuentra con gmail
//        String subject = "PRUEBA CORREO";
//        StringBuffer body = new StringBuffer("<html><br>");
//        body.append("<img src=\"cid:image1\" style=\"width:100%;height:auto\"/>");
//        body.append("<form style=\"padding: 0px 14px 0px 14px;border-bottom:none !important;border-top:none !important;border: solid 1px red;\"> Un cordial Saludo.<br><br>");
//        //body.append(user.getNombre() + "<br>");
//        body.append("HOLA!!</form>");
//        body.append("<img src=\"cid:image2\" style=\"width:100%;height:auto\" /><br>");
//        body.append("</html>");
//
//        // inline images
//        Map<String, String> inlineImages = new HashMap<String, String>();
//        inlineImages.put("image1", "/root/plantillaCabecera.png");//Imagen de cabecera
//        inlineImages.put("image2", "/root/plantillaPie.png");//Imagen de pie
//
////        inlineImages.put("image1", "https://ibb.co/f06yLB8");//Imagen de cabecera
////        inlineImages.put("image2", "https://ibb.co/gSf3WLw");//Imagen de pie
//        try {
//            System.out.println("host: "+host+" port: "+port+" correo Envia: "+correoEnvia+ " claveCorreo: "
//                    +claveCorreo+" mailTo: "+mailTo+" subject: "+subject);
//            send(host, port, correoEnvia, claveCorreo, mailTo, subject, body.toString(), inlineImages); 
//            System.out.println("Email sent.");
//        } catch (Exception ex) {
//            System.out.println("Could not send email.");
//            ex.printStackTrace();
//        }
//    }

    /**
     * SETTERS Y GETTERS *
     */
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getFactorAutenticacion() {
        return factorAutenticacion;
    }

    public void setFactorAutenticacion(String factorAutenticacion) {
        this.factorAutenticacion = factorAutenticacion;
    }

}
