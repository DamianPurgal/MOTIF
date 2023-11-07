package com.deemor.motif.alert.mapper;

import com.deemor.motif.alert.dto.AlertDto;
import com.deemor.motif.alert.dto.AlertPage;
import com.deemor.motif.alert.dto.AlertStatistics;
import com.deemor.motif.alert.entity.Alert;
import com.deemor.motif.model.AlertModelApi;
import com.deemor.motif.model.AlertPageModelApi;
import com.deemor.motif.model.AlertStatisticsModelApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AlertMapper {

    @Mapping(target = "user", source = "user.username")
    AlertDto mapEntityToDto(Alert alert);

    List<AlertDto> mapEntityToDto(List<Alert> alert);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Alert mapDtoToAlert(AlertDto alertDto);

    AlertModelApi mapDtoToModelApi(AlertDto alertDto);
    List<AlertModelApi> mapDtoToModelApi(List<AlertDto> alertDto);

    AlertPageModelApi mapDtoToModelApi(AlertPage alertPage);

    AlertStatisticsModelApi mapDtoToModelApi(AlertStatistics alertStatistics);
}
