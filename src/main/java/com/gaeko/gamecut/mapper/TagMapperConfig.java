package com.gaeko.gamecut.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.MapperConfig;

@MapperConfig
@DecoratedWith(TagMapperDecorator.class)
public interface TagMapperConfig {
}
