package test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.jt.dao.BlogDao;
import com.jt.entity.Blog;

public class Test01 {
	
	private SqlSessionFactory factory = null;
	
	@Before
	public void init() throws IOException{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-configs.xml"));
	}
	
	@Test
	public void testFindPageBlogs(){
		SqlSession session = factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		List<Blog> list = dao.findPageBlogs(0,3,"id","desc");
		for (Blog blog : list) {
			System.out.println(blog);
		}
		session.close();
	}
	
	@Test
	public void testInsertBlog(){
		SqlSession session = factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		Blog blog = new Blog();
		blog.setTitle("title-G");
		blog.setContent("content-G");
		blog.setCreatedTime(new Date());
		int rows = dao.insertBlog(blog);
		session.commit();
		System.out.println(blog);
		session.close();
	}
	
	@Test
	public void testUUID(){
		
	}
}
