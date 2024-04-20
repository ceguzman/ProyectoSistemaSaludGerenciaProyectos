package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeSpecialistDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSpecialistDTO.class);
        TypeSpecialistDTO typeSpecialistDTO1 = new TypeSpecialistDTO();
        typeSpecialistDTO1.setId(1L);
        TypeSpecialistDTO typeSpecialistDTO2 = new TypeSpecialistDTO();
        assertThat(typeSpecialistDTO1).isNotEqualTo(typeSpecialistDTO2);
        typeSpecialistDTO2.setId(typeSpecialistDTO1.getId());
        assertThat(typeSpecialistDTO1).isEqualTo(typeSpecialistDTO2);
        typeSpecialistDTO2.setId(2L);
        assertThat(typeSpecialistDTO1).isNotEqualTo(typeSpecialistDTO2);
        typeSpecialistDTO1.setId(null);
        assertThat(typeSpecialistDTO1).isNotEqualTo(typeSpecialistDTO2);
    }
}
