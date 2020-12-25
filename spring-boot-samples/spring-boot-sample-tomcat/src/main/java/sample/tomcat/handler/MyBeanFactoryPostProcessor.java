package sample.tomcat.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * ConfigurationClassPostProcessor这个类实现了BeanDefinitionRegistryPostProcessor、PriorityOrdered接口
 * 并且它的getOrder是返回的最大值，那么执行顺序最后，那我这里getOrder顺序为0应该在它之前执行？但是并不是这样
 * 在源码中，ConfigurationClassPostProcessor该类是在springboot容器里面就初始化了，这个类通过扫描包的路径
 * 加载有@Component等注解的类，在加载这些注解类之前就通过getBean方法实例化了，
 * 而自定义的MyBeanFactoryPostProcessor类有@Component注解，那么必然在
 * ConfigurationClassPostProcessor这个类后面执行了
 * @Author by yuanpeng
 * @Date 2020/11/29
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor,PriorityOrdered {
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		System.out.println("bean count = "+beanDefinitionRegistry.getBeanDefinitionNames().length);

		// 注册一个bean定义
		BeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClassName("sample.tomcat.handler.MyBean");
		beanDefinitionRegistry.registerBeanDefinition("myBeanName",beanDefinition);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		System.out.println("bean count = "+configurableListableBeanFactory.getBeanDefinitionNames().length);
		BeanDefinition name = configurableListableBeanFactory.getBeanDefinition("myBeanName");
		System.out.println(name.getBeanClassName());
		MyBean myBeanName = (MyBean)configurableListableBeanFactory.getBean("myBeanName");
		myBeanName.setName("提前设置bean的名字实例化bean");
		System.out.println(myBeanName);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
