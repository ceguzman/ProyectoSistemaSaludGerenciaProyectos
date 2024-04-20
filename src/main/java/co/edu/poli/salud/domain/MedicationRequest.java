package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A MedicationRequest.
 */
@Entity
@Table(name = "medication_request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Max(value = 10)
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Max(value = 10)
    @Column(name = "milligrams")
    private Integer milligrams;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicalProcedures", "medicationRequests", "clinicHistory" }, allowSetters = true)
    private MedicalAuthorization medicalAuthorization;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicationRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MedicationRequest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public MedicationRequest amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getMilligrams() {
        return this.milligrams;
    }

    public MedicationRequest milligrams(Integer milligrams) {
        this.setMilligrams(milligrams);
        return this;
    }

    public void setMilligrams(Integer milligrams) {
        this.milligrams = milligrams;
    }

    public MedicalAuthorization getMedicalAuthorization() {
        return this.medicalAuthorization;
    }

    public void setMedicalAuthorization(MedicalAuthorization medicalAuthorization) {
        this.medicalAuthorization = medicalAuthorization;
    }

    public MedicationRequest medicalAuthorization(MedicalAuthorization medicalAuthorization) {
        this.setMedicalAuthorization(medicalAuthorization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicationRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amount=" + getAmount() +
            ", milligrams=" + getMilligrams() +
            "}";
    }
}
