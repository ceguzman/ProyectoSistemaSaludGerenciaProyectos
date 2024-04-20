package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.PeopleAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.People;
import co.edu.poli.salud.domain.TypeDocument;
import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.repository.PeopleRepository;
import co.edu.poli.salud.service.PeopleService;
import co.edu.poli.salud.service.dto.PeopleDTO;
import co.edu.poli.salud.service.mapper.PeopleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeopleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PeopleResourceIT {

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_SURNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeopleRepository peopleRepository;

    @Mock
    private PeopleRepository peopleRepositoryMock;

    @Autowired
    private PeopleMapper peopleMapper;

    @Mock
    private PeopleService peopleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeopleMockMvc;

    private People people;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static People createEntity(EntityManager em) {
        People people = new People()
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .firstSurname(DEFAULT_FIRST_SURNAME)
            .secondName(DEFAULT_SECOND_NAME)
            .secondSurname(DEFAULT_SECOND_SURNAME);
        // Add required entity
        TypeDocument typeDocument;
        if (TestUtil.findAll(em, TypeDocument.class).isEmpty()) {
            typeDocument = TypeDocumentResourceIT.createEntity(em);
            em.persist(typeDocument);
            em.flush();
        } else {
            typeDocument = TestUtil.findAll(em, TypeDocument.class).get(0);
        }
        people.setTypeDocument(typeDocument);
        // Add required entity
        TypeSpecialist typeSpecialist;
        if (TestUtil.findAll(em, TypeSpecialist.class).isEmpty()) {
            typeSpecialist = TypeSpecialistResourceIT.createEntity(em);
            em.persist(typeSpecialist);
            em.flush();
        } else {
            typeSpecialist = TestUtil.findAll(em, TypeSpecialist.class).get(0);
        }
        people.setTypeSpecialist(typeSpecialist);
        return people;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static People createUpdatedEntity(EntityManager em) {
        People people = new People()
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .firstSurname(UPDATED_FIRST_SURNAME)
            .secondName(UPDATED_SECOND_NAME)
            .secondSurname(UPDATED_SECOND_SURNAME);
        // Add required entity
        TypeDocument typeDocument;
        if (TestUtil.findAll(em, TypeDocument.class).isEmpty()) {
            typeDocument = TypeDocumentResourceIT.createUpdatedEntity(em);
            em.persist(typeDocument);
            em.flush();
        } else {
            typeDocument = TestUtil.findAll(em, TypeDocument.class).get(0);
        }
        people.setTypeDocument(typeDocument);
        // Add required entity
        TypeSpecialist typeSpecialist;
        if (TestUtil.findAll(em, TypeSpecialist.class).isEmpty()) {
            typeSpecialist = TypeSpecialistResourceIT.createUpdatedEntity(em);
            em.persist(typeSpecialist);
            em.flush();
        } else {
            typeSpecialist = TestUtil.findAll(em, TypeSpecialist.class).get(0);
        }
        people.setTypeSpecialist(typeSpecialist);
        return people;
    }

    @BeforeEach
    public void initTest() {
        people = createEntity(em);
    }

    @Test
    @Transactional
    void createPeople() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);
        var returnedPeopleDTO = om.readValue(
            restPeopleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PeopleDTO.class
        );

        // Validate the People in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPeople = peopleMapper.toEntity(returnedPeopleDTO);
        assertPeopleUpdatableFieldsEquals(returnedPeople, getPersistedPeople(returnedPeople));
    }

    @Test
    @Transactional
    void createPeopleWithExistingId() throws Exception {
        // Create the People with an existing ID
        people.setId(1L);
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        people.setDocumentNumber(null);

        // Create the People, which fails.
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        people.setFirstName(null);

        // Create the People, which fails.
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstSurnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        people.setFirstSurname(null);

        // Create the People, which fails.
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get all the peopleList
        restPeopleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(people.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].firstSurname").value(hasItem(DEFAULT_FIRST_SURNAME)))
            .andExpect(jsonPath("$.[*].secondName").value(hasItem(DEFAULT_SECOND_NAME)))
            .andExpect(jsonPath("$.[*].secondSurname").value(hasItem(DEFAULT_SECOND_SURNAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsEnabled() throws Exception {
        when(peopleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeopleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(peopleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(peopleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeopleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(peopleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get the people
        restPeopleMockMvc
            .perform(get(ENTITY_API_URL_ID, people.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(people.getId().intValue()))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.firstSurname").value(DEFAULT_FIRST_SURNAME))
            .andExpect(jsonPath("$.secondName").value(DEFAULT_SECOND_NAME))
            .andExpect(jsonPath("$.secondSurname").value(DEFAULT_SECOND_SURNAME));
    }

    @Test
    @Transactional
    void getNonExistingPeople() throws Exception {
        // Get the people
        restPeopleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the people
        People updatedPeople = peopleRepository.findById(people.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPeople are not directly saved in db
        em.detach(updatedPeople);
        updatedPeople
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .firstSurname(UPDATED_FIRST_SURNAME)
            .secondName(UPDATED_SECOND_NAME)
            .secondSurname(UPDATED_SECOND_SURNAME);
        PeopleDTO peopleDTO = peopleMapper.toDto(updatedPeople);

        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peopleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO))
            )
            .andExpect(status().isOk());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeopleToMatchAllProperties(updatedPeople);
    }

    @Test
    @Transactional
    void putNonExistingPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peopleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(peopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeopleWithPatch() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the people using partial update
        People partialUpdatedPeople = new People();
        partialUpdatedPeople.setId(people.getId());

        partialUpdatedPeople.secondName(UPDATED_SECOND_NAME);

        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeople))
            )
            .andExpect(status().isOk());

        // Validate the People in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeopleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPeople, people), getPersistedPeople(people));
    }

    @Test
    @Transactional
    void fullUpdatePeopleWithPatch() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the people using partial update
        People partialUpdatedPeople = new People();
        partialUpdatedPeople.setId(people.getId());

        partialUpdatedPeople
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .firstSurname(UPDATED_FIRST_SURNAME)
            .secondName(UPDATED_SECOND_NAME)
            .secondSurname(UPDATED_SECOND_SURNAME);

        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeople))
            )
            .andExpect(status().isOk());

        // Validate the People in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeopleUpdatableFieldsEquals(partialUpdatedPeople, getPersistedPeople(partialUpdatedPeople));
    }

    @Test
    @Transactional
    void patchNonExistingPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peopleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(peopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(peopleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeople() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        people.setId(longCount.incrementAndGet());

        // Create the People
        PeopleDTO peopleDTO = peopleMapper.toDto(people);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(peopleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the People in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the people
        restPeopleMockMvc
            .perform(delete(ENTITY_API_URL_ID, people.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return peopleRepository.count();
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

    protected People getPersistedPeople(People people) {
        return peopleRepository.findById(people.getId()).orElseThrow();
    }

    protected void assertPersistedPeopleToMatchAllProperties(People expectedPeople) {
        assertPeopleAllPropertiesEquals(expectedPeople, getPersistedPeople(expectedPeople));
    }

    protected void assertPersistedPeopleToMatchUpdatableProperties(People expectedPeople) {
        assertPeopleAllUpdatablePropertiesEquals(expectedPeople, getPersistedPeople(expectedPeople));
    }
}
