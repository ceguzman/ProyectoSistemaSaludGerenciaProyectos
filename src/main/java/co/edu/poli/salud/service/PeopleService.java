package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.People;
import co.edu.poli.salud.repository.PeopleRepository;
import co.edu.poli.salud.service.dto.PeopleDTO;
import co.edu.poli.salud.service.mapper.PeopleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.People}.
 */
@Service
@Transactional
public class PeopleService {

    private final Logger log = LoggerFactory.getLogger(PeopleService.class);

    private final PeopleRepository peopleRepository;

    private final PeopleMapper peopleMapper;

    public PeopleService(PeopleRepository peopleRepository, PeopleMapper peopleMapper) {
        this.peopleRepository = peopleRepository;
        this.peopleMapper = peopleMapper;
    }

    /**
     * Save a people.
     *
     * @param peopleDTO the entity to save.
     * @return the persisted entity.
     */
    public PeopleDTO save(PeopleDTO peopleDTO) {
        log.debug("Request to save People : {}", peopleDTO);
        People people = peopleMapper.toEntity(peopleDTO);
        people = peopleRepository.save(people);
        return peopleMapper.toDto(people);
    }

    /**
     * Update a people.
     *
     * @param peopleDTO the entity to save.
     * @return the persisted entity.
     */
    public PeopleDTO update(PeopleDTO peopleDTO) {
        log.debug("Request to update People : {}", peopleDTO);
        People people = peopleMapper.toEntity(peopleDTO);
        people = peopleRepository.save(people);
        return peopleMapper.toDto(people);
    }

    /**
     * Partially update a people.
     *
     * @param peopleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeopleDTO> partialUpdate(PeopleDTO peopleDTO) {
        log.debug("Request to partially update People : {}", peopleDTO);

        return peopleRepository
            .findById(peopleDTO.getId())
            .map(existingPeople -> {
                peopleMapper.partialUpdate(existingPeople, peopleDTO);

                return existingPeople;
            })
            .map(peopleRepository::save)
            .map(peopleMapper::toDto);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeopleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return peopleRepository.findAll(pageable).map(peopleMapper::toDto);
    }

    /**
     * Get all the people with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PeopleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return peopleRepository.findAllWithEagerRelationships(pageable).map(peopleMapper::toDto);
    }

    /**
     * Get one people by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeopleDTO> findOne(Long id) {
        log.debug("Request to get People : {}", id);
        return peopleRepository.findOneWithEagerRelationships(id).map(peopleMapper::toDto);
    }

    /**
     * Delete the people by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete People : {}", id);
        peopleRepository.deleteById(id);
    }
}
