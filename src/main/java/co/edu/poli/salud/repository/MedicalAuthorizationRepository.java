package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.MedicalAuthorization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalAuthorization entity.
 */
@Repository
public interface MedicalAuthorizationRepository extends JpaRepository<MedicalAuthorization, Long> {
    default Optional<MedicalAuthorization> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicalAuthorization> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicalAuthorization> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicalAuthorization from MedicalAuthorization medicalAuthorization left join fetch medicalAuthorization.clinicHistory",
        countQuery = "select count(medicalAuthorization) from MedicalAuthorization medicalAuthorization"
    )
    Page<MedicalAuthorization> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicalAuthorization from MedicalAuthorization medicalAuthorization left join fetch medicalAuthorization.clinicHistory")
    List<MedicalAuthorization> findAllWithToOneRelationships();

    @Query(
        "select medicalAuthorization from MedicalAuthorization medicalAuthorization left join fetch medicalAuthorization.clinicHistory where medicalAuthorization.id =:id"
    )
    Optional<MedicalAuthorization> findOneWithToOneRelationships(@Param("id") Long id);
}
