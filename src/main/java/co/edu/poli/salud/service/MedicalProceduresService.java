package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.MedicalProcedures;
import co.edu.poli.salud.repository.MedicalProceduresRepository;
import co.edu.poli.salud.service.dto.MedicalProceduresDTO;
import co.edu.poli.salud.service.mapper.MedicalProceduresMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.MedicalProcedures}.
 */
@Service
@Transactional
public class MedicalProceduresService {

    private final Logger log = LoggerFactory.getLogger(MedicalProceduresService.class);

    private final MedicalProceduresRepository medicalProceduresRepository;

    private final MedicalProceduresMapper medicalProceduresMapper;

    public MedicalProceduresService(
        MedicalProceduresRepository medicalProceduresRepository,
        MedicalProceduresMapper medicalProceduresMapper
    ) {
        this.medicalProceduresRepository = medicalProceduresRepository;
        this.medicalProceduresMapper = medicalProceduresMapper;
    }

    /**
     * Save a medicalProcedures.
     *
     * @param medicalProceduresDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalProceduresDTO save(MedicalProceduresDTO medicalProceduresDTO) {
        log.debug("Request to save MedicalProcedures : {}", medicalProceduresDTO);
        MedicalProcedures medicalProcedures = medicalProceduresMapper.toEntity(medicalProceduresDTO);
        medicalProcedures = medicalProceduresRepository.save(medicalProcedures);
        return medicalProceduresMapper.toDto(medicalProcedures);
    }

    /**
     * Update a medicalProcedures.
     *
     * @param medicalProceduresDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalProceduresDTO update(MedicalProceduresDTO medicalProceduresDTO) {
        log.debug("Request to update MedicalProcedures : {}", medicalProceduresDTO);
        MedicalProcedures medicalProcedures = medicalProceduresMapper.toEntity(medicalProceduresDTO);
        medicalProcedures = medicalProceduresRepository.save(medicalProcedures);
        return medicalProceduresMapper.toDto(medicalProcedures);
    }

    /**
     * Partially update a medicalProcedures.
     *
     * @param medicalProceduresDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalProceduresDTO> partialUpdate(MedicalProceduresDTO medicalProceduresDTO) {
        log.debug("Request to partially update MedicalProcedures : {}", medicalProceduresDTO);

        return medicalProceduresRepository
            .findById(medicalProceduresDTO.getId())
            .map(existingMedicalProcedures -> {
                medicalProceduresMapper.partialUpdate(existingMedicalProcedures, medicalProceduresDTO);

                return existingMedicalProcedures;
            })
            .map(medicalProceduresRepository::save)
            .map(medicalProceduresMapper::toDto);
    }

    /**
     * Get all the medicalProcedures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalProceduresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalProcedures");
        return medicalProceduresRepository.findAll(pageable).map(medicalProceduresMapper::toDto);
    }

    /**
     * Get all the medicalProcedures with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicalProceduresDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicalProceduresRepository.findAllWithEagerRelationships(pageable).map(medicalProceduresMapper::toDto);
    }

    /**
     * Get one medicalProcedures by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalProceduresDTO> findOne(Long id) {
        log.debug("Request to get MedicalProcedures : {}", id);
        return medicalProceduresRepository.findOneWithEagerRelationships(id).map(medicalProceduresMapper::toDto);
    }

    /**
     * Delete the medicalProcedures by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalProcedures : {}", id);
        medicalProceduresRepository.deleteById(id);
    }
}
