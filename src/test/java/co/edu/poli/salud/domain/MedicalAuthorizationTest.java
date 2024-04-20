package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.ClinicHistoryTestSamples.*;
import static co.edu.poli.salud.domain.MedicalAuthorizationTestSamples.*;
import static co.edu.poli.salud.domain.MedicalProceduresTestSamples.*;
import static co.edu.poli.salud.domain.MedicationRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicalAuthorizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalAuthorization.class);
        MedicalAuthorization medicalAuthorization1 = getMedicalAuthorizationSample1();
        MedicalAuthorization medicalAuthorization2 = new MedicalAuthorization();
        assertThat(medicalAuthorization1).isNotEqualTo(medicalAuthorization2);

        medicalAuthorization2.setId(medicalAuthorization1.getId());
        assertThat(medicalAuthorization1).isEqualTo(medicalAuthorization2);

        medicalAuthorization2 = getMedicalAuthorizationSample2();
        assertThat(medicalAuthorization1).isNotEqualTo(medicalAuthorization2);
    }

    @Test
    void medicalProceduresTest() throws Exception {
        MedicalAuthorization medicalAuthorization = getMedicalAuthorizationRandomSampleGenerator();
        MedicalProcedures medicalProceduresBack = getMedicalProceduresRandomSampleGenerator();

        medicalAuthorization.addMedicalProcedures(medicalProceduresBack);
        assertThat(medicalAuthorization.getMedicalProcedures()).containsOnly(medicalProceduresBack);
        assertThat(medicalProceduresBack.getMedicalAuthorization()).isEqualTo(medicalAuthorization);

        medicalAuthorization.removeMedicalProcedures(medicalProceduresBack);
        assertThat(medicalAuthorization.getMedicalProcedures()).doesNotContain(medicalProceduresBack);
        assertThat(medicalProceduresBack.getMedicalAuthorization()).isNull();

        medicalAuthorization.medicalProcedures(new HashSet<>(Set.of(medicalProceduresBack)));
        assertThat(medicalAuthorization.getMedicalProcedures()).containsOnly(medicalProceduresBack);
        assertThat(medicalProceduresBack.getMedicalAuthorization()).isEqualTo(medicalAuthorization);

        medicalAuthorization.setMedicalProcedures(new HashSet<>());
        assertThat(medicalAuthorization.getMedicalProcedures()).doesNotContain(medicalProceduresBack);
        assertThat(medicalProceduresBack.getMedicalAuthorization()).isNull();
    }

    @Test
    void medicationRequestsTest() throws Exception {
        MedicalAuthorization medicalAuthorization = getMedicalAuthorizationRandomSampleGenerator();
        MedicationRequest medicationRequestBack = getMedicationRequestRandomSampleGenerator();

        medicalAuthorization.addMedicationRequests(medicationRequestBack);
        assertThat(medicalAuthorization.getMedicationRequests()).containsOnly(medicationRequestBack);
        assertThat(medicationRequestBack.getMedicalAuthorization()).isEqualTo(medicalAuthorization);

        medicalAuthorization.removeMedicationRequests(medicationRequestBack);
        assertThat(medicalAuthorization.getMedicationRequests()).doesNotContain(medicationRequestBack);
        assertThat(medicationRequestBack.getMedicalAuthorization()).isNull();

        medicalAuthorization.medicationRequests(new HashSet<>(Set.of(medicationRequestBack)));
        assertThat(medicalAuthorization.getMedicationRequests()).containsOnly(medicationRequestBack);
        assertThat(medicationRequestBack.getMedicalAuthorization()).isEqualTo(medicalAuthorization);

        medicalAuthorization.setMedicationRequests(new HashSet<>());
        assertThat(medicalAuthorization.getMedicationRequests()).doesNotContain(medicationRequestBack);
        assertThat(medicationRequestBack.getMedicalAuthorization()).isNull();
    }

    @Test
    void clinicHistoryTest() throws Exception {
        MedicalAuthorization medicalAuthorization = getMedicalAuthorizationRandomSampleGenerator();
        ClinicHistory clinicHistoryBack = getClinicHistoryRandomSampleGenerator();

        medicalAuthorization.setClinicHistory(clinicHistoryBack);
        assertThat(medicalAuthorization.getClinicHistory()).isEqualTo(clinicHistoryBack);

        medicalAuthorization.clinicHistory(null);
        assertThat(medicalAuthorization.getClinicHistory()).isNull();
    }
}
