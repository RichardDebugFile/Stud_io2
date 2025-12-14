package com.studio.stud_io2.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

/**
 * Acción para generar lista de clase en formato Excel.
 * 
 * RF-10: Generación de reportes (Lista de Clase en Excel)
 * Requisito: El sistema debe permitir la generación de reportes académicos.
 * Cumplimiento: Genera lista de estudiantes matriculados por sección.
 */
public class GenerarListaClaseExcelAction extends ViewBaseAction {

    public void execute() throws Exception {
        Long asignacionId = (Long) getView().getValue("id");

        if (asignacionId == null) {
            addError("Debe tener una asignacion abierta");
            return;
        }

        String jpql = "SELECT a.curso.nombre, a.seccion, a.periodo.nombre, a.docente.nombre, a.docente.apellido, a.horario FROM Asignacion a WHERE a.id = :id";

        Object[] datos = (Object[]) XPersistence.getManager().createQuery(jpql).setParameter("id", asignacionId)
                .getSingleResult();

        String curso = (String) datos[0];
        String seccion = (String) datos[1];
        String periodo = (String) datos[2];
        String docente = datos[3] + " " + datos[4];
        String horario = (String) datos[5];

        String jpqlE = "SELECT m.estudiante.cedula, m.estudiante.nombre, m.estudiante.apellido, m.estudiante.email FROM Matricula m WHERE m.asignacion.id = :id ORDER BY m.estudiante.apellido";

        List resultList = XPersistence.getManager().createQuery(jpqlE).setParameter("id", asignacionId).getResultList();

        Workbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("Lista");

        CellStyle bold = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        bold.setFont(f);

        int r = 0;
        Row row = sh.createRow(r++);
        Cell c = row.createCell(0);
        c.setCellValue("LISTA DE CLASE");
        c.setCellStyle(bold);

        r++;
        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Curso:");
        row.createCell(1).setCellValue(curso + " - " + seccion);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Periodo:");
        row.createCell(1).setCellValue(periodo);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Docente:");
        row.createCell(1).setCellValue(docente);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Horario:");
        row.createCell(1).setCellValue(horario);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Total:");
        row.createCell(1).setCellValue(resultList.size());

        r++;
        row = sh.createRow(r++);
        row.createCell(0).setCellValue("No.");
        row.createCell(1).setCellValue("Cedula");
        row.createCell(2).setCellValue("Nombre");
        row.createCell(3).setCellValue("Email");

        int num = 1;
        for (Object item : resultList) {
            Object[] est = (Object[]) item;
            row = sh.createRow(r++);
            row.createCell(0).setCellValue(num++);
            row.createCell(1).setCellValue((String) est[0]);
            row.createCell(2).setCellValue(est[2] + " " + est[1]);
            row.createCell(3).setCellValue((String) est[3]);
        }

        sh.autoSizeColumn(0);
        sh.autoSizeColumn(1);
        sh.autoSizeColumn(2);
        sh.autoSizeColumn(3);

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File outFile = new File(tempDir, "lista_" + curso.replace(" ", "_") + "_" + seccion + ".xlsx");

        FileOutputStream fos = new FileOutputStream(outFile);
        wb.write(fos);
        fos.close();
        wb.close();

        addMessage("Lista generada: " + outFile.getAbsolutePath());
    }
}
