package alpha.club.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.QueryProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alpha.club.HibernateUtil;
//import alpha.club.model.Autos;
import alpha.club.model.AutosActivos;
import alpha.club.model.Clientes;
//import alpha.club.model.Clientes;
import alpha.club.repository.AutosActivosRepository;
import jakarta.persistence.Query;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AutosActivosController {
	@Autowired
	AutosActivosRepository autosActivosRepository;

	@GetMapping("/autosactivos")
	public ResponseEntity<List<AutosActivos>> getAllAutosActivos(@RequestParam(required = false) String marca) {
		try {
			List<AutosActivos> autos = new ArrayList<AutosActivos>();

			if (marca == null)
				autosActivosRepository.findAll().forEach(autos::add);
			
			if (autos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(autos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/autosactivos/activo")
	public ResponseEntity<List<AutosActivos>> findByActivo() {
		try {
			List<AutosActivos> autos = autosActivosRepository.findByActivo(true);
	
			if (autos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(autos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/autosactivos/getautosactivosbyidclub/{club}")
	public ResponseEntity<List<AutosActivos>> getAutosActivo(@PathVariable("club") Integer club) {
		
		try {			
			 Query nativeQuery = HibernateUtil.getSessionFactory().openSession().createNativeQuery("\r\n"
			 		+ "SELECT a.id_chip , c.nombre as cnombre , a.placas, a.id_cliente , a.activo,  c.club   FROM  autos a	JOIN  clientes c  \r\n"
			 		+ "ON a.id_cliente = c.idcliente	and a.activo=true	  \r\n"
			 		+ " and c.activo=true and c.acceso_permitido=true \r\n"
			 		+ " and c.club = ?",  alpha.club.model.AutosActivos.class);
	        	
			 		nativeQuery.setParameter(1, club );
			 		  System.out.println("+++++ Club: "+club);
			 		  
			 List<AutosActivos> autos = nativeQuery.getResultList();
					 
			return new ResponseEntity<>(autos, HttpStatus.OK);
		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/autosactivos/getautosactivos/{club}")
	public ResponseEntity<List<AutosActivos>> getAutosActivos(@PathVariable("club") Integer club) {
			
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json");    
	        Query nativeQuery = HibernateUtil.getSessionFactory().openSession().createNativeQuery("\r\n"
			 		+ " SELECT a.id_chip , c.nombre as cnombre , a.placas, a.id_cliente,  a.activo , c.club  FROM  autos a	JOIN  clientes c  \r\n"
			 		+ " ON a.id_cliente = c.idcliente	and a.activo=true	  \r\n"
			 		+ " and c.activo=true and c.acceso_permitido=true \r\n"
			 		+ " and c.club = ?",  alpha.club.model.AutosActivos.class);
	        	
			 		nativeQuery.setParameter(1, club );
			 		  System.out.println("----- Club: "+club);
		
			 List<AutosActivos> autos = nativeQuery.getResultList();
	        if(autos != null) return new ResponseEntity<>(autos, headers, HttpStatus.OK);
	        else return new ResponseEntity<>(headers, HttpStatus.OK); 
	}
	 
}