package ec.edu.uce.planTitulacion.ejb.impl;

import static ec.edu.uce.planTitulacion.ejb.constantes.ConstantesSistema.TIPO_IDENTIFICACION_CEDULA_VALUE;
import ec.edu.uce.planTitulacion.ejb.constantes.RolConstantes;
import static ec.edu.uce.planTitulacion.ejb.constantes.RolConstantes.ROL_BD_ESTUDIANTE_LABEL;
import static ec.edu.uce.planTitulacion.ejb.constantes.RolConstantes.ROL_BD_ESTUDIANTE_VALUE;
import ec.edu.uce.planTitulacion.ejb.dao.UsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Etnia;
import ec.edu.uce.planTitulacion.ejb.dto.Persona;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import static ec.edu.uce.planTitulacion.ejb.utilities.FechaUtilToSql.fechaUtilToSql;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class UsuarioDaoImpl extends DAO implements UsuarioDao {

    @Override
    public boolean registrar(Usuario usuario) throws Exception {

        String nick = sacarNick(usuario.getUsrPersona().getPrsMailInstitucional());
        Date prsfechaNacimientoSql = fechaUtilToSql(usuario.getUsrPersona().getPrsFechaNacimiento());
        boolean flag = false;
        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("INSERT INTO persona (prs_identificacion, prs_nombres, prs_primer_apellido, prs_segundo_apellido,prs_mail_institucional, prs_mail_personal, prs_telefono, prs_carnet_conadis, prs_tipo_identificacion, prs_sexo, prs_discapacidad, prs_porcentaje_discapacidad, etn_id, prs_fecha_nacimiento) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            st.setString(1, usuario.getUsrPersona().getPrsIdentificacion());
            st.setString(2, usuario.getUsrPersona().getPrsNombres());
            st.setString(3, usuario.getUsrPersona().getPrsPrimerApellido());
            st.setString(4, usuario.getUsrPersona().getPrsSegundoApellido());
            st.setString(5, usuario.getUsrPersona().getPrsMailInstitucional());
            st.setString(6, usuario.getUsrPersona().getPrsMailPersonal());
            st.setString(7, usuario.getUsrPersona().getPrsTelefono());
            st.setString(8, usuario.getUsrPersona().getPrsCarnetConadis());
            st.setInt(9, usuario.getUsrPersona().getPrsTipoIdentificacion());
            st.setInt(10, usuario.getUsrPersona().getPrsSexo());
            st.setInt(11, usuario.getUsrPersona().getPrsDiscapacidad());
            st.setInt(12, usuario.getUsrPersona().getPrsPorcentajeDiscapacidad());
            st.setInt(13, usuario.getUsrPersona().getPrsEtnia().getEtnId());
            st.setDate(14, prsfechaNacimientoSql);
            st.executeUpdate();
            st.close();

            PreparedStatement st2 = this.getCn().prepareStatement("SELECT MAX(prs_id) AS prs_id FROM persona");
            ResultSet rs;
            int prsId = 0;
            rs = st2.executeQuery();
            while (rs.next()) {
                prsId = rs.getInt("prs_id");
            }
            rs.close();
            st2.close();

            PreparedStatement st3 = this.getCn().prepareStatement("INSERT INTO usuario (usr_nick, usr_password, prs_id, usr_identificacion) VALUES(?,?,?,?)");
            st3.setString(1, nick);
            st3.setString(2, usuario.getUsrPassword());
            st3.setInt(3, prsId);
            st3.setString(4, usuario.getUsrPersona().getPrsIdentificacion());
            st3.executeUpdate();
            st3.close();

            PreparedStatement st4 = this.getCn().prepareStatement("SELECT MAX(usr_id) AS usr_id FROM usuario");
            ResultSet rs2;
            int usrId = 0;
            rs2 = st4.executeQuery();
            while (rs2.next()) {
                usrId = rs2.getInt("usr_id");
            }
            rs2.close();
            st4.close();

            PreparedStatement st5 = this.getCn().prepareStatement("INSERT INTO usuario_rol (rol_id, usr_id) VALUES(" + ROL_BD_ESTUDIANTE_VALUE + ",?)");
            st5.setInt(1, usrId);
            st5.executeUpdate();
            st5.close();
            this.getCn().commit();
            flag = true;

        } catch (Exception e) {
            this.getCn().rollback();
            return false;

        } finally {
            this.Cerrar();
        }

        return flag;
    }

    @Override
    public List<Usuario> listarUsuario() throws Exception {
        List<Usuario> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional, u.usr_nick FROM usuario u, persona pe");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                Persona person = new Persona();
                usuario.setUsrId(rs.getInt("usr_id"));
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                usuario.setUsrPersona(person);
                usuario.setUsrNick(rs.getString("usr_nick"));
                lista.add(usuario);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public List<Usuario> listarUserByPlan(Plan plan) throws Exception {
        List<Usuario> lista;
        ResultSet rs;
        try {

            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional,pe.prs_mail_personal, u.usr_nick\n"
                    + "FROM plan p, usuario u, plan_usuario pu, persona pe\n"
                    + "WHERE pu.usr_id = u.usr_id AND\n"
                    + "u.prs_id=pe.prs_id AND\n"
                    + "pu.plus_postulado='TRUE' AND\n"
                    + "p.pln_id= pu.pln_id AND p.pln_id=?");

            st.setInt(1, plan.getPlnId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                Persona person = new Persona();
                usuario.setUsrId(rs.getInt("usr_id"));
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                person.setPrsMailPersonal(rs.getString("prs_mail_personal"));
                usuario.setUsrPersona(person);
                usuario.setUsrNick(rs.getString("usr_nick"));
                lista.add(usuario);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public Usuario buscarUsuarioLogin(String nick, String pass) throws Exception {
        Usuario usuario;
        ResultSet rs;
        String nickCorreo = sacarNick(nick);
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional, u.usr_nick \n"
                    + "FROM usuario u, persona pe \n"
                    + "WHERE u.prs_id=pe.prs_id \n"
                    + "AND (usr_nick = ? OR usr_identificacion = ? OR usr_nick = ?)\n"
                    + "AND usr_password = ?");
            st.setString(1, nick);
            st.setString(2, nick);
            st.setString(3, nickCorreo);
            st.setString(4, pass);
            rs = st.executeQuery();
            usuario = new Usuario();

            while (rs.next()) {
                usuario.setUsrId(rs.getInt("usr_id"));
                Persona person = new Persona();
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                usuario.setUsrPersona(person);
                usuario.setUsrNick(rs.getString("usr_nick"));
            }

            if (usuario.getUsrId() == null) {
                return null;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return usuario;

    }

    @Override
    public List<String> autoCompletarEstudiante(String query) throws Exception {
        List<String> lista;
        ResultSet rs;
        query = "%" + query + "%";
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT pe.prs_nombres\n"
                    + "FROM usuario u, rol r, rol_usuario ru, persona pe\n"
                    + "WHERE UPPER(pe.prs_primer_apellido) LIKE UPPER( ? ) AND\n"
                    + "u.usr_id=ru.usr_id AND\n"
                    + "ru.rol_id = r.rol_id AND\n"
                    + "pe.prs_id=u.prs_id AND\n"
                    + "r.rol_descripcion='" + ROL_BD_ESTUDIANTE_LABEL + "'");
            st.setString(1, query);
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                String aux = rs.getString("prs_nombres");
                lista.add(aux);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;

    }

    @Override
    public boolean existeEstudiante(String txtEstudiante) throws Exception {
        ResultSet rs;
        List<Usuario> lista;
        boolean flag = true;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id FROM usuario u, usuario_rol ru, rol r\n"
                    + "WHERE ru.usr_id=u.usr_id AND\n"
                    + "ru.rol_id = r.rol_id AND\n"
                    + "pe.prs_id = u.prs_id AND\n"
                    + "r.rol_descripcion = 'Estudiante' AND\n"
                    + "pe.prs_nombres= ? ");
            st.setString(1, txtEstudiante);
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setUsrId(rs.getInt("usr_id"));
                lista.add(u);
            }
            if (lista.isEmpty()) {
                flag = false;
            }
        } catch (Exception e) {
            return false;

        } finally {
            this.Cerrar();
        }
        return flag;
    }

    @Override
    public boolean existeIdentificacion(String prsIdentificacion) {
        boolean flag = false;
        ResultSet rs;
        int usr_id = -1;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT prs_id FROM persona WHERE prs_identificacion = ?");
            st.setString(1, prsIdentificacion);
            rs = st.executeQuery();
            while (rs.next()) {
                usr_id = (rs.getInt("prs_id"));
            }
            if (usr_id == -1) {
                flag = true;
            }
        } catch (Exception e) {

        }
        return flag;
    }

    @Override
    public Usuario buscarUsuarioPrecursor(Plan plan) throws Exception {
        Usuario user = new Usuario();
        Persona person = new Persona();
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, pe.prs_nombres FROM usuario u, persona pe WHERE u.prs_id=pe.prs_id AND u.usr_id= ?");
            st.setInt(1, plan.getPlnPropuestoPor());
            rs = st.executeQuery();
            while (rs.next()) {
                user.setUsrId(rs.getInt("usr_id"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                user.setUsrPersona(person);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return user;

    }

    private String sacarNick(String correo) {
        String aux = "";
        if (correo.length() > 11) {
            for (int i = 0; i < correo.length() - 11; i++) {
                aux = aux + correo.charAt(i);
            }
        }
        return aux;
    }

    public Usuario findTutorByPlan(Plan plan) throws Exception {
        Usuario user = new Usuario();
        Persona person = new Persona();
        ResultSet rs;
        try {
            this.Conectar();
            //PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, pe.prs_nombres FROM usuario u, persona pe WHERE u.prs_id=pe.prs_id AND u.usr_id= ?");
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, p.prs_nombres, p.prs_primer_apellido\n"
                    + "FROM plan pl, usuario u, plan_usuario pu,persona p, rol r, usuario_rol ur\n"
                    + "WHERE pl.pln_id=pu.pln_id AND\n"
                    + "pu.usr_id=u.usr_id AND\n"
                    + "u.usr_id=ur.usr_id AND\n"
                    + "u.prs_id=p.prs_id AND\n"
                    + "ur.rol_id=r.rol_id AND\n"
                    + "r.rol_descripcion= ? AND\n"
                    + "pl.pln_id= ?");
            st.setString(1, RolConstantes.ROL_BD_DOCENTE_LABEL);
            st.setInt(2, plan.getPlnId());
            rs = st.executeQuery();
            while (rs.next()) {
                user.setUsrId(rs.getInt("usr_id"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                user.setUsrPersona(person);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return user;
    }

    @Override
    public Usuario cargarUsuario(Usuario user) throws Exception {
        Usuario usuario = new Usuario();
        Persona person = new Persona();
        Etnia etnia = new Etnia();
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, p.prs_identificacion, p.prs_nombres, p.prs_primer_apellido, p.prs_segundo_apellido, p.prs_sexo, p.prs_mail_personal, p.prs_mail_institucional, p.prs_fecha_nacimiento, p.prs_telefono, e.etn_id, p.prs_id\n"
                    + "FROM usuario u,persona p, etnia e\n"
                    + "WHERE u.prs_id=p.prs_id AND\n"
                    + "p.etn_id=e.etn_id AND\n"
                    + "u.usr_id=?");
            st.setInt(1, user.getUsrId());
            rs = st.executeQuery();

            while (rs.next()) {
                usuario.setUsrId(rs.getInt("usr_id"));
                person.setPrsIdentificacion(rs.getString("prs_identificacion"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsSegundoApellido(rs.getString("prs_segundo_apellido"));
                person.setPrsSexo(rs.getInt("prs_sexo"));
                person.setPrsMailPersonal(rs.getString("prs_mail_personal"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                person.setPrsFechaNacimiento(rs.getDate("prs_fecha_nacimiento"));
                person.setPrsTelefono(rs.getString("prs_telefono"));
                person.setPrsId(rs.getInt("prs_id"));
                etnia.setEtnId(rs.getInt("etn_id"));
                person.setPrsEtnia(etnia);
                usuario.setUsrPersona(person);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return usuario;
    }

    @Override
    public boolean actualizarDatos(Usuario usuario) throws Exception {
        boolean flag = false;
        Date prsfechaNacimientoSql = fechaUtilToSql(usuario.getUsrPersona().getPrsFechaNacimiento());

        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("UPDATE persona SET \n"
                    + "prs_nombres = ?, prs_primer_apellido = ?, prs_segundo_apellido = ?, prs_mail_institucional = ?, prs_mail_personal = ?, prs_telefono = ?, prs_sexo = ?, etn_id = ?, prs_fecha_nacimiento = ?\n"
                    + "WHERE prs_id = ?");
            st.setString(1, usuario.getUsrPersona().getPrsNombres());
            st.setString(2, usuario.getUsrPersona().getPrsPrimerApellido());
            st.setString(3, usuario.getUsrPersona().getPrsSegundoApellido());
            st.setString(4, usuario.getUsrPersona().getPrsMailInstitucional());
            st.setString(5, usuario.getUsrPersona().getPrsMailPersonal());
            st.setString(6, usuario.getUsrPersona().getPrsTelefono());
            st.setInt(7, usuario.getUsrPersona().getPrsSexo());
            st.setInt(8, usuario.getUsrPersona().getPrsEtnia().getEtnId());
            st.setDate(9, prsfechaNacimientoSql);
            st.setInt(10, usuario.getUsrPersona().getPrsId());
            st.executeUpdate();
            st.close();
            this.getCn().commit();
            flag = true;
//            System.out.println(usuario.getUsrPersona().getPrsNombres()+usuario.getUsrPersona().getPrsPrimerApellido()+usuario.getUsrPersona().getPrsSegundoApellido()+
//                    usuario.getUsrPersona().getPrsMailInstitucional()+usuario.getUsrPersona().getPrsMailPersonal()+usuario.getUsrPersona().getPrsTelefono()+
//                    usuario.getUsrPersona().getPrsSexo()+usuario.getUsrPersona().getPrsEtnia().getEtnId()+prsfechaNacimientoSql+usuario.getUsrPersona().getPrsId());
        } catch (Exception e) {
            this.getCn().rollback();
            return false;
        } finally {
            this.Cerrar();
        }
        return flag;
    }

    public boolean verificarContrasena(Integer usrId, String usrPassword) throws Exception {
        boolean flag = false;
        Usuario usuario;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT usr_id FROM usuario\n"
                    + "WHERE usr_id= ? AND\n"
                    + "usr_password= ?");
            st.setInt(1, usrId);
            st.setString(2, usrPassword);
            rs = st.executeQuery();
            usuario = new Usuario();
            while (rs.next()) {
                usuario.setUsrId(rs.getInt("usr_id"));
            }
            if (usuario.getUsrId() != null) {
                flag = true;
            }
        } catch (Exception e) {
        } finally {
            this.Cerrar();
        }
        return flag;
    }

    public boolean actualizarContrasena(Integer usrId, String usrPasswordNueva) throws Exception {
        boolean flag = false;
        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareCall("UPDATE usuario SET\n"
                    + "usr_password = ?\n"
                    + "WHERE usr_id = ?");
            st.setString(1, usrPasswordNueva);
            st.setInt(2, usrId);
            st.executeUpdate();
            st.close();
            this.getCn().commit();
            flag = true;
        } catch (Exception e) {
            this.getCn().rollback();
            return false;
        } finally {
            this.Cerrar();
        }
        return flag;
    }
}
