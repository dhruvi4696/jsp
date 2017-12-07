package com.cg.course.service;

import java.util.ArrayList;

import com.cg.course.dao.CourseDao;
import com.cg.course.dto.Course;

public class CourseService {
	
	private CourseDao eDao;
	
	public CourseService()
	{
		eDao=new CourseDao();
	}
	
	public boolean addcourse(Course course)
	{
		return eDao.addCourse(course);
	}
	
	public ArrayList<Course> retrieveAllcourse()
	{
		return eDao.retrieveAllCourse() ;
	}
	
	public Course retrieveCourse(int eid)
	{
		return eDao.retrieveCourse(eid);
	}

	public int deleteCourse(int did) {
		return eDao.deleteCourse(did);
	}
	
	
}
