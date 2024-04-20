package co.edu.poli.salud.domain;

import co.edu.poli.salud.domain.enumeration.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeSpecialist.
 */
@Entity
@Table(name = "type_specialist")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeSpecialist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "specialist_type", length = 50, nullable = false, unique = true)
    private String specialistType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_specialist", nullable = false)
    private State stateSpecialist;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeSpecialist")
    @JsonIgnoreProperties(value = { "typeSpecialist" }, allowSetters = true)
    private Set<MedicalAppointments> medicalsAppointments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeSpecialist")
    @JsonIgnoreProperties(value = { "clinicHistorys", "typeDocument", "typeSpecialist" }, allowSetters = true)
    private Set<People> people = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeSpecialist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialistType() {
        return this.specialistType;
    }

    public TypeSpecialist specialistType(String specialistType) {
        this.setSpecialistType(specialistType);
        return this;
    }

    public void setSpecialistType(String specialistType) {
        this.specialistType = specialistType;
    }

    public State getStateSpecialist() {
        return this.stateSpecialist;
    }

    public TypeSpecialist stateSpecialist(State stateSpecialist) {
        this.setStateSpecialist(stateSpecialist);
        return this;
    }

    public void setStateSpecialist(State stateSpecialist) {
        this.stateSpecialist = stateSpecialist;
    }

    public Set<MedicalAppointments> getMedicalsAppointments() {
        return this.medicalsAppointments;
    }

    public void setMedicalsAppointments(Set<MedicalAppointments> medicalAppointments) {
        if (this.medicalsAppointments != null) {
            this.medicalsAppointments.forEach(i -> i.setTypeSpecialist(null));
        }
        if (medicalAppointments != null) {
            medicalAppointments.forEach(i -> i.setTypeSpecialist(this));
        }
        this.medicalsAppointments = medicalAppointments;
    }

    public TypeSpecialist medicalsAppointments(Set<MedicalAppointments> medicalAppointments) {
        this.setMedicalsAppointments(medicalAppointments);
        return this;
    }

    public TypeSpecialist addMedicalsAppointments(MedicalAppointments medicalAppointments) {
        this.medicalsAppointments.add(medicalAppointments);
        medicalAppointments.setTypeSpecialist(this);
        return this;
    }

    public TypeSpecialist removeMedicalsAppointments(MedicalAppointments medicalAppointments) {
        this.medicalsAppointments.remove(medicalAppointments);
        medicalAppointments.setTypeSpecialist(null);
        return this;
    }

    public Set<People> getPeople() {
        return this.people;
    }

    public void setPeople(Set<People> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setTypeSpecialist(null));
        }
        if (people != null) {
            people.forEach(i -> i.setTypeSpecialist(this));
        }
        this.people = people;
    }

    public TypeSpecialist people(Set<People> people) {
        this.setPeople(people);
        return this;
    }

    public TypeSpecialist addPeople(People people) {
        this.people.add(people);
        people.setTypeSpecialist(this);
        return this;
    }

    public TypeSpecialist removePeople(People people) {
        this.people.remove(people);
        people.setTypeSpecialist(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSpecialist)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeSpecialist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSpecialist{" +
            "id=" + getId() +
            ", specialistType='" + getSpecialistType() + "'" +
            ", stateSpecialist='" + getStateSpecialist() + "'" +
            "}";
    }
}
