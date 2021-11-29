package ec.edu.uce.planTitulacion.ejb.dto;

public class Rol {
    private Integer rolId;
    private String rolDescripcion;
    private Integer rolTipo;

    public Integer getRolTipo() {
        return rolTipo;
    }

    public void setRolTipo(Integer rolTipo) {
        this.rolTipo = rolTipo;
    }
    
    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer idRol) {
        this.rolId = idRol;
    }

    public String getRolDescripcion() {
        return rolDescripcion;
    }

    public void setRoldescripcion(String rolDescripcion) {
        this.rolDescripcion = rolDescripcion;
    }
}
