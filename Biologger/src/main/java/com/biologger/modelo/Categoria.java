/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "categoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c")
    , @NamedQuery(name = "Categoria.findByIdcategoria", query = "SELECT c FROM Categoria c WHERE c.idcategoria = :idcategoria")
    , @NamedQuery(name = "Categoria.findByNombre", query = "SELECT c FROM Categoria c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Categoria.findBySubcategoria", query = "SELECT c FROM Categoria c WHERE c.subcategoria = :subcategoria")
    , @NamedQuery(name = "Categoria.findByIdcatprincipal", query = "SELECT c FROM Categoria c WHERE c.idcatprincipal = :idcatprincipal")})
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcategoria", nullable = false)
    private Integer idcategoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subcategoria", nullable = false)
    private boolean subcategoria;
    @Column(name = "idcatprincipal")
    private Integer idcatprincipal;

    public Categoria() {
    }

    public Categoria(Integer idcategoria) {
        this.idcategoria = idcategoria;
    }

    public Categoria(Integer idcategoria, String nombre, boolean subcategoria) {
        this.idcategoria = idcategoria;
        this.nombre = nombre;
        this.subcategoria = subcategoria;
    }

    public Integer getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(Integer idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(boolean subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Integer getIdcatprincipal() {
        return idcatprincipal;
    }

    public void setIdcatprincipal(Integer idcatprincipal) {
        this.idcatprincipal = idcatprincipal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcategoria != null ? idcategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.idcategoria == null && other.idcategoria != null) || (this.idcategoria != null && !this.idcategoria.equals(other.idcategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.biologger.modelo.Categoria[ idcategoria=" + idcategoria + " ]";
    }
    
}
