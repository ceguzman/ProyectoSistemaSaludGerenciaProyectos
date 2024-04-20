package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.service.dto.TypeSpecialistDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeSpecialist} and its DTO {@link TypeSpecialistDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeSpecialistMapper extends EntityMapper<TypeSpecialistDTO, TypeSpecialist> {}
