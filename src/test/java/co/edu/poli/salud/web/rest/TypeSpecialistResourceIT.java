package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.TypeSpecialistAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.domain.enumeration.State;
import co.edu.poli.salud.repository.TypeSpecialistRepository;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
import co.edu.poli.salud.service.mapper.TypeSpecialistMapper;
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
 * Integration tests for the {@link TypeSpecialistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeSpecialistResourceIT {

    private static final String DEFAULT_SPECIALIST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALIST_TYPE = "BBBBBBBBBB";

    private static final State DEFAULT_STATE_SPECIALIST = State.ACTIVE;
    private static final State UPDATED_STATE_SPECIALIST = State.INACTIVE;

    private static final String ENTITY_API_URL = "/api/type-specialists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeSpecialistRepository typeSpecialistRepository;

    @Autowired
    private TypeSpecialistMapper typeSpecialistMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeSpecialistMockMvc;

    private TypeSpecialist typeSpecialist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSpecialist createEntity(EntityManager em) {
        TypeSpecialist typeSpecialist = new TypeSpecialist()
            .specialistType(DEFAULT_SPECIALIST_TYPE)
            .stateSpecialist(DEFAULT_STATE_SPECIALIST);
        return typeSpecialist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSpecialist createUpdatedEntity(EntityManager em) {
        TypeSpecialist typeSpecialist = new TypeSpecialist()
            .specialistType(UPDATED_SPECIALIST_TYPE)
            .stateSpecialist(UPDATED_STATE_SPECIALIST);
        return typeSpecialist;
    }

    @BeforeEach
    public void initTest() {
        typeSpecialist = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeSpecialist() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);
        var returnedTypeSpecialistDTO = om.readValue(
            restTypeSpecialistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeSpecialistDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeSpecialistDTO.class
        );

        // Validate the TypeSpecialist in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeSpecialist = typeSpecialistMapper.toEntity(returnedTypeSpecialistDTO);
        assertTypeSpecialistUpdatableFieldsEquals(returnedTypeSpecialist, getPersistedTypeSpecialist(returnedTypeSpecialist));
    }

    @Test
    @Transactional
    void createTypeSpecialistWithExistingId() throws Exception {
        // Create the TypeSpecialist with an existing ID
        typeSpecialist.setId(1L);
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSpecialistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeSpecialistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSpecialistTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeSpecialist.setSpecialistType(null);

        // Create the TypeSpecialist, which fails.
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        restTypeSpecialistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeSpecialistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateSpecialistIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeSpecialist.setStateSpecialist(null);

        // Create the TypeSpecialist, which fails.
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        restTypeSpecialistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeSpecialistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeSpecialists() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        // Get all the typeSpecialistList
        restTypeSpecialistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSpecialist.getId().intValue())))
            .andExpect(jsonPath("$.[*].specialistType").value(hasItem(DEFAULT_SPECIALIST_TYPE)))
            .andExpect(jsonPath("$.[*].stateSpecialist").value(hasItem(DEFAULT_STATE_SPECIALIST.toString())));
    }

    @Test
    @Transactional
    void getTypeSpecialist() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        // Get the typeSpecialist
        restTypeSpecialistMockMvc
            .perform(get(ENTITY_API_URL_ID, typeSpecialist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeSpecialist.getId().intValue()))
            .andExpect(jsonPath("$.specialistType").value(DEFAULT_SPECIALIST_TYPE))
            .andExpect(jsonPath("$.stateSpecialist").value(DEFAULT_STATE_SPECIALIST.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTypeSpecialist() throws Exception {
        // Get the typeSpecialist
        restTypeSpecialistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeSpecialist() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeSpecialist
        TypeSpecialist updatedTypeSpecialist = typeSpecialistRepository.findById(typeSpecialist.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeSpecialist are not directly saved in db
        em.detach(updatedTypeSpecialist);
        updatedTypeSpecialist.specialistType(UPDATED_SPECIALIST_TYPE).stateSpecialist(UPDATED_STATE_SPECIALIST);
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(updatedTypeSpecialist);

        restTypeSpecialistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeSpecialistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeSpecialistDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeSpecialistToMatchAllProperties(updatedTypeSpecialist);
    }

    @Test
    @Transactional
    void putNonExistingTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeSpecialistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeSpecialistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeSpecialistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeSpecialistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeSpecialistWithPatch() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeSpecialist using partial update
        TypeSpecialist partialUpdatedTypeSpecialist = new TypeSpecialist();
        partialUpdatedTypeSpecialist.setId(typeSpecialist.getId());

        partialUpdatedTypeSpecialist.stateSpecialist(UPDATED_STATE_SPECIALIST);

        restTypeSpecialistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSpecialist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeSpecialist))
            )
            .andExpect(status().isOk());

        // Validate the TypeSpecialist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeSpecialistUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeSpecialist, typeSpecialist),
            getPersistedTypeSpecialist(typeSpecialist)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeSpecialistWithPatch() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeSpecialist using partial update
        TypeSpecialist partialUpdatedTypeSpecialist = new TypeSpecialist();
        partialUpdatedTypeSpecialist.setId(typeSpecialist.getId());

        partialUpdatedTypeSpecialist.specialistType(UPDATED_SPECIALIST_TYPE).stateSpecialist(UPDATED_STATE_SPECIALIST);

        restTypeSpecialistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSpecialist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeSpecialist))
            )
            .andExpect(status().isOk());

        // Validate the TypeSpecialist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeSpecialistUpdatableFieldsEquals(partialUpdatedTypeSpecialist, getPersistedTypeSpecialist(partialUpdatedTypeSpecialist));
    }

    @Test
    @Transactional
    void patchNonExistingTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeSpecialistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeSpecialistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeSpecialistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeSpecialist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeSpecialist.setId(longCount.incrementAndGet());

        // Create the TypeSpecialist
        TypeSpecialistDTO typeSpecialistDTO = typeSpecialistMapper.toDto(typeSpecialist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSpecialistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeSpecialistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSpecialist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeSpecialist() throws Exception {
        // Initialize the database
        typeSpecialistRepository.saveAndFlush(typeSpecialist);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeSpecialist
        restTypeSpecialistMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeSpecialist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeSpecialistRepository.count();
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

    protected TypeSpecialist getPersistedTypeSpecialist(TypeSpecialist typeSpecialist) {
        return typeSpecialistRepository.findById(typeSpecialist.getId()).orElseThrow();
    }

    protected void assertPersistedTypeSpecialistToMatchAllProperties(TypeSpecialist expectedTypeSpecialist) {
        assertTypeSpecialistAllPropertiesEquals(expectedTypeSpecialist, getPersistedTypeSpecialist(expectedTypeSpecialist));
    }

    protected void assertPersistedTypeSpecialistToMatchUpdatableProperties(TypeSpecialist expectedTypeSpecialist) {
        assertTypeSpecialistAllUpdatablePropertiesEquals(expectedTypeSpecialist, getPersistedTypeSpecialist(expectedTypeSpecialist));
    }
}
