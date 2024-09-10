package MiTecho.MiTecho.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.model.Usuario;
@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
            List<Orden> findByUsuario (Usuario usuario);
}
