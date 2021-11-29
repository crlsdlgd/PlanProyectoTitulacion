package ec.edu.uce.planTitulacion.ejb.jdbc.impl;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

    static final String HOST="ec2-3-231-48-230.compute-1.amazonaws.com";
    static final String DATABASE="d59aqmr6t3kb3h";
    static final String USER="ilbnjnzlsefowm";
    static final String PORT="5432";
    static final String PASSWORD="df9ee98f613b2bb065791cb8a76ac85afdb7cdef329cc525f6c0fbc277a6181a";
    
    private Connection cn;

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        this.cn = cn;
    }

    public void Conectar() throws Exception {

        try {
            Class.forName("org.postgresql.Driver");
            cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/titulacion?user=postgres&password=Pass_1992");
            //cn = DriverManager.getConnection("jdbc:postgresql://"+HOST+":"+PORT+"/"+DATABASE+"?ssl=true&sslmode=require",USER,PASSWORD);
        } catch (Exception e) {
            throw e;
        }
    }

    public void Cerrar() throws Exception {

        try {
            if (cn != null) {
                if (cn.isClosed() == false) {
                    cn.close();
                }
            }
        } catch (Exception e) {
        throw e;
        }

    }
}
