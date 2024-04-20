package co.edu.poli.salud.service.dto;

import co.edu.poli.salud.domain.enumeration.State;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.MedicalAuthorization} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalAuthorizationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String detailAuthorization;

    @NotNull
    private State stateAuthorization;

    @NotNull
    private LocalDate dateAuthorization;

    @NotNull
    private ClinicHistoryDTO clinicHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailAuthorization() {
        return detailAuthorization;
    }

    public void setDetailAuthorization(String detailAuthorization) {
        this.detailAuthorization = detailAuthorization;
    }

    public State getStateAuthorization() {
        return stateAuthorization;
    }

    public void setStateAuthorization(State stateAuthorization) {
        this.stateAuthorization = stateAuthorization;
    }

    public LocalDate getDateAuthorization() {
        return dateAuthorization;
    }

    public void setDateAuthorization(LocalDate dateAuthorization) {
        this.dateAuthorization = dateAuthorization;
    }

    public ClinicHistoryDTO getClinicHistory() {
        return clinicHistory;
    }

    public void setClinicHistory(ClinicHistoryDTO clinicHistory) {
        this.clinicHistory = clinicHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalAuthorizationDTO)) {
            return false;
        }

        MedicalAuthorizationDTO medicalAuthorizationDTO = (MedicalAuthorizationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicalAuthorizationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalAuthorizationDTO{" +
            "id=" + getId() +
            ", detailAuthorization='" + getDetailAuthorization() + "'" +
            ", stateAuthorization='" + getStateAuthorization() + "'" +
            ", dateAuthorization='" + getDateAuthorization() + "'" +
            ", clinicHistory=" + getClinicHistory() +
            "}";
    }
}
