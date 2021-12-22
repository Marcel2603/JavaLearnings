package de.herhold.reactives3.reactiveFileServer.helper.mapping;

import de.herhold.reactives3.api.fileApi.reactiveServer.model.FileInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileInformationMapper {
    FileInformationMapper INSTANCE = Mappers.getMapper(FileInformationMapper.class);

    FileInformation mapModelToApi(de.herhold.reactives3.reactiveFileServer.model.FileInformation fileInformation);
}
