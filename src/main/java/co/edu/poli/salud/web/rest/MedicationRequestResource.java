package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.MedicationRequestRepository;
import co.edu.poli.salud.service.MedicationRequestService;
import co.edu.poli.salud.service.dto.MedicationRequestDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.MedicationRequest}.
 */
@RestController
@RequestMapping("/api/medication-requests")
public class MedicationRequestResource {

    private final Logger log = LoggerFactory.getLogger(MedicationRequestResource.class);

    private static final String ENTITY_NAME = "medicationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationRequestService medicationRequestService;

    private final MedicationRequestRepository medicationRequestRepository;

    public MedicationRequestResource(
        MedicationRequestService medicationRequestService,
        MedicationRequestRepository medicationRequestRepository
    ) {
        this.medicationRequestService = medicationRequestService;
        this.medicationRequestRepository = medicationRequestRepository;
    }

    /**
     * {@code POST  /medication-requests} : Create a new medicationRequest.
     *
     * @param medicationRequestDTO the medicationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicationRequestDTO, or with status {@code 400 (Bad Request)} if the medicationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicationRequestDTO> createMedicationRequest(@Valid @RequestBody MedicationRequestDTO medicationRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save MedicationRequest : {}", medicationRequestDTO);
        if (medicationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicationRequestDTO = medicationRequestService.save(medicationRequestDTO);
        return ResponseEntity.created(new URI("/api/medication-requests/" + medicationRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicationRequestDTO.getId().toString()))
            .body(medicationRequestDTO);
    }

    /**
     * {@code PUT  /medication-requests/:id} : Updates an existing medicationRequest.
     *
     * @param id the id of the medicationRequestDTO to save.
     * @param medicationRequestDTO the medicationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the medicationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicationRequestDTO> updateMedicationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicationRequestDTO medicationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicationRequest : {}, {}", id, medicationRequestDTO);
        if (medicationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicationRequestDTO = medicationRequestService.update(medicationRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicationRequestDTO.getId().toString()))
            .body(medicationRequestDTO);
    }

    /**
     * {@code PATCH  /medication-requests/:id} : Partial updates given fields of an existing medicationRequest, field will ignore if it is null
     *
     * @param id the id of the medicationRequestDTO to save.
     * @param medicationRequestDTO the medicationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the medicationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicationRequestDTO> partialUpdateMedicationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicationRequestDTO medicationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicationRequest partially : {}, {}", id, medicationRequestDTO);
        if (medicationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicationRequestDTO> result = medicationRequestService.partialUpdate(medicationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medication-requests} : get all the medicationRequests.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicationRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicationRequestDTO>> getAllMedicationRequests(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MedicationRequests");
        Page<MedicationRequestDTO> page;
        if (eagerload) {
            page = medicationRequestService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicationRequestService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medication-requests/:id} : get the "id" medicationRequest.
     *
     * @param id the id of the medicationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicationRequestDTO> getMedicationRequest(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicationRequest : {}", id);
        Optional<MedicationRequestDTO> medicationRequestDTO = medicationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationRequestDTO);
    }

    /**
     * {@code DELETE  /medication-requests/:id} : delete the "id" medicationRequest.
     *
     * @param id the id of the medicationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationRequest(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicationRequest : {}", id);
        medicationRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
