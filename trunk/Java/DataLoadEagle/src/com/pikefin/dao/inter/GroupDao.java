package com.pikefin.dao.inter;

import java.util.List;
import com.pikefin.businessobjects.Groups;
import com.pikefin.exceptions.GenericException;

public interface GroupDao {
	public Groups saveGroupInfo(Groups GroupEntity) throws GenericException;
	public Groups updateGroupInfo(Groups GroupEntity) throws GenericException;
	public Boolean deleteGroupInfo(Groups GroupEntity ) throws GenericException;
	public Boolean deleteGroupInfoById(Integer GroupId ) throws GenericException;
	public Groups loadGroupInfo(Integer GroupId) throws GenericException;
	public List<Groups> loadAllGroups() throws GenericException;

}
