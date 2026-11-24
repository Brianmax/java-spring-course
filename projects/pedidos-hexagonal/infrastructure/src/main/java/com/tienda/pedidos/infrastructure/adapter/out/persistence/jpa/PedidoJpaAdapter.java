package com.tienda.pedidos.infrastructure.adapter.out.persistence.jpa;

import com.tienda.pedidos.application.port.out.PedidoRepositoryPort;
import com.tienda.pedidos.domain.model.EstadoPedido;
import com.tienda.pedidos.domain.model.Pedido;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida JPA para PostgreSQL: confina Spring Data JPA detrás de PedidoRepositoryPort.
 */
@Repository
@Profile("postgres")
public class PedidoJpaAdapter implements PedidoRepositoryPort {

    private final SpringDataPedidoRepository jpaRepository;
    private final PedidoMapper mapper;

    public PedidoJpaAdapter(SpringDataPedidoRepository jpaRepository, PedidoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Pedido> buscarPorId(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Pedido> listarTodos() {
        return jpaRepository.findAll(Sort.by("id")).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Pedido> buscarEnPreparacionAnterioresA(Instant fechaLimite) {
        return jpaRepository.findByEstadoAndCreadoEnBefore(EstadoPedido.EN_PREPARACION, fechaLimite).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorId(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity guardado = jpaRepository.save(mapper.toEntity(pedido));
        return mapper.toDomain(guardado);
    }
}
