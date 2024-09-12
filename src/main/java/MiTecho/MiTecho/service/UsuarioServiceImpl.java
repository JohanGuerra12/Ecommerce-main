package MiTecho.MiTecho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MiTecho.MiTecho.model.Usuario;
import MiTecho.MiTecho.repository.IUsuarioRepository;
@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
	private IUsuarioRepository usuarioRepository;
	
	@Override
	public Optional<Usuario> findById(Integer id){
		
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		// TODO Auto-generated method stub
		return usuarioRepository.save(usuario);
	}
	@Override
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
}

