package com.jt.entity;

/**myBatis 性能提升方案之一:
 * 对实体对象中的而属性提供set/get方法
 * 如果没有,则底层会执行映射识别对象的属性字段,效率较低
 * 
 * @author soft01
 *
 */
public class Author {
	/**
	 * id为Author对象的唯一标识,值由数据库产生,具体内容为UUID字符串
	 * 使用uuid是为了尽量避免高并发环境下的线程安全问题(也有可能会重复)
	 */
	private String id; 
	private String username;
	private String password;
	private String email;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Author [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + "]";
	}
	
	
	
}
