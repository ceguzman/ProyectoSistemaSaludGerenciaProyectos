package co.edu.poli.salud.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSpecialistAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeSpecialistAllPropertiesEquals(TypeSpecialist expected, TypeSpecialist actual) {
        assertTypeSpecialistAutoGeneratedPropertiesEquals(expected, actual);
        assertTypeSpecialistAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeSpecialistAllUpdatablePropertiesEquals(TypeSpecialist expected, TypeSpecialist actual) {
        assertTypeSpecialistUpdatableFieldsEquals(expected, actual);
        assertTypeSpecialistUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeSpecialistAutoGeneratedPropertiesEquals(TypeSpecialist expected, TypeSpecialist actual) {
        assertThat(expected)
            .as("Verify TypeSpecialist auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeSpecialistUpdatableFieldsEquals(TypeSpecialist expected, TypeSpecialist actual) {
        assertThat(expected)
            .as("Verify TypeSpecialist relevant properties")
            .satisfies(e -> assertThat(e.getSpecialistType()).as("check specialistType").isEqualTo(actual.getSpecialistType()))
            .satisfies(e -> assertThat(e.getStateSpecialist()).as("check stateSpecialist").isEqualTo(actual.getStateSpecialist()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeSpecialistUpdatableRelationshipsEquals(TypeSpecialist expected, TypeSpecialist actual) {}
}
