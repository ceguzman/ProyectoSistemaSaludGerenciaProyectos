package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalAuthorizationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalAuthorization getMedicalAuthorizationSample1() {
        return new MedicalAuthorization().id(1L).detailAuthorization("detailAuthorization1");
    }

    public static MedicalAuthorization getMedicalAuthorizationSample2() {
        return new MedicalAuthorization().id(2L).detailAuthorization("detailAuthorization2");
    }

    public static MedicalAuthorization getMedicalAuthorizationRandomSampleGenerator() {
        return new MedicalAuthorization().id(longCount.incrementAndGet()).detailAuthorization(UUID.randomUUID().toString());
    }
}
