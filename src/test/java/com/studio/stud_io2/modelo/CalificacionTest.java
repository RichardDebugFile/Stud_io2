package com.studio.stud_io2.modelo;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * White Box Testing - Calificacion
 * 
 * Pruebas de caja blanca aplicando:
 * - Cobertura de sentencia
 * - Cobertura de decisión
 * - Cobertura de camino
 * 
 * Método bajo prueba:
 * - determinarEstadoFinal(BigDecimal promedio)
 */
@DisplayName("Calificacion - White Box Testing")
class CalificacionTest {

    @Nested
    @DisplayName("determinarEstadoFinal(BigDecimal promedio) - Tests de Decisión Binaria")
    class DeterminarEstadoFinalTests {

        @Test
        @DisplayName("Camino 1: promedio = 70.00 (valor límite inferior APROBADO) → APROBADO")
        void testDeterminarEstado_ExactlySeventyAprobado() {
            BigDecimal promedio = new BigDecimal("70.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio exacto de 70.00 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }

        @Test
        @DisplayName("Camino 1: promedio = 90.00 (valor alto) → APROBADO")
        void testDeterminarEstado_HighGradeAprobado() {
            BigDecimal promedio = new BigDecimal("90.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 90.00 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }

        @Test
        @DisplayName("Camino 1: promedio = 100.00 (máximo) → APROBADO")
        void testDeterminarEstado_MaximumGradeAprobado() {
            BigDecimal promedio = new BigDecimal("100.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 100.00 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }

        @Test
        @DisplayName("Camino 1: promedio = 70.01 (justo encima del límite) → APROBADO")
        void testDeterminarEstado_JustAboveLimitAprobado() {
            BigDecimal promedio = new BigDecimal("70.01");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 70.01 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }

        @Test
        @DisplayName("Camino 2: promedio = 69.99 (valor límite superior REPROBADO) → REPROBADO")
        void testDeterminarEstado_JustBelowLimitReprobado() {
            BigDecimal promedio = new BigDecimal("69.99");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 69.99 debe ser REPROBADO")
                    .isEqualTo("REPROBADO");
        }

        @Test
        @DisplayName("Camino 2: promedio = 50.00 (valor medio bajo) → REPROBADO")
        void testDeterminarEstado_MidLowGradeReprobado() {
            BigDecimal promedio = new BigDecimal("50.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 50.00 debe ser REPROBADO")
                    .isEqualTo("REPROBADO");
        }

        @Test
        @DisplayName("Camino 2: promedio = 0.00 (mínimo) → REPROBADO")
        void testDeterminarEstado_ZeroGradeReprobado() {
            BigDecimal promedio = new BigDecimal("0.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 0.00 debe ser REPROBADO")
                    .isEqualTo("REPROBADO");
        }

        @Test
        @DisplayName("Camino 2: promedio = 1.00 (muy bajo) → REPROBADO")
        void testDeterminarEstado_VeryLowGradeReprobado() {
            BigDecimal promedio = new BigDecimal("1.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio de 1.00 debe ser REPROBADO")
                    .isEqualTo("REPROBADO");
        }

        @ParameterizedTest
        @CsvSource({
                "100.00, APROBADO",
                "95.50, APROBADO",
                "90.00, APROBADO",
                "85.25, APROBADO",
                "80.00, APROBADO",
                "75.75, APROBADO",
                "70.00, APROBADO",
                "70.01, APROBADO",
                "69.99, REPROBADO",
                "65.00, REPROBADO",
                "60.00, REPROBADO",
                "50.00, REPROBADO",
                "40.00, REPROBADO",
                "30.00, REPROBADO",
                "20.00, REPROBADO",
                "10.00, REPROBADO",
                "0.00, REPROBADO"
        })
        @DisplayName("Prueba parametrizada: Múltiples promedios")
        void testDeterminarEstado_MultipleGrades(BigDecimal promedio, String expectedEstado) {
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio %.2f debe ser %s", promedio, expectedEstado)
                    .isEqualTo(expectedEstado);
        }
    }

    @Nested
    @DisplayName("Tests de Borde y Valores Límite")
    class EdgeCaseTests {

        @Test
        @DisplayName("Promedio negativo (caso inválido pero funcional) → REPROBADO")
        void testDeterminarEstado_NegativeGrade() {
            BigDecimal promedio = new BigDecimal("-10.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio negativo debe ser REPROBADO")
                    .isEqualTo("REPROBADO");
        }

        @Test
        @DisplayName("Promedio muy alto (>100, caso inválido pero funcional) → APROBADO")
        void testDeterminarEstado_AboveMaximum() {
            BigDecimal promedio = new BigDecimal("150.00");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio mayor a 100 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }

        @Test
        @DisplayName("Promedio con muchos decimales (69.999999999) → REPROBADO")
        void testDeterminarEstado_ManyDecimals() {
            BigDecimal promedio = new BigDecimal("69.999999999");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio 69.999999999 debe ser REPROBADO (< 70.00)")
                    .isEqualTo("REPROBADO");
        }

        @Test
        @DisplayName("Promedio exacto con precisión alta (70.000000000) → APROBADO")
        void testDeterminarEstado_HighPrecisionExactly70() {
            BigDecimal promedio = new BigDecimal("70.000000000");
            String resultado = Calificacion.determinarEstadoFinal(promedio);
            assertThat(resultado)
                    .as("Promedio 70.000000000 debe ser APROBADO")
                    .isEqualTo("APROBADO");
        }
    }

    @Nested
    @DisplayName("Tests de Cobertura Completa de Caminos")
    class PathCoverageTests {

        @Test
        @DisplayName("Documenta cobertura de camino TRUE (promedio >= 70)")
        void testPath_True() {
            // Camino verdadero: compareTo >= 0
            BigDecimal[] promediosAprobados = {
                    new BigDecimal("70.00"),
                    new BigDecimal("75.00"),
                    new BigDecimal("80.00"),
                    new BigDecimal("85.00"),
                    new BigDecimal("90.00"),
                    new BigDecimal("95.00"),
                    new BigDecimal("100.00")
            };

            for (BigDecimal promedio : promediosAprobados) {
                String resultado = Calificacion.determinarEstadoFinal(promedio);
                assertThat(resultado)
                        .as("Camino TRUE: Promedio %.2f debe ser APROBADO", promedio)
                        .isEqualTo("APROBADO");
            }
        }

        @Test
        @DisplayName("Documenta cobertura de camino FALSE (promedio < 70)")
        void testPath_False() {
            // Camino falso: compareTo < 0
            BigDecimal[] promediosReprobados = {
                    new BigDecimal("0.00"),
                    new BigDecimal("10.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("30.00"),
                    new BigDecimal("40.00"),
                    new BigDecimal("50.00"),
                    new BigDecimal("60.00"),
                    new BigDecimal("69.99")
            };

            for (BigDecimal promedio : promediosReprobados) {
                String resultado = Calificacion.determinarEstadoFinal(promedio);
                assertThat(resultado)
                        .as("Camino FALSE: Promedio %.2f debe ser REPROBADO", promedio)
                        .isEqualTo("REPROBADO");
            }
        }

        @Test
        @DisplayName("Resumen: 100% cobertura de decisión (TRUE y FALSE)")
        void testCompleteCoverage() {
            // Este test documenta que ambos caminos están cubiertos
            assertThat(Calificacion.determinarEstadoFinal(new BigDecimal("70.00")))
                    .as("Rama TRUE cubierta")
                    .isEqualTo("APROBADO");

            assertThat(Calificacion.determinarEstadoFinal(new BigDecimal("69.99")))
                    .as("Rama FALSE cubierta")
                    .isEqualTo("REPROBADO");
        }
    }

    /*
     * NOTA: calcularPromedioPonderado(Long matriculaId) requiere:
     * - EntityManager mock
     * - Datos de prueba en base de datos
     * - Contexto JPA configurado
     * 
     * Por ser una prueba de integración más que unitaria, se excluye del scope
     * de pruebas de caja blanca puras. En un entorno real, se probaría con:
     * - @ExtendWith(MockitoExtension.class)
     * - @Mock EntityManager em
     * - Stub de createQuery() y getResultList()
     */
}
