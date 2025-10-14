package com.sdu_ai_lab.project_tracker.mappers;

import com.sdu_ai_lab.project_tracker.dto.TagDto;
import com.sdu_ai_lab.project_tracker.entities.TagEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(TagEntity entity);
    TagEntity toEntity(TagDto dto);
}
