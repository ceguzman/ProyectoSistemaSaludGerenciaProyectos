package co.edu.poli.salud.service;

import co.edu.poli.salud.domain.TypeDocument;
import co.edu.poli.salud.repository.TypeDocumentRepository;
import co.edu.poli.salud.service.dto.TypeDocumentDTO;
import co.edu.poli.salud.service.mapper.TypeDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.poli.salud.domain.TypeDocument}.
 */
@Service
@Transactional
public class TypeDocumentService {

    private final Logger log = LoggerFactory.getLogger(TypeDocumentService.class);

    private final TypeDocumentRepository typeDocumentRepository;

    private final TypeDocumentMapper typeDocumentMapper;

    public TypeDocumentService(TypeDocumentRepository typeDocumentRepository, TypeDocumentMapper typeDocumentMapper) {
        this.typeDocumentRepository = typeDocumentRepository;
        this.typeDocumentMapper = typeDocumentMapper;
    }

    /**
     * Save a typeDocument.
     *
     * @param typeDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDocumentDTO save(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to save TypeDocument : {}", typeDocumentDTO);
        TypeDocument typeDocument = typeDocumentMapper.toEntity(typeDocumentDTO);
        typeDocument = typeDocumentRepository.save(typeDocument);
        return typeDocumentMapper.toDto(typeDocument);
    }

    /**
     * Update a typeDocument.
     *
     * @param typeDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDocumentDTO update(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to update TypeDocument : {}", typeDocumentDTO);
        TypeDocument typeDocument = typeDocumentMapper.toEntity(typeDocumentDTO);
        typeDocument = typeDocumentRepository.save(typeDocument);
        return typeDocumentMapper.toDto(typeDocument);
    }

    /**
     * Partially update a typeDocument.
     *
     * @param typeDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeDocumentDTO> partialUpdate(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to partially update TypeDocument : {}", typeDocumentDTO);

        return typeDocumentRepository
            .findById(typeDocumentDTO.getId())
            .map(existingTypeDocument -> {
                typeDocumentMapper.partialUpdate(existingTypeDocument, typeDocumentDTO);

                return existingTypeDocument;
            })
            .map(typeDocumentRepository::save)
            .map(typeDocumentMapper::toDto);
    }

    /**
     * Get all the typeDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeDocuments");
        return typeDocumentRepository.findAll(pageable).map(typeDocumentMapper::toDto);
    }

    /**
     * Get one typeDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeDocumentDTO> findOne(Long id) {
        log.debug("Request to get TypeDocument : {}", id);
        return typeDocumentRepository.findById(id).map(typeDocumentMapper::toDto);
    }

    /**
     * Delete the typeDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeDocument : {}", id);
        typeDocumentRepository.deleteById(id);
    }
}
