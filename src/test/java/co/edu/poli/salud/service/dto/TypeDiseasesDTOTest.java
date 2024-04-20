package co.edu.poli.salud.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeDiseasesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDiseasesDTO.class);
        TypeDiseasesDTO typeDiseasesDTO1 = new TypeDiseasesDTO();
        typeDiseasesDTO1.setId(1L);
        TypeDiseasesDTO typeDiseasesDTO2 = new TypeDiseasesDTO();
        assertThat(typeDiseasesDTO1).isNotEqualTo(typeDiseasesDTO2);
        typeDiseasesDTO2.setId(typeDiseasesDTO1.getId());
        assertThat(typeDiseasesDTO1).isEqualTo(typeDiseasesDTO2);
        typeDiseasesDTO2.setId(2L);
        assertThat(typeDiseasesDTO1).isNotEqualTo(typeDiseasesDTO2);
        typeDiseasesDTO1.setId(null);
        assertThat(typeDiseasesDTO1).isNotEqualTo(typeDiseasesDTO2);
    }
}
