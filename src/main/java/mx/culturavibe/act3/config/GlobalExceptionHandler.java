package mx.culturavibe.act3.config;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Map<String, Object>> noEncontrado(NoSuchElementException e) {
		return error(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> solicitudIncorrecta(IllegalArgumentException e) {
		return error(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, Object>> conflicto(IllegalStateException e) {
		return error(HttpStatus.CONFLICT, e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException e) {
		Map<String, String> campos = new HashMap<>();
		for (FieldError campo : e.getBindingResult().getFieldErrors()) {
			campos.put(campo.getField(), campo.getDefaultMessage());
		}
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("error", "Validación fallida");
		cuerpo.put("campos", campos);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
	}

	private ResponseEntity<Map<String, Object>> error(HttpStatus estado, String mensaje) {
		Map<String, Object> cuerpo = new HashMap<>();
		cuerpo.put("error", estado.getReasonPhrase());
		cuerpo.put("mensaje", mensaje);
		return ResponseEntity.status(estado).body(cuerpo);
	}
}
