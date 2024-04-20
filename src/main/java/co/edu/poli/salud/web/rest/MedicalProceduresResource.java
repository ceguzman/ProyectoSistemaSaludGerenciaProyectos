package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.MedicalProceduresRepository;
import co.edu.poli.salud.service.MedicalProceduresService;
import co.edu.poli.salud.service.dto.MedicalProceduresDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.MedicalProcedures}.
 */
@RestController
@RequestMapping("/api/medical-procedures")
public class MedicalProceduresResource {

    private final Logger log = LoggerFactory.getLogger(MedicalProceduresResource.class);

    private static final String ENTITY_NAME = "medicalProcedures";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalProceduresService medicalProceduresService;

    private final MedicalProceduresRepository medicalProceduresRepository;

    public MedicalProceduresResource(
        MedicalProceduresService medicalProceduresService,
        MedicalProceduresRepository medicalProceduresRepository
    ) {
        this.medicalProceduresService = medicalProceduresService;
        this.medicalProceduresRepository = medicalProceduresRepository;
    }

    /**
     * {@code POST  /medical-procedures} : Create a new medicalProcedures.
     *
     * @param medicalProceduresDTO the medicalProceduresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalProceduresDTO, or with status {@code 400 (Bad Request)} if the medicalProcedures has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicalProceduresDTO> createMedicalProcedures(@Valid @RequestBody MedicalProceduresDTO medicalProceduresDTO)
        throws URISyntaxException {
        log.debug("REST request to save MedicalProcedures : {}", medicalProceduresDTO);
        if (medicalProceduresDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalProcedures cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicalProceduresDTO = medicalProceduresService.save(medicalProceduresDTO);
        return ResponseEntity.created(new URI("/api/medical-procedures/" + medicalProceduresDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicalProceduresDTO.getId().toString()))
            .body(medicalProceduresDTO);
    }

    /**
     * {@code PUT  /medical-procedures/:id} : Updates an existing medicalProcedures.
     *
     * @param id the id of the medicalProceduresDTO to save.
     * @param medicalProceduresDTO the medicalProceduresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalProceduresDTO,
     * or with status {@code 400 (Bad Request)} if the medicalProceduresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalProceduresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalProceduresDTO> updateMedicalProcedures(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicalProceduresDTO medicalProceduresDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicalProcedures : {}, {}", id, medicalProceduresDTO);
        if (medicalProceduresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalProceduresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalProceduresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicalProceduresDTO = medicalProceduresService.update(medicalProceduresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalProceduresDTO.getId().toString()))
            .body(medicalProceduresDTO);
    }

    /**
     * {@code PATCH  /medical-procedures/:id} : Partial updates given fields of an existing medicalProcedures, field will ignore if it is null
     *
     * @param id the id of the medicalProceduresDTO to save.
     * @param medicalProceduresDTO the medicalProceduresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalProceduresDTO,
     * or with status {@code 400 (Bad Request)} if the medicalProceduresDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalProceduresDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalProceduresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicalProceduresDTO> partialUpdateMedicalProcedures(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicalProceduresDTO medicalProceduresDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicalProcedures partially : {}, {}", id, medicalProceduresDTO);
        if (medicalProceduresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalProceduresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalProceduresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicalProceduresDTO> result = medicalProceduresService.partialUpdate(medicalProceduresDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalProceduresDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medical-procedures} : get all the medicalProcedures.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalProcedures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicalProceduresDTO>> getAllMedicalProcedures(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MedicalProcedures");
        Page<MedicalProceduresDTO> page;
        if (eagerload) {
            page = medicalProceduresService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicalProceduresService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-procedures/:id} : get the "id" medicalProcedures.
     *
     * @param id the id of the medicalProceduresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalProceduresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalProceduresDTO> getMedicalProcedures(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicalProcedures : {}", id);
        Optional<MedicalProceduresDTO> medicalProceduresDTO = medicalProceduresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalProceduresDTO);
    }

    /**
     * {@code DELETE  /medical-procedures/:id} : delete the "id" medicalProcedures.
     *
     * @param id the id of the medicalProceduresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalProcedures(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicalProcedures : {}", id);
        medicalProceduresService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
