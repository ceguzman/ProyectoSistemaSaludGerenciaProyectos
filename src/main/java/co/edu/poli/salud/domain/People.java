package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A People.
 */
@Entity
@Table(name = "people")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "document_number", length = 255, nullable = false)
    private String documentNumber;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_surname", length = 50, nullable = false)
    private String firstSurname;

    @Size(max = 50)
    @Column(name = "second_name", length = 50)
    private String secondName;

    @Size(max = 50)
    @Column(name = "second_surname", length = 50)
    private String secondSurname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "people")
    @JsonIgnoreProperties(value = { "medicalAuthorizations", "typeDisease", "people" }, allowSetters = true)
    private Set<ClinicHistory> clinicHistorys = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private TypeDocument typeDocument;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "medicalsAppointments", "people" }, allowSetters = true)
    private TypeSpecialist typeSpecialist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public People id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public People documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public People firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstSurname() {
        return this.firstSurname;
    }

    public People firstSurname(String firstSurname) {
        this.setFirstSurname(firstSurname);
        return this;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public People secondName(String secondName) {
        this.setSecondName(secondName);
        return this;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondSurname() {
        return this.secondSurname;
    }

    public People secondSurname(String secondSurname) {
        this.setSecondSurname(secondSurname);
        return this;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public Set<ClinicHistory> getClinicHistorys() {
        return this.clinicHistorys;
    }

    public void setClinicHistorys(Set<ClinicHistory> clinicHistories) {
        if (this.clinicHistorys != null) {
            this.clinicHistorys.forEach(i -> i.setPeople(null));
        }
        if (clinicHistories != null) {
            clinicHistories.forEach(i -> i.setPeople(this));
        }
        this.clinicHistorys = clinicHistories;
    }

    public People clinicHistorys(Set<ClinicHistory> clinicHistories) {
        this.setClinicHistorys(clinicHistories);
        return this;
    }

    public People addClinicHistorys(ClinicHistory clinicHistory) {
        this.clinicHistorys.add(clinicHistory);
        clinicHistory.setPeople(this);
        return this;
    }

    public People removeClinicHistorys(ClinicHistory clinicHistory) {
        this.clinicHistorys.remove(clinicHistory);
        clinicHistory.setPeople(null);
        return this;
    }

    public TypeDocument getTypeDocument() {
        return this.typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public People typeDocument(TypeDocument typeDocument) {
        this.setTypeDocument(typeDocument);
        return this;
    }

    public TypeSpecialist getTypeSpecialist() {
        return this.typeSpecialist;
    }

    public void setTypeSpecialist(TypeSpecialist typeSpecialist) {
        this.typeSpecialist = typeSpecialist;
    }

    public People typeSpecialist(TypeSpecialist typeSpecialist) {
        this.setTypeSpecialist(typeSpecialist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof People)) {
            return false;
        }
        return getId() != null && getId().equals(((People) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "People{" +
            "id=" + getId() +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", firstSurname='" + getFirstSurname() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", secondSurname='" + getSecondSurname() + "'" +
            "}";
    }
}
