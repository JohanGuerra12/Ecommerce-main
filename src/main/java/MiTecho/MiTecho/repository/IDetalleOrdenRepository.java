package MiTecho.MiTecho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MiTecho.MiTecho.model.DetalleOrden;
@Repository
public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {

}