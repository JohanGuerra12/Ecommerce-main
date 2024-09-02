package MiTecho.MiTecho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MiTecho.MiTecho.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {

	

}
