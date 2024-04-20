package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A MedicalAppointments.
 */
@Entity
@Table(name = "medical_appointments")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalAppointments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_medical", nullable = false)
    private LocalDate dateMedical;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicalsAppointments", "people" }, allowSetters = true)
    private TypeSpecialist typeSpecialist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedicalAppointments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateMedical() {
        return this.dateMedical;
    }

    public MedicalAppointments dateMedical(LocalDate dateMedical) {
        this.setDateMedical(dateMedical);
        return this;
    }

    public void setDateMedical(LocalDate dateMedical) {
        this.dateMedical = dateMedical;
    }

    public TypeSpecialist getTypeSpecialist() {
        return this.typeSpecialist;
    }

    public void setTypeSpecialist(TypeSpecialist typeSpecialist) {
        this.typeSpecialist = typeSpecialist;
    }

    public MedicalAppointments typeSpecialist(TypeSpecialist typeSpecialist) {
        this.setTypeSpecialist(typeSpecialist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicalAppointments)) {
            return false;
        }
        return getId() != null && getId().equals(((MedicalAppointments) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalAppointments{" +
            "id=" + getId() +
            ", dateMedical='" + getDateMedical() + "'" +
            "}";
    }
}
