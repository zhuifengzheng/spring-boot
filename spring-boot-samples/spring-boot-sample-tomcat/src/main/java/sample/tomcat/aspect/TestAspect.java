package sample.tomcat.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author by yuanpeng
 * @Date 2020/11/30
 */
@Aspect
@Component
public class TestAspect {

	// 定义切点（切入位置）
	@Pointcut("execution(* sample.tomcat.web.SampleController.*(..))")
	private void pointcut(){}


	@Before("pointcut()")
	public void before(JoinPoint joinPoint){
		System.out.println("我是前置通知");
	}

}