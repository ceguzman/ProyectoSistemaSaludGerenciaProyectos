package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ClinicHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClinicHistory getClinicHistorySample1() {
        return new ClinicHistory().id(1L);
    }

    public static ClinicHistory getClinicHistorySample2() {
        return new ClinicHistory().id(2L);
    }

    public static ClinicHistory getClinicHistoryRandomSampleGenerator() {
        return new ClinicHistory().id(longCount.incrementAndGet());
    }
}
