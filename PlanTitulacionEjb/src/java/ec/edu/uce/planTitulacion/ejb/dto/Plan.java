package ec.edu.uce.planTitulacion.ejb.dto;

import java.util.Date;

public class Plan {
    private Integer plnId;
    private Integer plnPropuestoPor;
    private String plnTema;
    private String plnDetalle;
    private String plnObjetivos;
    private String plnJustificación;
    private String plnObservaciones;
    private Date plnFecha;
    private boolean plnAprobado;
    private String plnEstado;

    
    public String getPlnEstado() {
        return plnEstado;
    }

    public void setPlnEstado(String plnEstado) {
        this.plnEstado = plnEstado;
    }
    
    public String getPlnObservaciones() {
        return plnObservaciones;
    }

    public void setPlnObservaciones(String plnObservaciones) {
        this.plnObservaciones = plnObservaciones;
    }
    

    public Integer getPlnPropuestoPor() {
        return plnPropuestoPor;
    }

    public void setPlnPropuestoPor(Integer plnPropuestoPor) {
        this.plnPropuestoPor = plnPropuestoPor;
    }

    public String getPlnObjetivos() {
        return plnObjetivos;
    }

    public void setPlnObjetivos(String plnObjetivos) {
        this.plnObjetivos = plnObjetivos;
    }

    public String getPlnJustificación() {
        return plnJustificación;
    }

    public void setPlnJustificación(String plnJustificación) {
        this.plnJustificación = plnJustificación;
    }
    
    public String getPlnDetalle() {
        return plnDetalle;
    }

    public void setPlnDetalle(String plnDetalle) {
        this.plnDetalle = plnDetalle;
    }

    public boolean isPlnAprobado() {
        return plnAprobado;
    }

    public void setAprobado(boolean plnAprobado) {
        this.plnAprobado = plnAprobado;
    }

    public Integer getPlnId() {
        return plnId;
    }

    public void setPlnId(Integer plnId) {
        this.plnId = plnId;
    }

    public String getPlnTema() {
        return plnTema;
    }

    public void setPlnTema(String plnTema) {
        this.plnTema = plnTema;
    }

    public Date getPlnFecha() {
        return plnFecha;
    }

    public void setPlnFecha(Date plnFecha) {
        this.plnFecha = plnFecha;
    }
}
