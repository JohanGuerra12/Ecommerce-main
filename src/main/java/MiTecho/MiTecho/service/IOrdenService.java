package MiTecho.MiTecho.service;

import java.util.List;

import MiTecho.MiTecho.model.Orden;

public interface IOrdenService {
  List<Orden> findAll();
  Orden save(Orden orden);
  
}
