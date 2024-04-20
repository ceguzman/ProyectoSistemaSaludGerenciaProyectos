package co.edu.poli.salud.repository;

import co.edu.poli.salud.domain.People;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the People entity.
 */
@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {
    default Optional<People> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<People> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<People> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select people from People people left join fetch people.typeDocument left join fetch people.typeSpecialist",
        countQuery = "select count(people) from People people"
    )
    Page<People> findAllWithToOneRelationships(Pageable pageable);

    @Query("select people from People people left join fetch people.typeDocument left join fetch people.typeSpecialist")
    List<People> findAllWithToOneRelationships();

    @Query(
        "select people from People people left join fetch people.typeDocument left join fetch people.typeSpecialist where people.id =:id"
    )
    Optional<People> findOneWithToOneRelationships(@Param("id") Long id);
}
