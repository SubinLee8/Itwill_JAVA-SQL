package com.itwill.jdbc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.itwill.jdbc.model.Blog;
import static com.itwill.jdbc.model.Blog.Entity.*;
import static com.itwill.jdbc.oracle.OracleJdbc.*;
import oracle.jdbc.OracleDriver;

//MVC아키텍쳐에서 Controller 담당하는 객체. Dao.
//CRUD(Create, Read, Update, Delete) -> sql의 insert,select,update,delete 메소드와 같다.
//DB테이블에서 insert, selelct, update, delete 쿼리를 실행하고 결과를 리턴하는 기능.
public enum BlogDao {
	INSTANCE; // 블로그 다오 타입의 싱글톤 객체.

	// enum 타입 생성자는 private만 가능.
	BlogDao() {
		try {
			// 오라클 jdbc 드라이버를 메모리에 로딩/등록.
			DriverManager.registerDriver(new OracleDriver());
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			if(rs!=null) rs.close();
			if(stmt!=null)stmt.close();
			if(conn!=null)conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeResources (Connection conn, PreparedStatement stmt) {
		closeResources(conn, stmt,null);
	}
	
	private Blog getBlogFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt(COL_ID);
		String title = rs.getString(COL_TITLE);
		String content=rs.getString(COL_CONTENT);
		String author=rs.getString(COL_AUTHOR);
		Timestamp createdTime=rs.getTimestamp(COL_CREATED_TIME);
		Timestamp modifiedTime=rs.getTimestamp(COL_MODIFIED_TIME);
		return Blog.builder().id(id).title(title).author(author).content(content)
				.createdTime(createdTime).modifiedTime(modifiedTime).build();
	}
	
	//전체목록보기에서 사용할 문장.
	private static final String SQL_SELECT_ALL = String.format("select * from %s order by %s", TBL_BLOGS, COL_ID);
	
	//CRUD의 Read
	public List<Blog> read() {
		List<Blog> blogs = new ArrayList<Blog>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			stmt=conn.prepareStatement(SQL_SELECT_ALL);
			rs=stmt.executeQuery();
			while(rs.next()) {
				Blog blog = getBlogFromResultSet(rs);
				blogs.add(blog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt, rs);
		}
		
		return blogs;
	}
	
	//새 블로그 작성에서 사용할 sql 문장
	private static final String SQL_INSERT=
			String.format("insert into %s (%s,%s,%s,%s,%s) values(?,?,?,systimestamp,systimestamp)", 
			TBL_BLOGS, COL_TITLE, COL_CONTENT, COL_AUTHOR, COL_CREATED_TIME, COL_MODIFIED_TIME);
	//CRUD의 Create
	public int create(Blog blog) {
		int result = 0;
		Connection conn=null;
		PreparedStatement stmt = null;
		
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			stmt=conn.prepareStatement(SQL_INSERT);
			stmt.setString(1, blog.getTitle());
			stmt.setString(2, blog.getContent());
			stmt.setString(3, blog.getAuthor());
			stmt.executeUpdate();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt);
		}
		
		return result;
	}
	
	private static final String SQL_DELETE = String.format("delete from %s where %s=?", TBL_BLOGS,COL_ID);
	
	public int deleteBlogById(Integer id) {
		int result = 0;
		Connection conn=null;
		PreparedStatement stmt = null;
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			stmt=conn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt);
		}
		return result;
	}
	
	//블로그 아이디(pk)로 행 1개를 검색하는 sql.
	//select * from blogs where id=?
	private static final String SQL_SELECT_BY_ID=String.format("select * from %s where %s = ?", TBL_BLOGS,COL_ID);
	public Blog read(Integer id) {
		Blog blog=null;
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			stmt=conn.prepareStatement(SQL_SELECT_BY_ID);
			stmt.setInt(1, id);
			rs=stmt.executeQuery();
			while(rs.next()) {
				blog = getBlogFromResultSet(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt,rs);
		}
		
		return blog;
	}
	
	//블로그 아이디 아이디의 제목, 내용, 수정시간을 업데이트 하는 sql.
	private static final String SQL_UPDATE=String.format("update %s set %s = ?, %s=?, %s=systimestamp where %s=?", 
			TBL_BLOGS, COL_TITLE, COL_CONTENT,COL_MODIFIED_TIME, COL_ID);
	
	public int update(Blog blog) {
		int result=-1;
		Connection conn=null;
		PreparedStatement stmt = null;
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			stmt=conn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, blog.getTitle());
			stmt.setString(2, blog.getContent());
			stmt.setInt(3, blog.getId());
			stmt.executeUpdate();
			result=1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt);
		}
		
		return result;
	}
	
	//대소문자 구분 없이 제목에 포함된 문자열로 검색하기.
	private static final String SQL_SELECT_BY_TITLE=String.format("select * from %s where upper(%s) like upper(?) order by %s desc", 
			TBL_BLOGS,COL_TITLE,COL_ID);
	
	//대소문자 구분 없이 내용에 포함된 "
	private static final String SQL_SELECT_BY_CONTENT=String.format("select * from %s where upper(%s) like upper(?) order by %s desc", 
			TBL_BLOGS,COL_CONTENT,COL_ID);
	
	//대소문자 구분 없이 작성자에 포함된 "
	private static final String SQL_SELECT_BY_AUTHOR=String.format("select * from %s where upper(%s) like upper(?) order by %s desc", 
			TBL_BLOGS,COL_AUTHOR,COL_ID);
	
	//대소문자 구분 없이 제목 또는 내용에 포함된 "
	private static final String SQL_SELECT_BY_TITLE_OR_CONTENT=String.format("select * from %s where upper(%s) like upper(?) or upper(%s) like upper(?) order by %s desc", 
			TBL_BLOGS,COL_TITLE,COL_CONTENT,COL_ID);
	
	//제목, 내용, 제목+내용, 작성자 검색을 수행하는 메서드.
	public List<Blog> read(int type, String keyword){
		List<Blog> results=new ArrayList<>();
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			
			String searchKeyword = "%"+keyword+"%";
			switch(type) {
			case 0:
				stmt=conn.prepareStatement(SQL_SELECT_BY_TITLE);
				stmt.setString(1, searchKeyword);
				break;
			case 1:
				stmt=conn.prepareStatement(SQL_SELECT_BY_CONTENT);
				stmt.setString(1, searchKeyword);
				break;
			case 2:
				stmt=conn.prepareStatement(SQL_SELECT_BY_TITLE_OR_CONTENT);
				stmt.setString(1, searchKeyword);
				stmt.setString(2, searchKeyword);
				break;
			case 3:
				stmt=conn.prepareStatement(SQL_SELECT_BY_AUTHOR);
				stmt.setString(1, searchKeyword);
				break;		
			}
			
			rs=stmt.executeQuery();
			
			while(rs.next()) {
				results.add(getBlogFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeResources(conn, stmt,rs);
		}
		
		return results;
	}

	
	
}
	