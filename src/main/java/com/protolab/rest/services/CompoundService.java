package com.protolab.rest.services;

import com.protolab.rest.mappers.CompoundMapper;
import com.protolab.rest.mappers.HazardStatementMapper;
import com.protolab.rest.mappers.PictogramMapper;
import com.protolab.rest.mappers.PrecautionaryStatementMapper;
import com.protolab.rest.models.Compound;
import com.protolab.rest.models.dtos.CompoundDto;
import com.protolab.rest.repositories.CompoundRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompoundService {

    private CompoundRepository compoundRepository;
    private CompoundMapper compoundMapper;
    private HazardStatementMapper hazardStatementMapper;
    private PrecautionaryStatementMapper precautionaryStatementMapper;
    private PictogramMapper pictogramMapper;

    public CompoundService(CompoundRepository compoundRepository, CompoundMapper compoundMapper,
                           HazardStatementMapper hazardStatementMapper,
                           PrecautionaryStatementMapper precautionaryStatementMapper,
                           PictogramMapper pictogramMapper) {
        this.compoundRepository = compoundRepository;
        this.compoundMapper = compoundMapper;
        this.hazardStatementMapper = hazardStatementMapper;
        this.precautionaryStatementMapper = precautionaryStatementMapper;
        this.pictogramMapper = pictogramMapper;
    }

    public List<Compound> getCompounds() {
        return compoundRepository.findAll();
    }

    public List<CompoundDto> getCompoundsNoTimestamp() {
        return compoundRepository
                .findAll()
                .stream()
                .map(compoundMapper::map)
                .collect(Collectors.toList());
    }

    public Compound addCompound(CompoundDto compoundDto) {
        return compoundRepository
                .save(compoundMapper.reverseMap(compoundDto));
    }

    public void updateCompound(CompoundDto compoundDto) {
        compoundRepository
                .findCompoundByCompoundId(compoundDto.getCompoundId())
                .ifPresent(c -> {
                    c.setName(compoundDto.getName());
                    c.setCas(compoundDto.getCas());
                    c.setWe(compoundDto.getWe());
                    c.setFormula(compoundDto.getFormula());
                    c.setUn(compoundDto.getUn());
                    c.setBaseClass(compoundDto.getBaseClass());
                    c.setHelperClass(compoundDto.getHelperClass());
                    c.setPackagingGroup(compoundDto.getPackagingGroup());
                    c.setApplicableHazardStatements(
                            compoundDto.getHazardStatementDtos()
                            .stream().map(hazardStatementMapper::reverseMap)
                            .collect(Collectors.toList()));
                    c.setApplicablePrecautionaryStatements(
                            compoundDto.getPrecautionaryStatementDtos()
                            .stream().map(precautionaryStatementMapper::reverseMap)
                            .collect(Collectors.toList()));
                    c.setApplicablePictograms(
                            compoundDto.getPictogramDtos()
                            .stream().map(pictogramMapper::reverseMap)
                            .collect(Collectors.toList())
                    );

                    compoundRepository.save(c);
                });
    }

    public void deleteCompound(Long compoundId) {
            compoundRepository.findCompoundByCompoundId(compoundId)
                    .ifPresent(c -> compoundRepository.delete(c));
    }

//  NOT IN USE YET
//
//    public List<CompoundDto> getCompoundsByNameOrFormula(String searchString) {
//        String pattern = "%" + searchString + "%";
//        return compoundRepository
//                .findCompoundsByNameOrFormula(pattern)
//                .stream()
//                .map(compoundMapper::map)
//                .collect(Collectors.toList());
//    }
//
//
//    public void deleteCompound(String name) {
//        compoundRepository.deleteCompoundByCompoundName(name);
//    }
}