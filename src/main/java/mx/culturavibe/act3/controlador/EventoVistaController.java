package mx.culturavibe.act3.controlador;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mx.culturavibe.act3.dto.EventoDTO;
import mx.culturavibe.act3.servicio.EventoService;

@Controller
@RequestMapping("/eventos")
public class EventoVistaController {

	private final EventoService eventos;

	@Value("${culturavibe.nombre}")
	private String nombreApp;

	@Value("${culturavibe.autora}")
	private String autora;

	public EventoVistaController(EventoService eventos) {
		this.eventos = eventos;
	}

	@GetMapping
	public String lista(@PageableDefault(size = 12, sort = "fecha") Pageable pageable, Model model) {
		Page<EventoDTO> pagina = eventos.listar(pageable);
		model.addAttribute("pagina", pagina);
		model.addAttribute("eventos", pagina.getContent());
		model.addAttribute("nombreApp", nombreApp);
		model.addAttribute("autora", autora);
		return "eventos/lista";
	}

	@GetMapping("/{id}")
	public String detalle(@PathVariable Long id, Model model) {
		try {
			model.addAttribute("evento", eventos.buscarPorId(id));
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:/eventos";
		}
		model.addAttribute("nombreApp", nombreApp);
		model.addAttribute("autora", autora);
		return "eventos/detalle";
	}
}
