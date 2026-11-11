package mx.culturavibe.act3.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mx.culturavibe.act3.modelo.Categoria;
import mx.culturavibe.act3.modelo.Evento;

public record EventoDTO(
		Long id,
		@NotBlank(message = "El nombre del evento es obligatorio")
		@Size(max = 150, message = "El nombre no puede pasar de 150 caracteres")
		String nombre,
		@NotNull(message = "La fecha del evento es obligatoria")
		LocalDate fecha,
		@NotBlank(message = "La hora del evento es obligatoria")
		String hora,
		@NotBlank(message = "La sede del evento es obligatoria")
		String sede,
		String direccion,
		@NotBlank(message = "La ciudad del evento es obligatoria")
		String ciudad,
		@NotBlank(message = "El estado del evento es obligatorio")
		String estado,
		@NotNull(message = "La categoría del evento es obligatoria")
		Long categoriaId,
		String categoriaNombre,
		String precio,
		String estadoVenta,
		String descripcion,
		List<String> artistas,
		String url
) {

	public static EventoDTO desde(Evento evento) {
		return new EventoDTO(
				evento.getId(),
				evento.getNombre(),
				evento.getFecha(),
				evento.getHora(),
				evento.getSede(),
				evento.getDireccion(),
				evento.getCiudad(),
				evento.getEstado(),
				evento.getCategoria().getId(),
				evento.getCategoria().getNombre(),
				evento.getPrecio(),
				evento.getEstadoVenta(),
				evento.getDescripcion(),
				evento.getArtistas(),
				evento.getUrl()
		);
	}

	public Evento aEvento(Categoria categoria) {
		Evento evento = new Evento();
		evento.setNombre(nombre);
		evento.setFecha(fecha);
		evento.setHora(hora);
		evento.setSede(sede);
		evento.setDireccion(direccion);
		evento.setCiudad(ciudad);
		evento.setEstado(estado);
		evento.setCategoria(categoria);
		evento.setPrecio(precio);
		evento.setEstadoVenta(estadoVenta == null ? "onsale" : estadoVenta);
		evento.setDescripcion(descripcion);
		evento.setArtistas(artistas == null ? new ArrayList<>() : new ArrayList<>(artistas));
		evento.setUrl(url);
		return evento;
	}

	public void aplicarA(Evento evento, Categoria categoria) {
		evento.setNombre(nombre);
		evento.setFecha(fecha);
		evento.setHora(hora);
		evento.setSede(sede);
		evento.setDireccion(direccion);
		evento.setCiudad(ciudad);
		evento.setEstado(estado);
		evento.setCategoria(categoria);
		evento.setPrecio(precio);
		evento.setEstadoVenta(estadoVenta);
		evento.setDescripcion(descripcion);
		evento.setArtistas(artistas == null ? new ArrayList<>() : new ArrayList<>(artistas));
		evento.setUrl(url);
	}
}
