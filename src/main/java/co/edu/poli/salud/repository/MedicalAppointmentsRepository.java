package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.MedicalAppointments;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicalAppointments entity.
 */
@Repository
public interface MedicalAppointmentsRepository extends JpaRepository<MedicalAppointments, Long> {
    default Optional<MedicalAppointments> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicalAppointments> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicalAppointments> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicalAppointments from MedicalAppointments medicalAppointments left join fetch medicalAppointments.typeSpecialist",
        countQuery = "select count(medicalAppointments) from MedicalAppointments medicalAppointments"
    )
    Page<MedicalAppointments> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicalAppointments from MedicalAppointments medicalAppointments left join fetch medicalAppointments.typeSpecialist")
    List<MedicalAppointments> findAllWithToOneRelationships();

    @Query(
        "select medicalAppointments from MedicalAppointments medicalAppointments left join fetch medicalAppointments.typeSpecialist where medicalAppointments.id =:id"
    )
    Optional<MedicalAppointments> findOneWithToOneRelationships(@Param("id") Long id);
}
