package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.MedicalAuthorizationTestSamples.*;
import static co.edu.poli.salud.domain.MedicalProceduresTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalProceduresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalProcedures.class);
        MedicalProcedures medicalProcedures1 = getMedicalProceduresSample1();
        MedicalProcedures medicalProcedures2 = new MedicalProcedures();
        assertThat(medicalProcedures1).isNotEqualTo(medicalProcedures2);

        medicalProcedures2.setId(medicalProcedures1.getId());
        assertThat(medicalProcedures1).isEqualTo(medicalProcedures2);

        medicalProcedures2 = getMedicalProceduresSample2();
        assertThat(medicalProcedures1).isNotEqualTo(medicalProcedures2);
    }

    @Test
    void medicalAuthorizationTest() throws Exception {
        MedicalProcedures medicalProcedures = getMedicalProceduresRandomSampleGenerator();
        MedicalAuthorization medicalAuthorizationBack = getMedicalAuthorizationRandomSampleGenerator();

        medicalProcedures.setMedicalAuthorization(medicalAuthorizationBack);
        assertThat(medicalProcedures.getMedicalAuthorization()).isEqualTo(medicalAuthorizationBack);

        medicalProcedures.medicalAuthorization(null);
        assertThat(medicalProcedures.getMedicalAuthorization()).isNull();
    }
}
