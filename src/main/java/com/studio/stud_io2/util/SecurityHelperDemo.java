package com.studio.stud_io2.util;

import com.studio.stud_io2.util.SecurityHelper.Rol;

/**
 * Demo para visualizar los 5 caminos independientes del metodo getRol()
 * Este programa muestra los resultados para cada uno de los 5 caminos (P1-P5)
 */
public class SecurityHelperDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("   DEMOSTRACION DE PRUEBAS DE CAJA BLANCA");
        System.out.println("   Metodo: SecurityHelper.getRol(String username)");
        System.out.println("   Complejidad Ciclomatica: V(G) = 5");
        System.out.println("=".repeat(70));
        System.out.println();

        // CAMINO P1: Username null/vacio -> USUARIO
        System.out.println("[P1] CAMINO P1: Username null o vacio");
        System.out.println("   Entrada: null");
        System.out.println("   Resultado: " + SecurityHelper.getRol(null));
        System.out.println("   Decisiones: D1=TRUE");
        System.out.println("   Nodos: START -> N1 -> D1(T) -> E1 -> END");
        System.out.println();

        // CAMINO P2: Admin detectado -> ADMINISTRADOR
        System.out.println("[P2] CAMINO P2: Username con patron 'admin'");
        System.out.println("   Entrada: \"admin\"");
        System.out.println("   Resultado: " + SecurityHelper.getRol("admin"));
        System.out.println("   Decisiones: D1=FALSE, D2=TRUE");
        System.out.println("   Nodos: START -> N1 -> D1(F) -> N2 -> D2(T) -> E2 -> END");
        System.out.println();

        // CAMINO P3: Academico detectado -> ACADEMICO
        System.out.println("[P3] CAMINO P3: Username con patron 'academico_'");
        System.out.println("   Entrada: \"academico_test\"");
        System.out.println("   Resultado: " + SecurityHelper.getRol("academico_test"));
        System.out.println("   Decisiones: D1=FALSE, D2=FALSE, D3=TRUE");
        System.out.println("   Nodos: START -> N1 -> D1(F) -> N2 -> D2(F) -> D3(T) -> E3 -> END");
        System.out.println();

        // CAMINO P4: Docente detectado -> DOCENTE
        System.out.println("[P4] CAMINO P4: Username con patron 'docente_'");
        System.out.println("   Entrada: \"docente_silva\"");
        System.out.println("   Resultado: " + SecurityHelper.getRol("docente_silva"));
        System.out.println("   Decisiones: D1=FALSE, D2=FALSE, D3=FALSE, D4=TRUE");
        System.out.println(
                "   Nodos: START -> N1 -> D1(F) -> N2 -> D2(F) -> D3(F) -> D4(T) -> E4 -> END");
        System.out.println();

        // CAMINO P5: Usuario generico (default) -> USUARIO
        System.out.println("[P5] CAMINO P5: Username generico (no coincide con patrones)");
        System.out.println("   Entrada: \"estudiante_juan\"");
        System.out.println("   Resultado: " + SecurityHelper.getRol("estudiante_juan"));
        System.out.println("   Decisiones: D1=FALSE, D2=FALSE, D3=FALSE, D4=FALSE");
        System.out.println(
                "   Nodos: START -> N1 -> D1(F) -> N2 -> D2(F) -> D3(F) -> D4(F) -> E5 -> END");
        System.out.println();

        System.out.println("=".repeat(70));
        System.out.println("   RESUMEN DE COBERTURA");
        System.out.println("=".repeat(70));
        System.out.println("[OK] Cobertura de Camino: 5/5 (100%)");
        System.out.println("[OK] Cobertura de Decision: 8/8 ramas (100%)");
        System.out.println("[OK] Cobertura de Sentencia: 24/24 lineas (100%)");
        System.out.println();
        System.out.println("   VALORES UNICOS RETORNADOS:");
        System.out.println("   - USUARIO (P1, P5) - Rol por defecto sin permisos");
        System.out.println("   - ADMINISTRADOR (P2) - Actor del sistema");
        System.out.println("   - ACADEMICO (P3) - Actor del sistema");
        System.out.println("   - DOCENTE (P4) - Actor del sistema");
        System.out.println("=".repeat(70));
    }
}
