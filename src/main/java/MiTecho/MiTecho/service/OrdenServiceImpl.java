package MiTecho.MiTecho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MiTecho.MiTecho.model.Orden;
import MiTecho.MiTecho.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {
	@Autowired
	private IOrdenRepository ordenRepository;
	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}
}
