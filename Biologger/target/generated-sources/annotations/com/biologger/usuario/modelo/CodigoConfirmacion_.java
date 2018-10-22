package com.biologger.usuario.modelo;

import com.biologger.usuario.modelo.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.3.v20180807-rNA", date="2018-10-22T06:02:42")
@StaticMetamodel(CodigoConfirmacion.class)
public class CodigoConfirmacion_ { 

    public static volatile SingularAttribute<CodigoConfirmacion, String> codigo;
    public static volatile SingularAttribute<CodigoConfirmacion, Date> fechaCreacion;
    public static volatile SingularAttribute<CodigoConfirmacion, Integer> idconfirmacion;
    public static volatile SingularAttribute<CodigoConfirmacion, Usuario> idusuario;

}