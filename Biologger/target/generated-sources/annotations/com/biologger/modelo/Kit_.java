package com.biologger.modelo;

import com.biologger.usuario.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.3.v20180807-rNA", date="2018-10-22T06:02:42")
@StaticMetamodel(Kit.class)
public class Kit_ { 

    public static volatile SingularAttribute<Kit, Serializable> materiales;
    public static volatile SingularAttribute<Kit, Date> fechavencimiento;
    public static volatile SingularAttribute<Kit, Integer> idkit;
    public static volatile SingularAttribute<Kit, Usuario> idusuario;

}