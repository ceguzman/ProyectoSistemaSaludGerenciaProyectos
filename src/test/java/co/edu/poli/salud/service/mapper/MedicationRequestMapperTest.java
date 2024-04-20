package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.MedicationRequestAsserts.*;
import static co.edu.poli.salud.domain.MedicationRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationRequestMapperTest {

    private MedicationRequestMapper medicationRequestMapper;

    @BeforeEach
    void setUp() {
        medicationRequestMapper = new MedicationRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationRequestSample1();
        var actual = medicationRequestMapper.toEntity(medicationRequestMapper.toDto(expected));
        assertMedicationRequestAllPropertiesEquals(expected, actual);
    }
}
