package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MedicationRequest getMedicationRequestSample1() {
        return new MedicationRequest().id(1L).name("name1").amount(1).milligrams(1);
    }

    public static MedicationRequest getMedicationRequestSample2() {
        return new MedicationRequest().id(2L).name("name2").amount(2).milligrams(2);
    }

    public static MedicationRequest getMedicationRequestRandomSampleGenerator() {
        return new MedicationRequest()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .amount(intCount.incrementAndGet())
            .milligrams(intCount.incrementAndGet());
    }
}
