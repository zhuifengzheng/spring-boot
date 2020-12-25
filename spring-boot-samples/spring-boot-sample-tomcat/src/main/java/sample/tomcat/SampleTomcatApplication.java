/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.tomcat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SpringBootApplication
public class SampleTomcatApplication {

	private static Log logger = LogFactory.getLog(SampleTomcatApplication.class);

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				logger.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}

		};
	}

	public static void main(String[] args) {
//		Map<String,Object> map = new HashMap<>();
////		map.putIfAbsent("1",1);
//		Object o = map.computeIfAbsent("1", k -> new SampleTomcatApplication().test(k));
//		LinkedList linkedList = (LinkedList)map.computeIfAbsent("2", k -> new LinkedList<>());
//
//		System.out.println(o);
//		linkedList.add("1");
//		LinkedList linkedList2 = (LinkedList)map.computeIfAbsent("2", k -> new LinkedList<>());
//		linkedList2.add("2");
//		System.out.println(linkedList2);
		ConfigurableApplicationContext run =
				SpringApplication.run(SampleTomcatApplication.class, args);
		System.out.println("bean 最后定义数量为："+run.getBeanDefinitionCount());
	}

	public String test(String str){

		return str+"test";
	}

}
