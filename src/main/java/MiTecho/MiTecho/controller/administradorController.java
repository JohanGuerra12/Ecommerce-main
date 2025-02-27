package MiTecho.MiTecho.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.model.Producto;
import MiTecho.MiTecho.service.IOrdenService;
import MiTecho.MiTecho.service.IUsuarioService;
import MiTecho.MiTecho.service.ProductoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Controller
@RequestMapping("/administrador")

public class administradorController {
@Autowired
	private ProductoService productoService;
@Autowired
private IUsuarioService usuarioService;

@Autowired
private IOrdenService ordensService;


private Logger logg= LoggerFactory.getLogger(administradorController.class);
	
	@GetMapping("")
	public String home(Model model) {
		
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";	
	}
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordensService.findAll());
		return "administrador/ordenes";
	}
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		logg.info("Id de la orden {}",id);
		Orden orden= ordensService.findById(id).get();

		model.addAttribute("detalles", orden.getDetalles());

		return "administrador/detalleorden";
	}



}
