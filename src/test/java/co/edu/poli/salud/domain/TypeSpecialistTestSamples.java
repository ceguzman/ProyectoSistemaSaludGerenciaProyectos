package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeSpecialistTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeSpecialist getTypeSpecialistSample1() {
        return new TypeSpecialist().id(1L).specialistType("specialistType1");
    }

    public static TypeSpecialist getTypeSpecialistSample2() {
        return new TypeSpecialist().id(2L).specialistType("specialistType2");
    }

    public static TypeSpecialist getTypeSpecialistRandomSampleGenerator() {
        return new TypeSpecialist().id(longCount.incrementAndGet()).specialistType(UUID.randomUUID().toString());
    }
}
