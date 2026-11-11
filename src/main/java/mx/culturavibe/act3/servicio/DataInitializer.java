package mx.culturavibe.act3.servicio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import mx.culturavibe.act3.dto.EventoDTO;
import mx.culturavibe.act3.modelo.Categoria;
import mx.culturavibe.act3.repositorio.CategoriaRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final CategoriaRepository categorias;
	private final EventoService eventos;

	public DataInitializer(CategoriaRepository categorias, EventoService eventos) {
		this.categorias = categorias;
		this.eventos = eventos;
	}

	@Override
	public void run(String... args) {
		if (categorias.count() > 0) {
			return;
		}
		Categoria musica = categorias.save(new Categoria("Música", "Conciertos y festivales de música en vivo."));
		Categoria cine = categorias.save(new Categoria("Cine", "Proyecciones y festivales de cine."));
		Categoria arte = categorias.save(new Categoria("Arte", "Exposiciones de arte y cultura visual."));
		Categoria libros = categorias.save(new Categoria("Libros", "Ferias del libro, presentaciones y talleres."));
		Categoria teatro = categorias.save(new Categoria("Teatro", "Obras y puestas en escena."));

		List<EventoDTO> iniciales = List.of(
				new EventoDTO(null, "Concierto Sinfónico de Gala",
						LocalDate.of(2026, 11, 12), "20:00", "Palacio de Bellas Artes",
						"Av. Juárez s/n, Centro Histórico",
						"Ciudad de México", "CDMX", musica.getId(), null,
						"$450", "onsale",
						"Gala sinfónica con obras de compositores mexicanos e invitados internacionales.",
						List.of("Orquesta Sinfónica Nacional"),
						"https://www.ticketmaster.com.mx"),
				new EventoDTO(null, "Festival Internacional de Cine",
						LocalDate.of(2026, 11, 22), "18:00", "Conjunto Santander",
						"Av. Periférico Norte 1695",
						"Guadalajara", "JAL", cine.getId(), null,
						"$180", "onsale",
						"Proyecciones de cine nacional e internacional con funciones al aire libre.",
						List.of("Cineastas nacionales e internacionales"),
						"https://www.ticketmaster.com.mx"),
				new EventoDTO(null, "Noche de Jazz en el Barrio Antiguo",
						LocalDate.of(2026, 11, 25), "21:00", "Barrio Antiguo",
						"Calle Morelos 300, Barrio Antiguo",
						"Monterrey", "NL", musica.getId(), null,
						"$250", "onsale",
						"Una noche de jazz en vivo con músicos locales y artistas invitados.",
						List.of("Jazz Quartet MX"),
						"https://www.ticketmaster.com.mx"),
				new EventoDTO(null, "Exposición de Arte Contemporáneo",
						LocalDate.of(2026, 11, 28), "11:00", "Museo Amparo",
						"Av. 2 Oriente 607, Centro Histórico",
						"Puebla", "PUE", arte.getId(), null,
						"Gratis", "onsale",
						"Muestra de arte contemporáneo de creadores mexicanos.",
						List.of("Artistas contemporáneos mexicanos"),
						"https://www.ticketmaster.com.mx"),
				new EventoDTO(null, "Feria del Libro",
						LocalDate.of(2026, 12, 2), "10:00", "Centro Cultural",
						"Av. Morelos Sur 100, Centro",
						"Cuernavaca", "MOR", libros.getId(), null,
						"Gratis", "onsale",
						"Venta y presentación de libros con autores invitados y talleres.",
						List.of("Autores invitados"),
						"https://www.ticketmaster.com.mx"),
				new EventoDTO(null, "Festival de Teatro Clásico",
						LocalDate.of(2026, 12, 5), "19:00", "Teatro Principal",
						"Av. Costera Miguel Alemán 100",
						"Acapulco", "GRO", teatro.getId(), null,
						"$320", "onsale",
						"Puesta en escena de clásicos del teatro universal.",
						List.of("Compañía Nacional de Teatro"),
						"https://www.ticketmaster.com.mx")
		);
		iniciales.forEach(eventos::crear);
	}
}
