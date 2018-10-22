/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.modelo;

import com.biologger.usuario.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "kit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kit.findAll", query = "SELECT k FROM Kit k")
    , @NamedQuery(name = "Kit.findByIdkit", query = "SELECT k FROM Kit k WHERE k.idkit = :idkit")
    , @NamedQuery(name = "Kit.findByMateriales", query = "SELECT k FROM Kit k WHERE k.materiales = :materiales")
    , @NamedQuery(name = "Kit.findByFechavencimiento", query = "SELECT k FROM Kit k WHERE k.fechavencimiento = :fechavencimiento")})
public class Kit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idkit", nullable = false)
    private Integer idkit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "materiales", nullable = false)
    private Serializable materiales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechavencimiento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavencimiento;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario idusuario;

    public Kit() {
    }

    public Kit(Integer idkit) {
        this.idkit = idkit;
    }

    public Kit(Integer idkit, Serializable materiales, Date fechavencimiento) {
        this.idkit = idkit;
        this.materiales = materiales;
        this.fechavencimiento = fechavencimiento;
    }

    public Integer getIdkit() {
        return idkit;
    }

    public void setIdkit(Integer idkit) {
        this.idkit = idkit;
    }

    public Serializable getMateriales() {
        return materiales;
    }

    public void setMateriales(Serializable materiales) {
        this.materiales = materiales;
    }

    public Date getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(Date fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public Usuario getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Usuario idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idkit != null ? idkit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kit)) {
            return false;
        }
        Kit other = (Kit) object;
        if ((this.idkit == null && other.idkit != null) || (this.idkit != null && !this.idkit.equals(other.idkit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.biologger.modelo.Kit[ idkit=" + idkit + " ]";
    }
    
}
