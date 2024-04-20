package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.MedicalAppointmentsRepository;
import co.edu.poli.salud.service.MedicalAppointmentsService;
import co.edu.poli.salud.service.dto.MedicalAppointmentsDTO;
import co.edu.poli.salud.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.poli.salud.domain.MedicalAppointments}.
 */
@RestController
@RequestMapping("/api/medical-appointments")
public class MedicalAppointmentsResource {

    private final Logger log = LoggerFactory.getLogger(MedicalAppointmentsResource.class);

    private static final String ENTITY_NAME = "medicalAppointments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalAppointmentsService medicalAppointmentsService;

    private final MedicalAppointmentsRepository medicalAppointmentsRepository;

    public MedicalAppointmentsResource(
        MedicalAppointmentsService medicalAppointmentsService,
        MedicalAppointmentsRepository medicalAppointmentsRepository
    ) {
        this.medicalAppointmentsService = medicalAppointmentsService;
        this.medicalAppointmentsRepository = medicalAppointmentsRepository;
    }

    /**
     * {@code POST  /medical-appointments} : Create a new medicalAppointments.
     *
     * @param medicalAppointmentsDTO the medicalAppointmentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalAppointmentsDTO, or with status {@code 400 (Bad Request)} if the medicalAppointments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicalAppointmentsDTO> createMedicalAppointments(
        @Valid @RequestBody MedicalAppointmentsDTO medicalAppointmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MedicalAppointments : {}", medicalAppointmentsDTO);
        if (medicalAppointmentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalAppointments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicalAppointmentsDTO = medicalAppointmentsService.save(medicalAppointmentsDTO);
        return ResponseEntity.created(new URI("/api/medical-appointments/" + medicalAppointmentsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicalAppointmentsDTO.getId().toString()))
            .body(medicalAppointmentsDTO);
    }

    /**
     * {@code PUT  /medical-appointments/:id} : Updates an existing medicalAppointments.
     *
     * @param id the id of the medicalAppointmentsDTO to save.
     * @param medicalAppointmentsDTO the medicalAppointmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalAppointmentsDTO,
     * or with status {@code 400 (Bad Request)} if the medicalAppointmentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalAppointmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalAppointmentsDTO> updateMedicalAppointments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicalAppointmentsDTO medicalAppointmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicalAppointments : {}, {}", id, medicalAppointmentsDTO);
        if (medicalAppointmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalAppointmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalAppointmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicalAppointmentsDTO = medicalAppointmentsService.update(medicalAppointmentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalAppointmentsDTO.getId().toString()))
            .body(medicalAppointmentsDTO);
    }

    /**
     * {@code PATCH  /medical-appointments/:id} : Partial updates given fields of an existing medicalAppointments, field will ignore if it is null
     *
     * @param id the id of the medicalAppointmentsDTO to save.
     * @param medicalAppointmentsDTO the medicalAppointmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalAppointmentsDTO,
     * or with status {@code 400 (Bad Request)} if the medicalAppointmentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalAppointmentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalAppointmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicalAppointmentsDTO> partialUpdateMedicalAppointments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicalAppointmentsDTO medicalAppointmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicalAppointments partially : {}, {}", id, medicalAppointmentsDTO);
        if (medicalAppointmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalAppointmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalAppointmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicalAppointmentsDTO> result = medicalAppointmentsService.partialUpdate(medicalAppointmentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalAppointmentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medical-appointments} : get all the medicalAppointments.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalAppointments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicalAppointmentsDTO>> getAllMedicalAppointments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MedicalAppointments");
        Page<MedicalAppointmentsDTO> page;
        if (eagerload) {
            page = medicalAppointmentsService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicalAppointmentsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-appointments/:id} : get the "id" medicalAppointments.
     *
     * @param id the id of the medicalAppointmentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalAppointmentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalAppointmentsDTO> getMedicalAppointments(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicalAppointments : {}", id);
        Optional<MedicalAppointmentsDTO> medicalAppointmentsDTO = medicalAppointmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalAppointmentsDTO);
    }

    /**
     * {@code DELETE  /medical-appointments/:id} : delete the "id" medicalAppointments.
     *
     * @param id the id of the medicalAppointmentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalAppointments(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicalAppointments : {}", id);
        medicalAppointmentsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
