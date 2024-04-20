package co.edu.poli.salud.domain;

import static co.edu.poli.salud.domain.PeopleTestSamples.*;
import static co.edu.poli.salud.domain.TypeDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.edu.poli.salud.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TypeDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDocument.class);
        TypeDocument typeDocument1 = getTypeDocumentSample1();
        TypeDocument typeDocument2 = new TypeDocument();
        assertThat(typeDocument1).isNotEqualTo(typeDocument2);

        typeDocument2.setId(typeDocument1.getId());
        assertThat(typeDocument1).isEqualTo(typeDocument2);

        typeDocument2 = getTypeDocumentSample2();
        assertThat(typeDocument1).isNotEqualTo(typeDocument2);
    }

    @Test
    void peopleTest() throws Exception {
        TypeDocument typeDocument = getTypeDocumentRandomSampleGenerator();
        People peopleBack = getPeopleRandomSampleGenerator();

        typeDocument.addPeople(peopleBack);
        assertThat(typeDocument.getPeople()).containsOnly(peopleBack);
        assertThat(peopleBack.getTypeDocument()).isEqualTo(typeDocument);

        typeDocument.removePeople(peopleBack);
        assertThat(typeDocument.getPeople()).doesNotContain(peopleBack);
        assertThat(peopleBack.getTypeDocument()).isNull();

        typeDocument.people(new HashSet<>(Set.of(peopleBack)));
        assertThat(typeDocument.getPeople()).containsOnly(peopleBack);
        assertThat(peopleBack.getTypeDocument()).isEqualTo(typeDocument);

        typeDocument.setPeople(new HashSet<>());
        assertThat(typeDocument.getPeople()).doesNotContain(peopleBack);
        assertThat(peopleBack.getTypeDocument()).isNull();
    }
}
