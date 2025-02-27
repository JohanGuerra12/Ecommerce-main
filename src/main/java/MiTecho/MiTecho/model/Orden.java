package MiTecho.MiTecho.model;

import java.util.List;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="ordenes")
public class Orden {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String numero;
    private Date FechaCreacion;
    private Date FechaRecibido;
    private double total;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy="orden")
    private List<DetalleOrden> detalles;

    public Orden() {
    }

    public Orden(Integer id, String numero, Date fechaCreacion, Date fechaRecibido, double total, Usuario usuario) {
        this.id = id;
        this.numero = numero;
        this.FechaCreacion = fechaCreacion;
        this.FechaRecibido = fechaRecibido;
        this.total = total;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.FechaCreacion = fechaCreacion;
    }

    public Date getFechaRecibido() {
        return FechaRecibido;
    }

    public void setFechaRecibido(Date fechaRecibido) {
        this.FechaRecibido = fechaRecibido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleOrden> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Orden [id=" + id + ", numero=" + numero + ", FechaCreacion=" + FechaCreacion + ", FechaRecibido="
                + FechaRecibido + ", total=" + total + "]";
    }	
}
