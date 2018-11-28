/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.pedido.modelo;

import com.biologger.modelo.Pedido;
import com.biologger.modelo.jpa.PedidoJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author alex aldaco
 */
public class PedidoJpa extends PedidoJpaController {

    public PedidoJpa(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Pedido> filtrarPedidos() {
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT p FROM Pedido p";
            Query q = em.createQuery(query);
            return q.getResultList();
        } finally {
            em.close();
        }
        
    }
}
