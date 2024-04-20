package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClinicHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClinicHistoryDTO.class);
        ClinicHistoryDTO clinicHistoryDTO1 = new ClinicHistoryDTO();
        clinicHistoryDTO1.setId(1L);
        ClinicHistoryDTO clinicHistoryDTO2 = new ClinicHistoryDTO();
        assertThat(clinicHistoryDTO1).isNotEqualTo(clinicHistoryDTO2);
        clinicHistoryDTO2.setId(clinicHistoryDTO1.getId());
        assertThat(clinicHistoryDTO1).isEqualTo(clinicHistoryDTO2);
        clinicHistoryDTO2.setId(2L);
        assertThat(clinicHistoryDTO1).isNotEqualTo(clinicHistoryDTO2);
        clinicHistoryDTO1.setId(null);
        assertThat(clinicHistoryDTO1).isNotEqualTo(clinicHistoryDTO2);
    }
}
