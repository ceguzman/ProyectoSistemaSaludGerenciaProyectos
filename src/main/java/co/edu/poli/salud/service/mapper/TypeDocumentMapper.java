package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.TypeDocument;
import co.edu.poli.salud.service.dto.TypeDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeDocument} and its DTO {@link TypeDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeDocumentMapper extends EntityMapper<TypeDocumentDTO, TypeDocument> {}
