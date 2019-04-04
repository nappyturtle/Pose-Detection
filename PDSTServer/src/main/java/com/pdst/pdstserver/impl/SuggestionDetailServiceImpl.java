package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.dto.SuggestionDetailDTOFrontEnd;
import com.pdst.pdstserver.model.SuggestionDetail;
import com.pdst.pdstserver.repository.SuggestionDetailRepository;
import com.pdst.pdstserver.service.SuggestionDetailService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SuggestionDetailServiceImpl implements SuggestionDetailService {

    private final SuggestionDetailRepository suggestionDetailRepository;

    public SuggestionDetailServiceImpl(SuggestionDetailRepository suggestionDetailRepository) {
        this.suggestionDetailRepository = suggestionDetailRepository;
    }

    @Override
    public List<SuggestionDetail> getAllSuggestionDetails() {
        return suggestionDetailRepository.findAll();
    }

    @Override
    public List<SuggestionDetail> getSuggestionDetails(String field, Object value) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(new SuggestionDetail());
        beanWrapper.setPropertyValue(field, value);
        SuggestionDetail suggestionDetail = (SuggestionDetail) beanWrapper.getWrappedInstance();
        suggestionDetail.setStatus("active");
        Example<SuggestionDetail> suggestionDetailExample = Example.of(suggestionDetail, ExampleMatcher.matchingAll().withIgnorePaths("id"));
        List<SuggestionDetail> suggestionDetails = suggestionDetailRepository.findAll(suggestionDetailExample, new Sort(Sort.Direction.ASC, "createdTime"));
        return suggestionDetails;
    }

    @Override
    public SuggestionDetail createSuggestionDetail(SuggestionDetail suggestionDetail) {
        suggestionDetail.setStatus("active");
        return suggestionDetailRepository.saveAndFlush(suggestionDetail);
    }

    @Override
    public Boolean saveComment(SuggestionDetail suggestionDetailTemp) {

        SuggestionDetail suggestionDetail1 = suggestionDetailRepository.findById(suggestionDetailTemp.getId());
        suggestionDetail1.setComment(suggestionDetailTemp.getComment());
        SuggestionDetail checked = suggestionDetailRepository.save(suggestionDetail1);
        if(checked != null){
            return true;
        }
        return false;
    }

    @Override
    public List<SuggestionDetailDTOFrontEnd> getAllSuggestionDetailByStaffOrAdmin(int suggestionId) {
        List<SuggestionDetail> suggestionDetails = suggestionDetailRepository.findAllBySuggestionIdAndStatus(suggestionId,"active");
        List<SuggestionDetailDTOFrontEnd> dtoList = new ArrayList<>();
        for(SuggestionDetail suggestionDetail :  suggestionDetails){
            SuggestionDetailDTOFrontEnd dto = new SuggestionDetailDTOFrontEnd();
            dto.setId(suggestionDetail.getId());
            dto.setImgUrl(suggestionDetail.getImgUrl());
            dto.setStandardImgUrl(suggestionDetail.getStandardImgUrl());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean editStatusSuggestionDetailByStaffOrAdmin(SuggestionDetailDTOFrontEnd dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        SuggestionDetail suggestionDetail = suggestionDetailRepository.findById(dto.getId());
        if(!dto.getStatus().equals("on")){
            suggestionDetail.setStatus(dto.getStatus());
        }

        suggestionDetail.setDescription(dto.getDescription());
        suggestionDetail.setComment(dto.getComment());
        suggestionDetail.setUpdatedTime(sdf.format(date));
        SuggestionDetail suggestionDetail1Res = suggestionDetailRepository.save(suggestionDetail);
        if(suggestionDetail1Res != null){
            return true;
        }
        return false;
    }

    @Override
    public SuggestionDetailDTOFrontEnd getSuggestionDetailById(int suggestionDetailId) {
        SuggestionDetail suggestionDetail = suggestionDetailRepository.findById(suggestionDetailId);
        SuggestionDetailDTOFrontEnd dto = new SuggestionDetailDTOFrontEnd();
        dto.setId(suggestionDetail.getId());
        dto.setImgUrl(suggestionDetail.getImgUrl());
        dto.setStandardImgUrl(suggestionDetail.getStandardImgUrl());
        dto.setComment(suggestionDetail.getComment());
        dto.setDescription(suggestionDetail.getDescription());
        dto.setStatus(suggestionDetail.getStatus());
        return dto;
    }
}
