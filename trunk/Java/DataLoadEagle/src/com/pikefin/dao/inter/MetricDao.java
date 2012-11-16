package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.Metric;
import com.pikefin.exceptions.GenericException;

public interface MetricDao {
	public Metric saveMetricInfo(Metric metricEntity) throws GenericException;
	public Metric updateMetricInfo(Metric metricEntity) throws GenericException;
	public Boolean deleteMetricInfo(Metric metricEntity ) throws GenericException;
	public Boolean deleteMetricInfoById(Integer metricId ) throws GenericException;
	public Metric loadMetricInfo(Integer metricId) throws GenericException;
	public List<Metric> loadAllMetrics() throws GenericException;
}
