package ec.edu.uce.planTitulacion.jsf.managedBean.sesion;

import ec.edu.uce.planTitulacion.ejb.dto.Rol;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean(name="sesionUsuarioBean")
@SessionScoped
public class SesionUsuarioBean implements Serializable{
    
    private static final long serialVersionUID = 2300187948955776410L;
    
    private boolean estudiante, docente, consejo;
    List<Rol> listaRolUsuario;

    public boolean getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }

    public boolean getDocente() {
        return docente;
    }

    public void setDocente(boolean docente) {
        this.docente = docente;
    }

    public boolean getConsejo() {
        return consejo;
    }

    public void setConsejo(boolean consejo) {
        this.consejo = consejo;
    }
    
    
}
