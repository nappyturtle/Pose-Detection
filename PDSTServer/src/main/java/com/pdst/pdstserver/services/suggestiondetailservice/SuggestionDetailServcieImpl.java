package com.pdst.pdstserver.services.suggestiondetailservice;

import com.pdst.pdstserver.models.SuggestionDetail;
import com.pdst.pdstserver.repositories.SuggestionDetailRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestionDetailServcieImpl implements SuggestionDetailServcie {

    private final SuggestionDetailRepository suggestionDetailRepository;

    public SuggestionDetailServcieImpl(SuggestionDetailRepository suggestionDetailRepository) {
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
}
