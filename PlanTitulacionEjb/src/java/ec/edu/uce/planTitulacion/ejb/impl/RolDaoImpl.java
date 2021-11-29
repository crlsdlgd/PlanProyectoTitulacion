package ec.edu.uce.planTitulacion.ejb.impl;
import ec.edu.uce.planTitulacion.ejb.dao.RolDao;
import ec.edu.uce.planTitulacion.ejb.dto.Rol;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class RolDaoImpl extends DAO implements RolDao {

    @Override
    public List<Rol> buscarRolByUser(Usuario user) throws Exception {
        List<Rol> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT r.rol_id, r.rol_descripcion\n"
                    + "FROM rol r, usuario u, usuario_rol ru\n"
                    + "WHERE ru.usr_id = u.usr_id AND\n"
                    + "r.rol_id= ru.rol_id AND u.usr_id= ? ");

            st.setInt(1, user.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setRolId(rs.getInt("rol_id"));
                rol.setRoldescripcion(rs.getString("rol_descripcion"));
   
                lista.add(rol);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

}
