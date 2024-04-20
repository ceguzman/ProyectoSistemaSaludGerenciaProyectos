package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalAuthorizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalAuthorizationDTO.class);
        MedicalAuthorizationDTO medicalAuthorizationDTO1 = new MedicalAuthorizationDTO();
        medicalAuthorizationDTO1.setId(1L);
        MedicalAuthorizationDTO medicalAuthorizationDTO2 = new MedicalAuthorizationDTO();
        assertThat(medicalAuthorizationDTO1).isNotEqualTo(medicalAuthorizationDTO2);
        medicalAuthorizationDTO2.setId(medicalAuthorizationDTO1.getId());
        assertThat(medicalAuthorizationDTO1).isEqualTo(medicalAuthorizationDTO2);
        medicalAuthorizationDTO2.setId(2L);
        assertThat(medicalAuthorizationDTO1).isNotEqualTo(medicalAuthorizationDTO2);
        medicalAuthorizationDTO1.setId(null);
        assertThat(medicalAuthorizationDTO1).isNotEqualTo(medicalAuthorizationDTO2);
    }
}
