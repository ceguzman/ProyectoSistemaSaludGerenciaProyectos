package co.edu.poli.salud.web.rest;

import co.edu.poli.salud.repository.TypeDiseasesRepository;
import co.edu.poli.salud.service.TypeDiseasesService;
import co.edu.poli.salud.service.dto.TypeDiseasesDTO;
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
 * REST controller for managing {@link co.edu.poli.salud.domain.TypeDiseases}.
 */
@RestController
@RequestMapping("/api/type-diseases")
public class TypeDiseasesResource {

    private final Logger log = LoggerFactory.getLogger(TypeDiseasesResource.class);

    private static final String ENTITY_NAME = "typeDiseases";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeDiseasesService typeDiseasesService;

    private final TypeDiseasesRepository typeDiseasesRepository;

    public TypeDiseasesResource(TypeDiseasesService typeDiseasesService, TypeDiseasesRepository typeDiseasesRepository) {
        this.typeDiseasesService = typeDiseasesService;
        this.typeDiseasesRepository = typeDiseasesRepository;
    }

    /**
     * {@code POST  /type-diseases} : Create a new typeDiseases.
     *
     * @param typeDiseasesDTO the typeDiseasesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeDiseasesDTO, or with status {@code 400 (Bad Request)} if the typeDiseases has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeDiseasesDTO> createTypeDiseases(@Valid @RequestBody TypeDiseasesDTO typeDiseasesDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeDiseases : {}", typeDiseasesDTO);
        if (typeDiseasesDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeDiseases cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeDiseasesDTO = typeDiseasesService.save(typeDiseasesDTO);
        return ResponseEntity.created(new URI("/api/type-diseases/" + typeDiseasesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, typeDiseasesDTO.getId().toString()))
            .body(typeDiseasesDTO);
    }

    /**
     * {@code PUT  /type-diseases/:id} : Updates an existing typeDiseases.
     *
     * @param id the id of the typeDiseasesDTO to save.
     * @param typeDiseasesDTO the typeDiseasesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDiseasesDTO,
     * or with status {@code 400 (Bad Request)} if the typeDiseasesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeDiseasesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeDiseasesDTO> updateTypeDiseases(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeDiseasesDTO typeDiseasesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeDiseases : {}, {}", id, typeDiseasesDTO);
        if (typeDiseasesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDiseasesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDiseasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeDiseasesDTO = typeDiseasesService.update(typeDiseasesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeDiseasesDTO.getId().toString()))
            .body(typeDiseasesDTO);
    }

    /**
     * {@code PATCH  /type-diseases/:id} : Partial updates given fields of an existing typeDiseases, field will ignore if it is null
     *
     * @param id the id of the typeDiseasesDTO to save.
     * @param typeDiseasesDTO the typeDiseasesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDiseasesDTO,
     * or with status {@code 400 (Bad Request)} if the typeDiseasesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeDiseasesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeDiseasesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeDiseasesDTO> partialUpdateTypeDiseases(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeDiseasesDTO typeDiseasesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeDiseases partially : {}, {}", id, typeDiseasesDTO);
        if (typeDiseasesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeDiseasesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeDiseasesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeDiseasesDTO> result = typeDiseasesService.partialUpdate(typeDiseasesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeDiseasesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-diseases} : get all the typeDiseases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeDiseases in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeDiseasesDTO>> getAllTypeDiseases(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeDiseases");
        Page<TypeDiseasesDTO> page = typeDiseasesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-diseases/:id} : get the "id" typeDiseases.
     *
     * @param id the id of the typeDiseasesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeDiseasesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeDiseasesDTO> getTypeDiseases(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeDiseases : {}", id);
        Optional<TypeDiseasesDTO> typeDiseasesDTO = typeDiseasesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDiseasesDTO);
    }

    /**
     * {@code DELETE  /type-diseases/:id} : delete the "id" typeDiseases.
     *
     * @param id the id of the typeDiseasesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeDiseases(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeDiseases : {}", id);
        typeDiseasesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
