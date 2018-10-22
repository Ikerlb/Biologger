package com.biologger.usuario.modelo;

import com.biologger.modelo.Kit;
import com.biologger.usuario.modelo.CodigoConfirmacion;
import com.biologger.usuario.modelo.ProfesorValidacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.3.v20180807-rNA", date="2018-10-22T06:02:42")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Date> ultimaActualizacion;
    public static volatile SingularAttribute<Usuario, Date> fechaRegistro;
    public static volatile SingularAttribute<Usuario, CodigoConfirmacion> codigoConfirmacion;
    public static volatile SingularAttribute<Usuario, Date> ultimoAcceso;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile SingularAttribute<Usuario, Integer> idusuario;
    public static volatile SingularAttribute<Usuario, String> nombreusuario;
    public static volatile SingularAttribute<Usuario, Integer> rolid;
    public static volatile SingularAttribute<Usuario, String> foto;
    public static volatile SingularAttribute<Usuario, String> correo;
    public static volatile SingularAttribute<Usuario, String> contrasena;
    public static volatile ListAttribute<Usuario, Kit> kitList;
    public static volatile SingularAttribute<Usuario, ProfesorValidacion> profesorValidacion;
    public static volatile SingularAttribute<Usuario, Boolean> activo;

}