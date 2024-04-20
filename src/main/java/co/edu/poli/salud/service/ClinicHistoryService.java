package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.ClinicHistory;
import co.edu.poli.salud.repository.ClinicHistoryRepository;
import co.edu.poli.salud.service.dto.ClinicHistoryDTO;
import co.edu.poli.salud.service.mapper.ClinicHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.ClinicHistory}.
 */
@Service
@Transactional
public class ClinicHistoryService {

    private final Logger log = LoggerFactory.getLogger(ClinicHistoryService.class);

    private final ClinicHistoryRepository clinicHistoryRepository;

    private final ClinicHistoryMapper clinicHistoryMapper;

    public ClinicHistoryService(ClinicHistoryRepository clinicHistoryRepository, ClinicHistoryMapper clinicHistoryMapper) {
        this.clinicHistoryRepository = clinicHistoryRepository;
        this.clinicHistoryMapper = clinicHistoryMapper;
    }

    /**
     * Save a clinicHistory.
     *
     * @param clinicHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ClinicHistoryDTO save(ClinicHistoryDTO clinicHistoryDTO) {
        log.debug("Request to save ClinicHistory : {}", clinicHistoryDTO);
        ClinicHistory clinicHistory = clinicHistoryMapper.toEntity(clinicHistoryDTO);
        clinicHistory = clinicHistoryRepository.save(clinicHistory);
        return clinicHistoryMapper.toDto(clinicHistory);
    }

    /**
     * Update a clinicHistory.
     *
     * @param clinicHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ClinicHistoryDTO update(ClinicHistoryDTO clinicHistoryDTO) {
        log.debug("Request to update ClinicHistory : {}", clinicHistoryDTO);
        ClinicHistory clinicHistory = clinicHistoryMapper.toEntity(clinicHistoryDTO);
        clinicHistory = clinicHistoryRepository.save(clinicHistory);
        return clinicHistoryMapper.toDto(clinicHistory);
    }

    /**
     * Partially update a clinicHistory.
     *
     * @param clinicHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClinicHistoryDTO> partialUpdate(ClinicHistoryDTO clinicHistoryDTO) {
        log.debug("Request to partially update ClinicHistory : {}", clinicHistoryDTO);

        return clinicHistoryRepository
            .findById(clinicHistoryDTO.getId())
            .map(existingClinicHistory -> {
                clinicHistoryMapper.partialUpdate(existingClinicHistory, clinicHistoryDTO);

                return existingClinicHistory;
            })
            .map(clinicHistoryRepository::save)
            .map(clinicHistoryMapper::toDto);
    }

    /**
     * Get all the clinicHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ClinicHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClinicHistories");
        return clinicHistoryRepository.findAll(pageable).map(clinicHistoryMapper::toDto);
    }

    /**
     * Get all the clinicHistories with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ClinicHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clinicHistoryRepository.findAllWithEagerRelationships(pageable).map(clinicHistoryMapper::toDto);
    }

    /**
     * Get one clinicHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClinicHistoryDTO> findOne(Long id) {
        log.debug("Request to get ClinicHistory : {}", id);
        return clinicHistoryRepository.findOneWithEagerRelationships(id).map(clinicHistoryMapper::toDto);
    }

    /**
     * Delete the clinicHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ClinicHistory : {}", id);
        clinicHistoryRepository.deleteById(id);
    }
}
