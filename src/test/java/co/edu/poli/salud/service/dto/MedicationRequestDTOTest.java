package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationRequestDTO.class);
        MedicationRequestDTO medicationRequestDTO1 = new MedicationRequestDTO();
        medicationRequestDTO1.setId(1L);
        MedicationRequestDTO medicationRequestDTO2 = new MedicationRequestDTO();
        assertThat(medicationRequestDTO1).isNotEqualTo(medicationRequestDTO2);
        medicationRequestDTO2.setId(medicationRequestDTO1.getId());
        assertThat(medicationRequestDTO1).isEqualTo(medicationRequestDTO2);
        medicationRequestDTO2.setId(2L);
        assertThat(medicationRequestDTO1).isNotEqualTo(medicationRequestDTO2);
        medicationRequestDTO1.setId(null);
        assertThat(medicationRequestDTO1).isNotEqualTo(medicationRequestDTO2);
    }
}
