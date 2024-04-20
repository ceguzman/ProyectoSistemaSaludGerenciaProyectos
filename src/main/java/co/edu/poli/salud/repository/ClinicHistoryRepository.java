package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.ClinicHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClinicHistory entity.
 */
@Repository
public interface ClinicHistoryRepository extends JpaRepository<ClinicHistory, Long> {
    default Optional<ClinicHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ClinicHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ClinicHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select clinicHistory from ClinicHistory clinicHistory left join fetch clinicHistory.typeDisease left join fetch clinicHistory.people",
        countQuery = "select count(clinicHistory) from ClinicHistory clinicHistory"
    )
    Page<ClinicHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select clinicHistory from ClinicHistory clinicHistory left join fetch clinicHistory.typeDisease left join fetch clinicHistory.people"
    )
    List<ClinicHistory> findAllWithToOneRelationships();

    @Query(
        "select clinicHistory from ClinicHistory clinicHistory left join fetch clinicHistory.typeDisease left join fetch clinicHistory.people where clinicHistory.id =:id"
    )
    Optional<ClinicHistory> findOneWithToOneRelationships(@Param("id") Long id);
}
