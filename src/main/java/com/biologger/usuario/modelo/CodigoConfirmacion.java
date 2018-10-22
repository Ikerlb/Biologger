/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "codigo_confirmacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idusuario"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CodigoConfirmacion.findAll", query = "SELECT c FROM CodigoConfirmacion c")
    , @NamedQuery(name = "CodigoConfirmacion.findByCodigo", query = "SELECT c FROM CodigoConfirmacion c WHERE c.codigo = :codigo")
    , @NamedQuery(name = "CodigoConfirmacion.findByFechaCreacion", query = "SELECT c FROM CodigoConfirmacion c WHERE c.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "CodigoConfirmacion.findByIdconfirmacion", query = "SELECT c FROM CodigoConfirmacion c WHERE c.idconfirmacion = :idconfirmacion")})
public class CodigoConfirmacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "codigo", nullable = false, length = 2147483647)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idconfirmacion", nullable = false)
    private Integer idconfirmacion;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @OneToOne
    private Usuario idusuario;

    public CodigoConfirmacion() {
    }

    public CodigoConfirmacion(Integer idconfirmacion) {
        this.idconfirmacion = idconfirmacion;
    }

    public CodigoConfirmacion(Integer idconfirmacion, String codigo, Date fechaCreacion) {
        this.idconfirmacion = idconfirmacion;
        this.codigo = codigo;
        this.fechaCreacion = fechaCreacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdconfirmacion() {
        return idconfirmacion;
    }

    public void setIdconfirmacion(Integer idconfirmacion) {
        this.idconfirmacion = idconfirmacion;
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
        hash += (idconfirmacion != null ? idconfirmacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CodigoConfirmacion)) {
            return false;
        }
        CodigoConfirmacion other = (CodigoConfirmacion) object;
        if ((this.idconfirmacion == null && other.idconfirmacion != null) || (this.idconfirmacion != null && !this.idconfirmacion.equals(other.idconfirmacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.biologger.usuario.modelo.CodigoConfirmacion[ idconfirmacion=" + idconfirmacion + " ]";
    }
    
}
