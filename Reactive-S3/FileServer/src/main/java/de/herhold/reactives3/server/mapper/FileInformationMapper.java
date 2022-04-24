package de.herhold.reactives3.server.mapper;

import de.herhold.reactives3.api.fileApi.server.model.FileInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileInformationMapper {
    FileInformationMapper INSTANCE = Mappers.getMapper(FileInformationMapper.class);

    FileInformation mapFileInformationToApi(de.herhold.reactives3.server.model.FileInformation fileInformation);
}
