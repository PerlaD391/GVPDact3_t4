package mx.culturavibe.act3.servicio;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.culturavibe.act3.dto.EventoDTO;
import mx.culturavibe.act3.modelo.Categoria;
import mx.culturavibe.act3.modelo.Evento;
import mx.culturavibe.act3.repositorio.EventoRepository;

@Service
@Transactional
public class EventoService {

	private final EventoRepository eventos;
	private final CategoriaService categorias;

	public EventoService(EventoRepository eventos, CategoriaService categorias) {
		this.eventos = eventos;
		this.categorias = categorias;
	}

	@Transactional(readOnly = true)
	public Page<EventoDTO> listar(Pageable pageable) {
		return eventos.findAll(pageable).map(EventoDTO::desde);
	}

	@Transactional(readOnly = true)
	public Page<EventoDTO> listarPorCategoria(Long categoriaId, Pageable pageable) {
		categorias.buscarEntidad(categoriaId);
		return eventos.findByCategoriaId(categoriaId, pageable).map(EventoDTO::desde);
	}

	@Transactional(readOnly = true)
	public EventoDTO buscarPorId(Long id) {
		return EventoDTO.desde(buscarEntidad(id));
	}

	@Transactional(readOnly = true)
	public Evento buscarEntidad(Long id) {
		return eventos.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No existe un evento con id " + id));
	}

	public EventoDTO crear(EventoDTO dto) {
		Categoria categoria = categorias.buscarEntidad(dto.categoriaId());
		Evento evento = dto.aEvento(categoria);
		return EventoDTO.desde(eventos.save(evento));
	}

	public EventoDTO actualizar(Long id, EventoDTO dto) {
		Evento evento = buscarEntidad(id);
		Categoria categoria = categorias.buscarEntidad(dto.categoriaId());
		dto.aplicarA(evento, categoria);
		return EventoDTO.desde(eventos.save(evento));
	}

	public void eliminar(Long id) {
		eventos.delete(buscarEntidad(id));
	}
}
