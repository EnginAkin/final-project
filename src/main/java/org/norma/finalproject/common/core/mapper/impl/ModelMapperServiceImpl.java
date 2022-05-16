package org.norma.finalproject.common.core.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.norma.finalproject.common.core.mapper.ModelMapperService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelMapperServiceImpl implements ModelMapperService {
    private final ModelMapper modelMapper;


    public ModelMapper forDto() {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    public ModelMapper forRequest() {
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
