package com.tienda.cupones.port;

import com.tienda.cupones.domain.Cupon;

import java.util.List;
import java.util.Optional;

public interface CuponRepository {

    Optional<Cupon> buscarPorCodigo(String codigo);

    Cupon guardar(Cupon cupon);

    List<Cupon> listarTodos();
}
