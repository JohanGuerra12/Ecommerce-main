package MiTecho.MiTecho.controller;

import MiTecho.MiTecho.service.ReporteDetalleOrdenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

@RestController
@RequestMapping("/reportes/detalle-orden")
public class ReporteDetalleOrdenController {

    @Autowired
    private ReporteDetalleOrdenService reporteDetalleOrdenService;

    @GetMapping("/generar")
    public void generarReporteDetalleOrden(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Reporte_Detalle_Orden.pdf");

            OutputStream outputStream = response.getOutputStream();
            reporteDetalleOrdenService.generarReporteDetalleOrden(outputStream); // Ahora se usa el método correcto del Service
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

