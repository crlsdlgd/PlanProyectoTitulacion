package ec.edu.uce.planTitulacion.jsf.managedBeans;

import ec.edu.uce.planTitulacion.ejb.dao.PlanDao;
import ec.edu.uce.planTitulacion.ejb.dao.UsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@Named(value = "controladorPlan")
@SessionScoped
public class ControladorPlan implements Serializable {

    private static final long serialversionuid = 1L;
    @EJB
    private UsuarioDao usuarioDao;

    @EJB
    private PlanDao planDao;

    private List<Plan> listaPlan;
    private List<Usuario> listUsuario;
    private List<String> listIntegrantes;
    private Plan plan;
    private String txtEstudiante, txtTema,txtDetalle, txtObjetivos, txtJustificacion;
    private Date txtFecha;
    
    public void anadirEstudiante() throws Exception{
        boolean flag = false;
        if (usuarioDao.existeEstudiante(txtEstudiante)) {
            for (int i = 0; i < listIntegrantes.size(); i++) {
                if (txtEstudiante.equals(listIntegrantes.get(i))) {
                    flag = true;
                    i = listIntegrantes.size();
                }
            }
        }else{
            flag=true;
        }
        if (!flag) {
            listIntegrantes.add(txtEstudiante);
        }

    }

    
    public void quitarEstudiante(String txt) {
        listIntegrantes.remove(txt);
    }

    public void guardarPlan() throws Exception {

        planDao.guardarPlan(txtTema, new java.sql.Date(txtFecha.getTime()), listIntegrantes, ControladorUsuario.user);
    }

    public void guardarPropuestaPlan() throws Exception{
        planDao.guardarPropuestaPlan(txtTema, txtDetalle, txtObjetivos, txtJustificacion, ControladorUsuario.user);
        addMessage("Plan Guardado con exito");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void postularTema(Plan plan)throws Exception{
        this.plan=plan;
        RequestContext.getCurrentInstance().execute("PF('wdlPostulacion').show();" );
    }
    
    public void postular()throws Exception{
        planDao.postular(plan, ControladorUsuario.user);
        addMessage("Postulación Exitosa");
    }
    
    public void cargarMisPostulaciones() throws Exception{
        listaPlan = planDao.cargarMisPostulaciones(ControladorUsuario.user);
    }
    
    public void cargarMisProyectos() throws Exception{
        listaPlan = planDao.cargarMisProyectos(ControladorUsuario.user);
    }
    
    public void listoRevision() throws Exception{
        planDao.listoRevision(plan);
        addMessage("El Plan se envió para revisión");
//        cargarMisPostulaciones();
    }
    
    public void noListoRevision() throws Exception{
        planDao.noListoRevision(plan);
        addMessage("El Plan se removió antes de ser revisado");
        //cargarMisPostulaciones();
    }
    
    public void aprobarTema() throws Exception{
        planDao.aprobarTema(plan);
        addMessage("El Plan se aprobó correctamente");
        //cargarMisPostulaciones();
    }
    
    public void noAprobarTema() throws Exception{
        planDao.noAprobarTema(plan);
        addMessage("El Plan se descarto correctamente");
        //cargarMisPostulaciones();
    }
    
    public void selecionarPlan(Plan plan){
        this.plan=plan;
    }
    
    public void cargarPlanesPorAprobar() throws Exception{
        listaPlan = planDao.cargarPlanesPorAprobar(ControladorUsuario.user);
    }

    public void cargarPlanesPorRevisar() throws Exception{
        listaPlan = planDao.cargarPlanesPorRevisar();
    }
    
    @PostConstruct
    public void init() {
        listIntegrantes = new ArrayList<String>();
    }

    public ControladorPlan() {
    }

    public String getTxtDetalle() {
        return txtDetalle;
    }

    public void setTxtDetalle(String txtDetalle) {
        this.txtDetalle = txtDetalle;
    }
    
    public void cargarPlanesAprobados() throws Exception {
        listaPlan = planDao.listarPlanesAprobados();
    }
    
    public void cargarPlanesNoAprobadosNiplus_postulados() throws Exception {
        listaPlan = planDao.listarPlanesNoAprobadosNiplus_postulados(ControladorUsuario.user);
    }

    public void listarUsuarioByPlan(Plan plan) throws Exception {
        listUsuario = usuarioDao.listarUserByPlan(plan);
    }

    public List<String> autoCompletarEstudiante(String query) throws Exception {
        List<String> lista = usuarioDao.autoCompletarEstudiante(query);
        return lista;
    }

    public String getTxtObjetivos() {
        return txtObjetivos;
    }

    public void setTxtObjetivos(String txtObjetivos) {
        this.txtObjetivos = txtObjetivos;
    }

    public String getTxtJustificacion() {
        return txtJustificacion;
    }

    public void setTxtJustificacion(String txtJustificacion) {
        this.txtJustificacion = txtJustificacion;
    }
    
    public Date getTxtFecha() {
        return txtFecha;
    }

    public void setTxtFecha(Date txtFecha) {
        this.txtFecha = txtFecha;
    }

    public String getTxtTema() {
        return txtTema;
    }

    public void setTxtTema(String txtTema) {
        this.txtTema = txtTema;
    }

    public String getTxtEstudiante() {
        return txtEstudiante;
    }

    public void setTxtEstudiante(String txtEstudiante) {
        this.txtEstudiante = txtEstudiante;
    }


    public List<String> getListIntegrantes() {
        return listIntegrantes;
    }

    public void setListIntegrantes(List<String> listIntegrantes) {
        this.listIntegrantes = listIntegrantes;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public List<Usuario> getListUsuario() {
        return listUsuario;
    }

    public void setListUsuario(List<Usuario> listUsuario) {
        this.listUsuario = listUsuario;
    }


    public List<Plan> getListaPlan() {
        return listaPlan;
    }

    public void setListaPlan(List<Plan> listaPlan) {
        this.listaPlan = listaPlan;
    }

}
