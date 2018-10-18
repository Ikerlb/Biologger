## Biologger < Branch Alex Aldaco >

#### Ingenieria de Software


------------

- Chávez Muñoa Ian
- Eduardo Cortez Flores Luis Enrique
- Lissarrague Berumen Iker Ortega
- Martinez Jesus Martin
- Vargas Aldaco Alejandro Emmanuel


------------

**Intrucciones
**

1. Para descargar el branch
`git clone -b alex --single-branch https://github.com/Ikerlb/Biologger.git`

2. Crear la base de datos en postgresql en Mac o Linux (Busquen la versión alternativa para estos comandos en Windows)
`sudo -u postgres -i`
`createdb Biologger`
`cd /path/to/this/proyect`
`psql -b Biologger -f Biologger.sql`

3. Configurar el archivo persistence.xml
`nano src/main/resources/META-INF/persistence.xml`
Hay que cambiar el password y el user, según como tengas configurado postgresql, en estas dos líneas:
`<property name="javax.persistence.jdbc.user" value="postgres"/>`
`<property name="javax.persistence.jdbc.password" value="ElPassword!"/>`

4. Para el envio de correos debes ingresar una cuenta de correo con una conexión smtp y lo puedes configurar en el archivo `src/main/webapp/WEB-INF/classes/correo.properties.`
Si usas una cuenta de gmail (o @ciencias.unam.mx), asegurate que permita el envio de correos desde aplicaciones menos seguras -- Busca en google cómo se hace --

	*Ejemplo:*

    	correo = alex.aldaco@khronostelecom.mx
    	remitente = Alex Aldaco
    	usuario = alex.aldaco
    	contrasena = password
    	host = mail.khronostelecom.mx
    	puerto = 587

5. Ya que tengas todo configurado limpias la aplicacions y haces un deploy
`mvn clean install`

6. Luego corre la aplicación con el siguiente comando
`mvn tomcat7:run`

7. Para visializar el index de la aplicación entra por la siguiente ruta en tu navegador
`http://localhost:8080/Biologger/faces/index.xhtml`

8. Puedes acceder a las siguientes url del proyecto:

    	http://localhost:8080/Biologger/faces/usuario/iniciar-sesion.xhtml
    	http://localhost:8080/Biologger/faces/usuario/registro.xhtml
    	http://localhost:8080/Biologger/faces/usuario/confirmar-correo.xhtml
    	http://localhost:8080/Biologger/faces/usuario/restaurar-cuenta.xhtml
