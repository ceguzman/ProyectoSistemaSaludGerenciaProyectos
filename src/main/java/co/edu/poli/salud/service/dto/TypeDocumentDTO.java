package co.edu.poli.salud.service.dto;

import co.edu.poli.salud.domain.enumeration.State;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.TypeDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String initials;

    @NotNull
    @Size(max = 20)
    private String documentName;

    @NotNull
    private State stateTypeDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public State getStateTypeDocument() {
        return stateTypeDocument;
    }

    public void setStateTypeDocument(State stateTypeDocument) {
        this.stateTypeDocument = stateTypeDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDocumentDTO)) {
            return false;
        }

        TypeDocumentDTO typeDocumentDTO = (TypeDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDocumentDTO{" +
            "id=" + getId() +
            ", initials='" + getInitials() + "'" +
            ", documentName='" + getDocumentName() + "'" +
            ", stateTypeDocument='" + getStateTypeDocument() + "'" +
            "}";
    }
}
