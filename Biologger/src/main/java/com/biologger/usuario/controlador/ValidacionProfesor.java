/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.usuario.modelo.ProfesorValidacion;
import com.biologger.usuario.modelo.ProfesorValidacionJpa;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alexa
 */
public class ValidacionProfesor {
    
    public static ProfesorValidacion generar(String numeroTrabajador) {
        EntityManagerFactory emf = UtilidadDePersistencia.getEntityManagerFactory();
        ProfesorValidacion validacion = new ProfesorValidacion();
        ProfesorValidacionJpa validacionJPA = new ProfesorValidacionJpa(emf);
        validacion.setNumeroTrabajador(numeroTrabajador);
        validacionJPA.create(validacion);
        return validacion;
    }
}
