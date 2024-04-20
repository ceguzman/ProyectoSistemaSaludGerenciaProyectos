package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.MedicalAuthorizationTestSamples.*;
import static co.edu.poli.salud.domain.MedicationRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationRequest.class);
        MedicationRequest medicationRequest1 = getMedicationRequestSample1();
        MedicationRequest medicationRequest2 = new MedicationRequest();
        assertThat(medicationRequest1).isNotEqualTo(medicationRequest2);

        medicationRequest2.setId(medicationRequest1.getId());
        assertThat(medicationRequest1).isEqualTo(medicationRequest2);

        medicationRequest2 = getMedicationRequestSample2();
        assertThat(medicationRequest1).isNotEqualTo(medicationRequest2);
    }

    @Test
    void medicalAuthorizationTest() throws Exception {
        MedicationRequest medicationRequest = getMedicationRequestRandomSampleGenerator();
        MedicalAuthorization medicalAuthorizationBack = getMedicalAuthorizationRandomSampleGenerator();

        medicationRequest.setMedicalAuthorization(medicalAuthorizationBack);
        assertThat(medicationRequest.getMedicalAuthorization()).isEqualTo(medicalAuthorizationBack);

        medicationRequest.medicalAuthorization(null);
        assertThat(medicationRequest.getMedicalAuthorization()).isNull();
    }
}
