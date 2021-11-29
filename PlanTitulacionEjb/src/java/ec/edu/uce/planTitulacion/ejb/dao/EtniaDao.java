package ec.edu.uce.planTitulacion.ejb.dao;

import ec.edu.uce.planTitulacion.ejb.dto.Etnia;
import java.util.List;
import javax.ejb.Local;

@Local
public interface EtniaDao {
    public List<Etnia> listarEtnia() throws Exception;
}
