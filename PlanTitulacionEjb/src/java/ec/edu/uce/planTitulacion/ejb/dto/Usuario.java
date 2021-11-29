package ec.edu.uce.planTitulacion.ejb.dto;

public class Usuario {
    private Integer usrId;
//    private String nombre;
//    private String email;
    private Persona usrPersona;
    private String usrPassword;
    private String usrNick;
    private String usrIdentificacion;

    
    public Persona getUsrPersona() {
        return usrPersona;
    }

    public void setUsrPersona(Persona usrPersona) {
        this.usrPersona = usrPersona;
    }
    
    public String getUsrIdentificacion() {
        return usrIdentificacion;
    }

    public void setUsrIdentificacion(String usrIdentificacion) {
        this.usrIdentificacion = usrIdentificacion;
    }
    
    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getUsrPassword() {
        return usrPassword;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getUsrNick() {
        return usrNick;
    }

    public void setUsrNick(String usrNick) {
        this.usrNick = usrNick;
    }

}
