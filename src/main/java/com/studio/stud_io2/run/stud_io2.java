package com.studio.stud_io2.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicacion.
 *
 * Con OpenXava Studio/Eclipse: Boton derecho del raton > Run As > Java Application
 */

public class stud_io2 {

	public static void main(String[] args) throws Exception {
		DBServer.start("stud_io2-db"); // Para usar tu propia base de datos comenta esta linea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("stud_io2"); // Usa AppServer.run("") para funcionar en el contexto raiz
	}

}
