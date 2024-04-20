package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.repository.MedicalAuthorizationRepository;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
import co.edu.poli.salud.service.mapper.MedicalAuthorizationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.MedicalAuthorization}.
 */
@Service
@Transactional
public class MedicalAuthorizationService {

    private final Logger log = LoggerFactory.getLogger(MedicalAuthorizationService.class);

    private final MedicalAuthorizationRepository medicalAuthorizationRepository;

    private final MedicalAuthorizationMapper medicalAuthorizationMapper;

    public MedicalAuthorizationService(
        MedicalAuthorizationRepository medicalAuthorizationRepository,
        MedicalAuthorizationMapper medicalAuthorizationMapper
    ) {
        this.medicalAuthorizationRepository = medicalAuthorizationRepository;
        this.medicalAuthorizationMapper = medicalAuthorizationMapper;
    }

    /**
     * Save a medicalAuthorization.
     *
     * @param medicalAuthorizationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalAuthorizationDTO save(MedicalAuthorizationDTO medicalAuthorizationDTO) {
        log.debug("Request to save MedicalAuthorization : {}", medicalAuthorizationDTO);
        MedicalAuthorization medicalAuthorization = medicalAuthorizationMapper.toEntity(medicalAuthorizationDTO);
        medicalAuthorization = medicalAuthorizationRepository.save(medicalAuthorization);
        return medicalAuthorizationMapper.toDto(medicalAuthorization);
    }

    /**
     * Update a medicalAuthorization.
     *
     * @param medicalAuthorizationDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalAuthorizationDTO update(MedicalAuthorizationDTO medicalAuthorizationDTO) {
        log.debug("Request to update MedicalAuthorization : {}", medicalAuthorizationDTO);
        MedicalAuthorization medicalAuthorization = medicalAuthorizationMapper.toEntity(medicalAuthorizationDTO);
        medicalAuthorization = medicalAuthorizationRepository.save(medicalAuthorization);
        return medicalAuthorizationMapper.toDto(medicalAuthorization);
    }

    /**
     * Partially update a medicalAuthorization.
     *
     * @param medicalAuthorizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalAuthorizationDTO> partialUpdate(MedicalAuthorizationDTO medicalAuthorizationDTO) {
        log.debug("Request to partially update MedicalAuthorization : {}", medicalAuthorizationDTO);

        return medicalAuthorizationRepository
            .findById(medicalAuthorizationDTO.getId())
            .map(existingMedicalAuthorization -> {
                medicalAuthorizationMapper.partialUpdate(existingMedicalAuthorization, medicalAuthorizationDTO);

                return existingMedicalAuthorization;
            })
            .map(medicalAuthorizationRepository::save)
            .map(medicalAuthorizationMapper::toDto);
    }

    /**
     * Get all the medicalAuthorizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalAuthorizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalAuthorizations");
        return medicalAuthorizationRepository.findAll(pageable).map(medicalAuthorizationMapper::toDto);
    }

    /**
     * Get all the medicalAuthorizations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicalAuthorizationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicalAuthorizationRepository.findAllWithEagerRelationships(pageable).map(medicalAuthorizationMapper::toDto);
    }

    /**
     * Get one medicalAuthorization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalAuthorizationDTO> findOne(Long id) {
        log.debug("Request to get MedicalAuthorization : {}", id);
        return medicalAuthorizationRepository.findOneWithEagerRelationships(id).map(medicalAuthorizationMapper::toDto);
    }

    /**
     * Delete the medicalAuthorization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalAuthorization : {}", id);
        medicalAuthorizationRepository.deleteById(id);
    }
}
