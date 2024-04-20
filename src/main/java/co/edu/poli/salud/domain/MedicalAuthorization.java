package co.edu.poli.salud.domain;

import co.edu.poli.salud.domain.enumeration.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A MedicalAuthorization.
 */
@Entity
@Table(name = "medical_authorization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalAuthorization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "detail_authorization", length = 50, nullable = false)
    private String detailAuthorization;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_authorization", nullable = false)
    private State stateAuthorization;

    @NotNull
    @Column(name = "date_authorization", nullable = false)
    private LocalDate dateAuthorization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "medicalAuthorization")
    @JsonIgnoreProperties(value = { "medicalAuthorization" }, allowSetters = true)
    private Set<MedicalProcedures> medicalProcedures = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "medicalAuthorization")
    @JsonIgnoreProperties(value = { "medicalAuthorization" }, allowSetters = true)
    private Set<MedicationRequest> medicationRequests = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicalAuthorizations", "typeDisease", "people" }, allowSetters = true)
    private ClinicHistory clinicHistory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalAuthorization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailAuthorization() {
        return this.detailAuthorization;
    }

    public MedicalAuthorization detailAuthorization(String detailAuthorization) {
        this.setDetailAuthorization(detailAuthorization);
        return this;
    }

    public void setDetailAuthorization(String detailAuthorization) {
        this.detailAuthorization = detailAuthorization;
    }

    public State getStateAuthorization() {
        return this.stateAuthorization;
    }

    public MedicalAuthorization stateAuthorization(State stateAuthorization) {
        this.setStateAuthorization(stateAuthorization);
        return this;
    }

    public void setStateAuthorization(State stateAuthorization) {
        this.stateAuthorization = stateAuthorization;
    }

    public LocalDate getDateAuthorization() {
        return this.dateAuthorization;
    }

    public MedicalAuthorization dateAuthorization(LocalDate dateAuthorization) {
        this.setDateAuthorization(dateAuthorization);
        return this;
    }

    public void setDateAuthorization(LocalDate dateAuthorization) {
        this.dateAuthorization = dateAuthorization;
    }

    public Set<MedicalProcedures> getMedicalProcedures() {
        return this.medicalProcedures;
    }

    public void setMedicalProcedures(Set<MedicalProcedures> medicalProcedures) {
        if (this.medicalProcedures != null) {
            this.medicalProcedures.forEach(i -> i.setMedicalAuthorization(null));
        }
        if (medicalProcedures != null) {
            medicalProcedures.forEach(i -> i.setMedicalAuthorization(this));
        }
        this.medicalProcedures = medicalProcedures;
    }

    public MedicalAuthorization medicalProcedures(Set<MedicalProcedures> medicalProcedures) {
        this.setMedicalProcedures(medicalProcedures);
        return this;
    }

    public MedicalAuthorization addMedicalProcedures(MedicalProcedures medicalProcedures) {
        this.medicalProcedures.add(medicalProcedures);
        medicalProcedures.setMedicalAuthorization(this);
        return this;
    }

    public MedicalAuthorization removeMedicalProcedures(MedicalProcedures medicalProcedures) {
        this.medicalProcedures.remove(medicalProcedures);
        medicalProcedures.setMedicalAuthorization(null);
        return this;
    }

    public Set<MedicationRequest> getMedicationRequests() {
        return this.medicationRequests;
    }

    public void setMedicationRequests(Set<MedicationRequest> medicationRequests) {
        if (this.medicationRequests != null) {
            this.medicationRequests.forEach(i -> i.setMedicalAuthorization(null));
        }
        if (medicationRequests != null) {
            medicationRequests.forEach(i -> i.setMedicalAuthorization(this));
        }
        this.medicationRequests = medicationRequests;
    }

    public MedicalAuthorization medicationRequests(Set<MedicationRequest> medicationRequests) {
        this.setMedicationRequests(medicationRequests);
        return this;
    }

    public MedicalAuthorization addMedicationRequests(MedicationRequest medicationRequest) {
        this.medicationRequests.add(medicationRequest);
        medicationRequest.setMedicalAuthorization(this);
        return this;
    }

    public MedicalAuthorization removeMedicationRequests(MedicationRequest medicationRequest) {
        this.medicationRequests.remove(medicationRequest);
        medicationRequest.setMedicalAuthorization(null);
        return this;
    }

    public ClinicHistory getClinicHistory() {
        return this.clinicHistory;
    }

    public void setClinicHistory(ClinicHistory clinicHistory) {
        this.clinicHistory = clinicHistory;
    }

    public MedicalAuthorization clinicHistory(ClinicHistory clinicHistory) {
        this.setClinicHistory(clinicHistory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalAuthorization)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalAuthorization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalAuthorization{" +
            "id=" + getId() +
            ", detailAuthorization='" + getDetailAuthorization() + "'" +
            ", stateAuthorization='" + getStateAuthorization() + "'" +
            ", dateAuthorization='" + getDateAuthorization() + "'" +
            "}";
    }
}
