package mx.culturavibe.act3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
		Long id,
		@NotBlank(message = "El nombre de la categoría es obligatorio")
		@Size(max = 60, message = "El nombre no puede pasar de 60 caracteres")
		String nombre,
		@Size(max = 500, message = "La descripción no puede pasar de 500 caracteres")
		String descripcion
) {

	public static CategoriaDTO desde(mx.culturavibe.act3.modelo.Categoria categoria) {
		return new CategoriaDTO(
				categoria.getId(),
				categoria.getNombre(),
				categoria.getDescripcion()
		);
	}
}
