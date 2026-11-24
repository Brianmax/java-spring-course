package com.tienda.cupones.persistence;

import com.tienda.cupones.domain.Pedido;
import com.tienda.cupones.port.PedidoRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoJpaRepositoryAdapter implements PedidoRepository {

    private final SpringDataPedidoJpaRepository jpaRepository;

    public PedidoJpaRepositoryAdapter(SpringDataPedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity saved = jpaRepository.save(toEntity(pedido));
        return toDomain(saved);
    }

    @Override
    public List<Pedido> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .sorted(Comparator.comparing(Pedido::id))
                .toList();
    }

    @Override
    public boolean existePorId(String id) {
        return jpaRepository.existsById(id);
    }

    private Pedido toDomain(PedidoEntity entity) {
        return new Pedido(
                entity.getId(),
                entity.getSubtotal(),
                entity.getDescuentoAplicado(),
                entity.getTotalFinal(),
                entity.getCodigoCuponAplicado(),
                entity.isEnvioGratis()
        );
    }

    private PedidoEntity toEntity(Pedido pedido) {
        return new PedidoEntity(
                pedido.id(),
                pedido.subtotal(),
                pedido.descuentoAplicado(),
                pedido.totalFinal(),
                pedido.codigoCuponAplicado(),
                pedido.envioGratis()
        );
    }
}
