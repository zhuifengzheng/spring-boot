package sample.tomcat.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 容器初始化bean后会发布事件，这里就可以通过@EventListener监听到
 * @Author by yuanpeng
 * @Date 2020/12/2
 */
@Component
public class ServicePublisher {


	@EventListener
	public void publish(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		System.out.println("容器bean数量："+applicationContext.getBeanDefinitionCount());
	}
}
