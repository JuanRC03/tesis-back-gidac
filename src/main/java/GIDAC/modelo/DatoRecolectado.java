package GIDAC.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
@Table(name = "dato_recolectado")
@Entity
public class DatoRecolectado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDatoRecolectado;
    
    private float valor;
    private boolean vigencia=true;
    private boolean editable=true;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    
    @OneToMany(mappedBy = "datoRecolectado",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SolicitudActualizarDato> solicitudActualizarDato = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_dataset")
    private Dataset dataset;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_variable")
    private Variable variable;

    public Integer getIdDatoRecolectado() {
        return idDatoRecolectado;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Variable getVariable() {
        return variable;
    }
    
     public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public float getValor() {
        return valor;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setIdDatoRecolectado(Integer idDatoRecolectado) {
        this.idDatoRecolectado = idDatoRecolectado;
    }

    

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

   

    public void setValor(float valor) {
        this.valor = valor;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    public Set<SolicitudActualizarDato> getSolicitudActualizarDato() {
        return solicitudActualizarDato;
    }

    public void setSolicitudActualizarDato(Set<SolicitudActualizarDato> solicitudActualizarDato) {
        this.solicitudActualizarDato = solicitudActualizarDato;
    }

    public boolean isEditable() {
        return editable;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    
    
    
    
}