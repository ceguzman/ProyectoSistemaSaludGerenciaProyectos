package co.edu.poli.salud.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PeopleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static People getPeopleSample1() {
        return new People()
            .id(1L)
            .documentNumber("documentNumber1")
            .firstName("firstName1")
            .firstSurname("firstSurname1")
            .secondName("secondName1")
            .secondSurname("secondSurname1");
    }

    public static People getPeopleSample2() {
        return new People()
            .id(2L)
            .documentNumber("documentNumber2")
            .firstName("firstName2")
            .firstSurname("firstSurname2")
            .secondName("secondName2")
            .secondSurname("secondSurname2");
    }

    public static People getPeopleRandomSampleGenerator() {
        return new People()
            .id(longCount.incrementAndGet())
            .documentNumber(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .firstSurname(UUID.randomUUID().toString())
            .secondName(UUID.randomUUID().toString())
            .secondSurname(UUID.randomUUID().toString());
    }
}
