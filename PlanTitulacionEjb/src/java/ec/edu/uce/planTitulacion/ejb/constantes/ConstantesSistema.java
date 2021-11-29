
package ec.edu.uce.planTitulacion.ejb.constantes;

public class ConstantesSistema {
    //constantes de genero en la base de datos
    public static final String SEXO_MASCULINO_LABEL="MASCULINO";
    public static final int SEXO_MASCULINO_VALUE=Integer.valueOf(0);
    public static final String SEXO_FEMENINO_LABEL="FEMENINO";
    public static final int SEXO_FEMENINO_VALUE=Integer.valueOf(1);

    //constantes de tipo de identificacion
    public static final String TIPO_IDENTIFICACION_CEDULA_LABEL="CEDULA";
    public static final int TIPO_IDENTIFICACION_CEDULA_VALUE=Integer.valueOf(0);
    public static final String TIPO_IDENTIFICACION_PASAPORTE_LABEL="PASAPORTE";
    public static final int TIPO_IDENTIFICACION_PASAPORTE_VALUE=Integer.valueOf(1);
    
    public static final String NO_DISCAPACIDAD_LABEL="NO";
    public static final int NO_DISCAPACIDAD_VALUE=Integer.valueOf(0);
    public static final String SI_DISCAPACIDAD_LABEL="SI";
    public static final int SI_DISCAPACIDAD_VALUE=Integer.valueOf(1);
    
    
    public static final String MSG_CAMPO_REQUERIDO="Campo Requerido";
    public static final String APP_SELECCIONE = "SELECCIONE...";
}
