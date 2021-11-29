package ec.edu.uce.planTitulacion.ejb.impl;

import ec.edu.uce.planTitulacion.ejb.dao.RolUsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Persona;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Rol;
import ec.edu.uce.planTitulacion.ejb.dto.UsuarioRol;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class RolUsuarioDaoImpl extends DAO implements RolUsuarioDao {

    @Override
    public List<UsuarioRol> listarIntegrantesByPlan(Plan plan) throws Exception {
        List<UsuarioRol> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional, pu.plus_postulado, r.rol_id, r.rol_descripcion\n"
                    + "FROM usuario u, plan_usuario pu, plan p, rol r, usuario_rol ru, persona pe\n"
                    + "WHERE pu.usr_id=u.usr_id  AND\n"
                    + "pu.pln_id=p.pln_id AND\n"
                    + "ru.usr_id=u.usr_id AND\n"
                    + "ru.rol_id=r.rol_id AND\n"
                    + "u.prs_id=pe.prs_id AND\n"
                    + "p.pln_aprobado='FALSE' AND\n"
                    + "pu.plus_postulado='TRUE' AND\n"
                    + "p.pln_id=?");
            st.setInt(1, plan.getPlnId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario user = new Usuario();
                Rol rol = new Rol();
                Persona person = new Persona();
                UsuarioRol rolUser = new UsuarioRol();
                user.setUsrId(rs.getInt("usr_id"));
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                user.setUsrPersona(person);
                rol.setRolId(rs.getInt("rol_id"));
                rol.setRoldescripcion(rs.getString("rol_descripcion"));
                rolUser.setUsuario(user);
                rolUser.setRol(rol);
                lista.add(rolUser);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

}
