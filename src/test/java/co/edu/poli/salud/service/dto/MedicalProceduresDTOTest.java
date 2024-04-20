package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicalProceduresDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalProceduresDTO.class);
        MedicalProceduresDTO medicalProceduresDTO1 = new MedicalProceduresDTO();
        medicalProceduresDTO1.setId(1L);
        MedicalProceduresDTO medicalProceduresDTO2 = new MedicalProceduresDTO();
        assertThat(medicalProceduresDTO1).isNotEqualTo(medicalProceduresDTO2);
        medicalProceduresDTO2.setId(medicalProceduresDTO1.getId());
        assertThat(medicalProceduresDTO1).isEqualTo(medicalProceduresDTO2);
        medicalProceduresDTO2.setId(2L);
        assertThat(medicalProceduresDTO1).isNotEqualTo(medicalProceduresDTO2);
        medicalProceduresDTO1.setId(null);
        assertThat(medicalProceduresDTO1).isNotEqualTo(medicalProceduresDTO2);
    }
}
