package co.edu.poli.salud.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.poli.salud.domain.TypeDiseases} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDiseasesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String diseasesType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseasesType() {
        return diseasesType;
    }

    public void setDiseasesType(String diseasesType) {
        this.diseasesType = diseasesType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDiseasesDTO)) {
            return false;
        }

        TypeDiseasesDTO typeDiseasesDTO = (TypeDiseasesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeDiseasesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDiseasesDTO{" +
            "id=" + getId() +
            ", diseasesType='" + getDiseasesType() + "'" +
            "}";
    }
}
