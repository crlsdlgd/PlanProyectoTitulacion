package ec.edu.uce.planTitulacion.jsf.managedBeans;

import ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema;
import ec.edu.uce.planTitulacion.ejb.dao.EtniaDao;
import ec.edu.uce.planTitulacion.ejb.dao.UbicacionDao;
import ec.edu.uce.planTitulacion.ejb.dao.UsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Etnia;
import ec.edu.uce.planTitulacion.ejb.dto.Persona;
import ec.edu.uce.planTitulacion.ejb.dto.Ubicacion;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.servicios.VerificarRecaptcha;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.context.RequestContext;

@Named(value = "controladorRegistro")
@SessionScoped
public class ControladorRegistro implements Serializable {

    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private EtniaDao etniaDao;
    @EJB
    private UbicacionDao ubicacionDao;

    private Usuario usuarioRegistro;
    private Persona personaregistro;
    private Etnia etniaRegistro;
    private List<Etnia> listaEtnia;
    private List<Ubicacion> listaUbicacion;

    private String prsIdentificacion;
    private Integer prsId;
    private String prsNombres;
    private String prsPrimerApellido;
    private String prsSegundoApellido;
    private String prsMailInstitucional;
    private String prsMailPersonal;
    private String prsTelefono;
    private String prsCarnetConadis;
    private String usrPassword;
    private String usrPassword2;
    private Integer prsTipoIdentificacion;
    private Integer prsSexo;
    private Integer prsDiscapacidad;
    private Integer prsPorcentajeDiscapacidad;
    private Integer prsEtnia;
    private Date prsFechaNacimiento;

    private String usrPasswordNueva;
    private String usrPasswordNueva2;

    private List<SelectItem> listaSexo;
    private List<SelectItem> listaTipoIdentificacion;
    private List<SelectItem> listaDiscapacidad;

    public void cargarCombosRegistro() throws Exception {
        cargarTipoIdentificacion();
        cargarEtnia();
        cargarNacionalidad();
        cargarSexo();
        cargarDiscapacidad();
    }

    private void cargarTipoIdentificacion() {
        this.listaTipoIdentificacion = new ArrayList<SelectItem>();
        listaTipoIdentificacion.clear();
        SelectItem itemTipoIdentificacion = new SelectItem(ConstantesSistema.TIPO_IDENTIFICACION_CEDULA_VALUE, ConstantesSistema.TIPO_IDENTIFICACION_CEDULA_LABEL);
        SelectItem itemTipoIdentificacion2 = new SelectItem(ConstantesSistema.TIPO_IDENTIFICACION_PASAPORTE_VALUE, ConstantesSistema.TIPO_IDENTIFICACION_PASAPORTE_LABEL);
        this.listaTipoIdentificacion.add(itemTipoIdentificacion);
        this.listaTipoIdentificacion.add(itemTipoIdentificacion2);
    }

    private void cargarEtnia() throws Exception {
        listaEtnia = etniaDao.listarEtnia();
    }

    private void cargarNacionalidad() throws Exception {
        listaUbicacion = ubicacionDao.listarUbicacion();
    }

    private void cargarSexo() {
        this.listaSexo = new ArrayList<SelectItem>();
        listaSexo.clear();
        SelectItem itemTipoIdentificacion = new SelectItem(ConstantesSistema.SEXO_MASCULINO_VALUE, ConstantesSistema.SEXO_MASCULINO_LABEL);
        SelectItem itemTipoIdentificacion2 = new SelectItem(ConstantesSistema.SEXO_FEMENINO_VALUE, ConstantesSistema.SEXO_FEMENINO_LABEL);
        this.listaSexo.add(itemTipoIdentificacion);
        this.listaSexo.add(itemTipoIdentificacion2);
    }

    private void cargarDiscapacidad() {
        this.listaDiscapacidad = new ArrayList<SelectItem>();
        listaDiscapacidad.clear();
        SelectItem itemDiscapacidad = new SelectItem(ConstantesSistema.NO_DISCAPACIDAD_VALUE, ConstantesSistema.NO_DISCAPACIDAD_LABEL);
        SelectItem itemDiscapacidad2 = new SelectItem(ConstantesSistema.SI_DISCAPACIDAD_VALUE, ConstantesSistema.SI_DISCAPACIDAD_LABEL);
        this.listaDiscapacidad.add(itemDiscapacidad);
        this.listaDiscapacidad.add(itemDiscapacidad2);
    }

    public void verificarCatpchaRegistro() {
        System.out.println("verificarCaptcha pass: " + usrPassword);
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
            boolean verify = VerificarRecaptcha.verificar(gRecaptchaResponse);
            if (verify) {
                System.out.println("Captcha correcto");
                RequestContext.getCurrentInstance().execute("PF('wlgRegistro').show();");
            } else {
                System.out.println("Captcha incorrecto");
            }
        } catch (Exception e) {

        }
    }

    public String guardarUsuario() throws Exception {

        String aux = "";
        boolean flag = false;
        boolean isIdentificacionExists = usuarioDao.existeIdentificacion(prsIdentificacion);
        if (usrPassword.equals(usrPassword2)) {
            String encripted = DigestUtils.md5Hex(usrPassword);
            usrPassword = encripted;

            cargarUsuarioRegistro();
            if (isIdentificacionExists) {
                flag = usuarioDao.registrar(usuarioRegistro);

            }

        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        if (flag) {
            addMessage("Registro Exitoso!");
            aux = "inicio";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al momento de ingresar usuario", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        if (!isIdentificacionExists) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No. Identificación duplicada", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return aux;
    }

    private void cargarUsuarioRegistro() {

        personaregistro = new Persona();
        etniaRegistro = new Etnia();
        usuarioRegistro = new Usuario();
        personaregistro.setPrsIdentificacion(prsIdentificacion);
        personaregistro.setPrsNombres(prsNombres);
        personaregistro.setPrsPrimerApellido(prsPrimerApellido);
        personaregistro.setPrsSegundoApellido(prsSegundoApellido);
        personaregistro.setPrsMailInstitucional(prsMailInstitucional);
        personaregistro.setPrsMailPersonal(prsMailPersonal);
        personaregistro.setPrsTelefono(prsTelefono);
        personaregistro.setPrsCarnetConadis(prsCarnetConadis);
        personaregistro.setPrsTipoIdentificacion(prsTipoIdentificacion);
        personaregistro.setPrsSexo(prsSexo);
        personaregistro.setPrsDiscapacidad(prsDiscapacidad);
        personaregistro.setPrsPorcentajeDiscapacidad(prsPorcentajeDiscapacidad);
        personaregistro.setPrsFechaNacimiento(prsFechaNacimiento);
        personaregistro.setPrsId(prsId);
        etniaRegistro.setEtnId(prsEtnia);
        personaregistro.setPrsEtnia(etniaRegistro);
        usuarioRegistro.setUsrPassword(usrPassword);
        usuarioRegistro.setUsrPersona(personaregistro);
    }

    public void cargarUsuario() throws Exception {

        usuarioRegistro = usuarioDao.cargarUsuario(ControladorUsuario.user);
        prsIdentificacion = usuarioRegistro.getUsrPersona().getPrsIdentificacion();
        prsNombres = usuarioRegistro.getUsrPersona().getPrsNombres();
        prsPrimerApellido = usuarioRegistro.getUsrPersona().getPrsPrimerApellido();
        prsSegundoApellido = usuarioRegistro.getUsrPersona().getPrsSegundoApellido();
        prsMailInstitucional = usuarioRegistro.getUsrPersona().getPrsMailInstitucional();
        prsMailPersonal = usuarioRegistro.getUsrPersona().getPrsMailPersonal();
        prsTelefono = usuarioRegistro.getUsrPersona().getPrsTelefono();
        prsSexo = usuarioRegistro.getUsrPersona().getPrsSexo();
        prsFechaNacimiento = usuarioRegistro.getUsrPersona().getPrsFechaNacimiento();
        prsId = usuarioRegistro.getUsrPersona().getPrsId();
        prsEtnia = usuarioRegistro.getUsrPersona().getPrsEtnia().getEtnId();
//        cargarUsuarioRegistro();
    }

    public String editPersona() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String fieldPrsId = params.get("action");

        Persona objPersona = new Persona();

        objPersona.setPrsId(prsId);
        objPersona.setPrsIdentificacion(prsIdentificacion);
        objPersona.setPrsNombres(prsNombres);
        objPersona.setPrsPrimerApellido(prsPrimerApellido);
        objPersona.setPrsSegundoApellido(prsSegundoApellido);
        objPersona.setPrsMailInstitucional(prsMailInstitucional);
        objPersona.setPrsMailPersonal(prsMailPersonal);
        objPersona.setPrsTelefono(prsTelefono);
        objPersona.setPrsSexo(prsSexo);
        objPersona.setPrsFechaNacimiento(prsFechaNacimiento);
        objPersona.setPrsEtnia(etniaRegistro);

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("editPersona", objPersona);

        return "irEditarPerfil";
    }

    public String updatePersona() throws Exception {
        String aux = "";
        boolean flag = false;
//        cargarUsuarioRegistro();

        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        System.out.println(params.get("updatePrsId"));
//        int fieldPrsId = Integer.parseInt(params.get("updatePrsId"));
        System.out.println("-----------------------------------");
        System.out.println(prsIdentificacion);
        System.out.println(prsNombres);
        System.out.println(prsPrimerApellido);
        System.out.println(prsSegundoApellido);
        System.out.println(prsMailInstitucional);
        System.out.println(prsMailPersonal);
        System.out.println(prsTelefono);
        System.out.println(prsSexo);
        System.out.println(prsFechaNacimiento);
        System.out.println(prsId);
        System.out.println(prsEtnia);
        System.out.println("-----------------------------------");

        Persona persona = new Persona();
        Usuario usuario = new Usuario();
        Etnia etnia = new Etnia();
        persona.setPrsId(prsId);
        persona.setPrsNombres(prsNombres);
        persona.setPrsPrimerApellido(prsPrimerApellido);
        persona.setPrsSegundoApellido(prsSegundoApellido);
        persona.setPrsMailInstitucional(prsMailInstitucional);
        persona.setPrsMailPersonal(prsMailPersonal);
        persona.setPrsTelefono(prsTelefono);
        persona.setPrsSexo(prsSexo);
        persona.setPrsFechaNacimiento(prsFechaNacimiento);
        etnia.setEtnId(prsEtnia);
        persona.setPrsEtnia(etnia);

        usuario.setUsrPersona(persona);
//        flag = usuarioDao.actualizarDatos(usuario);
        if (flag) {
            addMessage("Se guardaron los cambios");
            aux = "irPrincipal";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al momento de actualizar los cambios", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return aux;
    }

    public String updateContrasena() throws Exception {
        String aux = "";
        boolean flag = false;
        String encripted = DigestUtils.md5Hex(usrPassword);
        usrPassword = encripted;
        encripted = DigestUtils.md5Hex(usrPasswordNueva);
        usrPasswordNueva = encripted;
        encripted = DigestUtils.md5Hex(usrPasswordNueva2);
        usrPasswordNueva2 = encripted;
        flag = usuarioDao.verificarContrasena(ControladorUsuario.user.getUsrId(), usrPassword);
        if (flag) {
            if (usrPasswordNueva.equals(usrPasswordNueva2)) {
                boolean flag2 = usuarioDao.actualizarContrasena(ControladorUsuario.user.getUsrId(), usrPasswordNueva);
                if (flag2) {
                    addMessage("Se guardaron los cambios");
                    aux = "irPrincipal";
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al momento de actualizar su contraseña", null);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al momento de actualizar su contraseña", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return aux;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Integer getPrsTipoIdentificacion() {
        return prsTipoIdentificacion;
    }

    public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
        this.prsTipoIdentificacion = prsTipoIdentificacion;
    }

    public String getPrsIdentificacion() {
        return prsIdentificacion;
    }

    public void setPrsIdentificacion(String prsIdentificacion) {
        this.prsIdentificacion = prsIdentificacion;
    }

    public String getPrsNombres() {
        return prsNombres;
    }

    public void setPrsNombres(String prsNombres) {
        this.prsNombres = prsNombres;
    }

    public String getPrsPrimerApellido() {
        return prsPrimerApellido;
    }

    public void setPrsPrimerApellido(String prsPrimerApellido) {
        this.prsPrimerApellido = prsPrimerApellido;
    }

    public String getPrsSegundoApellido() {
        return prsSegundoApellido;
    }

    public void setPrsSegundoApellido(String prsSegundoApellido) {
        this.prsSegundoApellido = prsSegundoApellido;
    }

    public String getPrsMailInstitucional() {
        return prsMailInstitucional;
    }

    public void setPrsMailInstitucional(String prsMailInstitucional) {
        this.prsMailInstitucional = prsMailInstitucional;
    }

    public String getPrsMailPersonal() {
        return prsMailPersonal;
    }

    public void setPrsMailPersonal(String prsMailPersonal) {
        this.prsMailPersonal = prsMailPersonal;
    }

    public String getPrsTelefono() {
        return prsTelefono;
    }

    public void setPrsTelefono(String prsTelefono) {
        this.prsTelefono = prsTelefono;
    }

    public String getPrsCarnetConadis() {
        return prsCarnetConadis;
    }

    public void setPrsCarnetConadis(String prsCarnetConadis) {
        this.prsCarnetConadis = prsCarnetConadis;
    }

    public String getUsrPassword() {
        return usrPassword;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getUsrPassword2() {
        return usrPassword2;
    }

    public void setUsrPassword2(String usrPassword2) {
        this.usrPassword2 = usrPassword2;
    }

    public Integer getPrsSexo() {
        return prsSexo;
    }

    public void setPrsSexo(Integer prsSexo) {
        this.prsSexo = prsSexo;
    }

    public Integer getPrsDiscapacidad() {
        return prsDiscapacidad;
    }

    public void setPrsDiscapacidad(Integer prsDiscapacidad) {
        this.prsDiscapacidad = prsDiscapacidad;
    }

    public Integer getPrsPorcentajeDiscapacidad() {
        return prsPorcentajeDiscapacidad;
    }

    public void setPrsPorcentajeDiscapacidad(Integer prsPorcentajeDiscapacidad) {
        this.prsPorcentajeDiscapacidad = prsPorcentajeDiscapacidad;
    }

    public Date getPrsFechaNacimiento() {
        return prsFechaNacimiento;
    }

    public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
        this.prsFechaNacimiento = prsFechaNacimiento;
    }

    public List<Ubicacion> getListaUbicacion() {
        return listaUbicacion;
    }

    public void setListaUbicacion(List<Ubicacion> listaUbicacion) {
        this.listaUbicacion = listaUbicacion;
    }

    public List<SelectItem> getListaSexo() {
        return listaSexo;
    }

    public void setListaSexo(List<SelectItem> listaSexo) {
        this.listaSexo = listaSexo;
    }

    public List<SelectItem> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<SelectItem> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }

    public List<SelectItem> getListaDiscapacidad() {
        return listaDiscapacidad;
    }

    public void setListaDiscapacidad(List<SelectItem> listaDiscapacidad) {
        this.listaDiscapacidad = listaDiscapacidad;
    }

    public Usuario getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(Usuario usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    public Persona getPersonaregistro() {
        return personaregistro;
    }

    public void setPersonaregistro(Persona personaregistro) {
        this.personaregistro = personaregistro;
    }

    public Integer getPrsEtnia() {
        return prsEtnia;
    }

    public void setPrsEtnia(Integer prsEtnia) {
        this.prsEtnia = prsEtnia;
    }

    public List<Etnia> getListaEtnia() {
        return listaEtnia;
    }

    public void setListaEtnia(List<Etnia> listaEtnia) {
        this.listaEtnia = listaEtnia;
    }

    public Integer getPrsId() {
        return prsId;
    }

    public void setPrsId(Integer prsId) {
        this.prsId = prsId;
    }

    public String getUsrPasswordNueva() {
        return usrPasswordNueva;
    }

    public void setUsrPasswordNueva(String usrPasswordNueva) {
        this.usrPasswordNueva = usrPasswordNueva;
    }

    public String getUsrPasswordNueva2() {
        return usrPasswordNueva2;
    }

    public void setUsrPasswordNueva2(String usrPasswordNueva2) {
        this.usrPasswordNueva2 = usrPasswordNueva2;
    }

}
