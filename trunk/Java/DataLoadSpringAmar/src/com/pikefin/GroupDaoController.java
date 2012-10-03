package com.pikefin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pikefin.businessobjects.Groups;
import com.pikefin.dao.inter.GroupDao;

@Controller
public class GroupDaoController {
	@Autowired
	private GroupDao groupDao;
	@RequestMapping("/save")
	public void saveGroup(){
		Groups groups=new Groups();
		groups.setName("Hello Group");
		groups.setCreated(new Date());
		try{
			groupDao.saveGroupInfo(groups);
		}catch (Exception e) {
			System.out.println(e);
		}
	
	}
}
