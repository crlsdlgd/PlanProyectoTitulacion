package ec.edu.uce.planTitulacion.jsf.validadores;

import ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validarCedula")
public class ValidarCedula implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent comp, Object valor) throws ValidatorException {
        String cedula = (String) valor;
        Integer tipoIdentificacion = (Integer) comp.getAttributes().get("tipoIdentificacion");
        boolean flag = false;
        int cont = 0, aux;
        if (tipoIdentificacion==ConstantesSistema.TIPO_IDENTIFICACION_CEDULA_VALUE) {
            try {
                if (cedula.length() == 10) {
                    for (int i = 0; i < cedula.length(); i++) {
                        if ((int) cedula.charAt(i) < 48 && (int) cedula.charAt(i) > 57) {
                            flag = false;
                        }
                    }
                    int[] matriz = new int[10];
                    matriz[0] = Integer.parseInt(cedula.charAt(0) + "");
                    matriz[1] = Integer.parseInt(cedula.charAt(1) + "");
                    matriz[2] = Integer.parseInt(cedula.charAt(2) + "");
                    matriz[3] = Integer.parseInt(cedula.charAt(3) + "");
                    matriz[4] = Integer.parseInt(cedula.charAt(4) + "");
                    matriz[5] = Integer.parseInt(cedula.charAt(5) + "");
                    matriz[6] = Integer.parseInt(cedula.charAt(6) + "");
                    matriz[7] = Integer.parseInt(cedula.charAt(7) + "");
                    matriz[8] = Integer.parseInt(cedula.charAt(8) + "");
                    matriz[9] = Integer.parseInt(cedula.charAt(9) + "");

                    for (int i = 0; i < 9; i += 2) {
                        aux = matriz[i] * 2;
                        if (aux > 9) {
                            aux -= 9;
                        }
                        cont = cont + aux;
                    }
                    cont = cont + matriz[1] + matriz[3] + matriz[5] + matriz[7];

                    cont = cont % 10;

                    if ((10 - cont) == matriz[9]) {
                        flag = true;
                    }

                    if (!flag) {
                        FacesMessage msg = new FacesMessage("Cédula Erronea");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        fc.addMessage(comp.getClientId(fc), msg);
                        throw new ValidatorException(msg);
                    }

                } else {
                    ((UIInput) comp).setValid(flag);
                    FacesMessage msg = new FacesMessage("la cédula debe tener 10 dígitos");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    fc.addMessage(comp.getClientId(fc), msg);
                    throw new ValidatorException(msg);
                }
            } catch (Exception e) {
            }
        }
    }

}
