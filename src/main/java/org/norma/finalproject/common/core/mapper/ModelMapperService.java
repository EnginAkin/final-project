package org.norma.finalproject.common.core.mapper;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {


    ModelMapper forDto();

    ModelMapper forRequest();
}
