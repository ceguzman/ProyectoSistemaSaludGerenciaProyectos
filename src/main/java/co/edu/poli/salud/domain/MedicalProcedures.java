package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A MedicalProcedures.
 */
@Entity
@Table(name = "medical_procedures")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalProcedures implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "type_procedures", length = 50, nullable = false)
    private String typeProcedures;

    @NotNull
    @Size(max = 50)
    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_procedures", nullable = false)
    private LocalDate dateProcedures;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicalProcedures", "medicationRequests", "clinicHistory" }, allowSetters = true)
    private MedicalAuthorization medicalAuthorization;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalProcedures id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeProcedures() {
        return this.typeProcedures;
    }

    public MedicalProcedures typeProcedures(String typeProcedures) {
        this.setTypeProcedures(typeProcedures);
        return this;
    }

    public void setTypeProcedures(String typeProcedures) {
        this.typeProcedures = typeProcedures;
    }

    public String getDescription() {
        return this.description;
    }

    public MedicalProcedures description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateProcedures() {
        return this.dateProcedures;
    }

    public MedicalProcedures dateProcedures(LocalDate dateProcedures) {
        this.setDateProcedures(dateProcedures);
        return this;
    }

    public void setDateProcedures(LocalDate dateProcedures) {
        this.dateProcedures = dateProcedures;
    }

    public MedicalAuthorization getMedicalAuthorization() {
        return this.medicalAuthorization;
    }

    public void setMedicalAuthorization(MedicalAuthorization medicalAuthorization) {
        this.medicalAuthorization = medicalAuthorization;
    }

    public MedicalProcedures medicalAuthorization(MedicalAuthorization medicalAuthorization) {
        this.setMedicalAuthorization(medicalAuthorization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalProcedures)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalProcedures) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalProcedures{" +
            "id=" + getId() +
            ", typeProcedures='" + getTypeProcedures() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateProcedures='" + getDateProcedures() + "'" +
            "}";
    }
}
