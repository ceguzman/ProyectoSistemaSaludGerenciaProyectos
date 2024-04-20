package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A ClinicHistory.
 */
@Entity
@Table(name = "clinic_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClinicHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_clinic", nullable = false)
    private LocalDate dateClinic;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clinicHistory")
    @JsonIgnoreProperties(value = { "medicalProcedures", "medicationRequests", "clinicHistory" }, allowSetters = true)
    private Set<MedicalAuthorization> medicalAuthorizations = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "clinicHistorys" }, allowSetters = true)
    private TypeDiseases typeDisease;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "clinicHistorys", "typeDocument", "typeSpecialist" }, allowSetters = true)
    private People people;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClinicHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateClinic() {
        return this.dateClinic;
    }

    public ClinicHistory dateClinic(LocalDate dateClinic) {
        this.setDateClinic(dateClinic);
        return this;
    }

    public void setDateClinic(LocalDate dateClinic) {
        this.dateClinic = dateClinic;
    }

    public Set<MedicalAuthorization> getMedicalAuthorizations() {
        return this.medicalAuthorizations;
    }

    public void setMedicalAuthorizations(Set<MedicalAuthorization> medicalAuthorizations) {
        if (this.medicalAuthorizations != null) {
            this.medicalAuthorizations.forEach(i -> i.setClinicHistory(null));
        }
        if (medicalAuthorizations != null) {
            medicalAuthorizations.forEach(i -> i.setClinicHistory(this));
        }
        this.medicalAuthorizations = medicalAuthorizations;
    }

    public ClinicHistory medicalAuthorizations(Set<MedicalAuthorization> medicalAuthorizations) {
        this.setMedicalAuthorizations(medicalAuthorizations);
        return this;
    }

    public ClinicHistory addMedicalAuthorizations(MedicalAuthorization medicalAuthorization) {
        this.medicalAuthorizations.add(medicalAuthorization);
        medicalAuthorization.setClinicHistory(this);
        return this;
    }

    public ClinicHistory removeMedicalAuthorizations(MedicalAuthorization medicalAuthorization) {
        this.medicalAuthorizations.remove(medicalAuthorization);
        medicalAuthorization.setClinicHistory(null);
        return this;
    }

    public TypeDiseases getTypeDisease() {
        return this.typeDisease;
    }

    public void setTypeDisease(TypeDiseases typeDiseases) {
        this.typeDisease = typeDiseases;
    }

    public ClinicHistory typeDisease(TypeDiseases typeDiseases) {
        this.setTypeDisease(typeDiseases);
        return this;
    }

    public People getPeople() {
        return this.people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public ClinicHistory people(People people) {
        this.setPeople(people);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClinicHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ClinicHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClinicHistory{" +
            "id=" + getId() +
            ", dateClinic='" + getDateClinic() + "'" +
            "}";
    }
}
