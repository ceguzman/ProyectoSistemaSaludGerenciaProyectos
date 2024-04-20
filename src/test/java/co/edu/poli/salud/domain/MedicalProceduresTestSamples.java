package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalProceduresTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicalProcedures getMedicalProceduresSample1() {
        return new MedicalProcedures().id(1L).typeProcedures("typeProcedures1").description("description1");
    }

    public static MedicalProcedures getMedicalProceduresSample2() {
        return new MedicalProcedures().id(2L).typeProcedures("typeProcedures2").description("description2");
    }

    public static MedicalProcedures getMedicalProceduresRandomSampleGenerator() {
        return new MedicalProcedures()
            .id(longCount.incrementAndGet())
            .typeProcedures(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
