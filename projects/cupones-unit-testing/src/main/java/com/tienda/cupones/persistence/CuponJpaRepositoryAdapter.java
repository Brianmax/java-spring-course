package com.tienda.cupones.persistence;

import com.tienda.cupones.domain.Cupon;
import com.tienda.cupones.port.CuponRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class CuponJpaRepositoryAdapter implements CuponRepository {

    private final SpringDataCuponJpaRepository jpaRepository;

    public CuponJpaRepositoryAdapter(SpringDataCuponJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Cupon> buscarPorCodigo(String codigo) {
        return jpaRepository.findById(codigo).map(this::toDomain);
    }

    @Override
    public Cupon guardar(Cupon cupon) {
        CuponEntity saved = jpaRepository.save(toEntity(cupon));
        return toDomain(saved);
    }

    @Override
    public List<Cupon> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .sorted(Comparator.comparing(Cupon::codigo))
                .toList();
    }

    private Cupon toDomain(CuponEntity entity) {
        return new Cupon(
                entity.getCodigo(),
                entity.getPorcentaje(),
                entity.getMinimo(),
                entity.getTope(),
                entity.isActivo()
        );
    }

    private CuponEntity toEntity(Cupon cupon) {
        return new CuponEntity(
                cupon.codigo(),
                cupon.porcentaje(),
                cupon.minimo(),
                cupon.tope(),
                cupon.activo()
        );
    }
}
