package co.edu.poli.salud.domain;

import co.edu.poli.salud.domain.enumeration.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeDocument.
 */
@Entity
@Table(name = "type_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "initials", length = 20, nullable = false, unique = true)
    private String initials;

    @NotNull
    @Size(max = 20)
    @Column(name = "document_name", length = 20, nullable = false, unique = true)
    private String documentName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state_type_document", nullable = false)
    private State stateTypeDocument;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeDocument")
    @JsonIgnoreProperties(value = { "clinicHistorys", "typeDocument", "typeSpecialist" }, allowSetters = true)
    private Set<People> people = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitials() {
        return this.initials;
    }

    public TypeDocument initials(String initials) {
        this.setInitials(initials);
        return this;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public TypeDocument documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public State getStateTypeDocument() {
        return this.stateTypeDocument;
    }

    public TypeDocument stateTypeDocument(State stateTypeDocument) {
        this.setStateTypeDocument(stateTypeDocument);
        return this;
    }

    public void setStateTypeDocument(State stateTypeDocument) {
        this.stateTypeDocument = stateTypeDocument;
    }

    public Set<People> getPeople() {
        return this.people;
    }

    public void setPeople(Set<People> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setTypeDocument(null));
        }
        if (people != null) {
            people.forEach(i -> i.setTypeDocument(this));
        }
        this.people = people;
    }

    public TypeDocument people(Set<People> people) {
        this.setPeople(people);
        return this;
    }

    public TypeDocument addPeople(People people) {
        this.people.add(people);
        people.setTypeDocument(this);
        return this;
    }

    public TypeDocument removePeople(People people) {
        this.people.remove(people);
        people.setTypeDocument(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDocument{" +
            "id=" + getId() +
            ", initials='" + getInitials() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", stateTypeDocument='" + getStateTypeDocument() + "'" +
            "}";
    }
}
