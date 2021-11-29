package ec.edu.uce.planTitulacion.ejb.dao;


import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PlanDao {
    public void registrar(Plan plan) throws Exception;
    public List<Plan> listarPlan() throws Exception;
    public List<Plan> listarPlanesAprobados() throws Exception;
    public List<Plan> listarPlanByUser(Usuario usuario) throws Exception;

    public void guardarPlan(String tema, Date fecha, List<String> listIntegrantes, Usuario user) throws Exception;

    public void guardarPropuestaPlan(String txtTema, String txtDetalle, String txtObjetivos, String txtJustificacion, Usuario user) throws Exception;

    public List<Plan> listarPlanesNoAprobadosNiplus_postulados(Usuario user) throws Exception;

    public void postular(Plan plan, Usuario user)throws Exception;

    public List<Plan> cargarMisPostulaciones(Usuario user)throws Exception;
    public List<Plan> cargarMisProyectos(Usuario user)throws Exception;

    public void listoRevision(Plan plan)throws Exception;

    public List<Plan> cargarPlanesPorAprobar(Usuario user)throws Exception;

    public void noListoRevision(Plan plan)throws Exception;

    public void aprobarTema(Plan plan)throws Exception;

    public List<Plan> cargarPlanesPorRevisar()throws Exception;

    public void noAprobarTema(Plan plan)throws Exception;

}
