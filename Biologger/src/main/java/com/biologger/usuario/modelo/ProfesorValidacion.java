/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "profesor_validacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"numero_trabajador"})
    , @UniqueConstraint(columnNames = {"idusuario"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProfesorValidacion.findAll", query = "SELECT p FROM ProfesorValidacion p")
    , @NamedQuery(name = "ProfesorValidacion.findByIdProfesorValidacion", query = "SELECT p FROM ProfesorValidacion p WHERE p.idProfesorValidacion = :idProfesorValidacion")
    , @NamedQuery(name = "ProfesorValidacion.findByNumeroTrabajador", query = "SELECT p FROM ProfesorValidacion p WHERE p.numeroTrabajador = :numeroTrabajador")})
public class ProfesorValidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_profesor_validacion", nullable = false)
    private Integer idProfesorValidacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numero_trabajador", nullable = false, length = 50)
    private String numeroTrabajador;
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @OneToOne
    private Usuario idusuario;

    public ProfesorValidacion() {
    }

    public ProfesorValidacion(Integer idProfesorValidacion) {
        this.idProfesorValidacion = idProfesorValidacion;
    }

    public ProfesorValidacion(Integer idProfesorValidacion, String numeroTrabajador) {
        this.idProfesorValidacion = idProfesorValidacion;
        this.numeroTrabajador = numeroTrabajador;
    }

    public Integer getIdProfesorValidacion() {
        return idProfesorValidacion;
    }

    public void setIdProfesorValidacion(Integer idProfesorValidacion) {
        this.idProfesorValidacion = idProfesorValidacion;
    }

    public String getNumeroTrabajador() {
        return numeroTrabajador;
    }

    public void setNumeroTrabajador(String numeroTrabajador) {
        this.numeroTrabajador = numeroTrabajador;
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
        hash += (idProfesorValidacion != null ? idProfesorValidacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfesorValidacion)) {
            return false;
        }
        ProfesorValidacion other = (ProfesorValidacion) object;
        if ((this.idProfesorValidacion == null && other.idProfesorValidacion != null) || (this.idProfesorValidacion != null && !this.idProfesorValidacion.equals(other.idProfesorValidacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.biologger.usuario.modelo.ProfesorValidacion[ idProfesorValidacion=" + idProfesorValidacion + " ]";
    }
    
}
