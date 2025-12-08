package com.studio.stud_io2.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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

public class GenerarBoletaExcelAction extends ViewBaseAction {

    public void execute() throws Exception {
        Long matriculaId = (Long) getView().getValue("id");

        if (matriculaId == null) {
            addError("Debe tener una matricula abierta");
            return;
        }

        String jpql = "SELECT m.estudiante.nombre, m.estudiante.apellido, m.asignacion.curso.nombre, m.asignacion.seccion, m.asignacion.periodo.nombre FROM Matricula m WHERE m.id = :id";

        Object[] datos = (Object[]) XPersistence.getManager().createQuery(jpql).setParameter("id", matriculaId)
                .getSingleResult();

        String nombre = datos[0] + " " + datos[1];
        String curso = (String) datos[2];
        String seccion = (String) datos[3];
        String periodo = (String) datos[4];

        String jpqlC = "SELECT r.nombre, c.nota, r.ponderacion FROM Calificacion c JOIN c.rubro r WHERE c.matricula.id = :id ORDER BY r.nombre";

        List resultList = XPersistence.getManager().createQuery(jpqlC).setParameter("id", matriculaId).getResultList();

        BigDecimal promedio = com.studio.stud_io2.modelo.Calificacion.calcularPromedioPonderado(matriculaId);
        String estado = com.studio.stud_io2.modelo.Calificacion.determinarEstadoFinal(promedio);

        Workbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("Boleta");

        CellStyle bold = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        bold.setFont(f);

        int r = 0;
        Row row = sh.createRow(r++);
        Cell c = row.createCell(0);
        c.setCellValue("BOLETA DE CALIFICACIONES");
        c.setCellStyle(bold);

        r++;
        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Estudiante:");
        row.createCell(1).setCellValue(nombre);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Curso:");
        row.createCell(1).setCellValue(curso + " - " + seccion);

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Periodo:");
        row.createCell(1).setCellValue(periodo);

        r++;
        row = sh.createRow(r++);
        row.createCell(0).setCellValue("Rubro");
        row.createCell(1).setCellValue("Nota");
        row.createCell(2).setCellValue("Ponderacion");

        for (Object item : resultList) {
            Object[] cal = (Object[]) item;
            row = sh.createRow(r++);
            row.createCell(0).setCellValue((String) cal[0]);
            row.createCell(1).setCellValue(((BigDecimal) cal[1]).doubleValue());
            row.createCell(2).setCellValue(((BigDecimal) cal[2]).doubleValue());
        }

        r++;
        row = sh.createRow(r++);
        row.createCell(0).setCellValue("PROMEDIO:");
        row.createCell(1).setCellValue(promedio.doubleValue());

        row = sh.createRow(r++);
        row.createCell(0).setCellValue("ESTADO:");
        row.createCell(1).setCellValue(estado);

        sh.autoSizeColumn(0);
        sh.autoSizeColumn(1);
        sh.autoSizeColumn(2);

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File outFile = new File(tempDir, "boleta_" + nombre.replace(" ", "_") + ".xlsx");

        FileOutputStream fos = new FileOutputStream(outFile);
        wb.write(fos);
        fos.close();
        wb.close();

        addMessage("Boleta generada: " + outFile.getAbsolutePath());
    }
}
