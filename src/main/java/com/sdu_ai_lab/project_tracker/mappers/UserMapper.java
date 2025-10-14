package com.sdu_ai_lab.project_tracker.mappers;

import com.sdu_ai_lab.project_tracker.dto.UserDto;
import com.sdu_ai_lab.project_tracker.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity entity);
    UserEntity toEntity(UserDto dto);
}
