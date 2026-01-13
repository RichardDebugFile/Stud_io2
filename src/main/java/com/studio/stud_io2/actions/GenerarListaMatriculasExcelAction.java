package com.studio.stud_io2.actions;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import com.studio.stud_io2.modelo.Matricula;

/**
 * Acción para generar lista de matrículas en formato Excel.
 *
 * RF-10: Generación de reportes (Lista de Matrículas en Excel)
 * Genera Excel con formato correcto para columnas numéricas (promedio y porcentaje asistencia)
 */
public class GenerarListaMatriculasExcelAction extends TabBaseAction {

    public void execute() throws Exception {
        // Obtener todas las matrículas
        String jpql = "SELECT m FROM Matricula m ORDER BY m.estudiante.apellido, m.estudiante.nombre";

        @SuppressWarnings("unchecked")
        List<Matricula> matriculas = XPersistence.getManager().createQuery(jpql).getResultList();

        if (matriculas.isEmpty()) {
            addError("No existen matrículas para exportar");
            return;
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("Matriculas");

        // Estilos
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        CellStyle numberStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        numberStyle.setDataFormat(format.getFormat("0.00"));

        // Encabezados
        int r = 0;
        Row headerRow = sh.createRow(r++);

        String[] headers = {"Apellido", "Nombre", "Curso", "Sección",
                           "Promedio Ponderado", "Estado Final",
                           "Porcentaje Asistencia", "Estado Matrícula"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        for (Matricula m : matriculas) {
            Row row = sh.createRow(r++);

            // Apellido
            row.createCell(0).setCellValue(m.getEstudiante().getApellido());

            // Nombre
            row.createCell(1).setCellValue(m.getEstudiante().getNombre());

            // Curso
            row.createCell(2).setCellValue(m.getAsignacion().getCurso().getNombre());

            // Sección
            row.createCell(3).setCellValue(m.getAsignacion().getSeccion());

            // Promedio Ponderado (con formato decimal)
            Cell promedioCell = row.createCell(4);
            BigDecimal promedio = m.getPromedioPonderado();
            promedioCell.setCellValue(promedio.doubleValue());
            promedioCell.setCellStyle(numberStyle);

            // Estado Final
            row.createCell(5).setCellValue(m.getEstadoFinal());

            // Porcentaje Asistencia (con formato decimal)
            Cell porcentajeCell = row.createCell(6);
            Double porcentaje = m.getPorcentajeAsistencia();
            porcentajeCell.setCellValue(porcentaje);
            porcentajeCell.setCellStyle(numberStyle);

            // Estado Matrícula
            row.createCell(7).setCellValue(m.getEstado());
        }

        // Ajustar ancho de columnas
        sh.autoSizeColumn(0); // Apellido
        sh.setColumnWidth(0, sh.getColumnWidth(0) + 1024);

        sh.autoSizeColumn(1); // Nombre
        sh.setColumnWidth(1, sh.getColumnWidth(1) + 1024);

        sh.autoSizeColumn(2); // Curso
        sh.setColumnWidth(2, sh.getColumnWidth(2) + 1024);

        sh.setColumnWidth(3, 3000); // Sección

        sh.setColumnWidth(4, 5000); // Promedio Ponderado - ancho fijo para decimales

        sh.autoSizeColumn(5); // Estado Final
        sh.setColumnWidth(5, sh.getColumnWidth(5) + 1024);

        sh.setColumnWidth(6, 5000); // Porcentaje Asistencia - ancho fijo para decimales

        sh.autoSizeColumn(7); // Estado Matrícula
        sh.setColumnWidth(7, sh.getColumnWidth(7) + 1024);

        // Guardar archivo
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File outFile = new File(tempDir, "lista_matriculas.xlsx");

        FileOutputStream fos = new FileOutputStream(outFile);
        wb.write(fos);
        fos.close();
        wb.close();

        addMessage("Lista de matrículas generada: " + outFile.getAbsolutePath());
    }
}
