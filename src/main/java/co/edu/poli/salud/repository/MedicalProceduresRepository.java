package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.MedicalProcedures;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalProcedures entity.
 */
@Repository
public interface MedicalProceduresRepository extends JpaRepository<MedicalProcedures, Long> {
    default Optional<MedicalProcedures> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicalProcedures> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicalProcedures> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicalProcedures from MedicalProcedures medicalProcedures left join fetch medicalProcedures.medicalAuthorization",
        countQuery = "select count(medicalProcedures) from MedicalProcedures medicalProcedures"
    )
    Page<MedicalProcedures> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicalProcedures from MedicalProcedures medicalProcedures left join fetch medicalProcedures.medicalAuthorization")
    List<MedicalProcedures> findAllWithToOneRelationships();

    @Query(
        "select medicalProcedures from MedicalProcedures medicalProcedures left join fetch medicalProcedures.medicalAuthorization where medicalProcedures.id =:id"
    )
    Optional<MedicalProcedures> findOneWithToOneRelationships(@Param("id") Long id);
}
