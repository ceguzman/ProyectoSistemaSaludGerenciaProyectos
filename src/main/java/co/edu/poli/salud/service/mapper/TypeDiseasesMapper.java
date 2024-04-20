package co.edu.poli.salud.service.mapper;

import co.edu.poli.salud.domain.TypeDiseases;
import co.edu.poli.salud.service.dto.TypeDiseasesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeDiseases} and its DTO {@link TypeDiseasesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeDiseasesMapper extends EntityMapper<TypeDiseasesDTO, TypeDiseases> {}
