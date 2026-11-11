package mx.culturavibe.act3.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import mx.culturavibe.act3.modelo.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

	Page<Evento> findByCategoriaId(Long categoriaId, Pageable pageable);

	Page<Evento> findByEstadoIgnoreCase(String estado, Pageable pageable);
}
