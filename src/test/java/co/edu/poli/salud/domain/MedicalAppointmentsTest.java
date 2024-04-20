package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.MedicalAppointmentsTestSamples.*;
import static co.edu.poli.salud.domain.TypeSpecialistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalAppointmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalAppointments.class);
        MedicalAppointments medicalAppointments1 = getMedicalAppointmentsSample1();
        MedicalAppointments medicalAppointments2 = new MedicalAppointments();
        assertThat(medicalAppointments1).isNotEqualTo(medicalAppointments2);

        medicalAppointments2.setId(medicalAppointments1.getId());
        assertThat(medicalAppointments1).isEqualTo(medicalAppointments2);

        medicalAppointments2 = getMedicalAppointmentsSample2();
        assertThat(medicalAppointments1).isNotEqualTo(medicalAppointments2);
    }

    @Test
    void typeSpecialistTest() throws Exception {
        MedicalAppointments medicalAppointments = getMedicalAppointmentsRandomSampleGenerator();
        TypeSpecialist typeSpecialistBack = getTypeSpecialistRandomSampleGenerator();

        medicalAppointments.setTypeSpecialist(typeSpecialistBack);
        assertThat(medicalAppointments.getTypeSpecialist()).isEqualTo(typeSpecialistBack);

        medicalAppointments.typeSpecialist(null);
        assertThat(medicalAppointments.getTypeSpecialist()).isNull();
    }
}
