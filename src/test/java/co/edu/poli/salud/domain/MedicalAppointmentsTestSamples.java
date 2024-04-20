package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalAppointmentsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalAppointments getMedicalAppointmentsSample1() {
        return new MedicalAppointments().id(1L);
    }

    public static MedicalAppointments getMedicalAppointmentsSample2() {
        return new MedicalAppointments().id(2L);
    }

    public static MedicalAppointments getMedicalAppointmentsRandomSampleGenerator() {
        return new MedicalAppointments().id(longCount.incrementAndGet());
    }
}
