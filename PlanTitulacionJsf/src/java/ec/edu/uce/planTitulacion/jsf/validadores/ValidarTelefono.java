package ec.edu.uce.planTitulacion.jsf.validadores;

import ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validarTelefono")
public class ValidarTelefono implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
        String strValor = (String) valor;
        boolean flag = false;
        if (strValor.length() >= 7) {
            for (int i = 0; i < strValor.length(); i++) {
                if ((int) strValor.charAt(i) < 48 || (int) strValor.charAt(i) > 57) {
                    flag = true;
                }
            }
        } else {
            FacesMessage msg = new FacesMessage("Menor a 7 digitos");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        if(flag){
            FacesMessage msg = new FacesMessage("telefono Invalido");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
