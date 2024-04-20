package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.ClinicHistoryAsserts.*;
import static co.edu.poli.salud.domain.ClinicHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClinicHistoryMapperTest {

    private ClinicHistoryMapper clinicHistoryMapper;

    @BeforeEach
    void setUp() {
        clinicHistoryMapper = new ClinicHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClinicHistorySample1();
        var actual = clinicHistoryMapper.toEntity(clinicHistoryMapper.toDto(expected));
        assertClinicHistoryAllPropertiesEquals(expected, actual);
    }
}
