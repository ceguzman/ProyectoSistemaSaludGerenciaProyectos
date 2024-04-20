package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.TypeDiseases;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeDiseases entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDiseasesRepository extends JpaRepository<TypeDiseases, Long> {}
