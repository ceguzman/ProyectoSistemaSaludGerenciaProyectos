package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.MedicationRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationRequestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Max(value = 10)
    private Integer amount;

    @Max(value = 10)
    private Integer milligrams;

    @NotNull
    private MedicalAuthorizationDTO medicalAuthorization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getMilligrams() {
        return milligrams;
    }

    public void setMilligrams(Integer milligrams) {
        this.milligrams = milligrams;
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
        if (!(o instanceof MedicationRequestDTO)) {
            return false;
        }

        MedicationRequestDTO medicationRequestDTO = (MedicationRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationRequestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", milligrams=" + getMilligrams() +
            ", medicalAuthorization=" + getMedicalAuthorization() +
            "}";
    }
}
