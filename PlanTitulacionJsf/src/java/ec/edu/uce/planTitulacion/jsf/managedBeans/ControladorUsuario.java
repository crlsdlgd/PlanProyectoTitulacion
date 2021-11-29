package ec.edu.uce.planTitulacion.jsf.managedBeans;

import ec.edu.uce.planTitulacion.ejb.constantes.RolConstantes;
import ec.edu.uce.planTitulacion.ejb.dao.PlanUsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dao.RolDao;
import ec.edu.uce.planTitulacion.ejb.dao.RolUsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dao.UsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Etnia;
import ec.edu.uce.planTitulacion.ejb.dto.Persona;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.PlanUsuario;
import ec.edu.uce.planTitulacion.ejb.dto.Rol;
import ec.edu.uce.planTitulacion.ejb.dto.UsuarioRol;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.servicios.VerificarRecaptcha;
import ec.edu.uce.planTitulacion.jsf.managedBean.sesion.SesionUsuarioBean;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;


import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.context.RequestContext;

@Named(value = "controladorUsuario")
@SessionScoped
public class ControladorUsuario implements Serializable {

    @EJB
    private RolDao rolDao;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private PlanUsuarioDao planUsuarioDao;
    @EJB
    private RolUsuarioDao rolUsuarioDao;

    public static Usuario user;
    private Usuario usuarioPrecursor;
    private List<String> listaRolUser;
    private List<PlanUsuario> listaPostulantes;
    private String nick, password, nombre, cedula, correo, password2;
    private String rolSelect;
    private Rol usuarioPrecursorRol;
    private List<UsuarioRol> listaIntegrantes;

    public String login() throws Exception {
        String ecripted = DigestUtils.md5Hex(password);
        password = ecripted;
        Usuario usr = usuarioDao.buscarUsuarioLogin(nick, password);
        String redireccion = "";
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
            boolean verify = VerificarRecaptcha.verificar(gRecaptchaResponse);
            if (verify) {
                if (usr == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario y/o Contraseña incorrectos", null));
                } else {
                    this.user = usr;
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usr);
                    
                    FacesContext context = FacesContext.getCurrentInstance();
                    SesionUsuarioBean sesion = context.getApplication().evaluateExpressionGet(context, "#{sesionUsuarioBean}", SesionUsuarioBean.class);
                    this.listaRolUser = new ArrayList<String>();
                    List<Rol> lista = rolDao.buscarRolByUser(user);
                    for(int i=0;i<lista.size();i++){
                        if (lista.get(i).getRolId() == RolConstantes.ROL_BD_ESTUDIANTE_VALUE) {
                            sesion.setEstudiante(true);
                        } else if (lista.get(i).getRolId() == RolConstantes.ROL_BD_DOCENTE_VALUE) {
                            sesion.setDocente(true);
                        } else if (lista.get(i).getRolId() == RolConstantes.ROL_BD_CONSEJO_VALUE) {
                            sesion.setConsejo(true);
                        }
                    }
                    for (int i = 0; i < lista.size(); i++) {
                        this.listaRolUser.add(lista.get(i).getRolDescripcion());
                    }
                    this.setListaRolUser(listaRolUser);

                    redireccion = "irPrincipal";
                }
            }
        } catch (Exception e) {
        }
        return redireccion;
    }

    public String redireccionRol() {
        String redireccion = "";
        try {
            FacesContext contex = FacesContext.getCurrentInstance();
            switch (rolSelect) {
                case "Estudiante":
                    redireccion = "irPrincipal";
                    break;
                case "Docente":
                    redireccion = "irHomeDocente";
                    break;
                case "Consejo":
                    redireccion = "irHomeConsejo";
                    break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encuentra implementado tal rol", null));
            }
        } catch (Exception e) {
        }
        return redireccion;
    }

    public void verUsuarioPrecursor(Plan plan) throws Exception {
        setUsuarioPrecursor(usuarioDao.buscarUsuarioPrecursor(plan));
        setUsuarioPrecursorRol(rolDao.buscarRolByUser(getUsuarioPrecursor()).get(0));
        RequestContext.getCurrentInstance().execute("PF('wdlWare2').show();");
    }

    public void cerrarSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public void verPostulantes(Plan plan) throws Exception {
        setListaPostulantes(planUsuarioDao.listarPostulantesByPlan(plan));
        RequestContext.getCurrentInstance().execute("PF('wdlWare3').show();");
    }

    public void verIntegrantes(Plan plan) throws Exception {
        setListaIntegrantes(rolUsuarioDao.listarIntegrantesByPlan(plan));
        RequestContext.getCurrentInstance().execute("PF('wdlWare3').show();");
    }

    public void guardarProyecto() throws Exception {
        planUsuarioDao.guardarProyecto(getListaPostulantes());
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void cargarUsuario(Usuario user) throws Exception{
        ControladorUsuario.user=usuarioDao.cargarUsuario(user);
    }
    
     public String updatePersona() throws Exception {
        String aux = "";
        boolean flag = false;

        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        System.out.println(params.get("updatePrsId"));
//        int fieldPrsId = Integer.parseInt(params.get("updatePrsId"));
        
        Persona persona = new Persona();
        Usuario usuario = new Usuario();
        Etnia etnia = new Etnia();
        persona.setPrsId(user.getUsrPersona().getPrsId());
        persona.setPrsNombres(user.getUsrPersona().getPrsNombres());
        persona.setPrsPrimerApellido(user.getUsrPersona().getPrsPrimerApellido());
        persona.setPrsSegundoApellido(user.getUsrPersona().getPrsSegundoApellido());
        persona.setPrsMailInstitucional(user.getUsrPersona().getPrsMailInstitucional());
        persona.setPrsMailPersonal(user.getUsrPersona().getPrsMailPersonal());
        persona.setPrsTelefono(user.getUsrPersona().getPrsTelefono());
        persona.setPrsSexo(user.getUsrPersona().getPrsSexo());
        persona.setPrsFechaNacimiento(user.getUsrPersona().getPrsFechaNacimiento());
        etnia.setEtnId(user.getUsrPersona().getPrsEtnia().getEtnId());
        persona.setPrsEtnia(etnia);
        
        usuario.setUsrPersona(persona);
        flag = usuarioDao.actualizarDatos(usuario);
        if (flag) {
            addMessage("Se guardaron los cambios");
            aux = "irPrincipal";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al momento de actualizar los cambios", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return aux;
    }
    
    public ControladorUsuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Usuario getUsuarioPrecursor() {
        return usuarioPrecursor;
    }

    public void setUsuarioPrecursor(Usuario usuarioPrecursor) {
        this.usuarioPrecursor = usuarioPrecursor;
    }

    public Rol getUsuarioPrecursorRol() {
        return usuarioPrecursorRol;
    }

    public void setUsuarioPrecursorRol(Rol usuarioPrecursorRol) {
        this.usuarioPrecursorRol = usuarioPrecursorRol;
    }

    public void setListaRolUser(List<String> listaRolUser) {
        this.listaRolUser = listaRolUser;
    }

    public List<String> getListaRolUser() throws Exception {

        return listaRolUser;
    }

    public String getRolSelect() {
        return rolSelect;
    }

    public void setRolSelect(String rolSelect) {
        this.rolSelect = rolSelect;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<PlanUsuario> getListaPostulantes() {
        return listaPostulantes;
    }

    public void setListaPostulantes(List<PlanUsuario> listaPostulantes) {
        this.listaPostulantes = listaPostulantes;
    }

    public List<UsuarioRol> getListaIntegrantes() {
        return listaIntegrantes;
    }

    public void setListaIntegrantes(List<UsuarioRol> listaIntegrantes) {
        this.listaIntegrantes = listaIntegrantes;
    }
}
