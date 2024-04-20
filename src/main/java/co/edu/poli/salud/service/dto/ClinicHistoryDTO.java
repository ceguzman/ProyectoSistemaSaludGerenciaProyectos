package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.ClinicHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClinicHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateClinic;

    @NotNull
    private TypeDiseasesDTO typeDisease;

    @NotNull
    private PeopleDTO people;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateClinic() {
        return dateClinic;
    }

    public void setDateClinic(LocalDate dateClinic) {
        this.dateClinic = dateClinic;
    }

    public TypeDiseasesDTO getTypeDisease() {
        return typeDisease;
    }

    public void setTypeDisease(TypeDiseasesDTO typeDisease) {
        this.typeDisease = typeDisease;
    }

    public PeopleDTO getPeople() {
        return people;
    }

    public void setPeople(PeopleDTO people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClinicHistoryDTO)) {
            return false;
        }

        ClinicHistoryDTO clinicHistoryDTO = (ClinicHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clinicHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClinicHistoryDTO{" +
            "id=" + getId() +
            ", dateClinic='" + getDateClinic() + "'" +
            ", typeDisease=" + getTypeDisease() +
            ", people=" + getPeople() +
            "}";
    }
}
