package ec.edu.uce.planTitulacion.ejb.impl;

import ec.edu.uce.planTitulacion.ejb.dao.UbicacionDao;
import ec.edu.uce.planTitulacion.ejb.dto.Ubicacion;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class UbicacionDaoImpl extends DAO implements UbicacionDao{

    @Override
    public List<Ubicacion> listarUbicacion() throws Exception {
        List<Ubicacion> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT ubc_id, ubc_descripcion, ubc_gentilicio FROM ubicacion ");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Ubicacion ubc = new Ubicacion();
                ubc.setUbcId(rs.getInt("ubc_id"));
                ubc.setUbcDescripcion(rs.getString("ubc_descripcion"));
                ubc.setUbcGentilicio(rs.getString("ubc_gentilicio"));
                lista.add(ubc);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }
 
}
