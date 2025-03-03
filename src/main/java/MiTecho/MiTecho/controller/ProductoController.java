package MiTecho.MiTecho.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import MiTecho.MiTecho.model.Producto;
import MiTecho.MiTecho.model.Usuario;
import MiTecho.MiTecho.service.IUsuarioService;
import MiTecho.MiTecho.service.ProductoService;
import MiTecho.MiTecho.service.UploadFileService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto: {}", producto);

        if (session.getAttribute("idusuario") == null) {
            LOGGER.error("La sesión está vacía");
            return "redirect:/usuario/login";
        }

        Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).orElse(null);
        if (u == null) {
            LOGGER.error("Usuario no encontrado");
            return "redirect:/usuario/login";
        }

        producto.setUsuario(u);

        if (file.isEmpty()) {
            LOGGER.error("El archivo de imagen está vacío");
            producto.setImagen("default.jpg");
        } else {
            String nombreImagen = upload.saveImage(file);
            if (nombreImagen != null) {
                producto.setImagen(nombreImagen);
            } else {
                LOGGER.error("Error al guardar la imagen");
                producto.setImagen("default.jpg");
            }
        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            LOGGER.info("Producto buscado: {}", producto);
            model.addAttribute("producto", producto);
            return "productos/edit";
        } else {
            LOGGER.error("Producto no encontrado con id: {}", id);
            return "redirect:/productos";
        }
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Optional<Producto> optionalProducto = productoService.get(producto.getId());
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();

            if (file.isEmpty()) {
                producto.setImagen(p.getImagen());
            } else {
                if (!p.getImagen().equals("default.jpg")) {
                    upload.deleteImage(p.getImagen());
                }
                String nombreImagen = upload.saveImage(file);
                producto.setImagen(nombreImagen);
            }

            producto.setUsuario(p.getUsuario());
            productoService.update(producto);
        } else {
            LOGGER.error("Producto no encontrado para actualizar");
        }
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Producto> optionalProducto = productoService.get(id);
        if (optionalProducto.isPresent()) {
            Producto p = optionalProducto.get();

            if (!p.getImagen().equals("default.jpg")) {
                upload.deleteImage(p.getImagen());
            }

            productoService.delete(id);
        } else {
            LOGGER.error("Producto no encontrado para eliminar");
        }
        return "redirect:/productos";
    }
}
