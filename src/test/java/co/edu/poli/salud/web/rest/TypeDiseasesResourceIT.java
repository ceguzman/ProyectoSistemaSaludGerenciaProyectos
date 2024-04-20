package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.TypeDiseasesAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.TypeDiseases;
import co.edu.poli.salud.repository.TypeDiseasesRepository;
import co.edu.poli.salud.service.dto.TypeDiseasesDTO;
import co.edu.poli.salud.service.mapper.TypeDiseasesMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeDiseasesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeDiseasesResourceIT {

    private static final String DEFAULT_DISEASES_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DISEASES_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-diseases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeDiseasesRepository typeDiseasesRepository;

    @Autowired
    private TypeDiseasesMapper typeDiseasesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeDiseasesMockMvc;

    private TypeDiseases typeDiseases;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDiseases createEntity(EntityManager em) {
        TypeDiseases typeDiseases = new TypeDiseases().diseasesType(DEFAULT_DISEASES_TYPE);
        return typeDiseases;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDiseases createUpdatedEntity(EntityManager em) {
        TypeDiseases typeDiseases = new TypeDiseases().diseasesType(UPDATED_DISEASES_TYPE);
        return typeDiseases;
    }

    @BeforeEach
    public void initTest() {
        typeDiseases = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeDiseases() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);
        var returnedTypeDiseasesDTO = om.readValue(
            restTypeDiseasesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiseasesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeDiseasesDTO.class
        );

        // Validate the TypeDiseases in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeDiseases = typeDiseasesMapper.toEntity(returnedTypeDiseasesDTO);
        assertTypeDiseasesUpdatableFieldsEquals(returnedTypeDiseases, getPersistedTypeDiseases(returnedTypeDiseases));
    }

    @Test
    @Transactional
    void createTypeDiseasesWithExistingId() throws Exception {
        // Create the TypeDiseases with an existing ID
        typeDiseases.setId(1L);
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDiseasesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiseasesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDiseasesTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeDiseases.setDiseasesType(null);

        // Create the TypeDiseases, which fails.
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        restTypeDiseasesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiseasesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeDiseases() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        // Get all the typeDiseasesList
        restTypeDiseasesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDiseases.getId().intValue())))
            .andExpect(jsonPath("$.[*].diseasesType").value(hasItem(DEFAULT_DISEASES_TYPE)));
    }

    @Test
    @Transactional
    void getTypeDiseases() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        // Get the typeDiseases
        restTypeDiseasesMockMvc
            .perform(get(ENTITY_API_URL_ID, typeDiseases.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeDiseases.getId().intValue()))
            .andExpect(jsonPath("$.diseasesType").value(DEFAULT_DISEASES_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingTypeDiseases() throws Exception {
        // Get the typeDiseases
        restTypeDiseasesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeDiseases() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiseases
        TypeDiseases updatedTypeDiseases = typeDiseasesRepository.findById(typeDiseases.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeDiseases are not directly saved in db
        em.detach(updatedTypeDiseases);
        updatedTypeDiseases.diseasesType(UPDATED_DISEASES_TYPE);
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(updatedTypeDiseases);

        restTypeDiseasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDiseasesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiseasesDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeDiseasesToMatchAllProperties(updatedTypeDiseases);
    }

    @Test
    @Transactional
    void putNonExistingTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDiseasesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiseasesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDiseasesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDiseasesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeDiseasesWithPatch() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiseases using partial update
        TypeDiseases partialUpdatedTypeDiseases = new TypeDiseases();
        partialUpdatedTypeDiseases.setId(typeDiseases.getId());

        restTypeDiseasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDiseases.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDiseases))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiseases in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDiseasesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeDiseases, typeDiseases),
            getPersistedTypeDiseases(typeDiseases)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeDiseasesWithPatch() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDiseases using partial update
        TypeDiseases partialUpdatedTypeDiseases = new TypeDiseases();
        partialUpdatedTypeDiseases.setId(typeDiseases.getId());

        partialUpdatedTypeDiseases.diseasesType(UPDATED_DISEASES_TYPE);

        restTypeDiseasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDiseases.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDiseases))
            )
            .andExpect(status().isOk());

        // Validate the TypeDiseases in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDiseasesUpdatableFieldsEquals(partialUpdatedTypeDiseases, getPersistedTypeDiseases(partialUpdatedTypeDiseases));
    }

    @Test
    @Transactional
    void patchNonExistingTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeDiseasesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDiseasesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDiseasesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeDiseases() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDiseases.setId(longCount.incrementAndGet());

        // Create the TypeDiseases
        TypeDiseasesDTO typeDiseasesDTO = typeDiseasesMapper.toDto(typeDiseases);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDiseasesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeDiseasesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDiseases in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeDiseases() throws Exception {
        // Initialize the database
        typeDiseasesRepository.saveAndFlush(typeDiseases);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeDiseases
        restTypeDiseasesMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeDiseases.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeDiseasesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TypeDiseases getPersistedTypeDiseases(TypeDiseases typeDiseases) {
        return typeDiseasesRepository.findById(typeDiseases.getId()).orElseThrow();
    }

    protected void assertPersistedTypeDiseasesToMatchAllProperties(TypeDiseases expectedTypeDiseases) {
        assertTypeDiseasesAllPropertiesEquals(expectedTypeDiseases, getPersistedTypeDiseases(expectedTypeDiseases));
    }

    protected void assertPersistedTypeDiseasesToMatchUpdatableProperties(TypeDiseases expectedTypeDiseases) {
        assertTypeDiseasesAllUpdatablePropertiesEquals(expectedTypeDiseases, getPersistedTypeDiseases(expectedTypeDiseases));
    }
}
