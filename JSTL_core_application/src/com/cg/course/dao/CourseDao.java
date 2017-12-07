package com.cg.course.dao;
/* Table Creation in database
 * CREATE table Course(
 * courseId NUMBER(6) PRIMARY KEY,
 * courseName VARCHAR2(30),
 * courseDuration NUMBER(5)
 * );
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.helpers.Loader;

import com.cg.course.dto.Course;
import com.cg.course.exception.CourseException;
public class CourseDao {
	Properties jdbcProperties;
	public  CourseDao()
	{
		FileInputStream fis;
		try {
			URL url=Loader.getResource("oracle.properties"); 
			fis = new FileInputStream(url.getFile());
			jdbcProperties=new Properties();
			jdbcProperties.load(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		Connection con=null;
		try {
			Class.forName(jdbcProperties.getProperty("driver"));
			String url=jdbcProperties.getProperty("url");
			String unm=jdbcProperties.getProperty("user");
			String pwd=jdbcProperties.getProperty("password");
			con=DriverManager.getConnection(url,unm,pwd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	public boolean addCourse(Course course)
	{
		boolean b = false;
		Connection con=getConnection();
		int insertSuccess=0;
		String insQry="insert into Course values(?,?,?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(insQry);
			pst.setInt(1, course.getCourseId());
			pst.setString(2,course.getCourseName());
			pst.setDouble(3,course.getCourseDuration());
			insertSuccess=pst.executeUpdate();
			
		if(insertSuccess>0)
			b=true;
		else
			b=false;
		throw new CourseException("can not insert data!");
		} 
		catch (SQLException | CourseException e) {
			System.out.println(e);
		}
		return b;
	}


	public ArrayList<Course> retrieveAllCourse()
	{
		ArrayList<Course> cList=new ArrayList<Course>();
		Connection con=getConnection();
		String qry="select courseId,courseName,courseDuration from Course" ;
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery(qry);
			while(rs.next())
			{
				Course course=new Course();
				course.setCourseId(rs.getInt(1));
				course.setCourseName(rs.getString(2));
				course.setCourseDuration(rs.getInt(3));
				cList.add(course);
			}	
		
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		return cList;
	}


	public Course retrieveCourse(int cid)
	{
		Course course=null;
		System.out.println("In dao "+cid);
		Connection con=getConnection();
		String qry="select courseId ,courseName, courseDuration from Course where courseId=?";
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(qry);
			pstmt.setInt(1, cid);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next())
			{
				course=new Course();
				course.setCourseId(rs.getInt("courseId"));
				course.setCourseName(rs.getString("courseName"));
				course.setCourseDuration(rs.getInt(3));
			}	
			else
			{
				throw new CourseException("Exception while retrieving by id!");
				}
		} catch (SQLException | CourseException e) 
		{
			System.out.println(e.getMessage());
		}

		return course;
	}

	public int deleteCourse(int did) 
	{
		System.out.println("In dao "+did);
		Connection con=getConnection();
		int n = 0;
		String qry="delete from Course where courseId = "+did;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = con.prepareStatement(qry);
			n = preparedStatement.executeUpdate();
			
			if(n!=0)
				{
					System.out.println("row deleted fror id "+did);
				}
			else
			{
				throw new CourseException("id not found!");
			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e);
		} catch (CourseException e) {
			System.out.println(e);
		}
		return n;
	}
}
