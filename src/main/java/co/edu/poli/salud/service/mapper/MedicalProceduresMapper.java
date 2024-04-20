package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.domain.MedicalProcedures;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
import co.edu.poli.salud.service.dto.MedicalProceduresDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalProcedures} and its DTO {@link MedicalProceduresDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalProceduresMapper extends EntityMapper<MedicalProceduresDTO, MedicalProcedures> {
    @Mapping(target = "medicalAuthorization", source = "medicalAuthorization", qualifiedByName = "medicalAuthorizationDetailAuthorization")
    MedicalProceduresDTO toDto(MedicalProcedures s);

    @Named("medicalAuthorizationDetailAuthorization")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "detailAuthorization", source = "detailAuthorization")
    MedicalAuthorizationDTO toDtoMedicalAuthorizationDetailAuthorization(MedicalAuthorization medicalAuthorization);
}
