package ec.edu.uce.planTitulacion.ejb.dao;

import ec.edu.uce.planTitulacion.ejb.dto.Ubicacion;
import java.util.List;
import javax.ejb.Local;

@Local
public interface UbicacionDao {
    public List<Ubicacion> listarUbicacion() throws Exception;
}
