package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.TypeSpecialist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeSpecialist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSpecialistRepository extends JpaRepository<TypeSpecialist, Long> {}
