package MiTecho.MiTecho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MiTecho.MiTecho.model.Orden;
@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {

}
