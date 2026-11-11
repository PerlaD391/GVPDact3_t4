package mx.culturavibe.act3.servicio;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.culturavibe.act3.dto.CategoriaDTO;
import mx.culturavibe.act3.modelo.Categoria;
import mx.culturavibe.act3.repositorio.CategoriaRepository;

@Service
@Transactional
public class CategoriaService {

	private final CategoriaRepository categorias;

	public CategoriaService(CategoriaRepository categorias) {
		this.categorias = categorias;
	}

	@Transactional(readOnly = true)
	public List<CategoriaDTO> listar() {
		return categorias.findAll().stream()
				.map(CategoriaDTO::desde)
				.toList();
	}

	@Transactional(readOnly = true)
	public CategoriaDTO buscarPorId(Long id) {
		return CategoriaDTO.desde(buscarEntidad(id));
	}

	@Transactional(readOnly = true)
	public Categoria buscarEntidad(Long id) {
		return categorias.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No existe una categoría con id " + id));
	}

	public CategoriaDTO crear(CategoriaDTO dto) {
		if (categorias.findByNombreIgnoreCase(dto.nombre()).isPresent()) {
			throw new IllegalArgumentException("Ya existe una categoría con el nombre " + dto.nombre());
		}
		Categoria categoria = new Categoria(dto.nombre(), dto.descripcion());
		return CategoriaDTO.desde(categorias.save(categoria));
	}

	public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
		Categoria categoria = buscarEntidad(id);
		categorias.findByNombreIgnoreCase(dto.nombre())
				.filter(otra -> !otra.getId().equals(id))
				.ifPresent(otra -> {
					throw new IllegalArgumentException("Ya existe una categoría con el nombre " + dto.nombre());
				});
		categoria.setNombre(dto.nombre());
		categoria.setDescripcion(dto.descripcion());
		return CategoriaDTO.desde(categorias.save(categoria));
	}

	public void eliminar(Long id) {
		Categoria categoria = buscarEntidad(id);
		if (!categoria.getEventos().isEmpty()) {
			throw new IllegalStateException("No se puede eliminar la categoría porque tiene eventos asociados");
		}
		categorias.delete(categoria);
	}
}
