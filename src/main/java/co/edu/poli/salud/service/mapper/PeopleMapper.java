package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.People;
import co.edu.poli.salud.domain.TypeDocument;
import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.service.dto.PeopleDTO;
import co.edu.poli.salud.service.dto.TypeDocumentDTO;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link People} and its DTO {@link PeopleDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeopleMapper extends EntityMapper<PeopleDTO, People> {
    @Mapping(target = "typeDocument", source = "typeDocument", qualifiedByName = "typeDocumentDocumentName")
    @Mapping(target = "typeSpecialist", source = "typeSpecialist", qualifiedByName = "typeSpecialistSpecialistType")
    PeopleDTO toDto(People s);

    @Named("typeDocumentDocumentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentName", source = "documentName")
    TypeDocumentDTO toDtoTypeDocumentDocumentName(TypeDocument typeDocument);

    @Named("typeSpecialistSpecialistType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "specialistType", source = "specialistType")
    TypeSpecialistDTO toDtoTypeSpecialistSpecialistType(TypeSpecialist typeSpecialist);
}
