package org.springframework.batch.admin.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.vendor.Database;

import java.util.Arrays;

import org.springframework.batch.admin.sample.model.Project;
import org.springframework.batch.admin.sample.model.dao.ProjectDaoInterface;
import org.springframework.batch.admin.sample.model.service.ProjectService;

public class SpringOrmMain {
	
	public static void main(String[] args) {
		
		//Create Spring application context
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring.xml");
		
		//Get service from context. (service's dependency (ProductDAO) is autowired in ProductService)
		ProjectDaoInterface projectService = ctx.getBean(ProjectService.class);
		
		//Do some data operation
		
		projectService.add(new Project("Bulb", Project.Type.GIT));
		projectService.add(new Project("Dijone mustard", Project.Type.GIT));
		
		System.out.println("listAll: " + projectService.listAll());
		
		//Test transaction rollback (duplicated key)
		
		try {
			projectService.addAll(Arrays.asList(new Project("Book", Project.Type.GIT), new Project("Soap", Project.Type.GIT), new Project("Computer", Project.Type.GIT)));
		} catch (DataAccessException dataAccessException) {
		}
		
		//Test element list after rollback
		System.out.println("listAll: " + projectService.listAll());
		
		ctx.close();
		
	}
}