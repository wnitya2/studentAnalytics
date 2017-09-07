package my.ais.business.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import my.ais.domain.Student;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class StudentDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Student> queryAll() {
        Query query = em.createQuery("from Student as s order by s.matrixId");
        List<Student> result = query.getResultList();     
        
        //set transient variables
        for (Student s : result){
        	setTransientVariables(s);
        }              
        return result;
    }
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Student> queryResearchStudentAll() {       
        List<Student> result = em.createQuery("select s from Student s where s.courseType LIKE :courseType")
									.setParameter("courseType", "%RESEARCH%")
									.getResultList();       
        //set transient variables
        for (Student s : result){
        	setTransientVariables(s);
        }              
        return result;
    }
	
	@Transactional(readOnly=true)
    public Student reload(Student student) {
		Student s = em.find(Student.class, student.getMatrixId());
		
		if (s != null)
		{
			setTransientVariables(s);
		}	
        return s;
    }
	
	@Transactional(readOnly=true)
    public Student get(String matrix) {	
		Student s = em.find(Student.class, matrix);		
		
		if (s != null)
		{
			setTransientVariables(s);
		}		
        return s;
    }	
 
    @Transactional
    public Student save(Student student) {
    	if (student != null)
		{
			setTransientVariables(student);
		}
        em.persist(student);
        return student;
    }
    
    @Transactional
    public Student update(Student student) {
    	if (student != null)
		{
			setTransientVariables(student);
		}
        student = em.merge(student);
        return student;
    }
    
    @Transactional
    public void delete(Student student) {
    	Student r = get(student.getMatrixId());
    	if(r!=null){
    		//em.remove(r);
    		Query query = em.createNativeQuery("delete from student where matrix_id='"+r.getMatrixId()+"'");	
    		query.executeUpdate();
    	}
    } 	
    
	private void setTransientVariables(Student s) {
		s.setProgramId(s.getProgram().getProgramId());
		s.setProgramDesc(s.getProgram().getProgramDesc());       
		s.setStatusId(s.getStatus().getStatusId());
		s.setStatusDesc(s.getStatus().getStatusDesc());
	}
	
	/*CHART - GENERAL CUSTOMIZATION*/
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartAllSemester() {       
		Query query = em.createNativeQuery("select distinct (enrolment_sem), "
				+ "count(*) as count from student group by enrolment_sem order by enrolment_sem asc;");		
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartAllYear(){
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) as count from student "
    			+ "group by substring(enrolment_date,1,4) "
    			+ "order by substring(enrolment_date,1,4) asc;");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountYearRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) as count from student  "
    			+ "where substring(enrolment_date,1,4) between " + min +" and "+ max +" " 
    			+ "group by substring(enrolment_date,1,4) "
    			+ "order by substring(enrolment_date,1,4) asc;");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (enrolment_sem), "
    			+ "count(*) as count from student "
    			+ "where enrolment_sem between " + min + " and " + max +" "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");		
        return query.getResultList();
    }
    
    /*CHART - COUNTRY CUSTOMIZATION*/
    private String getWhereClauseCountry(String country) {
		String whereClause = "";    	
    	if (country.equalsIgnoreCase("LOCAL")){
    		whereClause = "country ='MALAYSIA' ";
    	}
    	else if (country.equalsIgnoreCase("INTERNATIONAL")){
    		whereClause = "country not like 'MALAYSIA' ";
    	}
    	else{
    		whereClause = "country ='"+country+"' ";
    	}
		return whereClause;
	} 
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllCountryAllYear(){
    	Query query = em.createNativeQuery("select distinct (country) as country, "
    			+ "count(*) as count "
    			+ "from student "
    			+ "group by country "
    			+ "order by count desc;");		
        return query.getResultList();
    }
    
    @Transactional(readOnly=true)
	public Object chartSingleCountLocalAllYear(){
		Query query = em.createNativeQuery("SELECT count(*) from student where country = 'MALAYSIA';");		
	    return query.getSingleResult();
	}
    
    @Transactional(readOnly=true)
   	public Object chartSingleCountInterAllYear(){
   		Query query = em.createNativeQuery("SELECT count(*) from student where country not like 'MALAYSIA';");		
   	    return query.getSingleResult();
   	}
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountInterAllYear(){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) AS count from student "
    			+ "where country not like 'MALAYSIA' "
    			+ "group by (substring(enrolment_date,1,4));");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLocalAllYear(){
       	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
       			+ "count(*) AS count from student "
       			+ "where country = 'MALAYSIA' "
       			+ "group by (substring(enrolment_date,1,4));");
        return query.getResultList();
    }	
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificCountryAllYear(String country){
    	String whereClause = getWhereClauseCountry(country);
    	
       	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
       			+ "count(*) AS count from student where " + whereClause
       			+ "group by (substring(enrolment_date,1,4));");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllCountrySpecificYear(String year){
    	Query query = em.createNativeQuery("select distinct (country) as country, "
    			+ "count(*) as count from student "
    			+ "where substring(enrolment_date,1,4) ='"+year+"'  "
    			+ "group by country order by count desc;");
        return query.getResultList();    	
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountLocalSpecificYear(String year){
    	Query query = em.createNativeQuery("SELECT count(*) AS count from student "
    			+ "where country = 'MALAYSIA' and (substring(enrolment_date,1,4)) ='"+year+"'");    	
        return query.getSingleResult();     	
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountInterSpecificYear(String year){
    	Query query = em.createNativeQuery("SELECT count(*) AS count from student "
    			+ "where country not like 'MALAYSIA' and (substring(enrolment_date,1,4)) ='"+year+"'");    	
        return query.getSingleResult();    	
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllCountrySpecificSemester(String semester){
    	Query query = em.createNativeQuery("select distinct (country) as country, "
    			+ "count(*) as count from student "
    			+ "where enrolment_sem ='"+semester+"'  "
    			+ "group by country order by count desc;");
        return query.getResultList();    	
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountLocalSpecificSemester(String semester){
    	Query query = em.createNativeQuery("SELECT count(*) AS count from student "
    			+ "where country = 'MALAYSIA' and enrolment_sem ='"+semester+"'");    	
        return query.getSingleResult();     	
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountInterSpecificSemester(String semester){
    	Query query = em.createNativeQuery("SELECT count(*) AS count from student "
    			+ "where country not like 'MALAYSIA' and enrolment_sem ='"+semester+"'");    	
        return query.getSingleResult();    	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLocalYearRange(String min, String max){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) AS count from student where country = 'MALAYSIA' "
    			+ "and (substring(enrolment_date,1,4)) between '"+min+"' and '"+max+"' "
    			+ "group by substring(enrolment_date,1,4) "
    			+ "order by substring(enrolment_date,1,4) asc;");    	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountInterYearRange(String min, String max ){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) AS count from student where country not like 'MALAYSIA' "
    			+ "and (substring(enrolment_date,1,4)) between '"+min+"' and '"+max+"' "
    			+ "group by substring(enrolment_date,1,4) "
    			+ "order by substring(enrolment_date,1,4) asc;");    	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificCountryYearRange(String country, String min, String max ){
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, "
    			+ "count(*) AS count from student where " + whereClause
    			+ "and (substring(enrolment_date,1,4)) between '"+min+"' and '"+max+"' "
    			+ "group by substring(enrolment_date,1,4) "
    			+ "order by substring(enrolment_date,1,4) asc;");    	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLocalSemesterRange(String min, String max ){
    	Query query = em.createNativeQuery("SELECT distinct (enrolment_sem) as year, "
    			+ "count(*) AS count from student where country = 'MALAYSIA' "
    			+ "and (enrolment_sem) between '"+min+"' and '"+max+"' "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");    	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountInterSemesterRange(String min, String max ){
    	Query query = em.createNativeQuery("SELECT distinct (enrolment_sem) as year, "
    			+ "count(*) AS count from student where country not like 'MALAYSIA' "
    			+ "and (enrolment_sem) between '"+min+"' and '"+max+"' "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");    	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificCountrySemesterRange(String country, String min, String max ){
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("SELECT distinct (enrolment_sem) as year, "
    			+ "count(*) AS count from student where " + whereClause
    			+ "and (enrolment_sem) between '"+min+"' and '"+max+"' "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");   	
        return query.getResultList();     	
    }
    
    /*CHART - LEVEL CUSTOMIZATION*/
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelAllYear(){
    	Query query = em.createNativeQuery("select distinct (level) as level, "
    			+ "count(*) as count from student "
    			+ "group by level;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelMasterAllCountryAllYear(){
    	Query query = em.createNativeQuery("select distinct (country) as country, "
    			+ "count(*) as count from student "
    			+ "where level ='master' "
    			+ "group by country "
    			+ "order by count desc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelPhdAllCountryAllYear(){
    	Query query = em.createNativeQuery("select distinct (country) as country, "
    			+ "count(*) as count from student "
    			+ "where level ='phd' "
    			+ "group by country "
    			+ "order by count desc;");   	
        return query.getResultList();     	
    }   
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelLocalAllYear(){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, level, "
    			+ "count(*) AS count from student "
    			+ "where country ='MALAYSIA' group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelInterAllYear(){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, level, "
    			+ "count(*) AS count from student "
    			+ "where country not like 'MALAYSIA' "
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelSpecificCountryAllYear(String country){
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, level, "
    			+ "count(*) AS count from student "
    			+ "where " + whereClause
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelSpecificYear(String year){
	   	Query query = em.createNativeQuery("select distinct (level) as level, "
	   			+ "count(*) as count from student "
	   			+ "where (substring(enrolment_date,1,4)) ='"+year+"' "
	   			+ "group by level;");   	
	   	return query.getResultList();     	
    }
   
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelMasterAllCountrySpecificYear(String year){
	   	Query query = em.createNativeQuery("select distinct (country) as country, "
	   			+ "count(*) as count from student "
	   			+ "where level ='master' and (substring(enrolment_date,1,4)) ='"+year+"' "
	   			+ "group by country "
	   			+ "order by count desc;");   	
	   	return query.getResultList();     	
    }
       
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelPhdAllCountrySpecificYear(String year){
	   	Query query = em.createNativeQuery("select distinct (country) as country, "
	   			+ "count(*) as count from student "
	   			+ "where level ='phd' and (substring(enrolment_date,1,4)) ='"+year+"' "
	   			+ "group by country "
	   			+ "order by count desc;");   	
	   	return query.getResultList();     	
    }   
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelLocalSpecificYear(String year){
    	Query query = em.createNativeQuery("select distinct(level) as level, count(*) as count from student "
       			+ "where country = 'MALAYSIA' and (substring(enrolment_date,1,4)) ='"+year+"' "
       			+ "group by level;");   	
    	return query.getResultList();     	
    }
       
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelInterSpecificYear(String year){
    	Query query = em.createNativeQuery("select distinct(level) as level, count(*) as count from student "
          			+ "where country not like 'MALAYSIA' and (substring(enrolment_date,1,4)) ='"+year+"' "
          			+ "group by level;");  	
    	return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelSpecificSemester(String sem){
	   	Query query = em.createNativeQuery("select distinct (level) as level, count(*) as count from student "
	   			+ "where enrolment_sem ='"+sem+"' "
	   			+ "group by level;");   	
	   	return query.getResultList();     	
    }
   
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelMasterAllCountrySpecificSemester(String sem){
	   	Query query = em.createNativeQuery("select distinct (country) as country, count(*) as count from student "
	   			+ "where level ='master' and enrolment_sem ='"+sem+"' "
	   			+ "group by country order by count desc;");   	
	   	return query.getResultList();     	
    }
       
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelPhdAllCountrySpecificSemester(String sem){
    	Query query = em.createNativeQuery("select distinct (country) as country, count(*) as count from student "
	   			+ "where level ='phd' and enrolment_sem ='"+sem+"' "
	   			+ "group by country order by count desc;");   	
	   	return query.getResultList();     	
    }   
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelLocalSpecificSemester(String sem){
    	Query query = em.createNativeQuery("select distinct(level) as level, count(*) as count from student "
       			+ "where country = 'MALAYSIA' and enrolment_sem ='"+sem+"' "
       			+ "group by level;");   	
    	return query.getResultList();     	
    }
       
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelInterSpecificSemester(String sem){
    	Query query = em.createNativeQuery("select distinct(level) as level, count(*) as count from student "
          			+ "where country not like 'MALAYSIA' and enrolment_sem ='"+sem+"' "
          			+ "group by level;");  	
    	return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelAllCountriesYearRange(String min, String max){
    	Query query = em.createNativeQuery("SELECT distinct (substring(enrolment_date,1,4)) as year, level, count(*) AS count "
    			+ "from student where substring(enrolment_date,1,4) between "+min+" and "+max +" "
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");
    	return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelLocalYearRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, level, count(*) as count from student "
    			+ "where country = 'MALAYSIA' and substring(enrolment_date,1,4) between "+min+" and "+max +" "
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelInterYearRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, level, count(*) as count from student "
    			+ "where country not like 'MALAYSIA' and substring(enrolment_date,1,4) between "+min+" and "+max +" "
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelSpecificCountryYearRange(String country, String min, String max){
    	String whereClause = getWhereClauseCountry(country);
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, level, count(*) as count from student "
    			+ "where "+whereClause+"and substring(enrolment_date,1,4) between "+min+" and "+max +" "
    			+ "group by substring(enrolment_date,1,4), level "
    			+ "order by substring(enrolment_date,1,4) asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelAllCountriesSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("SELECT distinct (enrolment_sem) as semester, level, count(*) AS count from student "
    			+ "where enrolment_sem between "+min+" and "+max +" "
    			+ "group by enrolment_sem, level "
    			+ "order by enrolment_sem asc, level asc;");
    	return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelLocalSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (enrolment_sem) as semester, level, count(*) as count from student "
    			+ "where country = 'MALAYSIA' and enrolment_sem between "+min+" and "+max +" "
    			+ "group by enrolment_sem, level "
    			+ "order by enrolment_sem asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelInterSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select distinct (enrolment_sem) as semester, level, count(*) as count from student "
    			+ "where country not like 'MALAYSIA' and enrolment_sem between "+min+" and "+max +" "
    			+ "group by enrolment_sem, level "
    			+ "order by enrolment_sem asc, level asc;");   	
        return query.getResultList();     	
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountLevelSpecificCountrySemesterRange(String country, String min, String max){
    	String whereClause = getWhereClauseCountry(country);
    	Query query = em.createNativeQuery("select distinct (enrolment_sem) as semester, level, count(*) as count from student "
    			+ "where "+whereClause+"and enrolment_sem between "+min+" and "+max +" "
    			+ "group by enrolment_sem, level "
    			+ "order by enrolment_sem asc, level asc;");   	
        return query.getResultList();     	
    }
    
    /*CHART - PROGRAM CUSTOMIZATION*/
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramAllCountriesAllYear(){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramAllCountriesSpecificYear(String year){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where (substring(enrolment_date,1,4)) ='"+year+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramAllCountriesSpecificSemester(String sem){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where enrolment_sem ='"+sem+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramAllCountriesYearRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramAllCountriesSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where enrolment_sem between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramLocalAllYear(){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country='MALAYSIA' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramInterAllYear(){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country!='MALAYSIA' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramLocalSpecificYear(String year){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country='MALAYSIA' and (substring(enrolment_date,1,4)) = '"+year+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramInterSpecificYear(String year){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country!='MALAYSIA' and (substring(enrolment_date,1,4)) = '"+year+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramLocalSpecificSemester(String sem){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country='MALAYSIA' and enrolment_sem = '"+sem+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramInterSpecificSemester(String sem){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country!='MALAYSIA' and enrolment_sem = '"+sem+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramLocalYearRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country='MALAYSIA' and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramInterYearRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country!='MALAYSIA' and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramLocalSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country='MALAYSIA' and enrolment_sem between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramInterSemesterRange(String min, String max){
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where country!='MALAYSIA' and enrolment_sem between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }   
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramSpecificCountryAllYear(String country){
    	String whereClause = getWhereClauseCountry(country);    	
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where "+whereClause
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramSpecificCountrySpecificYear(String country, String year){
    	String whereClause = getWhereClauseCountry(country); 
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where "+whereClause+" and (substring(enrolment_date,1,4)) = '"+year+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramSpecificCountrySpecificSemester(String country, String sem){
    	String whereClause = getWhereClauseCountry(country); 
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where "+whereClause+" and enrolment_sem = '"+sem+"' "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramSpecificCountryYearRange(String country, String min, String max){
    	String whereClause = getWhereClauseCountry(country);
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where "+whereClause+" and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountAllProgramSpecificCountrySemesterRange(String country, String min, String max){
    	String whereClause = getWhereClauseCountry(country);
    	Query query = em.createNativeQuery("select program_id, count(*) as count from student "
    			+ "where "+whereClause+" and enrolment_sem between "+min+" and "+max+" "
    			+ "group by program_id "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramAllCountryAllYear(String program){    	
    	Query query = em.createNativeQuery("select country, count(*) as count from student "
    			+ "where program_id = '"+program+"' "
    			+ "group by country "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramAllCountrySpecificYear(String program, String year){    	
    	Query query = em.createNativeQuery("select country, count(*) as count from student "
    			+ "where program_id = '"+program+"'  and (substring(enrolment_date,1,4))= '"+year+"' "
    			+ "group by country "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramAllCountrySpecificSemester(String program, String sem){    	
    	Query query = em.createNativeQuery("select country, count(*) as count from student "
    			+ "where program_id = '"+program+"'  and enrolment_sem= '"+sem+"' "
    			+ "group by country "
    			+ "order by count desc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramAllCountryYearRange(String program, String min, String max){    	
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year,count(*) as count from student "
    			+ "where program_id='"+program+"' and (substring(enrolment_date,1,4)) between "+min+" and "+max +" "
    			+ "group by (substring(enrolment_date,1,4)) "
    			+ "order by (substring(enrolment_date,1,4)) asc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramAllCountrySemesterRange(String program, String min, String max){    	
    	Query query = em.createNativeQuery("select distinct enrolment_sem as semester,count(*) as count from student "
    			+ "where program_id='"+program+"' and enrolment_sem between "+min+" and "+max +" "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");   	
        return query.getResultList(); 
    }
    
  	@Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramLocalAllYear(String program){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and country='MALAYSIA';");   	
        return query.getSingleResult();
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramInterAllYear(String program){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and country!='MALAYSIA';");   	
        return query.getSingleResult();
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramLocalSpecificYear(String program, String year){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and (substring(enrolment_date,1,4))= '"+year+"' and country='MALAYSIA';");   	
        return query.getSingleResult(); 
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramInterSpecificYear(String program, String year){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and (substring(enrolment_date,1,4))= '"+year+"' and country!='MALAYSIA';");   	
        return query.getSingleResult();
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramLocalSpecificSemester(String program, String sem){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and enrolment_sem= '"+sem+"' and country='MALAYSIA';");   	
        return query.getSingleResult();
    }
    
    @Transactional(readOnly=true)
    public Object chartSingleCountSpecificProgramInterSpecificSemester(String program, String sem){    	
    	Query query = em.createNativeQuery("select count(*) as count from student "
    			+ "where program_id='"+program+"' and enrolment_sem= '"+sem+"' and country!='MALAYSIA';");   	
        return query.getSingleResult(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramLocalYearRange(String program, String min, String max){    	
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, count(*) as count from student "
    			+ "where country='MALAYSIA' and program_id='"+program+"' and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by (substring(enrolment_date,1,4)) "
    			+ "order by (substring(enrolment_date,1,4)) asc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramInterYearRange(String program, String min, String max){    	
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, count(*) as count from student "
    			+ "where country!='MALAYSIA' and program_id='"+program+"' and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
    			+ "group by (substring(enrolment_date,1,4)) "
    			+ "order by (substring(enrolment_date,1,4)) asc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramLocalSemesterRange(String program, String min, String max){    	
    	Query query = em.createNativeQuery("select distinct enrolment_sem as semester, count(*) as count from student "
    			+ "where country='MALAYSIA' and program_id='"+program+"' and enrolment_sem between "+min+" and "+max+" "
    			+ "group by enrolment_sem "
    			+ "order by enrolment_sem asc;");   	
        return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramInterSemesterRange(String program, String min, String max){    	
   	Query query = em.createNativeQuery("select distinct enrolment_sem as semester, count(*) as count from student "
   			+ "where country!='MALAYSIA' and program_id='"+program+"' and enrolment_sem between "+min+" and "+max+" "
   			+ "group by enrolment_sem "
   			+ "order by enrolment_sem asc;");   	
       return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramSpecificCountryAllYear(String program, String country){
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, count(*) as count from student "
   			+ "where "+whereClause+" and program_id='"+program+"' "
   			+ "group by (substring(enrolment_date,1,4)) "
   			+ "order by (substring(enrolment_date,1,4)) asc;");   	
    	return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramSpecificCountryYearRange(String program, String country, String min, String max){    
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("select distinct (substring(enrolment_date,1,4)) as year, count(*) as count from student "
   			+ "where "+whereClause+" and program_id='"+program+"'  and (substring(enrolment_date,1,4)) between "+min+" and "+max+" "
   			+ "group by (substring(enrolment_date,1,4)) "
   			+ "order by (substring(enrolment_date,1,4)) asc;");   	
    	return query.getResultList(); 
    }
    
    @SuppressWarnings("unchecked")
   	@Transactional(readOnly=true)
    public List<Object[]> chartCountSpecificProgramSpecificCountrySemesterRange(String program, String country, String min, String max){   
    	String whereClause = getWhereClauseCountry(country);
    	
    	Query query = em.createNativeQuery("select distinct enrolment_sem as semester, count(*) as count from student "
   			+ "where "+whereClause+" and program_id='"+program+"'  and enrolment_sem between "+min+" and "+max+" "
   			+ "group by enrolment_sem "
   			+ "order by enrolment_sem asc;");   	
       return query.getResultList(); 
    }    
}
