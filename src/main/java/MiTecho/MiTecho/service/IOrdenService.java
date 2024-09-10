package MiTecho.MiTecho.service;

import java.util.List;

import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
  
}
