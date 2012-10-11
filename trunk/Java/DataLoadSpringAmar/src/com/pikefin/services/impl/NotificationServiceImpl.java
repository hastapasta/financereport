package com.pikefin.services.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pikefin.exceptions.GenericException;
import com.pikefin.services.inter.NotificationService;
@Service
public class NotificationServiceImpl implements NotificationService{
	@Autowired
	private VelocityEngine velocityEngine;
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String generateVelocity(List<HashMap<String, String>> hm,
		String strListName, String emailTemplate) throws GenericException {
		VelocityContext vc = new VelocityContext();
		Template template = velocityEngine.getTemplate(emailTemplate);
		vc.put(strListName, hm);
		StringWriter writer = new StringWriter();
		template.merge(vc,writer);
		return writer.toString();
	}

}
