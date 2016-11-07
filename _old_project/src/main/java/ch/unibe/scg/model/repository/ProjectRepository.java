package ch.unibe.scg.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.scg.model.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {
	public List<Project> findByName(String name);
}