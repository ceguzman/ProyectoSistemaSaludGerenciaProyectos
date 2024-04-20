package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeDocument getTypeDocumentSample1() {
        return new TypeDocument().id(1L).initials("initials1").documentName("documentName1");
    }

    public static TypeDocument getTypeDocumentSample2() {
        return new TypeDocument().id(2L).initials("initials2").documentName("documentName2");
    }

    public static TypeDocument getTypeDocumentRandomSampleGenerator() {
        return new TypeDocument()
            .id(longCount.incrementAndGet())
            .initials(UUID.randomUUID().toString())
            .documentName(UUID.randomUUID().toString());
    }
}
