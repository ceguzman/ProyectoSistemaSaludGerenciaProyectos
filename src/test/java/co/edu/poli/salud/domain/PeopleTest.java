package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.ClinicHistoryTestSamples.*;
import static co.edu.poli.salud.domain.PeopleTestSamples.*;
import static co.edu.poli.salud.domain.TypeDocumentTestSamples.*;
import static co.edu.poli.salud.domain.TypeSpecialistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PeopleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(People.class);
        People people1 = getPeopleSample1();
        People people2 = new People();
        assertThat(people1).isNotEqualTo(people2);

        people2.setId(people1.getId());
        assertThat(people1).isEqualTo(people2);

        people2 = getPeopleSample2();
        assertThat(people1).isNotEqualTo(people2);
    }

    @Test
    void clinicHistorysTest() throws Exception {
        People people = getPeopleRandomSampleGenerator();
        ClinicHistory clinicHistoryBack = getClinicHistoryRandomSampleGenerator();

        people.addClinicHistorys(clinicHistoryBack);
        assertThat(people.getClinicHistorys()).containsOnly(clinicHistoryBack);
        assertThat(clinicHistoryBack.getPeople()).isEqualTo(people);

        people.removeClinicHistorys(clinicHistoryBack);
        assertThat(people.getClinicHistorys()).doesNotContain(clinicHistoryBack);
        assertThat(clinicHistoryBack.getPeople()).isNull();

        people.clinicHistorys(new HashSet<>(Set.of(clinicHistoryBack)));
        assertThat(people.getClinicHistorys()).containsOnly(clinicHistoryBack);
        assertThat(clinicHistoryBack.getPeople()).isEqualTo(people);

        people.setClinicHistorys(new HashSet<>());
        assertThat(people.getClinicHistorys()).doesNotContain(clinicHistoryBack);
        assertThat(clinicHistoryBack.getPeople()).isNull();
    }

    @Test
    void typeDocumentTest() throws Exception {
        People people = getPeopleRandomSampleGenerator();
        TypeDocument typeDocumentBack = getTypeDocumentRandomSampleGenerator();

        people.setTypeDocument(typeDocumentBack);
        assertThat(people.getTypeDocument()).isEqualTo(typeDocumentBack);

        people.typeDocument(null);
        assertThat(people.getTypeDocument()).isNull();
    }

    @Test
    void typeSpecialistTest() throws Exception {
        People people = getPeopleRandomSampleGenerator();
        TypeSpecialist typeSpecialistBack = getTypeSpecialistRandomSampleGenerator();

        people.setTypeSpecialist(typeSpecialistBack);
        assertThat(people.getTypeSpecialist()).isEqualTo(typeSpecialistBack);

        people.typeSpecialist(null);
        assertThat(people.getTypeSpecialist()).isNull();
    }
}
