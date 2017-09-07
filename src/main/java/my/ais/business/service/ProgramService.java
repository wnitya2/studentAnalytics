package my.ais.business.service;

import java.util.List;

import my.ais.domain.Program;

public interface ProgramService {
	
	List<Program> getProgramListAll();	
	List<Program> getProgramListNoDuplicate();
	Program getProgramById(String programId);
	
	

}
