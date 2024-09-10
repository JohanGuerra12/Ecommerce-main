package MiTecho.MiTecho.service;

import java.util.List;
import java.util.Optional;

import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Optional<Orden> findById(Integer id);
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario (Usuario usuario);
  
}
