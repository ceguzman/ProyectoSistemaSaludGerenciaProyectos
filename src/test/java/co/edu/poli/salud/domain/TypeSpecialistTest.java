package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.MedicalAppointmentsTestSamples.*;
import static co.edu.poli.salud.domain.PeopleTestSamples.*;
import static co.edu.poli.salud.domain.TypeSpecialistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TypeSpecialistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSpecialist.class);
        TypeSpecialist typeSpecialist1 = getTypeSpecialistSample1();
        TypeSpecialist typeSpecialist2 = new TypeSpecialist();
        assertThat(typeSpecialist1).isNotEqualTo(typeSpecialist2);

        typeSpecialist2.setId(typeSpecialist1.getId());
        assertThat(typeSpecialist1).isEqualTo(typeSpecialist2);

        typeSpecialist2 = getTypeSpecialistSample2();
        assertThat(typeSpecialist1).isNotEqualTo(typeSpecialist2);
    }

    @Test
    void medicalsAppointmentsTest() throws Exception {
        TypeSpecialist typeSpecialist = getTypeSpecialistRandomSampleGenerator();
        MedicalAppointments medicalAppointmentsBack = getMedicalAppointmentsRandomSampleGenerator();

        typeSpecialist.addMedicalsAppointments(medicalAppointmentsBack);
        assertThat(typeSpecialist.getMedicalsAppointments()).containsOnly(medicalAppointmentsBack);
        assertThat(medicalAppointmentsBack.getTypeSpecialist()).isEqualTo(typeSpecialist);

        typeSpecialist.removeMedicalsAppointments(medicalAppointmentsBack);
        assertThat(typeSpecialist.getMedicalsAppointments()).doesNotContain(medicalAppointmentsBack);
        assertThat(medicalAppointmentsBack.getTypeSpecialist()).isNull();

        typeSpecialist.medicalsAppointments(new HashSet<>(Set.of(medicalAppointmentsBack)));
        assertThat(typeSpecialist.getMedicalsAppointments()).containsOnly(medicalAppointmentsBack);
        assertThat(medicalAppointmentsBack.getTypeSpecialist()).isEqualTo(typeSpecialist);

        typeSpecialist.setMedicalsAppointments(new HashSet<>());
        assertThat(typeSpecialist.getMedicalsAppointments()).doesNotContain(medicalAppointmentsBack);
        assertThat(medicalAppointmentsBack.getTypeSpecialist()).isNull();
    }

    @Test
    void peopleTest() throws Exception {
        TypeSpecialist typeSpecialist = getTypeSpecialistRandomSampleGenerator();
        People peopleBack = getPeopleRandomSampleGenerator();

        typeSpecialist.addPeople(peopleBack);
        assertThat(typeSpecialist.getPeople()).containsOnly(peopleBack);
        assertThat(peopleBack.getTypeSpecialist()).isEqualTo(typeSpecialist);

        typeSpecialist.removePeople(peopleBack);
        assertThat(typeSpecialist.getPeople()).doesNotContain(peopleBack);
        assertThat(peopleBack.getTypeSpecialist()).isNull();

        typeSpecialist.people(new HashSet<>(Set.of(peopleBack)));
        assertThat(typeSpecialist.getPeople()).containsOnly(peopleBack);
        assertThat(peopleBack.getTypeSpecialist()).isEqualTo(typeSpecialist);

        typeSpecialist.setPeople(new HashSet<>());
        assertThat(typeSpecialist.getPeople()).doesNotContain(peopleBack);
        assertThat(peopleBack.getTypeSpecialist()).isNull();
    }
}
