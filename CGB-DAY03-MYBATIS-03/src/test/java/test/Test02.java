package test;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.jt.dao.AuthorDao;
import com.jt.dao.BlogDao;
import com.jt.entity.Author;
import com.jt.entity.Blog;
import com.jt.vo.BlogResult;

public class Test02 {
	private SqlSessionFactory factory = null;
	
	@Before
	public void init() throws IOException{
		factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-configs.xml"));
	}
	
	/**利用数据库生成的UUID,取得后保存在对象中后再插入数据库,因此从日志看,实际是与数据库进行了两次交互,因此效率较低.
	 * 也可以在Java程序中生成,减少与数据库的交互
	 */
	@Test
	public void testInsertAuthor(){
		Logger log = Logger.getLogger(getClass().getName());
		
		SqlSession session = factory.openSession();
		AuthorDao dao = session.getMapper(AuthorDao.class);
		Author author = new Author();
		/*author.setId(UUID.randomUUID().toString().replaceAll("-", ""));*/
		author.setUsername("尼罗河惨案");
		author.setPassword("无人生还");
		author.setEmail("尼罗河惨案@阿加莎.com");
		dao.insertAuthor(author);
		log.info(author);
		session.commit();
		session.close();
	}
	
	/**一级缓存默认开启,缓存在session的localcache中,
	 * 
	 */
	@Test
	public void testFindAuthorById(){
		Logger log = Logger.getLogger(getClass().getName());
		
		SqlSession session = factory.openSession();
		AuthorDao dao = session.getMapper(AuthorDao.class);
		String id = "0a9d5340073e49598889b4ff4219b05c";
		Author author1 = dao.findAuthorById(id);
		Author author2 = dao.findAuthorById(id);
		session.close();
		
		session = factory.openSession();
		dao = session.getMapper(AuthorDao.class);
		author1 = dao.findAuthorById(id);
		session.close();
	}
	
	/**一级缓存为session的情况:
	 * 同一个session在先查询,中间进行增删改操作时,缓存的localCache会被清空,
	 * 因此在进行二次查询时,只能再去数据库查找,结果是 两次查询,一次更新
	 */
	@Test
	public void testUpdateSession01(){
	
		SqlSession session = factory.openSession();
		AuthorDao dao = session.getMapper(AuthorDao.class);
		String id = "0a9d5340073e49598889b4ff4219b05c";
		Author author = dao.findAuthorById(id);
		author.setUsername("hahah");
		author.setEmail("haha@email.com");
		dao.updateAuthor(author);
		author = dao.findAuthorById(id);
		
		session.commit();
		session.close();
		
	}
	
	/**一级缓存为session的情况:
	 * 多事务并发执行,多个session同时存在读取与更新操作可能出现脏读的情况??
	 * 实际实行下面代码的时候,各个sqlsession中保存的是指向获取的对象的地址,因此如果直接操作session读取出来的对象
	 * 那么在同一个session多次读取的时候会出现读取了更新后的数据,但其实是对象本身发生了变更.
	 * 因此下面是new一个新的author对象,再由session2更新数据,session1在进行第二次查询时,
	 * 直接读取的缓存,缓存中保存的地址指向的对象未发生改变.因此两次读取的结果是一样的,未出现脏读的情况.(与是否自动提交没有关系)
	 * 问题是:老师描述的出现脏读的情况是什么时候出现的呢?
	 * 
	 * 一级缓存为statement的情况:
	 * 可能没有启用缓存,每次查询均去数据库查询
	 */
	
	@Test
	public void testUpdateSession02(){
		Logger log = Logger.getLogger(getClass().getName());
		
		SqlSession session1 = factory.openSession(true);
		SqlSession session2 = factory.openSession(true);
		AuthorDao dao1 =session1.getMapper(AuthorDao.class);
		 
		String id = "0a9d5340073e49598889b4ff4219b05c";
		Author author= dao1.findAuthorById(id);
		System.out.println(author);
		
		Author a=new Author();
		a.setId(id);
		a.setUsername("user-ccc");
		a.setEmail("ccc@t.com");

		AuthorDao dao2 =session2.getMapper(AuthorDao.class);
		/*author.setUsername("FF");
		author.setEmail("FF@email.com");*/
		int rows = dao2.updateAuthor(a);
		
		author = dao1.findAuthorById(id);
		System.out.println(author);
		
//		session1.commit();
		session1.close();
//		session2.commit();
		session2.close();
		
	}
	/**启用二级缓存查询数据:
	 * 
	 * 
	 */
	@Test
	public void testSqlFactoryCache(){
		
		SqlSession session1 = factory.openSession();
		SqlSession session2 = factory.openSession();
		SqlSession session3 = factory.openSession();
		
		AuthorDao dao1 = session1.getMapper(AuthorDao.class);
		AuthorDao dao2 = session2.getMapper(AuthorDao.class);
		AuthorDao dao3 = session3.getMapper(AuthorDao.class);
		
		// session1进行一次查询
		String id ="0a9d5340073e49598889b4ff4219b05c";
		Author author1 = dao1.findAuthorById(id);
		System.out.println(author1);
		
		/* 使用session2对session1取出的对象进行修改,并更新到数据库,更新时没有缓存,
		 * 即使事务提交二级缓存中也未保存数据
		 * 具体分为提交和不提交两种情况,但是提交与否并不影响
		 */
		Author author = new Author();
		author.setId(id);
		author.setUsername("帆帆");
		dao2.updateAuthor(author);
		session2.commit();
		
		/* session3读取更新后的数据,并提交因此保存到了二级缓存中,
		 * 因此二级缓存现在保存的是更新后的数据
		 */
		Author author3 = dao3.findAuthorById(id);
		System.out.println(author3);
		session3.commit();
		
		/* 使用session1再进行一次查询,此时二级缓存与一级缓存均保存有该id的缓存,此时都去了更新后的数据,
		 * 因此是否可以推论是 从二级缓存中读取的.
		 */
		Author author2 = dao1.findAuthorById(id);
		System.out.println(author2);
		
		session1.commit();
		session1.close();
//		session2.commit();
		session2.close();
//		session3.commit();
		session3.close();
		
	}
	
	@Test
	public void testFindBlogResultById(){
		SqlSession session =factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		BlogResult result = dao.findBlogResultById(2);
		System.out.println(result);
		
		session.close();
	}
	
	@Test
	public void testFindBlogResultById02(){
		SqlSession session =factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		BlogResult result = dao.findBlogResultWithId(2);
		System.out.println(result.getTitle());
		System.out.println(result.getContent());
//		System.out.println(result.getAuthor());
		
		session.close();
	}
	
	@Test
	public void testFindBlogResultById03(){
		SqlSession session =factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		List<BlogResult> result = dao.findBlogListWithId();
		System.out.println(result);
		
		session.close();
	}
	
	@Test
	public void testDeleteBlogById(){
		SqlSession session = factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		String[] ids ={"5","6"};
		int rows = dao.deleteBlogById(ids);
		
		session.commit();
		session.close();
		System.out.println(rows);
	}
	
	@Test
	public void testInsertBlogs(){
		SqlSession session = factory.openSession();
		BlogDao dao = session.getMapper(BlogDao.class);
		List<Blog> list = new LinkedList<Blog>();
		for(int i =5;i<7;i++){
			Blog blog = new Blog();
			blog.setTitle("title"+i);
			blog.setContent("content"+i);
			blog.setCreatedTime(new Date());
			list.add(blog);
		}
		
		int rows = dao.insertBlogs(list);
		System.out.println(rows);
		session.commit();
		session.close();
	}
}
