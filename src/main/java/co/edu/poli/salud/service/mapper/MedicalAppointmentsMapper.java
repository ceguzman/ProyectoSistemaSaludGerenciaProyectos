package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.MedicalAppointments;
import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.service.dto.MedicalAppointmentsDTO;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedicalAppointments} and its DTO {@link MedicalAppointmentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicalAppointmentsMapper extends EntityMapper<MedicalAppointmentsDTO, MedicalAppointments> {
    @Mapping(target = "typeSpecialist", source = "typeSpecialist", qualifiedByName = "typeSpecialistSpecialistType")
    MedicalAppointmentsDTO toDto(MedicalAppointments s);

    @Named("typeSpecialistSpecialistType")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "specialistType", source = "specialistType")
    TypeSpecialistDTO toDtoTypeSpecialistSpecialistType(TypeSpecialist typeSpecialist);
}
