package mx.culturavibe.act3.controlador;

import java.util.List;

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
import mx.culturavibe.act3.dto.CategoriaDTO;
import mx.culturavibe.act3.servicio.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

	private final CategoriaService categorias;

	public CategoriaController(CategoriaService categorias) {
		this.categorias = categorias;
	}

	@GetMapping
	public List<CategoriaDTO> listar() {
		return categorias.listar();
	}

	@GetMapping("/{id}")
	public CategoriaDTO buscarPorId(@PathVariable Long id) {
		return categorias.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<CategoriaDTO> crear(@Valid @RequestBody CategoriaDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categorias.crear(dto));
	}

	@PutMapping("/{id}")
	public CategoriaDTO actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
		return categorias.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		categorias.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
