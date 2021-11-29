package ec.edu.uce.planTitulacion.ejb.dto;

public class UsuarioRol {
    private Rol rolId;
    private Usuario usrId;
    private Integer usroEstado;

    public Integer getUsroEstado() {
        return usroEstado;
    }

    public void setUsroEstado(Integer usroEstado) {
        this.usroEstado = usroEstado;
    }
    
    public Rol getRol() {
        return rolId;
    }

    public void setRol(Rol rol) {
        this.rolId = rol;
    }

    public Usuario getUsuario() {
        return usrId;
    }

    public void setUsuario(Usuario usuario) {
        this.usrId = usuario;
    }
    
}
