package ec.edu.uce.planTitulacion.ejb.impl;

import ec.edu.uce.planTitulacion.ejb.dao.PlanUsuarioDao;
import ec.edu.uce.planTitulacion.ejb.dto.Persona;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.PlanUsuario;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import ec.edu.uce.planTitulacion.ejb.servicios.SendMailGmail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class PlanUsuarioDaoImpl extends DAO implements PlanUsuarioDao {

//    @EJB
//    private PlanDaoImpl planDaoImpl;

    @Override
    public List<PlanUsuario> listarPostulantesByPlan(Plan plan) throws Exception {
        List<PlanUsuario> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional,pu.plus_postulado \n"
                    + "FROM usuario u, plan_usuario pu, plan p, persona pe\n"
                    + "WHERE pu.usr_id=u.usr_id AND\n"
                    + "pu.pln_id=p.pln_id AND\n"
                    + "u.prs_id=pe.prs_id AND\n"
                    + "p.pln_aprobado='FALSE' AND\n"
                    + "pu.pln_id= ? ");
            st.setInt(1, plan.getPlnId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario user = new Usuario();
                PlanUsuario planUser = new PlanUsuario();
                Persona person = new Persona();
                user.setUsrId(rs.getInt("usr_id"));
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                user.setUsrPersona(person);
                planUser.setPlnId(plan);
                planUser.setUsrId(user);
                planUser.setPlusPostulado(rs.getBoolean("plus_postulado"));
                lista.add(planUser);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;

    }

    @Override
    public List<PlanUsuario> listarIntegrantesByPlan(Plan plan) throws Exception {
        List<PlanUsuario> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT u.usr_id, u.prs_id, pe.prs_primer_apellido, pe.prs_nombres, pe.prs_mail_institucional, pu.plus_postulado\n"
                    + "FROM usuario u, plan_usuario pu, plan p, persona pe\n"
                    + "WHERE pu.usr_id=u.usr_id AND\n"
                    + "pu.pln_id=p.pln_id AND\n"
                    + "u.prs_id=pe.prs_id AND\n"
                    + "p.pln_aprobado='TRUE' AND\n"
                    + "pu.plus_postulado='TRUE' AND\n"
                    + "pu.pln_id= ?");
            st.setInt(1, plan.getPlnId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Usuario user = new Usuario();
                PlanUsuario planUser = new PlanUsuario();
                Persona person = new Persona();
                user.setUsrId(rs.getInt("usr_id"));
                person.setPrsId(rs.getInt("prs_id"));
                person.setPrsPrimerApellido(rs.getString("prs_primer_apellido"));
                person.setPrsNombres(rs.getString("prs_nombres"));
                person.setPrsMailInstitucional(rs.getString("prs_mail_institucional"));
                user.setUsrPersona(person);
                planUser.setPlnId(plan);
                planUser.setUsrId(user);
                planUser.setPlusPostulado(rs.getBoolean("plus_postulado"));
                lista.add(planUser);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;

    }

    @Override
    public void guardarProyecto(List<PlanUsuario> listaPostulantes) throws Exception {

        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("UPDATE plan_usuario\n"
                    + "SET plus_postulado = ?\n"
                    + "WHERE pln_id=? AND usr_id=?;");
            for (int i = 0; i < listaPostulantes.size(); i++) {
                st.setBoolean(1, listaPostulantes.get(i).isPlusPostulado());
                st.setInt(2, listaPostulantes.get(i).getPlnId().getPlnId());
                st.setInt(3, listaPostulantes.get(i).getUsrId().getUsrId());
                st.executeUpdate();
            }
            st.close();
            this.getCn().commit();
//            SendMailGmail servicio = new SendMailGmail();
//            PlanDaoImpl plan = new PlanDaoImpl();
//            servicio.enviarPrimerMail(plan.findPlanById(listaPostulantes.get(0).getPlnId().getPlnId()));
        } catch (Exception e) {
            this.getCn().rollback();
            throw e;
        } finally {
            this.Cerrar();
        }

    }

}
