package de.herhold.reactives3.server.mapper;

import de.herhold.reactives3.api.fileApi.server.model.FileContent;
import de.herhold.reactives3.server.model.FileInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileContentMapper {
    FileContentMapper INSTANCE = Mappers.getMapper(FileContentMapper.class);

    @Mapping(target = "content", ignore = true)
    FileContent mapFileInformationToContent(FileInformation fileInformation);
}
