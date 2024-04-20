package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.ClinicHistoryTestSamples.*;
import static co.edu.poli.salud.domain.MedicalAuthorizationTestSamples.*;
import static co.edu.poli.salud.domain.PeopleTestSamples.*;
import static co.edu.poli.salud.domain.TypeDiseasesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClinicHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClinicHistory.class);
        ClinicHistory clinicHistory1 = getClinicHistorySample1();
        ClinicHistory clinicHistory2 = new ClinicHistory();
        assertThat(clinicHistory1).isNotEqualTo(clinicHistory2);

        clinicHistory2.setId(clinicHistory1.getId());
        assertThat(clinicHistory1).isEqualTo(clinicHistory2);

        clinicHistory2 = getClinicHistorySample2();
        assertThat(clinicHistory1).isNotEqualTo(clinicHistory2);
    }

    @Test
    void medicalAuthorizationsTest() throws Exception {
        ClinicHistory clinicHistory = getClinicHistoryRandomSampleGenerator();
        MedicalAuthorization medicalAuthorizationBack = getMedicalAuthorizationRandomSampleGenerator();

        clinicHistory.addMedicalAuthorizations(medicalAuthorizationBack);
        assertThat(clinicHistory.getMedicalAuthorizations()).containsOnly(medicalAuthorizationBack);
        assertThat(medicalAuthorizationBack.getClinicHistory()).isEqualTo(clinicHistory);

        clinicHistory.removeMedicalAuthorizations(medicalAuthorizationBack);
        assertThat(clinicHistory.getMedicalAuthorizations()).doesNotContain(medicalAuthorizationBack);
        assertThat(medicalAuthorizationBack.getClinicHistory()).isNull();

        clinicHistory.medicalAuthorizations(new HashSet<>(Set.of(medicalAuthorizationBack)));
        assertThat(clinicHistory.getMedicalAuthorizations()).containsOnly(medicalAuthorizationBack);
        assertThat(medicalAuthorizationBack.getClinicHistory()).isEqualTo(clinicHistory);

        clinicHistory.setMedicalAuthorizations(new HashSet<>());
        assertThat(clinicHistory.getMedicalAuthorizations()).doesNotContain(medicalAuthorizationBack);
        assertThat(medicalAuthorizationBack.getClinicHistory()).isNull();
    }

    @Test
    void typeDiseaseTest() throws Exception {
        ClinicHistory clinicHistory = getClinicHistoryRandomSampleGenerator();
        TypeDiseases typeDiseasesBack = getTypeDiseasesRandomSampleGenerator();

        clinicHistory.setTypeDisease(typeDiseasesBack);
        assertThat(clinicHistory.getTypeDisease()).isEqualTo(typeDiseasesBack);

        clinicHistory.typeDisease(null);
        assertThat(clinicHistory.getTypeDisease()).isNull();
    }

    @Test
    void peopleTest() throws Exception {
        ClinicHistory clinicHistory = getClinicHistoryRandomSampleGenerator();
        People peopleBack = getPeopleRandomSampleGenerator();

        clinicHistory.setPeople(peopleBack);
        assertThat(clinicHistory.getPeople()).isEqualTo(peopleBack);

        clinicHistory.people(null);
        assertThat(clinicHistory.getPeople()).isNull();
    }
}
