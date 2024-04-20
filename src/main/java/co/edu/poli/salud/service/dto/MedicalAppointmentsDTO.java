package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.MedicalAppointments} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalAppointmentsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateMedical;

    @NotNull
    private TypeSpecialistDTO typeSpecialist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateMedical() {
        return dateMedical;
    }

    public void setDateMedical(LocalDate dateMedical) {
        this.dateMedical = dateMedical;
    }

    public TypeSpecialistDTO getTypeSpecialist() {
        return typeSpecialist;
    }

    public void setTypeSpecialist(TypeSpecialistDTO typeSpecialist) {
        this.typeSpecialist = typeSpecialist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalAppointmentsDTO)) {
            return false;
        }

        MedicalAppointmentsDTO medicalAppointmentsDTO = (MedicalAppointmentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalAppointmentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalAppointmentsDTO{" +
            "id=" + getId() +
            ", dateMedical='" + getDateMedical() + "'" +
            ", typeSpecialist=" + getTypeSpecialist() +
            "}";
    }
}
