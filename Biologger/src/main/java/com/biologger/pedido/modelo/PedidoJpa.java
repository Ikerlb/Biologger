/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.pedido.modelo;

import com.biologger.modelo.jpa.PedidoJpaController;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alex aldaco
 */
public class PedidoJpa extends PedidoJpaController {

    public PedidoJpa(EntityManagerFactory emf) {
        super(emf);
    }

}
