package co.edu.poli.salud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeDiseases.
 */
@Entity
@Table(name = "type_diseases")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeDiseases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "diseases_type", length = 200, nullable = false, unique = true)
    private String diseasesType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeDisease")
    @JsonIgnoreProperties(value = { "medicalAuthorizations", "typeDisease", "people" }, allowSetters = true)
    private Set<ClinicHistory> clinicHistorys = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeDiseases id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiseasesType() {
        return this.diseasesType;
    }

    public TypeDiseases diseasesType(String diseasesType) {
        this.setDiseasesType(diseasesType);
        return this;
    }

    public void setDiseasesType(String diseasesType) {
        this.diseasesType = diseasesType;
    }

    public Set<ClinicHistory> getClinicHistorys() {
        return this.clinicHistorys;
    }

    public void setClinicHistorys(Set<ClinicHistory> clinicHistories) {
        if (this.clinicHistorys != null) {
            this.clinicHistorys.forEach(i -> i.setTypeDisease(null));
        }
        if (clinicHistories != null) {
            clinicHistories.forEach(i -> i.setTypeDisease(this));
        }
        this.clinicHistorys = clinicHistories;
    }

    public TypeDiseases clinicHistorys(Set<ClinicHistory> clinicHistories) {
        this.setClinicHistorys(clinicHistories);
        return this;
    }

    public TypeDiseases addClinicHistorys(ClinicHistory clinicHistory) {
        this.clinicHistorys.add(clinicHistory);
        clinicHistory.setTypeDisease(this);
        return this;
    }

    public TypeDiseases removeClinicHistorys(ClinicHistory clinicHistory) {
        this.clinicHistorys.remove(clinicHistory);
        clinicHistory.setTypeDisease(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeDiseases)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeDiseases) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeDiseases{" +
            "id=" + getId() +
            ", diseasesType='" + getDiseasesType() + "'" +
            "}";
    }
}
