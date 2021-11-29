package ec.edu.uce.planTitulacion.managedBeans.request.generales;

import ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "generalesConstantesForm")
@RequestScoped
public class GeneralesConstantesForm {

    public String getMsgCampoRequerido(){
        return ConstantesSistema.MSG_CAMPO_REQUERIDO;
    }
    
    public String getAppSeleccione(){
        return ConstantesSistema.APP_SELECCIONE;
    }
}
