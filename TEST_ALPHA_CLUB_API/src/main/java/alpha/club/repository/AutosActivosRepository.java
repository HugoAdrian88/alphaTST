package alpha.club.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import alpha.club.model.AutosActivos;

public interface AutosActivosRepository extends JpaRepository<AutosActivos, Long> {
  List<AutosActivos> findByActivo(boolean activo);
	
}