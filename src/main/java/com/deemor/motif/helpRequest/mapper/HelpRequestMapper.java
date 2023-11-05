package com.deemor.motif.helpRequest.mapper;

import com.deemor.motif.helpRequest.dto.HelpRequestAddDto;
import com.deemor.motif.helpRequest.dto.HelpRequestDto;
import com.deemor.motif.helpRequest.dto.HelpRequestEditDto;
import com.deemor.motif.helpRequest.dto.HelpRequestPage;
import com.deemor.motif.helpRequest.entity.HelpRequest;
import com.deemor.motif.model.HelpRequestAddModelApi;
import com.deemor.motif.model.HelpRequestEditModelApi;
import com.deemor.motif.model.HelpRequestModelApi;
import com.deemor.motif.model.HelpRequestPageModelApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HelpRequestMapper {


    @Mapping(target = "requester", source = "requester.username")
    HelpRequestDto mapEntityToDto(HelpRequest helpRequest);
    List<HelpRequestDto> mapEntityToDto(List<HelpRequest> helpRequest);

    HelpRequest mapAddDtoToEntity(HelpRequestAddDto helpRequest);

    HelpRequestModelApi mapDtoToModelApi(HelpRequestDto helpRequestDto);
    List<HelpRequestModelApi> mapDtoToModelApi(List<HelpRequestDto> helpRequestDto);

    HelpRequestPageModelApi mapDtoPageToModelApiPage(HelpRequestPage helpRequestPage);

    HelpRequestAddDto mapAddModelApiToDto(HelpRequestAddModelApi helpRequestAddModelApi);

    HelpRequestEditDto mapEditModelApiToDto(HelpRequestEditModelApi helpRequestEditModelApi);
}
