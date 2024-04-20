package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.ClinicHistory;
import co.edu.poli.salud.domain.People;
import co.edu.poli.salud.domain.TypeDiseases;
import co.edu.poli.salud.service.dto.ClinicHistoryDTO;
import co.edu.poli.salud.service.dto.PeopleDTO;
import co.edu.poli.salud.service.dto.TypeDiseasesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClinicHistory} and its DTO {@link ClinicHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClinicHistoryMapper extends EntityMapper<ClinicHistoryDTO, ClinicHistory> {
    @Mapping(target = "typeDisease", source = "typeDisease", qualifiedByName = "typeDiseasesDiseasesType")
    @Mapping(target = "people", source = "people", qualifiedByName = "peopleDocumentNumber")
    ClinicHistoryDTO toDto(ClinicHistory s);

    @Named("typeDiseasesDiseasesType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "diseasesType", source = "diseasesType")
    TypeDiseasesDTO toDtoTypeDiseasesDiseasesType(TypeDiseases typeDiseases);

    @Named("peopleDocumentNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "documentNumber", source = "documentNumber")
    PeopleDTO toDtoPeopleDocumentNumber(People people);
}
