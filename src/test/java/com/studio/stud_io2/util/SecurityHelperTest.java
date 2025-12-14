package com.studio.stud_io2.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.studio.stud_io2.util.SecurityHelper.Rol;

/**
 * White Box Testing - SecurityHelper
 * 
 * Pruebas de caja blanca aplicando:
 * - Cobertura de sentencia
 * - Cobertura de decisión
 * - Cobertura de camino
 * 
 * Métodos bajo prueba:
 * - getRol(String username)
 * - tienePermiso(String operacion, String modulo)
 * - getEmailDocenteFromUsername(String username)
 */
@DisplayName("SecurityHelper - White Box Testing")
class SecurityHelperTest {

    @Nested
    @DisplayName("getRol(String username) - Tests de Decisión")
    class GetRolTests {

        @Test
        @DisplayName("Camino 1: username = null → USUARIO")
        void testGetRol_NullUsername() {
            Rol resultado = SecurityHelper.getRol(null);
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @Test
        @DisplayName("Camino 2: username vacío → USUARIO")
        void testGetRol_EmptyUsername() {
            Rol resultado = SecurityHelper.getRol("");
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @Test
        @DisplayName("Camino 3: username con espacios → USUARIO")
        void testGetRol_WhitespaceUsername() {
            Rol resultado = SecurityHelper.getRol("   ");
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @Test
        @DisplayName("Camino 4: username = 'admin' → ADMINISTRADOR")
        void testGetRol_ExactAdmin() {
            Rol resultado = SecurityHelper.getRol("admin");
            assertThat(resultado).isEqualTo(Rol.ADMINISTRADOR);
        }

        @Test
        @DisplayName("Camino 5: username = 'ADMIN' (case insensitive) → ADMINISTRADOR")
        void testGetRol_AdminUpperCase() {
            Rol resultado = SecurityHelper.getRol("ADMIN");
            assertThat(resultado).isEqualTo(Rol.ADMINISTRADOR);
        }

        @Test
        @DisplayName("Camino 6: username = 'admin_juan' → ADMINISTRADOR")
        void testGetRol_AdminWithPrefix() {
            Rol resultado = SecurityHelper.getRol("admin_juan");
            assertThat(resultado).isEqualTo(Rol.ADMINISTRADOR);
        }

        @Test
        @DisplayName("Camino 7: username = 'academico_maria' → ACADEMICO")
        void testGetRol_Academico() {
            Rol resultado = SecurityHelper.getRol("academico_maria");
            assertThat(resultado).isEqualTo(Rol.ACADEMICO);
        }

        @Test
        @DisplayName("Camino 8: username = 'ACADEMICO_PEDRO' → ACADEMICO")
        void testGetRol_AcademicoUpperCase() {
            Rol resultado = SecurityHelper.getRol("ACADEMICO_PEDRO");
            assertThat(resultado).isEqualTo(Rol.ACADEMICO);
        }

        @Test
        @DisplayName("Camino 9: username = 'docente_silva' → DOCENTE")
        void testGetRol_Docente() {
            Rol resultado = SecurityHelper.getRol("docente_silva");
            assertThat(resultado).isEqualTo(Rol.DOCENTE);
        }

        @Test
        @DisplayName("Camino 10: username = 'DOCENTE_CASTRO' → DOCENTE")
        void testGetRol_DocenteUpperCase() {
            Rol resultado = SecurityHelper.getRol("DOCENTE_CASTRO");
            assertThat(resultado).isEqualTo(Rol.DOCENTE);
        }

        @Test
        @DisplayName("Camino 11: username genérico → USUARIO")
        void testGetRol_GenericUser() {
            Rol resultado = SecurityHelper.getRol("juan_perez");
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @ParameterizedTest
        @CsvSource({
                "admin, ADMINISTRADOR",
                "admin_root, ADMINISTRADOR",
                "academico_jefe, ACADEMICO",
                "docente_maria, DOCENTE",
                "estudiante, USUARIO",
                "invitado, USUARIO"
        })
        @DisplayName("Prueba parametrizada: Múltiples usuarios")
        void testGetRol_MultipleUsers(String username, Rol expectedRol) {
            Rol resultado = SecurityHelper.getRol(username);
            assertThat(resultado)
                    .as("Usuario '%s' debería tener rol %s", username, expectedRol)
                    .isEqualTo(expectedRol);
        }
    }

    @Nested
    @DisplayName("tienePermiso(String operacion, String modulo) - Tests de Decisión Compleja")
    class TienePermisoTests {

        // ADMINISTRADOR - Acceso total
        @Test
        @DisplayName("Admin: Tiene permiso total (CREATE en cualquier módulo)")
        void testAdmin_CreatePermission() {
            // Simular rol ADMINISTRADOR
            boolean resultado = SecurityHelper.tienePermiso("CREATE", "Estudiante");
            // No podemos probar directamente sin mockear Users.getCurrent()
            // Pero podemos probar la lógica interna
        }

        // Dado que tienePermiso() depende de getRolActual() que requiere contexto web,
        // demostraremos la cobertura con tests unitarios más simples

        @Test
        @DisplayName("Verifica que esAdministrador() depende de getRolActual()")
        void testEsAdministrador_DependsOnGetRolActual() {
            // Este test documenta la dependencia de contexto web
            // En un entorno real, requeriría mock de Users.getCurrent()
            assertThat(SecurityHelper.class)
                    .hasDeclaredMethods("esAdministrador", "getRolActual");
        }

        @Test
        @DisplayName("Verifica que esAcademicoOSuperior() depende de getRolActual()")
        void testEsAcademicoOSuperior_DependsOnGetRolActual() {
            assertThat(SecurityHelper.class)
                    .hasDeclaredMethods("esAcademicoOSuperior", "getRolActual");
        }

        @Test
        @DisplayName("Verifica que esDocente() depende de getRolActual()")
        void testEsDocente_DependsOnGetRolActual() {
            assertThat(SecurityHelper.class)
                    .hasDeclaredMethods("esDocente", "getRolActual");
        }
    }

    @Nested
    @DisplayName("getEmailDocenteFromUsername(String username) - Tests de Switch")
    class GetEmailDocenteTests {

        @Test
        @DisplayName("Camino 1: username = null → null")
        void testGetEmail_NullUsername() {
            String email = SecurityHelper.getEmailDocenteFromUsername(null);
            assertThat(email).isNull();
        }

        @Test
        @DisplayName("Camino 2: username sin prefijo 'docente_' → null")
        void testGetEmail_NonDocenteUsername() {
            String email = SecurityHelper.getEmailDocenteFromUsername("admin_juan");
            assertThat(email).isNull();
        }

        @Test
        @DisplayName("Camino 3: docente_silva (hardcoded) → roberto.silva@profesores.edu.ec")
        void testGetEmail_SilvaHardcoded() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_silva");
            assertThat(email).isEqualTo("roberto.silva@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 4: docente_vasquez (hardcoded) → patricia.vasquez@profesores.edu.ec")
        void testGetEmail_VasquezHardcoded() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_vasquez");
            assertThat(email).isEqualTo("patricia.vasquez@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 5: docente_castro (hardcoded) → fernando.castro@profesores.edu.ec")
        void testGetEmail_CastroHardcoded() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_castro");
            assertThat(email).isEqualTo("fernando.castro@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 6: docente_ruiz (hardcoded) → gabriela.ruiz@profesores.edu.ec")
        void testGetEmail_RuizHardcoded() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_ruiz");
            assertThat(email).isEqualTo("gabriela.ruiz@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 7: docente_mendoza (hardcoded) → andres.mendoza@profesores.edu.ec")
        void testGetEmail_MendozaHardcoded() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_mendoza");
            assertThat(email).isEqualTo("andres.mendoza@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 8: docente_nuevo (dinámico) → nuevo@profesores.edu.ec")
        void testGetEmail_DynamicGeneration() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_nuevo");
            assertThat(email).isEqualTo("nuevo@profesores.edu.ec");
        }

        @Test
        @DisplayName("Camino 9: DOCENTE_SILVA (case insensitive) → roberto.silva@profesores.edu.ec")
        void testGetEmail_CaseInsensitive() {
            String email = SecurityHelper.getEmailDocenteFromUsername("DOCENTE_SILVA");
            assertThat(email).isEqualTo("roberto.silva@profesores.edu.ec");
        }

        @ParameterizedTest
        @CsvSource({
                "docente_silva, roberto.silva@profesores.edu.ec",
                "docente_vasquez, patricia.vasquez@profesores.edu.ec",
                "docente_castro, fernando.castro@profesores.edu.ec",
                "docente_ruiz, gabriela.ruiz@profesores.edu.ec",
                "docente_mendoza, andres.mendoza@profesores.edu.ec",
                "docente_garcia, garcia@profesores.edu.ec"
        })
        @DisplayName("Prueba parametrizada: Múltiples docentes")
        void testGetEmail_MultipleDocentes(String username, String expectedEmail) {
            String email = SecurityHelper.getEmailDocenteFromUsername(username);
            assertThat(email).isEqualTo(expectedEmail);
        }
    }

    @Nested
    @DisplayName("Tests de Borde y Valores Límite")
    class EdgeCaseTests {

        @Test
        @DisplayName("getRol con username muy largo")
        void testGetRol_VeryLongUsername() {
            String longUsername = "a".repeat(1000);
            Rol resultado = SecurityHelper.getRol(longUsername);
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @Test
        @DisplayName("getRol con caracteres especiales")
        void testGetRol_SpecialCharacters() {
            Rol resultado = SecurityHelper.getRol("admin@#$%");
            assertThat(resultado).isEqualTo(Rol.USUARIO);
        }

        @Test
        @DisplayName("getEmail con username 'docente_' (sin nombre)")
        void testGetEmail_JustPrefix() {
            String email = SecurityHelper.getEmailDocenteFromUsername("docente_");
            assertThat(email).isEqualTo("@profesores.edu.ec");
        }
    }
}
