package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.domain.MedicationRequest;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
import co.edu.poli.salud.service.dto.MedicationRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicationRequest} and its DTO {@link MedicationRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationRequestMapper extends EntityMapper<MedicationRequestDTO, MedicationRequest> {
    @Mapping(target = "medicalAuthorization", source = "medicalAuthorization", qualifiedByName = "medicalAuthorizationDetailAuthorization")
    MedicationRequestDTO toDto(MedicationRequest s);

    @Named("medicalAuthorizationDetailAuthorization")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "detailAuthorization", source = "detailAuthorization")
    MedicalAuthorizationDTO toDtoMedicalAuthorizationDetailAuthorization(MedicalAuthorization medicalAuthorization);
}
