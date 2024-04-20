package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.TypeDiseasesAsserts.*;
import static co.edu.poli.salud.domain.TypeDiseasesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeDiseasesMapperTest {

    private TypeDiseasesMapper typeDiseasesMapper;

    @BeforeEach
    void setUp() {
        typeDiseasesMapper = new TypeDiseasesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeDiseasesSample1();
        var actual = typeDiseasesMapper.toEntity(typeDiseasesMapper.toDto(expected));
        assertTypeDiseasesAllPropertiesEquals(expected, actual);
    }
}
