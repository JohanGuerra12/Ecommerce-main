package MiTecho.MiTecho.service;

import MiTecho.MiTecho.model.Producto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.OutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReporteProductoService {

    public void generarReporteProductos(List<Producto> productos, OutputStream outputStream) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, outputStream);
        documento.open();

        try {
            Image logo = Image.getInstance("/static/img/Logo.png");
            logo.scaleAbsolute(100, 50);
            logo.setAlignment(Image.ALIGN_LEFT);
            documento.add(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(70, 130, 180));
        Paragraph titulo = new Paragraph("Reporte de Productos", fuenteEncabezado);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(new Paragraph(" "));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Paragraph fecha = new Paragraph("Fecha de impresión: " + sdf.format(new Date()),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        fecha.setAlignment(Element.ALIGN_RIGHT);
        documento.add(fecha);
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(6); // Mantener 6 columnas
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);
        tabla.setWidths(new float[]{1f, 3f, 5f, 2f, 2f, 3f}); // Ajustar el ancho de las 6 columnas

        Font fuenteEncabezadoTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14); // Tamaño más grande solo en encabezado
        Color colorEncabezado = new Color(173, 216, 230);

        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Cantidad", "Usuario"};
        for (String columna : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, fuenteEncabezadoTabla));
            celda.setBackgroundColor(colorEncabezado);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setPadding(8); // Aumentar padding para que las celdas sean más grandes
            tabla.addCell(celda);
        }

        boolean colorCebra = false;
        for (Producto producto : productos) {
            Color fondoFila = colorCebra ? new Color(230, 230, 230) : Color.WHITE;
            colorCebra = !colorCebra;

            agregarCelda(tabla, String.valueOf(producto.getId()), fondoFila);
            agregarCelda(tabla, producto.getNombre(), fondoFila);
            agregarCelda(tabla, producto.getDescripcion(), fondoFila);
            agregarCelda(tabla, String.valueOf(producto.getPrecio()), fondoFila);
            agregarCelda(tabla, String.valueOf(producto.getCantidad()), fondoFila);
            agregarCelda(tabla, producto.getUsuario().getNombre(), fondoFila); // Asegurar que el usuario existe
        }

        documento.add(tabla);
        documento.close();
    }

    private void agregarCelda(PdfPTable tabla, String texto, Color fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto));
        celda.setBackgroundColor(fondo);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }
}
