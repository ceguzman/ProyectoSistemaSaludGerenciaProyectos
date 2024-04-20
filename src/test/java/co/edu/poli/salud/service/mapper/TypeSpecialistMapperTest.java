package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.TypeSpecialistAsserts.*;
import static co.edu.poli.salud.domain.TypeSpecialistTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeSpecialistMapperTest {

    private TypeSpecialistMapper typeSpecialistMapper;

    @BeforeEach
    void setUp() {
        typeSpecialistMapper = new TypeSpecialistMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeSpecialistSample1();
        var actual = typeSpecialistMapper.toEntity(typeSpecialistMapper.toDto(expected));
        assertTypeSpecialistAllPropertiesEquals(expected, actual);
    }
}
