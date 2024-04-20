package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.MedicalAppointments;
import co.edu.poli.salud.repository.MedicalAppointmentsRepository;
import co.edu.poli.salud.service.dto.MedicalAppointmentsDTO;
import co.edu.poli.salud.service.mapper.MedicalAppointmentsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.MedicalAppointments}.
 */
@Service
@Transactional
public class MedicalAppointmentsService {

    private final Logger log = LoggerFactory.getLogger(MedicalAppointmentsService.class);

    private final MedicalAppointmentsRepository medicalAppointmentsRepository;

    private final MedicalAppointmentsMapper medicalAppointmentsMapper;

    public MedicalAppointmentsService(
        MedicalAppointmentsRepository medicalAppointmentsRepository,
        MedicalAppointmentsMapper medicalAppointmentsMapper
    ) {
        this.medicalAppointmentsRepository = medicalAppointmentsRepository;
        this.medicalAppointmentsMapper = medicalAppointmentsMapper;
    }

    /**
     * Save a medicalAppointments.
     *
     * @param medicalAppointmentsDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalAppointmentsDTO save(MedicalAppointmentsDTO medicalAppointmentsDTO) {
        log.debug("Request to save MedicalAppointments : {}", medicalAppointmentsDTO);
        MedicalAppointments medicalAppointments = medicalAppointmentsMapper.toEntity(medicalAppointmentsDTO);
        medicalAppointments = medicalAppointmentsRepository.save(medicalAppointments);
        return medicalAppointmentsMapper.toDto(medicalAppointments);
    }

    /**
     * Update a medicalAppointments.
     *
     * @param medicalAppointmentsDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicalAppointmentsDTO update(MedicalAppointmentsDTO medicalAppointmentsDTO) {
        log.debug("Request to update MedicalAppointments : {}", medicalAppointmentsDTO);
        MedicalAppointments medicalAppointments = medicalAppointmentsMapper.toEntity(medicalAppointmentsDTO);
        medicalAppointments = medicalAppointmentsRepository.save(medicalAppointments);
        return medicalAppointmentsMapper.toDto(medicalAppointments);
    }

    /**
     * Partially update a medicalAppointments.
     *
     * @param medicalAppointmentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicalAppointmentsDTO> partialUpdate(MedicalAppointmentsDTO medicalAppointmentsDTO) {
        log.debug("Request to partially update MedicalAppointments : {}", medicalAppointmentsDTO);

        return medicalAppointmentsRepository
            .findById(medicalAppointmentsDTO.getId())
            .map(existingMedicalAppointments -> {
                medicalAppointmentsMapper.partialUpdate(existingMedicalAppointments, medicalAppointmentsDTO);

                return existingMedicalAppointments;
            })
            .map(medicalAppointmentsRepository::save)
            .map(medicalAppointmentsMapper::toDto);
    }

    /**
     * Get all the medicalAppointments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicalAppointmentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MedicalAppointments");
        return medicalAppointmentsRepository.findAll(pageable).map(medicalAppointmentsMapper::toDto);
    }

    /**
     * Get all the medicalAppointments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MedicalAppointmentsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return medicalAppointmentsRepository.findAllWithEagerRelationships(pageable).map(medicalAppointmentsMapper::toDto);
    }

    /**
     * Get one medicalAppointments by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicalAppointmentsDTO> findOne(Long id) {
        log.debug("Request to get MedicalAppointments : {}", id);
        return medicalAppointmentsRepository.findOneWithEagerRelationships(id).map(medicalAppointmentsMapper::toDto);
    }

    /**
     * Delete the medicalAppointments by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MedicalAppointments : {}", id);
        medicalAppointmentsRepository.deleteById(id);
    }
}
