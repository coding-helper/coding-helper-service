package org.pplm.gadgets.coder.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.pplm.gadgets.coder.bean.Example;
import org.pplm.gadgets.coder.bean.Record;
import org.pplm.gadgets.coder.mapper.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Transactional
public abstract class BaseService<R extends Record, E extends Example> {	

	private BaseMapper<R, E> mapper;
	
	public BaseService(BaseMapper<R, E> mapper) {
		super();
		this.mapper = mapper;
	}

	public Page<R> selectByExample(E example, Pageable pageable) {
		PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
		List<R> list = mapper.selectByExample(example);
		return new PageImpl<R>(list, pageable, ((com.github.pagehelper.Page<R>)list).getTotal());
	}
	
    public long countByExample(E example) {
    	return mapper.countByExample(example);
    }
    public int deleteByExample(E example) {
    	return mapper.deleteByExample(example);
    }
    
    public int deleteByPrimaryKey(String id) {
    	return mapper.deleteByPrimaryKey(id);
    }
    
    public int insert(R record) {
    	return mapper.insert(record);
    }
    
    public int insertSelective(R record) {
    	return mapper.insertSelective(record);
    }
    
    public List<R> selectByExample(E example) {
    	return mapper.selectByExample(example);
    }
    
    public R selectByPrimaryKey(String id) {
    	return mapper.selectByPrimaryKey(id);
    }
    
    public int updateByExampleSelective(@Param("record") R record, @Param("example") E example) {
    	return mapper.updateByExampleSelective(record, example);
    }
    
    public int updateByExample(@Param("record") R record, @Param("example") E example) {
    	return mapper.updateByExample(record, example);
    }
    
    public int updateByPrimaryKeySelective(R record) {
    	return mapper.updateByPrimaryKeySelective(record);
    }
    
    public int updateByPrimaryKey(R record) {
    	return mapper.updateByPrimaryKey(record);
    }

}