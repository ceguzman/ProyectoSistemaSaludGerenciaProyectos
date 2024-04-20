package co.edu.poli.salud.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeDiseasesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeDiseasesAllPropertiesEquals(TypeDiseases expected, TypeDiseases actual) {
        assertTypeDiseasesAutoGeneratedPropertiesEquals(expected, actual);
        assertTypeDiseasesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeDiseasesAllUpdatablePropertiesEquals(TypeDiseases expected, TypeDiseases actual) {
        assertTypeDiseasesUpdatableFieldsEquals(expected, actual);
        assertTypeDiseasesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeDiseasesAutoGeneratedPropertiesEquals(TypeDiseases expected, TypeDiseases actual) {
        assertThat(expected)
            .as("Verify TypeDiseases auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeDiseasesUpdatableFieldsEquals(TypeDiseases expected, TypeDiseases actual) {
        assertThat(expected)
            .as("Verify TypeDiseases relevant properties")
            .satisfies(e -> assertThat(e.getDiseasesType()).as("check diseasesType").isEqualTo(actual.getDiseasesType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTypeDiseasesUpdatableRelationshipsEquals(TypeDiseases expected, TypeDiseases actual) {}
}