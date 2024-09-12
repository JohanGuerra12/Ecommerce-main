package MiTecho.MiTecho.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import MiTecho.MiTecho.model.DetalleOrden;
import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.model.Producto;
import MiTecho.MiTecho.model.Usuario;
import MiTecho.MiTecho.repository.IUsuarioRepository;
import MiTecho.MiTecho.service.IDetalleOrdenService;
import MiTecho.MiTecho.service.IOrdenService;
import MiTecho.MiTecho.service.ProductoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log= LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	@Autowired
	private IUsuarioRepository usuarioService;
	


	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {

		log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));

		model.addAttribute("productos", productoService.findAll());
		
		model.addAttribute("sesion",session.getAttribute("idusuario"));

		return "usuario/home";
	 }
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id , Model model, HttpSession session) {
		log.info("Id product enviado como parametro {}", id);
		Producto producto =  new Producto();	
		Optional<Producto> productoOptional = productoService.get(id);
		 model.addAttribute("sesion", session.getAttribute("idusuario"));
		producto = productoOptional.get();
		
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
	    Optional<Producto> optionalProducto = productoService.get(id);
	    
	    if (optionalProducto.isPresent()) {
	        Producto producto = optionalProducto.get();
	        log.info("Producto añadido: {}", producto);
	        log.info("Cantidad: {}", cantidad);
	        
	        // Buscar el detalle existente en la lista
	        DetalleOrden detalleExistente = detalles.stream()
	            .filter(detalle -> detalle.getProducto().getId().equals(id))
	            .findFirst()
	            .orElse(null);
	        
	        if (detalleExistente != null) {
	            // Actualizar cantidad y total si el detalle ya existe
	            detalleExistente.setCantidad(detalleExistente.getCantidad() + cantidad);
	            detalleExistente.setTotal(detalleExistente.getCantidad() * detalleExistente.getPrecio());
	        } else {
	            // Crear un nuevo detalle si no existe
	            DetalleOrden nuevoDetalle = new DetalleOrden();
	            nuevoDetalle.setCantidad(cantidad);
	            nuevoDetalle.setPrecio(producto.getPrecio());
	            nuevoDetalle.setNombre(producto.getNombre());
	            nuevoDetalle.setTotal(producto.getPrecio() * cantidad);
	            nuevoDetalle.setProducto(producto);
	            detalles.add(nuevoDetalle);
	        }

	        // Actualizar el total de la orden
	        double sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
	        orden.setTotal(sumaTotal);
	    } else {
	        log.warn("Producto con id {} no encontrado", id);
	    }

	    model.addAttribute("cart", detalles);
	    model.addAttribute("orden", orden);
	    return "usuario/carrito";
	}

	
	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id , Model model) {
		
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden: detalles) {
			if(detalleOrden.getProducto().getId()!=id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		// Poner la nueva lista con los productos restantes
		detalles = ordenesNueva;
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
	    model.addAttribute("orden", orden);
	    
		return "usuario/carrito";
	}
	@GetMapping("/getCart")
	public String getCart(Model model , HttpSession session) {
		
	    model.addAttribute("cart", detalles);
	    model.addAttribute("orden", orden);
	    model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())).get();
	    model.addAttribute("cart", detalles);
	    model.addAttribute("orden", orden);
	    model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	}
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
	    Date fechaCreacion = new Date();
	    orden.setFechaCreacion(fechaCreacion);
	    orden.setNumero(ordenService.generarNumeroOrden());

	    // Usuario
	    Usuario usuario =usuarioService.findById( Integer.parseInt(session.getAttribute("idusuario").toString())  ).get();	
	    orden.setUsuario(usuario);

	    // Guardar la orden
	    ordenService.save(orden);

	    // Guardar los detalles
	    for (DetalleOrden dt : detalles) {
	        dt.setOrden(orden); // Asociar detalle con la orden
	        detalleOrdenService.save(dt); // Guardar detalle
	    }

	    // Limpiar la lista y la orden
	    orden = new Orden();
	    detalles.clear();

	    return "redirect:/";
	}
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
	    log.info("Nombre del producto buscado: {}", nombre);
	    
	    // Convertir todo a minúsculas antes de comparar
	    List<Producto> productos = productoService.findAll().stream()
	        .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
	        .collect(Collectors.toList());
	    
	    model.addAttribute("productos", productos);
	    return "usuario/home";
	}

	
}
