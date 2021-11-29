package ec.edu.uce.planTitulacion.ejb.dto;

import java.util.List;

public class Ubicacion {
    private Integer ubcId;
    private String ubcDescripcion;
    private Integer ubcJerarquia;
    private String ubcGentilicio;
    private List<Persona> ubcPersona;

    public String getUbcGentilicio() {
        return ubcGentilicio;
    }

    public void setUbcGentilicio(String ubcGentilicio) {
        this.ubcGentilicio = ubcGentilicio;
    }
    
    public List<Persona> getUbcPersona() {
        return ubcPersona;
    }

    public void setUbcPersona(List<Persona> ubcPersona) {
        this.ubcPersona = ubcPersona;
    }
    public Integer getUbcId() {
        return ubcId;
    }

    public void setUbcId(Integer ubcId) {
        this.ubcId = ubcId;
    }

    public String getUbcDescripcion() {
        return ubcDescripcion;
    }

    public void setUbcDescripcion(String ubcDescripcion) {
        this.ubcDescripcion = ubcDescripcion;
    }

    public Integer getUbcJerarquia() {
        return ubcJerarquia;
    }

    public void setUbcJerarquia(Integer ubcJerarquia) {
        this.ubcJerarquia = ubcJerarquia;
    }
    
}
