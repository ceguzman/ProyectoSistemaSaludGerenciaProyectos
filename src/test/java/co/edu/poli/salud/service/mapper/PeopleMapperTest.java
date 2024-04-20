package co.edu.poli.salud.service.mapper;

import static co.edu.poli.salud.domain.PeopleAsserts.*;
import static co.edu.poli.salud.domain.PeopleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeopleMapperTest {

    private PeopleMapper peopleMapper;

    @BeforeEach
    void setUp() {
        peopleMapper = new PeopleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPeopleSample1();
        var actual = peopleMapper.toEntity(peopleMapper.toDto(expected));
        assertPeopleAllPropertiesEquals(expected, actual);
    }
}
