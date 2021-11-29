package ec.edu.uce.planTitulacion.ejb.impl;

import ec.edu.uce.planTitulacion.ejb.dao.EtniaDao;
import ec.edu.uce.planTitulacion.ejb.dto.Etnia;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class EtniaDaoImpl extends DAO implements EtniaDao{

    @Override
    public List<Etnia> listarEtnia() throws Exception {
        List<Etnia> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT etn_id, etn_descripcion FROM etnia ");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Etnia etnia = new Etnia();
                etnia.setEtnId(rs.getInt("etn_id"));
                etnia.setEtnDescripcion(rs.getString("etn_descripcion"));
                lista.add(etnia);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }
    
}
