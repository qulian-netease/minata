package cn.hyperchain.application;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by superlee on 2017/11/3.
 * 启动类，继承 SpringBootServletInitializer 并重写 configure 方法
 * 部署到外部tomcat容器中
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}
