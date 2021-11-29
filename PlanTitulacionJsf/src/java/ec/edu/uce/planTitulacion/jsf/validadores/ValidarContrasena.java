package ec.edu.uce.planTitulacion.jsf.validadores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validarContrasena")
public class ValidarContrasena implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
        String strValor = (String) valor;
        boolean mayusculas=false;
        boolean minusculas=false;
        boolean caracterEspecial=false;
        int contNumeros=0;
        
        for (int i = 0; i < strValor.length(); i++) {    
            if((int)strValor.charAt(i) >= 48 && (int)strValor.charAt(i) <= 57){//numeros
                contNumeros++;
            }else if ((int)strValor.charAt(i) >= 65 && (int)strValor.charAt(i) <= 90) {//Mayus
                mayusculas=true;
            }else if ((int)strValor.charAt(i) >= 97 && (int)strValor.charAt(i) <= 122) {//Minus
                minusculas=true;
            }else{
                caracterEspecial=true;//caracter especial
            }
        }
        if(!caracterEspecial || strValor.length()<8 || !mayusculas || !minusculas || contNumeros<2){
            FacesMessage msg = new FacesMessage("La contraseña debe tener al menos 8 caracteres entre mayusculas, minusculas, caracteres especiales y al menos 2 números");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
    }

}
