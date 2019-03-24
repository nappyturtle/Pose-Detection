package com.pdst.pdstserver.services.suggestiondetailservice;

import com.pdst.pdstserver.dtos.SuggestionDTOFrontEnd;
import com.pdst.pdstserver.dtos.SuggestionDetailDTOFrontEnd;
import com.pdst.pdstserver.models.SuggestionDetail;
import com.pdst.pdstserver.repositories.SuggestionDetailRepository;

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
    public List<SuggestionDetailDTOFrontEnd> getAllSuggestionDetailByStaffOrAdmin() {
        List<SuggestionDetail> suggestionDetails = suggestionDetailRepository.findAll();
        List<SuggestionDetailDTOFrontEnd> dtoList = new ArrayList<>();
        for(SuggestionDetail suggestionDetail :  suggestionDetails){
            SuggestionDetailDTOFrontEnd dto = new SuggestionDetailDTOFrontEnd();
            dto.setId(suggestionDetail.getId());
            dto.setImgUrl(suggestionDetail.getImgUrl());
            dto.setStandardImgUrl(suggestionDetail.getStandardImgUrl());
            dto.setDescription(suggestionDetail.getDescription());
            dto.setStatus(suggestionDetail.getStatus());
            dto.setComment(suggestionDetail.getComment());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean editStatusSuggestionDetailByStaffOrAdmin(int id, String status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = Calendar.getInstance().getTime();
        SuggestionDetail suggestionDetail = suggestionDetailRepository.findById(id);
        suggestionDetail.setStatus(status);
        suggestionDetail.setUpdatedTime(sdf.format(date));
        SuggestionDetail suggestionDetail1Res = suggestionDetailRepository.save(suggestionDetail);
        if(suggestionDetail1Res != null){
            return true;
        }
        return false;
    }
}
