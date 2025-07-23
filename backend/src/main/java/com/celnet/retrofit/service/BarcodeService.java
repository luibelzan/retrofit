package com.celnet.retrofit.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.celnet.retrofit.model.TProcesos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class BarcodeService {

    public void generarExcelConCodigoBarras(List<TProcesos> contadores, OutputStream outputStream) throws Exception {
        // Crea el libro y hoja de cálculo
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Códigos de Barras");

        // Crear estilo para centrar el contenido
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Crea el encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Número de Serie");
        headerRow.createCell(1).setCellValue("Código de Barras");

        // Aplica el estilo de centrado a las celdas del encabezado
        headerRow.getCell(0).setCellStyle(cellStyle);
        headerRow.getCell(1).setCellStyle(cellStyle);

        // Procesa cada objeto TProcesos en la lista
        int rowIndex = 1; // Comienza en la segunda fila (la primera es el encabezado)
        for (TProcesos contador : contadores) {
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(contador.getIdContador());
            row.getCell(0).setCellStyle(cellStyle);

            // Generar código de barras usando ZXing
            byte[] barcodeImage = generarCodigoDeBarras(contador.getIdContador());

            // Agregar la imagen del código de barras a la celda
            int pictureIdx = workbook.addPicture(barcodeImage, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(1);
            anchor.setRow1(rowIndex);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            // Ajustar dimensiones según la imagen
            int imgWidth = pict.getImageDimension().width;
            int imgHeight = pict.getImageDimension().height;
            sheet.setColumnWidth(1, imgWidth * 50);
            row.setHeightInPoints(imgHeight * 1f);

            pict.resize(1, 1);

            // Ajustar el ancho de la columna A (para el ID) para que quepa el texto
            sheet.setColumnWidth(0, (contador.getIdContador().length() + 2) * 300);

            rowIndex++;
        }

        // Escribe el libro de trabajo en el flujo de salida proporcionado
        workbook.write(outputStream);
        workbook.close();
    }

    private byte[] generarCodigoDeBarras(String texto) throws Exception {
        // Crear el generador de código de barras para Code128
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(texto, BarcodeFormat.CODE_128, 150, 50);

        // Convertir la matriz del código de barras en una imagen PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}