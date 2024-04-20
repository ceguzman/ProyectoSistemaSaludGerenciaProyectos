package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.ClinicHistory;
import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.service.dto.ClinicHistoryDTO;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalAuthorization} and its DTO {@link MedicalAuthorizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalAuthorizationMapper extends EntityMapper<MedicalAuthorizationDTO, MedicalAuthorization> {
    @Mapping(target = "clinicHistory", source = "clinicHistory", qualifiedByName = "clinicHistoryDateClinic")
    MedicalAuthorizationDTO toDto(MedicalAuthorization s);

    @Named("clinicHistoryDateClinic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateClinic", source = "dateClinic")
    ClinicHistoryDTO toDtoClinicHistoryDateClinic(ClinicHistory clinicHistory);
}
