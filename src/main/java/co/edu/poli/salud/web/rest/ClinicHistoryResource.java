package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.ClinicHistoryRepository;
import co.edu.poli.salud.service.ClinicHistoryService;
import co.edu.poli.salud.service.dto.ClinicHistoryDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.ClinicHistory}.
 */
@RestController
@RequestMapping("/api/clinic-histories")
public class ClinicHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ClinicHistoryResource.class);

    private static final String ENTITY_NAME = "clinicHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClinicHistoryService clinicHistoryService;

    private final ClinicHistoryRepository clinicHistoryRepository;

    public ClinicHistoryResource(ClinicHistoryService clinicHistoryService, ClinicHistoryRepository clinicHistoryRepository) {
        this.clinicHistoryService = clinicHistoryService;
        this.clinicHistoryRepository = clinicHistoryRepository;
    }

    /**
     * {@code POST  /clinic-histories} : Create a new clinicHistory.
     *
     * @param clinicHistoryDTO the clinicHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clinicHistoryDTO, or with status {@code 400 (Bad Request)} if the clinicHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClinicHistoryDTO> createClinicHistory(@Valid @RequestBody ClinicHistoryDTO clinicHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ClinicHistory : {}", clinicHistoryDTO);
        if (clinicHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new clinicHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clinicHistoryDTO = clinicHistoryService.save(clinicHistoryDTO);
        return ResponseEntity.created(new URI("/api/clinic-histories/" + clinicHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, clinicHistoryDTO.getId().toString()))
            .body(clinicHistoryDTO);
    }

    /**
     * {@code PUT  /clinic-histories/:id} : Updates an existing clinicHistory.
     *
     * @param id the id of the clinicHistoryDTO to save.
     * @param clinicHistoryDTO the clinicHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the clinicHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clinicHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClinicHistoryDTO> updateClinicHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClinicHistoryDTO clinicHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ClinicHistory : {}, {}", id, clinicHistoryDTO);
        if (clinicHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clinicHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clinicHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clinicHistoryDTO = clinicHistoryService.update(clinicHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clinicHistoryDTO.getId().toString()))
            .body(clinicHistoryDTO);
    }

    /**
     * {@code PATCH  /clinic-histories/:id} : Partial updates given fields of an existing clinicHistory, field will ignore if it is null
     *
     * @param id the id of the clinicHistoryDTO to save.
     * @param clinicHistoryDTO the clinicHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clinicHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the clinicHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clinicHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clinicHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClinicHistoryDTO> partialUpdateClinicHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClinicHistoryDTO clinicHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClinicHistory partially : {}, {}", id, clinicHistoryDTO);
        if (clinicHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clinicHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clinicHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClinicHistoryDTO> result = clinicHistoryService.partialUpdate(clinicHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clinicHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clinic-histories} : get all the clinicHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clinicHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClinicHistoryDTO>> getAllClinicHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ClinicHistories");
        Page<ClinicHistoryDTO> page;
        if (eagerload) {
            page = clinicHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = clinicHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clinic-histories/:id} : get the "id" clinicHistory.
     *
     * @param id the id of the clinicHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clinicHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClinicHistoryDTO> getClinicHistory(@PathVariable("id") Long id) {
        log.debug("REST request to get ClinicHistory : {}", id);
        Optional<ClinicHistoryDTO> clinicHistoryDTO = clinicHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clinicHistoryDTO);
    }

    /**
     * {@code DELETE  /clinic-histories/:id} : delete the "id" clinicHistory.
     *
     * @param id the id of the clinicHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicHistory(@PathVariable("id") Long id) {
        log.debug("REST request to delete ClinicHistory : {}", id);
        clinicHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
