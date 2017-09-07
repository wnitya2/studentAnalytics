package my.ais.business.service;

import java.util.LinkedList;
import java.util.List;

import my.ais.business.dao.StatusDao;
import my.ais.domain.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("statusService")
@Scope(value="singleton",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	StatusDao statusDao;	

	public List<Status> getStatusListAll() {
		return statusDao.queryAll();
	}

	public Status getStatusById(int statusId) {
		return statusDao.get(statusId);
	}

	public List<String> getStatusDescListAll() {
		List<String> result = new LinkedList<String>();
		
		for (Status s : statusDao.queryAll())
		{
			result.add(s.getStatusDesc());			
		}
		
		java.util.Collections.sort(result);
		
		return result;
	}
	
	public Status getStatusByDesc(String statusDesc) {
		int statusId = getStatusId(statusDesc);
		return statusDao.get(statusId);
	}

	public int getStatusId(String statusDesc) {
		int i = ((Integer) statusDao.getStatusId(statusDesc)).intValue();
		return i;
	}	
}
