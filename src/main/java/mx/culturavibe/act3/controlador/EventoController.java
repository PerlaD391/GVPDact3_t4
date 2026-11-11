package mx.culturavibe.act3.controlador;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.culturavibe.act3.dto.EventoDTO;
import mx.culturavibe.act3.servicio.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

	private final EventoService eventos;

	public EventoController(EventoService eventos) {
		this.eventos = eventos;
	}

	@GetMapping
	public Page<EventoDTO> listar(@PageableDefault(size = 12, sort = "fecha") Pageable pageable) {
		return eventos.listar(pageable);
	}

	@GetMapping("/categoria/{categoriaId}")
	public Page<EventoDTO> listarPorCategoria(@PathVariable Long categoriaId,
			@PageableDefault(size = 12, sort = "fecha") Pageable pageable) {
		return eventos.listarPorCategoria(categoriaId, pageable);
	}

	@GetMapping("/{id}")
	public EventoDTO buscarPorId(@PathVariable Long id) {
		return eventos.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<EventoDTO> crear(@Valid @RequestBody EventoDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(eventos.crear(dto));
	}

	@PutMapping("/{id}")
	public EventoDTO actualizar(@PathVariable Long id, @Valid @RequestBody EventoDTO dto) {
		return eventos.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		eventos.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
