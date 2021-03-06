package com.zlh.redistest.dao;

import com.zlh.redistest.domain.LearnResouce;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by tengj on 2017/4/22.
 */

@Mapper
public interface LearnMapper {
    int add(LearnResouce learnResouce);
    int update(LearnResouce learnResouce);
    int deleteByIds(String[] ids);
    LearnResouce queryLearnResouceById(Long id);
    public List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
