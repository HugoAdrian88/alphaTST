package alpha.club.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alpha.club.model.Clientes;
import alpha.club.repository.ClientesRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ClientesController {

	@Autowired
	ClientesRepository clientesRepository;

	@GetMapping("/clientes")
	public ResponseEntity<List<Clientes>> getAllClientes(@RequestParam(required = false) String nombre) {
		try {
			List<Clientes> clientes = new ArrayList<Clientes>();

			if (nombre == null)
				clientesRepository.findAll().forEach(clientes::add);
			else
				clientesRepository.findByNombreContaining(nombre).forEach(clientes::add);

			if (clientes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(clientes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/clientes/{id}")
	public ResponseEntity<Clientes> getClientesById(@PathVariable("id") long id) {
		Optional<Clientes> clientesData = clientesRepository.findById(id);

		if (clientesData.isPresent()) {
			return new ResponseEntity<>(clientesData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/clientes")
	public ResponseEntity<Clientes> createClientes(@RequestBody Clientes clientes) {
		try {
			Clientes _clientes = clientesRepository
					.save(new Clientes(clientes.getIdcliente() , clientes.getClub() ,  clientes.getNombre() , clientes.getFecha_alta(), clientes.getFecha_actualizacion(),  true, true));
			return new ResponseEntity<>(_clientes, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<Clientes> updateClientes(@PathVariable("id") long id, @RequestBody Clientes clientes) {
		Optional<Clientes> clientesData = clientesRepository.findById(id);

		if (clientesData.isPresent()) {
			Clientes _clientes = clientesData.get();
			_clientes.setIdcliente(clientes.getIdcliente());
			_clientes.setClub(clientes.getClub());
			_clientes.setNombre(clientes.getNombre());
			_clientes.setFecha_alta(clientes.getFecha_alta());
			_clientes.setFecha_actualizacion(clientes.getFecha_actualizacion());
			_clientes.setAcceso_permitido(clientes.isAcceso_permitido());
			_clientes.setActivo(clientes.isActivo());
			return new ResponseEntity<>(clientesRepository.save(_clientes), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<HttpStatus> deleteClientes(@PathVariable("id") long id) {
		try {
			clientesRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/clientes")
	public ResponseEntity<HttpStatus> deleteAllClientes() {
		try {
			clientesRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/clientes/activo")
	public ResponseEntity<List<Clientes>> findByActivo() {
		try {
			List<Clientes> clientes = clientesRepository.findByActivo(true);

			if (clientes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(clientes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/clientesbyclub/{club}")
	public ResponseEntity<List<Clientes>> findByClub(@PathVariable("club") Integer club) {
		try {
		List<Clientes> clientes = clientesRepository.findByClub(club);

		if (clientes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	} catch (Exception e) {
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
}