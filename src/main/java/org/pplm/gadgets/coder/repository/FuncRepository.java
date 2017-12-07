package org.pplm.gadgets.coder.repository;

import java.util.List;

import org.pplm.gadgets.coder.entity.Func;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FuncRepository extends BaseRepository<Func> {
	
	public Page<Func> findAllByPidAndDeleteFlag(String pid, Integer deleteFlag, Pageable pageable);
	public List<Func> findAllByPidAndDeleteFlag(String pid, Integer deleteFlag);
	
}
