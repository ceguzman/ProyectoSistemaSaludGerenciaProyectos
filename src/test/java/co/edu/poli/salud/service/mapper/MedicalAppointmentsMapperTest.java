package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.MedicalAppointmentsAsserts.*;
import static co.edu.poli.salud.domain.MedicalAppointmentsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicalAppointmentsMapperTest {

    private MedicalAppointmentsMapper medicalAppointmentsMapper;

    @BeforeEach
    void setUp() {
        medicalAppointmentsMapper = new MedicalAppointmentsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicalAppointmentsSample1();
        var actual = medicalAppointmentsMapper.toEntity(medicalAppointmentsMapper.toDto(expected));
        assertMedicalAppointmentsAllPropertiesEquals(expected, actual);
    }
}
