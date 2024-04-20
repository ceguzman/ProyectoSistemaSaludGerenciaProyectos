package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.MedicalAuthorizationAsserts.*;
import static co.edu.poli.salud.domain.MedicalAuthorizationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalAuthorizationMapperTest {

    private MedicalAuthorizationMapper medicalAuthorizationMapper;

    @BeforeEach
    void setUp() {
        medicalAuthorizationMapper = new MedicalAuthorizationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalAuthorizationSample1();
        var actual = medicalAuthorizationMapper.toEntity(medicalAuthorizationMapper.toDto(expected));
        assertMedicalAuthorizationAllPropertiesEquals(expected, actual);
    }
}
