package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalAppointmentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalAppointmentsDTO.class);
        MedicalAppointmentsDTO medicalAppointmentsDTO1 = new MedicalAppointmentsDTO();
        medicalAppointmentsDTO1.setId(1L);
        MedicalAppointmentsDTO medicalAppointmentsDTO2 = new MedicalAppointmentsDTO();
        assertThat(medicalAppointmentsDTO1).isNotEqualTo(medicalAppointmentsDTO2);
        medicalAppointmentsDTO2.setId(medicalAppointmentsDTO1.getId());
        assertThat(medicalAppointmentsDTO1).isEqualTo(medicalAppointmentsDTO2);
        medicalAppointmentsDTO2.setId(2L);
        assertThat(medicalAppointmentsDTO1).isNotEqualTo(medicalAppointmentsDTO2);
        medicalAppointmentsDTO1.setId(null);
        assertThat(medicalAppointmentsDTO1).isNotEqualTo(medicalAppointmentsDTO2);
    }
}
