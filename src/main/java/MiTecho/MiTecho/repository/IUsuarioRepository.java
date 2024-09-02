package MiTecho.MiTecho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MiTecho.MiTecho.model.Usuario;
@Repository
public interface IUsuarioRepository  extends JpaRepository<Usuario, Integer>{

}
