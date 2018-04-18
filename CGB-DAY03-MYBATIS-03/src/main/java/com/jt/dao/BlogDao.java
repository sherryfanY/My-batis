package com.jt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.entity.Blog;
import com.jt.vo.BlogResult;


public interface BlogDao {
	
	/**接口中有多个参数时建议使用@param注解进行参数定义
	 * 
	 * @param startIndex 分页查询时上一页记录的最后位置
	 * @param pageSize 每页最多显示多少条记录
	 * @param columnName 表中字段的名字
	 * @param orderMethod 排序方式desc 或者 asc
	 * @return
	 */
	List<Blog> findPageBlogs(
		@Param("startIndex")Integer startIndex,
		@Param("pageSize")Integer pageSize,
		@Param("columnName")String columnName,
		@Param("orderMethod")String orderMethod
			);
	
	int insertBlog(Blog blog);
	
	/**
	 * 基于博客id查询博客以及先关联的作者信息,mapper文件中通过一次查询实现
	 * @param Id
	 * @return
	 */	
	BlogResult findBlogResultById(Integer id);
	/**
	 * 基于博客id查询博客以及先关联的作者信息,mapper文件中通过多次查询实现
	 * @param Id
	 * @return
	 */
	BlogResult findBlogResultWithId(Integer Id);
	
	List<BlogResult> findBlogListWithId();
	
	int deleteBlogById(@Param("ids")String[] ids);

	int insertBlogs(@Param("list")List<Blog> list);
}
