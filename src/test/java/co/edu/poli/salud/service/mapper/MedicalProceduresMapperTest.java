package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.MedicalProceduresAsserts.*;
import static co.edu.poli.salud.domain.MedicalProceduresTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalProceduresMapperTest {

    private MedicalProceduresMapper medicalProceduresMapper;

    @BeforeEach
    void setUp() {
        medicalProceduresMapper = new MedicalProceduresMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalProceduresSample1();
        var actual = medicalProceduresMapper.toEntity(medicalProceduresMapper.toDto(expected));
        assertMedicalProceduresAllPropertiesEquals(expected, actual);
    }
}
