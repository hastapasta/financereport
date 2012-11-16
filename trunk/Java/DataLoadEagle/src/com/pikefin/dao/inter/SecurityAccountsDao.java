package com.pikefin.dao.inter;

import java.util.List;

import com.pikefin.businessobjects.SecurityAccounts;
import com.pikefin.exceptions.GenericException;

public interface SecurityAccountsDao {
	public SecurityAccounts saveSecurityAccountsInfo(SecurityAccounts columnEntity) throws GenericException;
	public SecurityAccounts updateSecurityAccountsInfo(SecurityAccounts columnEntity) throws GenericException;
	public Boolean deleteSecurityAccountsInfo(SecurityAccounts columnEntity ) throws GenericException;
	public Boolean deleteSecurityAccountsInfoById(Integer securityAccountId ) throws GenericException;
	public SecurityAccounts loadSecurityAccountsInfo(Integer securityAccountId) throws GenericException;
	public List<SecurityAccounts> loadAllSecurityAccounts() throws GenericException;
}
