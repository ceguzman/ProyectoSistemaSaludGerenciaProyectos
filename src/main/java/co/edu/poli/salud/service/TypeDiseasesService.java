package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.TypeDiseases;
import co.edu.poli.salud.repository.TypeDiseasesRepository;
import co.edu.poli.salud.service.dto.TypeDiseasesDTO;
import co.edu.poli.salud.service.mapper.TypeDiseasesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.TypeDiseases}.
 */
@Service
@Transactional
public class TypeDiseasesService {

    private final Logger log = LoggerFactory.getLogger(TypeDiseasesService.class);

    private final TypeDiseasesRepository typeDiseasesRepository;

    private final TypeDiseasesMapper typeDiseasesMapper;

    public TypeDiseasesService(TypeDiseasesRepository typeDiseasesRepository, TypeDiseasesMapper typeDiseasesMapper) {
        this.typeDiseasesRepository = typeDiseasesRepository;
        this.typeDiseasesMapper = typeDiseasesMapper;
    }

    /**
     * Save a typeDiseases.
     *
     * @param typeDiseasesDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDiseasesDTO save(TypeDiseasesDTO typeDiseasesDTO) {
        log.debug("Request to save TypeDiseases : {}", typeDiseasesDTO);
        TypeDiseases typeDiseases = typeDiseasesMapper.toEntity(typeDiseasesDTO);
        typeDiseases = typeDiseasesRepository.save(typeDiseases);
        return typeDiseasesMapper.toDto(typeDiseases);
    }

    /**
     * Update a typeDiseases.
     *
     * @param typeDiseasesDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDiseasesDTO update(TypeDiseasesDTO typeDiseasesDTO) {
        log.debug("Request to update TypeDiseases : {}", typeDiseasesDTO);
        TypeDiseases typeDiseases = typeDiseasesMapper.toEntity(typeDiseasesDTO);
        typeDiseases = typeDiseasesRepository.save(typeDiseases);
        return typeDiseasesMapper.toDto(typeDiseases);
    }

    /**
     * Partially update a typeDiseases.
     *
     * @param typeDiseasesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeDiseasesDTO> partialUpdate(TypeDiseasesDTO typeDiseasesDTO) {
        log.debug("Request to partially update TypeDiseases : {}", typeDiseasesDTO);

        return typeDiseasesRepository
            .findById(typeDiseasesDTO.getId())
            .map(existingTypeDiseases -> {
                typeDiseasesMapper.partialUpdate(existingTypeDiseases, typeDiseasesDTO);

                return existingTypeDiseases;
            })
            .map(typeDiseasesRepository::save)
            .map(typeDiseasesMapper::toDto);
    }

    /**
     * Get all the typeDiseases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDiseasesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeDiseases");
        return typeDiseasesRepository.findAll(pageable).map(typeDiseasesMapper::toDto);
    }

    /**
     * Get one typeDiseases by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeDiseasesDTO> findOne(Long id) {
        log.debug("Request to get TypeDiseases : {}", id);
        return typeDiseasesRepository.findById(id).map(typeDiseasesMapper::toDto);
    }

    /**
     * Delete the typeDiseases by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeDiseases : {}", id);
        typeDiseasesRepository.deleteById(id);
    }
}
