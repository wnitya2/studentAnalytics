package my.ais.business.service;

import java.util.List;

import my.ais.domain.Status;

public interface StatusService {
	
	List<Status> getStatusListAll();
	List<String> getStatusDescListAll();
	Status getStatusById(int statusId);		
	Status getStatusByDesc(String statusDesc);
	int getStatusId(String statusDesc);
	
}
