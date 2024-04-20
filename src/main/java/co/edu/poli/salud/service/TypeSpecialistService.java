package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.repository.TypeSpecialistRepository;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
import co.edu.poli.salud.service.mapper.TypeSpecialistMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.TypeSpecialist}.
 */
@Service
@Transactional
public class TypeSpecialistService {

    private final Logger log = LoggerFactory.getLogger(TypeSpecialistService.class);

    private final TypeSpecialistRepository typeSpecialistRepository;

    private final TypeSpecialistMapper typeSpecialistMapper;

    public TypeSpecialistService(TypeSpecialistRepository typeSpecialistRepository, TypeSpecialistMapper typeSpecialistMapper) {
        this.typeSpecialistRepository = typeSpecialistRepository;
        this.typeSpecialistMapper = typeSpecialistMapper;
    }

    /**
     * Save a typeSpecialist.
     *
     * @param typeSpecialistDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeSpecialistDTO save(TypeSpecialistDTO typeSpecialistDTO) {
        log.debug("Request to save TypeSpecialist : {}", typeSpecialistDTO);
        TypeSpecialist typeSpecialist = typeSpecialistMapper.toEntity(typeSpecialistDTO);
        typeSpecialist = typeSpecialistRepository.save(typeSpecialist);
        return typeSpecialistMapper.toDto(typeSpecialist);
    }

    /**
     * Update a typeSpecialist.
     *
     * @param typeSpecialistDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeSpecialistDTO update(TypeSpecialistDTO typeSpecialistDTO) {
        log.debug("Request to update TypeSpecialist : {}", typeSpecialistDTO);
        TypeSpecialist typeSpecialist = typeSpecialistMapper.toEntity(typeSpecialistDTO);
        typeSpecialist = typeSpecialistRepository.save(typeSpecialist);
        return typeSpecialistMapper.toDto(typeSpecialist);
    }

    /**
     * Partially update a typeSpecialist.
     *
     * @param typeSpecialistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeSpecialistDTO> partialUpdate(TypeSpecialistDTO typeSpecialistDTO) {
        log.debug("Request to partially update TypeSpecialist : {}", typeSpecialistDTO);

        return typeSpecialistRepository
            .findById(typeSpecialistDTO.getId())
            .map(existingTypeSpecialist -> {
                typeSpecialistMapper.partialUpdate(existingTypeSpecialist, typeSpecialistDTO);

                return existingTypeSpecialist;
            })
            .map(typeSpecialistRepository::save)
            .map(typeSpecialistMapper::toDto);
    }

    /**
     * Get all the typeSpecialists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeSpecialistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSpecialists");
        return typeSpecialistRepository.findAll(pageable).map(typeSpecialistMapper::toDto);
    }

    /**
     * Get one typeSpecialist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeSpecialistDTO> findOne(Long id) {
        log.debug("Request to get TypeSpecialist : {}", id);
        return typeSpecialistRepository.findById(id).map(typeSpecialistMapper::toDto);
    }

    /**
     * Delete the typeSpecialist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeSpecialist : {}", id);
        typeSpecialistRepository.deleteById(id);
    }
}
