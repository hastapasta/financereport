package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.PageCounters;
import com.pikefin.exceptions.GenericException;

public interface PageCountersDao {
	public PageCounters savePageCountersInfo(PageCounters pageCountersEntity) throws GenericException;
	public PageCounters updatePageCountersInfo(PageCounters pageCountersEntity) throws GenericException;
	public Boolean deletePageCountersInfo(PageCounters pageCountersEntity ) throws GenericException;
	public Boolean deletePageCountersInfoById(Integer pageCountersId ) throws GenericException;
	public PageCounters loadPageCountersInfo(Integer pageCountersId) throws GenericException;
	public List<PageCounters> loadAllPageCounters() throws GenericException;
}
