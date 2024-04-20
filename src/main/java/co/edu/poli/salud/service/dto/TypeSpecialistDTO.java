package co.edu.poli.salud.service.dto;

import co.edu.poli.salud.domain.enumeration.State;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.TypeSpecialist} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeSpecialistDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String specialistType;

    @NotNull
    private State stateSpecialist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }

    public State getStateSpecialist() {
        return stateSpecialist;
    }

    public void setStateSpecialist(State stateSpecialist) {
        this.stateSpecialist = stateSpecialist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSpecialistDTO)) {
            return false;
        }

        TypeSpecialistDTO typeSpecialistDTO = (TypeSpecialistDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeSpecialistDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSpecialistDTO{" +
            "id=" + getId() +
            ", specialistType='" + getSpecialistType() + "'" +
            ", stateSpecialist='" + getStateSpecialist() + "'" +
            "}";
    }
}
