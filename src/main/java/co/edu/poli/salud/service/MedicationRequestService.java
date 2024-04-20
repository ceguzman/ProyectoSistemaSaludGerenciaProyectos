package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.MedicationRequest;
import co.edu.poli.salud.repository.MedicationRequestRepository;
import co.edu.poli.salud.service.dto.MedicationRequestDTO;
import co.edu.poli.salud.service.mapper.MedicationRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.MedicationRequest}.
 */
@Service
@Transactional
public class MedicationRequestService {

    private final Logger log = LoggerFactory.getLogger(MedicationRequestService.class);

    private final MedicationRequestRepository medicationRequestRepository;

    private final MedicationRequestMapper medicationRequestMapper;

    public MedicationRequestService(
        MedicationRequestRepository medicationRequestRepository,
        MedicationRequestMapper medicationRequestMapper
    ) {
        this.medicationRequestRepository = medicationRequestRepository;
        this.medicationRequestMapper = medicationRequestMapper;
    }

    /**
     * Save a medicationRequest.
     *
     * @param medicationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationRequestDTO save(MedicationRequestDTO medicationRequestDTO) {
        log.debug("Request to save MedicationRequest : {}", medicationRequestDTO);
        MedicationRequest medicationRequest = medicationRequestMapper.toEntity(medicationRequestDTO);
        medicationRequest = medicationRequestRepository.save(medicationRequest);
        return medicationRequestMapper.toDto(medicationRequest);
    }

    /**
     * Update a medicationRequest.
     *
     * @param medicationRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicationRequestDTO update(MedicationRequestDTO medicationRequestDTO) {
        log.debug("Request to update MedicationRequest : {}", medicationRequestDTO);
        MedicationRequest medicationRequest = medicationRequestMapper.toEntity(medicationRequestDTO);
        medicationRequest = medicationRequestRepository.save(medicationRequest);
        return medicationRequestMapper.toDto(medicationRequest);
    }

    /**
     * Partially update a medicationRequest.
     *
     * @param medicationRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicationRequestDTO> partialUpdate(MedicationRequestDTO medicationRequestDTO) {
        log.debug("Request to partially update MedicationRequest : {}", medicationRequestDTO);

        return medicationRequestRepository
            .findById(medicationRequestDTO.getId())
            .map(existingMedicationRequest -> {
                medicationRequestMapper.partialUpdate(existingMedicationRequest, medicationRequestDTO);

                return existingMedicationRequest;
            })
            .map(medicationRequestRepository::save)
            .map(medicationRequestMapper::toDto);
    }

    /**
     * Get all the medicationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicationRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicationRequests");
        return medicationRequestRepository.findAll(pageable).map(medicationRequestMapper::toDto);
    }

    /**
     * Get all the medicationRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicationRequestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicationRequestRepository.findAllWithEagerRelationships(pageable).map(medicationRequestMapper::toDto);
    }

    /**
     * Get one medicationRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicationRequestDTO> findOne(Long id) {
        log.debug("Request to get MedicationRequest : {}", id);
        return medicationRequestRepository.findOneWithEagerRelationships(id).map(medicationRequestMapper::toDto);
    }

    /**
     * Delete the medicationRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicationRequest : {}", id);
        medicationRequestRepository.deleteById(id);
    }
}
