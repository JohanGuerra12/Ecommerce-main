package MiTecho.MiTecho.service;

import MiTecho.MiTecho.model.DetalleOrden;
import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.repository.IDetalleOrdenRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.OutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ReporteDetalleOrdenService {

    @Autowired
    private IDetalleOrdenRepository detalleOrdenRepository;

    public void generarReporteDetalleOrden(OutputStream outputStream) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, outputStream);
        documento.open();

        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(70, 130, 180));
        Paragraph titulo = new Paragraph("Reporte de Detalles de Orden", fuenteEncabezado);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(new Paragraph(" "));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Paragraph fecha = new Paragraph("Fecha de impresión: " + sdf.format(new Date()),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        fecha.setAlignment(Element.ALIGN_RIGHT);
        documento.add(fecha);
        documento.add(new Paragraph(" "));

        List<DetalleOrden> detallesOrden = obtenerTodosLosDetalles();
        generarTablaDetalleOrden(documento, detallesOrden);

        documento.close();
    }

    private List<DetalleOrden> obtenerTodosLosDetalles() {
        return detalleOrdenRepository.findAll();
    }

    private void agregarCelda(PdfPTable tabla, String texto, Color fondo, int rowspan) {
        PdfPCell celda = new PdfPCell(new Phrase(texto));
        celda.setBackgroundColor(fondo);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setRowspan(rowspan);
        tabla.addCell(celda);
    }

    private void generarTablaDetalleOrden(Document documento, List<DetalleOrden> detalles) throws Exception {
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);
        tabla.setWidths(new float[]{1f, 3f, 2f, 2f, 2f, 2f, 3f});

        Font fuenteColumnas = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Color colorEncabezado = new Color(173, 216, 230);

        String[] columnas = {"ID", "Producto", "Cantidad", "Total", "Orden N°", "Fecha Creación", "Usuario"};
        for (String columna : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, fuenteColumnas));
            celda.setBackgroundColor(colorEncabezado);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        boolean colorCebra = false;

        Map<String, List<DetalleOrden>> ordenesAgrupadas = new LinkedHashMap<>();
        for (DetalleOrden detalle : detalles) {
            String numeroOrden = detalle.getOrden().getNumero();
            ordenesAgrupadas.computeIfAbsent(numeroOrden, k -> new java.util.ArrayList<>()).add(detalle);
        }

        for (Map.Entry<String, List<DetalleOrden>> entry : ordenesAgrupadas.entrySet()) {
            List<DetalleOrden> detallesOrden = entry.getValue();
            int rowspan = detallesOrden.size();
            DetalleOrden primerDetalle = detallesOrden.get(0);
            Orden orden = primerDetalle.getOrden();

            Color fondoFila = colorCebra ? new Color(230, 230, 230) : Color.WHITE;
            colorCebra = !colorCebra;

            agregarCelda(tabla, String.valueOf(primerDetalle.getId()), fondoFila, rowspan);
            agregarCelda(tabla, primerDetalle.getProducto().getNombre(), fondoFila, 1);
            agregarCelda(tabla, String.valueOf(primerDetalle.getCantidad()), fondoFila, 1);
            agregarCelda(tabla, String.valueOf(primerDetalle.getTotal()), fondoFila, 1);
            agregarCelda(tabla, orden.getNumero(), fondoFila, rowspan);
            agregarCelda(tabla, dateFormat.format(orden.getFechaCreacion()), fondoFila, rowspan);
            agregarCelda(tabla, orden.getUsuario().getNombre(), fondoFila, rowspan);

            for (int i = 1; i < detallesOrden.size(); i++) {
                DetalleOrden detalle = detallesOrden.get(i);
                agregarCelda(tabla, detalle.getProducto().getNombre(), fondoFila, 1);
                agregarCelda(tabla, String.valueOf(detalle.getCantidad()), fondoFila, 1);
                agregarCelda(tabla, String.valueOf(detalle.getTotal()), fondoFila, 1);
            }
        }

        documento.add(tabla);
    }
}
