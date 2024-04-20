package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.MedicationRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicationRequest entity.
 */
@Repository
public interface MedicationRequestRepository extends JpaRepository<MedicationRequest, Long> {
    default Optional<MedicationRequest> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicationRequest> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicationRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicationRequest from MedicationRequest medicationRequest left join fetch medicationRequest.medicalAuthorization",
        countQuery = "select count(medicationRequest) from MedicationRequest medicationRequest"
    )
    Page<MedicationRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicationRequest from MedicationRequest medicationRequest left join fetch medicationRequest.medicalAuthorization")
    List<MedicationRequest> findAllWithToOneRelationships();

    @Query(
        "select medicationRequest from MedicationRequest medicationRequest left join fetch medicationRequest.medicalAuthorization where medicationRequest.id =:id"
    )
    Optional<MedicationRequest> findOneWithToOneRelationships(@Param("id") Long id);
}
