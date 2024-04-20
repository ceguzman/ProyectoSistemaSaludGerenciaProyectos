package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeDiseasesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeDiseases getTypeDiseasesSample1() {
        return new TypeDiseases().id(1L).diseasesType("diseasesType1");
    }

    public static TypeDiseases getTypeDiseasesSample2() {
        return new TypeDiseases().id(2L).diseasesType("diseasesType2");
    }

    public static TypeDiseases getTypeDiseasesRandomSampleGenerator() {
        return new TypeDiseases().id(longCount.incrementAndGet()).diseasesType(UUID.randomUUID().toString());
    }
}
