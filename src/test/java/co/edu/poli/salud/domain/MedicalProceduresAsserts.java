package co.edu.poli.salud.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MedicalProceduresAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMedicalProceduresAllPropertiesEquals(MedicalProcedures expected, MedicalProcedures actual) {
        assertMedicalProceduresAutoGeneratedPropertiesEquals(expected, actual);
        assertMedicalProceduresAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMedicalProceduresAllUpdatablePropertiesEquals(MedicalProcedures expected, MedicalProcedures actual) {
        assertMedicalProceduresUpdatableFieldsEquals(expected, actual);
        assertMedicalProceduresUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMedicalProceduresAutoGeneratedPropertiesEquals(MedicalProcedures expected, MedicalProcedures actual) {
        assertThat(expected)
            .as("Verify MedicalProcedures auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMedicalProceduresUpdatableFieldsEquals(MedicalProcedures expected, MedicalProcedures actual) {
        assertThat(expected)
            .as("Verify MedicalProcedures relevant properties")
            .satisfies(e -> assertThat(e.getTypeProcedures()).as("check typeProcedures").isEqualTo(actual.getTypeProcedures()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getDateProcedures()).as("check dateProcedures").isEqualTo(actual.getDateProcedures()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMedicalProceduresUpdatableRelationshipsEquals(MedicalProcedures expected, MedicalProcedures actual) {
        assertThat(expected)
            .as("Verify MedicalProcedures relationships")
            .satisfies(
                e -> assertThat(e.getMedicalAuthorization()).as("check medicalAuthorization").isEqualTo(actual.getMedicalAuthorization())
            );
    }
}
