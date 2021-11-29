package ec.edu.uce.planTitulacion.ejb.dao;

import ec.edu.uce.planTitulacion.ejb.dto.Rol;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RolDao {
    List<Rol> buscarRolByUser(Usuario user) throws Exception;
}
