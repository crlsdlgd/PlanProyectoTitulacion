package ec.edu.uce.planTitulacion.ejb.impl;

import ec.edu.uce.planTitulacion.ejb.dao.PlanDao;
import ec.edu.uce.planTitulacion.ejb.dto.Plan;
import ec.edu.uce.planTitulacion.ejb.dto.Usuario;
import ec.edu.uce.planTitulacion.ejb.jdbc.impl.DAO;
import ec.edu.uce.planTitulacion.ejb.servicios.SendMailGmail;
import ec.edu.uce.planTitulacion.ejb.utilities.FechaUtilToSql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class PlanDaoImpl extends DAO implements PlanDao {

    @Override
    public void registrar(Plan plan) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareStatement("INSERT INTO plan (pln_tema,pln_fecha) VALUES(?,?)");
            st.setString(1, plan.getPlnTema());
            st.setDate(2, (Date) plan.getPlnFecha());
            st.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<Plan> listarPlan() throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT pln_id, pln_tema, pln_fecha FROM plan ");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnFecha(rs.getDate("pln_fecha"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public List<Plan> listarPlanesAprobados() throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT pln_id, pln_tema, pln_fecha FROM plan WHERE pln_aprobado = 'TRUE' ");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnFecha(rs.getDate("pln_fecha"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public List<Plan> listarPlanesNoAprobadosNiplus_postulados(Usuario user) throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT p.pln_id, p.pln_tema, p.pln_detalle, pln_objetivos, pln_justificacion, p.pln_propuesto_por \n"
                    + "FROM plan p, plan_usuario pu\n"
                    + "WHERE p.pln_id=pu.pln_id AND\n"
                    + "p.pln_aprobado='FALSE' AND\n"
                    + "pu.plus_postulado='FALSE' AND\n"
                    + "pu.usr_id!= ? AND\n"
                    + "p.pln_propuesto_por!= ?\n"
                    + "EXCEPT (SELECT p.pln_id, p.pln_tema, p.pln_detalle, pln_objetivos, pln_justificacion, p.pln_propuesto_por \n"
                    + "FROM plan p, plan_usuario pu\n"
                    + "WHERE pu.pln_id=p.pln_id AND\n"
                    + "pu.usr_id =?)");
            st.setInt(1, user.getUsrId());
            st.setInt(2, user.getUsrId());
            st.setInt(3, user.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnDetalle(rs.getString("pln_detalle"));
                plan.setPlnObjetivos(rs.getString("pln_objetivos"));
                plan.setPlnJustificación(rs.getString("pln_justificacion"));
                plan.setPlnPropuestoPor(rs.getInt("pln_propuesto_por"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    public Plan findPlanById(Integer idPlan) throws Exception {
        Plan plan = new Plan();
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT pln_id, pln_tema, pln_fecha FROM plan WHERE pln_id = ? ");
            st.setInt(1, idPlan);
            rs = st.executeQuery();
            while (rs.next()) {
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnFecha(rs.getDate("pln_fecha"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return plan;
    }

    @Override
    public List<Plan> listarPlanByUser(Usuario usuario) throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT p.pln_id, p.pln_tema, p.pln_fecha\n"
                    + "FROM plan p, usuario u, plan_usuario pu\n"
                    + "WHERE p.pln_id= pu.pln_id AND\n"
                    + "pu.usr_id = u.usr_id AND u.usr_id=?");

            st.setInt(1, usuario.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("tema"));
                plan.setPlnFecha(rs.getDate("fecha"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public void guardarPlan(String tema, Date fecha, List<String> listIntegrantes, Usuario user) throws Exception {

        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("INSERT INTO plan (pln_tema,pln_fecha) VALUES(?,?)");
            st.setString(1, tema);
            st.setDate(2, (Date) fecha);
            st.executeUpdate();
            st.close();

            PreparedStatement st2 = this.getCn().prepareStatement("SELECT MAX(pln_id) AS pln_id FROM plan");
            ResultSet rs;
            int idPlan = 0;
            rs = st2.executeQuery();
            while (rs.next()) {
                idPlan = rs.getInt("pln_id");
            }
            rs.close();
            st2.close();

            PreparedStatement st5 = this.getCn().prepareStatement("INSERT INTO plan_usuario (pln_id, usr_id) VALUES(?,?)");
            st5.setInt(1, idPlan);
            st5.setInt(2, user.getUsrId());
            st5.executeUpdate();
            st5.close();

            List<Usuario> listUsuario = new ArrayList();
            for (int i = 0; i < listIntegrantes.size(); i++) {
                PreparedStatement st3 = this.getCn().prepareStatement("SELECT u.usr_id \n"
                        + "FROM usuario u, persona pe\n"
                        + "WHERE u.prs_id=pe.prs_id AND\n"
                        + "pe.prs_nombres=?");
                ResultSet rs2;
                st3.setString(1, listIntegrantes.get(i));
                rs2 = st3.executeQuery();
                while (rs2.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setUsrId(rs2.getInt("usr_id"));
                    listUsuario.add(usuario);
                }
                rs2.close();
                st3.close();
            }

            for (int i = 0; i < listUsuario.size(); i++) {
                PreparedStatement st4 = this.getCn().prepareStatement("INSERT INTO plan_usuario (pln_id,usr_id) VALUES (?,?)");
                st4.setInt(1, idPlan);
                st4.setInt(2, listUsuario.get(i).getUsrId());
                st4.executeUpdate();
                st4.close();
            }

            this.getCn().commit();
            SendMailGmail servicio = new SendMailGmail();
            servicio.enviarTemaARevisionMail(this.findPlanById(idPlan));
        } catch (Exception e) {
            this.getCn().rollback();
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    public void BarrerPlan() throws Exception {
        List<Plan> lista, lista2;
        int caso;
        try {
            lista = this.listarPlan();
            lista2 = lista;
            SendMailGmail servicio = new SendMailGmail();
//            System.out.println("No.Planes: " + lista.size());
            String fechaPlan = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < lista.size(); i++) {
//                System.out.println("Fecha del plan " + lista.get(i).getPlnId() + " : " + lista.get(i).getPlnFecha());

                if (lista.get(i).getPlnFecha() == null) {
                    caso = 0;
                } else {
                    fechaPlan = sdf.format(lista.get(i).getPlnFecha());
//                    System.out.println("------------------------- Leyo la fecha");
                    caso = tipoEmail(lista2.get(i).getPlnFecha());
                    lista.get(i).setPlnFecha(sdf.parse(fechaPlan));
                }
//                System.out.println("Confirma fecha: " + lista.get(i).getPlnFecha());
                switch (caso) {
                    case 1:
//                        System.out.println("paso x aqui!!!");
                        servicio.enviarQuinceDiasMail(lista.get(i));
                        break;
                    case 2:
//                        System.out.println("paso x aqui222!!!");
                        servicio.enviarCincoMesesrMail(lista.get(i));
                        break;
                    default:
//                        System.out.println("paso x aqui default!!!");
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private int tipoEmail(java.util.Date fecha) {
        int caso = 0;
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = DateFor.format(fecha);
        java.util.Date fechaAux = fecha;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaActual = new java.util.Date();
            fechaActual = sdf.parse(sdf.format(fechaActual));
            //fechaActual = sdf.parse(fechaActual.getYear()+"-"+fechaActual.getMonth()+"-"+fechaActual.getDate());
//            System.out.println("Fecha auxiliar: " + fechaAux);
            fechaAux.setDate(fechaAux.getDate() + 15);
//            System.out.println("Fecha auxiliar +15 dias: " + fechaAux);
//            System.out.println("Fecha Actual: " + fechaActual);
            if (fechaAux.compareTo(fechaActual) == 0) {
                fechaAux.setDate(fechaAux.getDate() - 15);
//                System.out.println(fechaAux.compareTo(fechaActual));
                caso = 1;
            } else {
                fechaAux.setDate(fechaAux.getDate() - 15);
                fechaAux.setMonth(fechaAux.getMonth() + 5);
//                System.out.println("Fecha auxiliar +5 meses: " + fechaAux);
                if (fechaAux.compareTo(fechaActual) == 0) {
//                    System.out.println(fechaAux.compareTo(fechaActual));
                    caso = 2;
                }
                //fechaAux.setMonth(fechaAux.getMonth() - 5);
                fecha = DateFor.parse(stringDate);
                fechaAux = DateFor.parse(stringDate);
            }
//            fecha = DateFor.parse(stringDate);
//            fechaAux=fecha;
        } catch (Exception e) {

        }
        return caso;
    }

    @Override
    public void guardarPropuestaPlan(String txtTema, String txtDetalle, String txtObjetivos, String txtJustificacion, Usuario user) throws Exception {
        try {
            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("INSERT INTO plan (pln_tema, pln_detalle, pln_propuesto_por, pln_objetivos, pln_justificacion, pln_aprobado) VALUES(?,?,?,?,?,FALSE)");
            st.setString(1, txtTema);
            st.setString(2, txtDetalle);
            st.setInt(3, user.getUsrId());
            st.setString(4, txtObjetivos);
            st.setString(5, txtJustificacion);
            
            st.executeUpdate();
            st.close();

            PreparedStatement st2 = this.getCn().prepareStatement("SELECT MAX(pln_id) AS pln_id FROM plan");
            ResultSet rs;
            int idPlan = 0;
            rs = st2.executeQuery();
            while (rs.next()) {
                idPlan = rs.getInt("pln_id");
            }
            rs.close();
            st2.close();

            PreparedStatement st3 = this.getCn().prepareStatement("INSERT INTO plan_usuario (pln_id, usr_id, plus_postulado) VALUES(?,?,FALSE)");
            st3.setInt(1, idPlan);
            st3.setInt(2, user.getUsrId());
            st3.executeUpdate();
            st3.close();
            this.getCn().commit();
        } catch (Exception e) {
            this.getCn().rollback();
            throw e;
        } finally {
            this.Cerrar();
        }

    }

    @Override
    public void postular(Plan plan, Usuario user) throws Exception {
        try {

            this.Conectar();
            this.getCn().setAutoCommit(false);
            PreparedStatement st = this.getCn().prepareStatement("INSERT INTO plan_usuario (pln_id,usr_id, plus_postulado) VALUES(?,?,FALSE)");
            st.setInt(1, plan.getPlnId());
            st.setInt(2, user.getUsrId());
            st.executeUpdate();
            st.close();
            this.getCn().commit();
        } catch (Exception e) {
            this.getCn().rollback();
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<Plan> cargarMisPostulaciones(Usuario usuario) throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT p.pln_id, p.pln_tema, p.pln_detalle, p.pln_objetivos, p.pln_justificacion, p.pln_propuesto_por"
                    + " FROM plan p, plan_usuario pu\n"
                    + "WHERE p.pln_id=pu.pln_id AND\n"
                    + "p.pln_aprobado='FALSE' AND\n"
//                    + "p.pln_listo='FALSE' AND\n"
                    + "pu.usr_id=?");

            st.setInt(1, usuario.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnDetalle(rs.getString("pln_detalle"));
                plan.setPlnObjetivos(rs.getString("pln_objetivos"));
                plan.setPlnJustificación(rs.getString("pln_justificacion"));
                plan.setPlnPropuestoPor(rs.getInt("pln_propuesto_por"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;

    }

    @Override
    public List<Plan> cargarMisProyectos(Usuario usuario) throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT p.pln_id, p.pln_tema, p.pln_detalle, pln_objetivos, pln_justificacion, p.pln_propuesto_por \n"
                    + "FROM plan p, plan_usuario pu\n"
                    + "WHERE p.pln_id=pu.pln_id AND\n"
                    + "p.pln_aprobado='TRUE' AND\n"
                    + "pu.usr_id=?");

            st.setInt(1, usuario.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnDetalle(rs.getString("pln_detalle"));
                plan.setPlnObjetivos(rs.getString("pln_objetivos"));
                plan.setPlnJustificación(rs.getString("pln_justificacion"));
                plan.setPlnPropuestoPor(rs.getInt("pln_propuesto_por"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;

    }

    @Override
    public void listoRevision(Plan plan) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareStatement("UPDATE plan \n"
                    + "SET pln_listo = TRUE\n"
                    + "WHERE pln_id= ? ");
            st.setInt(1, plan.getPlnId());
            st.executeUpdate();
            st.close();
            SendMailGmail servicio = new SendMailGmail();
            servicio.enviarTemaARevisionMail(plan);
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }

    }

    @Override
    public void noListoRevision(Plan plan) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareStatement("UPDATE plan \n"
                    + "SET pln_listo = FALSE\n"
                    + "WHERE pln_id= ? ");
            st.setInt(1, plan.getPlnId());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }

    }

    @Override
    public List<Plan> cargarPlanesPorAprobar(Usuario user) throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT p.pln_id, p.pln_tema, p.pln_detalle, pln_objetivos, pln_justificacion, p.pln_propuesto_por \n"
                    + "FROM plan p, plan_usuario pu\n"
                    + "WHERE p.pln_id=pu.pln_id AND\n"
                    + "p.pln_aprobado='FALSE' AND\n"
                    + "p.pln_listo='TRUE' AND\n"
                    + "pu.usr_id=?");

            st.setInt(1, user.getUsrId());
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnDetalle(rs.getString("pln_detalle"));
                plan.setPlnObjetivos(rs.getString("pln_objetivos"));
                plan.setPlnJustificación(rs.getString("pln_justificacion"));
                plan.setPlnPropuestoPor(rs.getInt("pln_propuesto_por"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public void aprobarTema(Plan plan) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareStatement("UPDATE plan \n"
                    + "SET pln_fecha = ?, pln_listo = FALSE, pln_aprobado= TRUE, pln_observaciones='N/A'\n"
                    + "WHERE pln_id= ? ");
//            st.setDate(1, (Date) new java.util.Date());
            st.setDate(1, FechaUtilToSql.fechaUtilToSql(new java.util.Date()));
            st.setInt(2, plan.getPlnId());
            st.executeUpdate();
            st.close();
            SendMailGmail servicio = new SendMailGmail();
            servicio.enviarTemaAprobadoMail(plan);
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

    @Override
    public List<Plan> cargarPlanesPorRevisar() throws Exception {
        List<Plan> lista;
        ResultSet rs;
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareCall("SELECT pln_id, pln_tema, pln_detalle, pln_objetivos, pln_justificacion, pln_propuesto_por \n"
                    + "FROM plan p\n"
                    + "WHERE pln_listo='TRUE'");
            rs = st.executeQuery();
            lista = new ArrayList();
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setPlnId(rs.getInt("pln_id"));
                plan.setPlnTema(rs.getString("pln_tema"));
                plan.setPlnDetalle(rs.getString("pln_detalle"));
                plan.setPlnObjetivos(rs.getString("pln_objetivos"));
                plan.setPlnJustificación(rs.getString("pln_justificacion"));
                plan.setPlnPropuestoPor(rs.getInt("pln_propuesto_por"));
                lista.add(plan);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
        return lista;
    }

    @Override
    public void noAprobarTema(Plan plan) throws Exception {
        try {
            this.Conectar();
            PreparedStatement st = this.getCn().prepareStatement("UPDATE plan \n"
                    + "SET pln_listo = FALSE, pln_observaciones= ?\n"
                    + "WHERE pln_id= ? ");
            st.setString(1, plan.getPlnObservaciones());
            st.setInt(2, plan.getPlnId());
            st.executeUpdate();
            st.close();
            SendMailGmail servicio = new SendMailGmail();
            servicio.enviarTemaNoAprobadoMail(plan);
        } catch (Exception e) {
            throw e;
        } finally {
            this.Cerrar();
        }
    }

}
