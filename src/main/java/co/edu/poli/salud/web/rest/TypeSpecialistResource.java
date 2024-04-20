package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.TypeSpecialistRepository;
import co.edu.poli.salud.service.TypeSpecialistService;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.TypeSpecialist}.
 */
@RestController
@RequestMapping("/api/type-specialists")
public class TypeSpecialistResource {

    private final Logger log = LoggerFactory.getLogger(TypeSpecialistResource.class);

    private static final String ENTITY_NAME = "typeSpecialist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSpecialistService typeSpecialistService;

    private final TypeSpecialistRepository typeSpecialistRepository;

    public TypeSpecialistResource(TypeSpecialistService typeSpecialistService, TypeSpecialistRepository typeSpecialistRepository) {
        this.typeSpecialistService = typeSpecialistService;
        this.typeSpecialistRepository = typeSpecialistRepository;
    }

    /**
     * {@code POST  /type-specialists} : Create a new typeSpecialist.
     *
     * @param typeSpecialistDTO the typeSpecialistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSpecialistDTO, or with status {@code 400 (Bad Request)} if the typeSpecialist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeSpecialistDTO> createTypeSpecialist(@Valid @RequestBody TypeSpecialistDTO typeSpecialistDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeSpecialist : {}", typeSpecialistDTO);
        if (typeSpecialistDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeSpecialist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeSpecialistDTO = typeSpecialistService.save(typeSpecialistDTO);
        return ResponseEntity.created(new URI("/api/type-specialists/" + typeSpecialistDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, typeSpecialistDTO.getId().toString()))
            .body(typeSpecialistDTO);
    }

    /**
     * {@code PUT  /type-specialists/:id} : Updates an existing typeSpecialist.
     *
     * @param id the id of the typeSpecialistDTO to save.
     * @param typeSpecialistDTO the typeSpecialistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSpecialistDTO,
     * or with status {@code 400 (Bad Request)} if the typeSpecialistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSpecialistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeSpecialistDTO> updateTypeSpecialist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeSpecialistDTO typeSpecialistDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeSpecialist : {}, {}", id, typeSpecialistDTO);
        if (typeSpecialistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSpecialistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSpecialistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeSpecialistDTO = typeSpecialistService.update(typeSpecialistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeSpecialistDTO.getId().toString()))
            .body(typeSpecialistDTO);
    }

    /**
     * {@code PATCH  /type-specialists/:id} : Partial updates given fields of an existing typeSpecialist, field will ignore if it is null
     *
     * @param id the id of the typeSpecialistDTO to save.
     * @param typeSpecialistDTO the typeSpecialistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSpecialistDTO,
     * or with status {@code 400 (Bad Request)} if the typeSpecialistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeSpecialistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeSpecialistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeSpecialistDTO> partialUpdateTypeSpecialist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeSpecialistDTO typeSpecialistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeSpecialist partially : {}, {}", id, typeSpecialistDTO);
        if (typeSpecialistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSpecialistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSpecialistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeSpecialistDTO> result = typeSpecialistService.partialUpdate(typeSpecialistDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeSpecialistDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-specialists} : get all the typeSpecialists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSpecialists in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeSpecialistDTO>> getAllTypeSpecialists(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TypeSpecialists");
        Page<TypeSpecialistDTO> page = typeSpecialistService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-specialists/:id} : get the "id" typeSpecialist.
     *
     * @param id the id of the typeSpecialistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSpecialistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeSpecialistDTO> getTypeSpecialist(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeSpecialist : {}", id);
        Optional<TypeSpecialistDTO> typeSpecialistDTO = typeSpecialistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeSpecialistDTO);
    }

    /**
     * {@code DELETE  /type-specialists/:id} : delete the "id" typeSpecialist.
     *
     * @param id the id of the typeSpecialistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeSpecialist(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeSpecialist : {}", id);
        typeSpecialistService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
