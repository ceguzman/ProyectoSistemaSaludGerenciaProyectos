package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.MedicalProcedures} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalProceduresDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String typeProcedures;

    @NotNull
    @Size(max = 50)
    private String description;

    @NotNull
    private LocalDate dateProcedures;

    @NotNull
    private MedicalAuthorizationDTO medicalAuthorization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeProcedures() {
        return typeProcedures;
    }

    public void setTypeProcedures(String typeProcedures) {
        this.typeProcedures = typeProcedures;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateProcedures() {
        return dateProcedures;
    }

    public void setDateProcedures(LocalDate dateProcedures) {
        this.dateProcedures = dateProcedures;
    }

    public MedicalAuthorizationDTO getMedicalAuthorization() {
        return medicalAuthorization;
    }

    public void setMedicalAuthorization(MedicalAuthorizationDTO medicalAuthorization) {
        this.medicalAuthorization = medicalAuthorization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalProceduresDTO)) {
            return false;
        }

        MedicalProceduresDTO medicalProceduresDTO = (MedicalProceduresDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalProceduresDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalProceduresDTO{" +
            "id=" + getId() +
            ", typeProcedures='" + getTypeProcedures() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateProcedures='" + getDateProcedures() + "'" +
            ", medicalAuthorization=" + getMedicalAuthorization() +
            "}";
    }
}
