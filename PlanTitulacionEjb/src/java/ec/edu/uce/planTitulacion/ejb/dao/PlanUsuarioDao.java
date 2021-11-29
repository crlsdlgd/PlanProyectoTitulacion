
package ec.edu.uce.planTitulacion.ejb.dao;


import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.PlanUsuario;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PlanUsuarioDao {

    public List<PlanUsuario> listarPostulantesByPlan(Plan plan)throws Exception;

    public void guardarProyecto(List<PlanUsuario> listaPostulantes)throws Exception;

    public List<PlanUsuario> listarIntegrantesByPlan(Plan plan)throws Exception;
    
}
