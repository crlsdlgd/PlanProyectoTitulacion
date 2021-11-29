package ec.edu.uce.planTitulacion.ejb.dto;

import java.util.Date;
import java.util.List;

public class Persona {
    private Integer prsId;
    private Ubicacion prsUbicacion;
    private Etnia prsEtnia;
    private Integer prsTipoIdentificacion;
    private String prsIdentificacion;
    private String prsPrimerApellido;
    private String prsSegundoApellido;
    private String prsNombres;
    private Integer prsSexo;
    private String prsMailPersonal;
    private String prsMailInstitucional;
    private String prsCelular;
    private String prsTelefono;
    private Date prsFechaNacimiento;
    private Integer prsDiscapacidad;
    private Integer prsPorcentajeDiscapacidad;
    private String prsCarnetConadis;

    public Integer getPrsTipoIdentificacion() {
        return prsTipoIdentificacion;
    }

    public void setPrsTipoIdentificacion(Integer prsTipoIdentificacion) {
        this.prsTipoIdentificacion = prsTipoIdentificacion;
    }

    
    
    public List<Usuario> getPrsUsuario() {
        return prsUsuario;
    }

    public void setPrsUsuario(List<Usuario> prsUsuario) {
        this.prsUsuario = prsUsuario;
    }
    private List<Usuario> prsUsuario;
    
    public Integer getPrsId() {
        return prsId;
    }

    public void setPrsId(Integer prsId) {
        this.prsId = prsId;
    }

    public Ubicacion getPrsUbicacion() {
        return prsUbicacion;
    }

    public void setPrsUbicacion(Ubicacion prsUbicacion) {
        this.prsUbicacion = prsUbicacion;
    }

    public Etnia getPrsEtnia() {
        return prsEtnia;
    }

    public void setPrsEtnia(Etnia prsEtnia) {
        this.prsEtnia = prsEtnia;
    }

    public String getPrsIdentificacion() {
        return prsIdentificacion;
    }

    public void setPrsIdentificacion(String prsIdentificacion) {
        this.prsIdentificacion = prsIdentificacion;
    }

    public String getPrsPrimerApellido() {
        return prsPrimerApellido;
    }

    public void setPrsPrimerApellido(String prsPrimerApellido) {
        this.prsPrimerApellido = prsPrimerApellido;
    }

    public String getPrsSegundoApellido() {
        return prsSegundoApellido;
    }

    public void setPrsSegundoApellido(String prsSegundoApellido) {
        this.prsSegundoApellido = prsSegundoApellido;
    }

    public String getPrsNombres() {
        return prsNombres;
    }

    public void setPrsNombres(String prsNombres) {
        this.prsNombres = prsNombres;
    }

    public Integer getPrsSexo() {
        return prsSexo;
    }

    public void setPrsSexo(Integer prsSexo) {
        this.prsSexo = prsSexo;
    }

    public String getPrsMailPersonal() {
        return prsMailPersonal;
    }

    public void setPrsMailPersonal(String prsMailPersonal) {
        this.prsMailPersonal = prsMailPersonal;
    }

    public String getPrsMailInstitucional() {
        return prsMailInstitucional;
    }

    public void setPrsMailInstitucional(String prsMailInstitucional) {
        this.prsMailInstitucional = prsMailInstitucional;
    }

    public String getPrsCelular() {
        return prsCelular;
    }

    public void setPrsCelular(String prsCelular) {
        this.prsCelular = prsCelular;
    }

    public String getPrsTelefono() {
        return prsTelefono;
    }

    public void setPrsTelefono(String prsTelefono) {
        this.prsTelefono = prsTelefono;
    }

    public Date getPrsFechaNacimiento() {
        return prsFechaNacimiento;
    }

    public void setPrsFechaNacimiento(Date prsFechaNacimiento) {
        this.prsFechaNacimiento = prsFechaNacimiento;
    }

    public Integer getPrsDiscapacidad() {
        return prsDiscapacidad;
    }

    public void setPrsDiscapacidad(Integer prsDiscapacidad) {
        this.prsDiscapacidad = prsDiscapacidad;
    }

    public Integer getPrsPorcentajeDiscapacidad() {
        return prsPorcentajeDiscapacidad;
    }

    public void setPrsPorcentajeDiscapacidad(Integer prsPorcentajeDiscapacidad) {
        this.prsPorcentajeDiscapacidad = prsPorcentajeDiscapacidad;
    }

    public String getPrsCarnetConadis() {
        return prsCarnetConadis;
    }

    public void setPrsCarnetConadis(String prsCarnetConadis) {
        this.prsCarnetConadis = prsCarnetConadis;
    }
    
    
}
