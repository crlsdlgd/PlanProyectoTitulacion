package ec.edu.uce.planTitulacion.jsf.managedBeans;

import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;

@Named(value = "controladorTemplate")
@SessionScoped
public class ControladorTemplate implements Serializable{

    public void verificarSession() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Usuario us = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            if(us==null){
                context.getExternalContext().redirect("../../error.xhtml");
            }   
        } catch (Exception e) {
        }
    }
    
}
