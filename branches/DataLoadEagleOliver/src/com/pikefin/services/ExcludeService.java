package com.pikefin.services;

import java.util.List;

import com.pikefin.businessobjects.Exclude;
import com.pikefin.exceptions.GenericException;

public interface ExcludeService {

  public boolean isTaskExcluded(List<Exclude> excludeList)
       throws GenericException;
  public List<Exclude> loadAllExcludesByTaskId(Integer taskId)
      throws GenericException;
}
