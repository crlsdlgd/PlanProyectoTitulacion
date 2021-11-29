
package ec.edu.uce.planTitulacion.ejb.dto;

import java.util.List;

public class Etnia {
    private Integer etnId;
    private String etnCodSniese;
    private String etnDescripcion;
    private List<Persona> etnPersonas;

    public List<Persona> getEtnPersonas() {
        return etnPersonas;
    }

    public void setEtnPersonas(List<Persona> etnPersonas) {
        this.etnPersonas = etnPersonas;
    }
    
    
    public Integer getEtnId() {
        return etnId;
    }

    public void setEtnId(Integer etnId) {
        this.etnId = etnId;
    }

    public String getEtnCodSniese() {
        return etnCodSniese;
    }

    public void setEtnCodSniese(String etnCodSniese) {
        this.etnCodSniese = etnCodSniese;
    }

    public String getEtnDescripcion() {
        return etnDescripcion;
    }

    public void setEtnDescripcion(String etnDescripcion) {
        this.etnDescripcion = etnDescripcion;
    }
}
