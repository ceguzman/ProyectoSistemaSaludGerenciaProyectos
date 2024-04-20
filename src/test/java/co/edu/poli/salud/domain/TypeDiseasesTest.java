package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.ClinicHistoryTestSamples.*;
import static co.edu.poli.salud.domain.TypeDiseasesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TypeDiseasesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDiseases.class);
        TypeDiseases typeDiseases1 = getTypeDiseasesSample1();
        TypeDiseases typeDiseases2 = new TypeDiseases();
        assertThat(typeDiseases1).isNotEqualTo(typeDiseases2);

        typeDiseases2.setId(typeDiseases1.getId());
        assertThat(typeDiseases1).isEqualTo(typeDiseases2);

        typeDiseases2 = getTypeDiseasesSample2();
        assertThat(typeDiseases1).isNotEqualTo(typeDiseases2);
    }

    @Test
    void clinicHistorysTest() throws Exception {
        TypeDiseases typeDiseases = getTypeDiseasesRandomSampleGenerator();
        ClinicHistory clinicHistoryBack = getClinicHistoryRandomSampleGenerator();

        typeDiseases.addClinicHistorys(clinicHistoryBack);
        assertThat(typeDiseases.getClinicHistorys()).containsOnly(clinicHistoryBack);
        assertThat(clinicHistoryBack.getTypeDisease()).isEqualTo(typeDiseases);

        typeDiseases.removeClinicHistorys(clinicHistoryBack);
        assertThat(typeDiseases.getClinicHistorys()).doesNotContain(clinicHistoryBack);
        assertThat(clinicHistoryBack.getTypeDisease()).isNull();

        typeDiseases.clinicHistorys(new HashSet<>(Set.of(clinicHistoryBack)));
        assertThat(typeDiseases.getClinicHistorys()).containsOnly(clinicHistoryBack);
        assertThat(clinicHistoryBack.getTypeDisease()).isEqualTo(typeDiseases);

        typeDiseases.setClinicHistorys(new HashSet<>());
        assertThat(typeDiseases.getClinicHistorys()).doesNotContain(clinicHistoryBack);
        assertThat(clinicHistoryBack.getTypeDisease()).isNull();
    }
}
