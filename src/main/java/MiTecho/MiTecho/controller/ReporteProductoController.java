package MiTecho.MiTecho.controller;

import MiTecho.MiTecho.model.Producto;
import MiTecho.MiTecho.repository.IProductoRepository;
import MiTecho.MiTecho.service.ReporteProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/reportes/productos")
public class ReporteProductoController {

    @Autowired
    private ReporteProductoService reporteProductoService;

    @Autowired
    private IProductoRepository productoRepository;

    @GetMapping("/generar")
    public void generarReporteProductos(HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Reporte_Productos.pdf");

            OutputStream outputStream = response.getOutputStream();
            List<Producto> productos = obtenerListaProductos();
            reporteProductoService.generarReporteProductos(productos, outputStream);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Producto> obtenerListaProductos() {
        return productoRepository.findAll(); // Reemplaza con tu lógica
    }
}
