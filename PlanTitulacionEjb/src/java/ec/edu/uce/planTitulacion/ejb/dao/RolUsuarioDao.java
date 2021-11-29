package ec.edu.uce.planTitulacion.ejb.dao;

import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.UsuarioRol;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RolUsuarioDao {

    public List<UsuarioRol> listarIntegrantesByPlan(Plan plan) throws Exception;
    
}
