package sample.tomcat.handler;

/**
 * @Author by yuanpeng
 * @Date 2020/11/29
 */
public class MyBean {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MyBean{" +
				"name='" + name + '\'' +
				'}';
	}
}
