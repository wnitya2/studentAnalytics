package my.ais.business.service;

import java.util.LinkedList;
import java.util.List;

import my.ais.business.dao.ProgramDao;
import my.ais.domain.Program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("programService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ProgramServiceImpl implements ProgramService {
	
	@Autowired
	ProgramDao programDao;

	public List<Program> getProgramListAll() {
		return programDao.queryAll();
	}

	public List<Program> getProgramListNoDuplicate() {
		List<Program> result = new LinkedList<Program>();
		for (Program p : programDao.queryAll())
		{
			if (!p.getProgramId().equalsIgnoreCase("MNP") &&
					!p.getProgramId().equalsIgnoreCase("MNA") &&
					!p.getProgramId().equalsIgnoreCase("MNS"))
			{
				result.add(p);
			}
		}
		
		return result;
	}

	public Program getProgramById(String programId) {
		return programDao.get(programId);
	}

}
