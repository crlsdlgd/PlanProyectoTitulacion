package ec.edu.uce.planTitulacion.ejb.dto;

public class PlanUsuario {
    private Plan plnId;
    private Usuario usrId;
    private boolean plusPostulado;

    public Plan getPlnId() {
        return plnId;
    }

    public void setPlnId(Plan plnId) {
        this.plnId = plnId;
    }

    public Usuario getUsrId() {
        return usrId;
    }

    public void setUsrId(Usuario usrId) {
        this.usrId = usrId;
    }

    public boolean isPlusPostulado() {
        return plusPostulado;
    }

    public void setPlusPostulado(boolean plusPostulado) {
        this.plusPostulado = plusPostulado;
    }
    
    
}
