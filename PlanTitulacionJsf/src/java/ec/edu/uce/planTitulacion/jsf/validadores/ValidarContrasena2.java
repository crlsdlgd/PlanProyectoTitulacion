package ec.edu.uce.planTitulacion.jsf.validadores;

import ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validarContrasena2")
public class ValidarContrasena2 implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
        String contrasena2 = (String) valor;
        String contrasena1 = (String) comp.getAttributes().get("contrasena1");
        boolean flag = false;
        
        try {
            if(!contrasena1.equals(contrasena2)){
                flag=true;
            }
        } catch (Exception e) {
        }
        if(flag){
            FacesMessage msg = new FacesMessage("Las contraseñas NO coiciden");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
