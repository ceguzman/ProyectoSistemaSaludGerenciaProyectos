package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.People} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeopleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String documentNumber;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String firstSurname;

    @Size(max = 50)
    private String secondName;

    @Size(max = 50)
    private String secondSurname;

    @NotNull
    private TypeDocumentDTO typeDocument;

    @NotNull
    private TypeSpecialistDTO typeSpecialist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public TypeDocumentDTO getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocumentDTO typeDocument) {
        this.typeDocument = typeDocument;
    }

    public TypeSpecialistDTO getTypeSpecialist() {
        return typeSpecialist;
    }

    public void setTypeSpecialist(TypeSpecialistDTO typeSpecialist) {
        this.typeSpecialist = typeSpecialist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeopleDTO)) {
            return false;
        }

        PeopleDTO peopleDTO = (PeopleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peopleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeopleDTO{" +
            "id=" + getId() +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", firstSurname='" + getFirstSurname() + "'" +
            ", secondName='" + getSecondName() + "'" +
            ", secondSurname='" + getSecondSurname() + "'" +
            ", typeDocument=" + getTypeDocument() +
            ", typeSpecialist=" + getTypeSpecialist() +
            "}";
    }
}
