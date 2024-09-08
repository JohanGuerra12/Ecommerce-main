package MiTecho.MiTecho.service;

import java.util.Optional;

import MiTecho.MiTecho.model.Usuario;

public interface IUsuarioService {
	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
		
}
