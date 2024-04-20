package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.MedicalAuthorizationRepository;
import co.edu.poli.salud.service.MedicalAuthorizationService;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.MedicalAuthorization}.
 */
@RestController
@RequestMapping("/api/medical-authorizations")
public class MedicalAuthorizationResource {

    private final Logger log = LoggerFactory.getLogger(MedicalAuthorizationResource.class);

    private static final String ENTITY_NAME = "medicalAuthorization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalAuthorizationService medicalAuthorizationService;

    private final MedicalAuthorizationRepository medicalAuthorizationRepository;

    public MedicalAuthorizationResource(
        MedicalAuthorizationService medicalAuthorizationService,
        MedicalAuthorizationRepository medicalAuthorizationRepository
    ) {
        this.medicalAuthorizationService = medicalAuthorizationService;
        this.medicalAuthorizationRepository = medicalAuthorizationRepository;
    }

    /**
     * {@code POST  /medical-authorizations} : Create a new medicalAuthorization.
     *
     * @param medicalAuthorizationDTO the medicalAuthorizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicalAuthorizationDTO, or with status {@code 400 (Bad Request)} if the medicalAuthorization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicalAuthorizationDTO> createMedicalAuthorization(
        @Valid @RequestBody MedicalAuthorizationDTO medicalAuthorizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MedicalAuthorization : {}", medicalAuthorizationDTO);
        if (medicalAuthorizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicalAuthorization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicalAuthorizationDTO = medicalAuthorizationService.save(medicalAuthorizationDTO);
        return ResponseEntity.created(new URI("/api/medical-authorizations/" + medicalAuthorizationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, medicalAuthorizationDTO.getId().toString()))
            .body(medicalAuthorizationDTO);
    }

    /**
     * {@code PUT  /medical-authorizations/:id} : Updates an existing medicalAuthorization.
     *
     * @param id the id of the medicalAuthorizationDTO to save.
     * @param medicalAuthorizationDTO the medicalAuthorizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalAuthorizationDTO,
     * or with status {@code 400 (Bad Request)} if the medicalAuthorizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicalAuthorizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicalAuthorizationDTO> updateMedicalAuthorization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicalAuthorizationDTO medicalAuthorizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedicalAuthorization : {}, {}", id, medicalAuthorizationDTO);
        if (medicalAuthorizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalAuthorizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalAuthorizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicalAuthorizationDTO = medicalAuthorizationService.update(medicalAuthorizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalAuthorizationDTO.getId().toString()))
            .body(medicalAuthorizationDTO);
    }

    /**
     * {@code PATCH  /medical-authorizations/:id} : Partial updates given fields of an existing medicalAuthorization, field will ignore if it is null
     *
     * @param id the id of the medicalAuthorizationDTO to save.
     * @param medicalAuthorizationDTO the medicalAuthorizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicalAuthorizationDTO,
     * or with status {@code 400 (Bad Request)} if the medicalAuthorizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicalAuthorizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicalAuthorizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicalAuthorizationDTO> partialUpdateMedicalAuthorization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicalAuthorizationDTO medicalAuthorizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedicalAuthorization partially : {}, {}", id, medicalAuthorizationDTO);
        if (medicalAuthorizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicalAuthorizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicalAuthorizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicalAuthorizationDTO> result = medicalAuthorizationService.partialUpdate(medicalAuthorizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicalAuthorizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medical-authorizations} : get all the medicalAuthorizations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicalAuthorizations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicalAuthorizationDTO>> getAllMedicalAuthorizations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MedicalAuthorizations");
        Page<MedicalAuthorizationDTO> page;
        if (eagerload) {
            page = medicalAuthorizationService.findAllWithEagerRelationships(pageable);
        } else {
            page = medicalAuthorizationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medical-authorizations/:id} : get the "id" medicalAuthorization.
     *
     * @param id the id of the medicalAuthorizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicalAuthorizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalAuthorizationDTO> getMedicalAuthorization(@PathVariable("id") Long id) {
        log.debug("REST request to get MedicalAuthorization : {}", id);
        Optional<MedicalAuthorizationDTO> medicalAuthorizationDTO = medicalAuthorizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicalAuthorizationDTO);
    }

    /**
     * {@code DELETE  /medical-authorizations/:id} : delete the "id" medicalAuthorization.
     *
     * @param id the id of the medicalAuthorizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalAuthorization(@PathVariable("id") Long id) {
        log.debug("REST request to delete MedicalAuthorization : {}", id);
        medicalAuthorizationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
