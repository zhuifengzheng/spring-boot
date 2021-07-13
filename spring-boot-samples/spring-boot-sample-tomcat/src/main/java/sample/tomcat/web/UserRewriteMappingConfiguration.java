package sample.tomcat.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;
import sample.tomcat.annotation.RewriteController;
import sample.tomcat.annotation.RewriteMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Effet
 */
@Configuration
public class UserRewriteMappingConfiguration implements EmbeddedValueResolverAware {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
    private StringValueResolver embeddedValueResolver;

    //controller 路径重写处理核心方法
    @PostConstruct
    public void init() {
		System.out.println("rewriteMapping init");
        initConfig();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RewriteController.class);
        System.out.println("0000000000000000000----------------------------");
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            final Object bean = entry.getValue();
            final Class<?> userType = ClassUtils.getUserClass(bean);
            Arrays.stream(userType.getDeclaredMethods()).forEach(method -> {
                method.setAccessible(true);
                if (method.isAnnotationPresent(RewriteMapping.class)) {
                    RequestMappingInfo info = getMappingForMethod(method, userType);
                    requestMappingHandlerMapping.unregisterMapping(info);
                    requestMappingHandlerMapping.registerMapping(info, entry.getKey(), method);
                }
            });
        }
    }

    private RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RewriteMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RewriteMapping.class);
        RequestCondition<?> condition = (element instanceof Class ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
    }

    private RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return null;
    }

    private RequestCondition<?> getCustomMethodCondition(Method method) {
        return null;
    }

    private RequestMappingInfo createRequestMappingInfo(
            RewriteMapping requestMapping, RequestCondition<?> customCondition) {

        return RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
                .methods(requestMapping.method())
                .params(requestMapping.params())
                .headers(requestMapping.headers())
                .consumes(requestMapping.consumes())
                .produces(requestMapping.produces())
                .mappingName(requestMapping.name())
                .customCondition(customCondition)
                .options(this.config)
                .build();
    }

    private String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
        if (this.embeddedValueResolver == null) {
            return patterns;
        } else {
            String[] resolvedPatterns = new String[patterns.length];
            for (int i = 0; i < patterns.length; i++) {
                resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
            }
            return resolvedPatterns;
        }
    }

    private void initConfig() {
        this.config = new RequestMappingInfo.BuilderConfiguration();
        this.config.setUrlPathHelper(new UrlPathHelper());
        this.config.setPathMatcher(new AntPathMatcher());
        this.config.setSuffixPatternMatch(true);
        this.config.setTrailingSlashMatch(true);
        this.config.setRegisteredSuffixPatternMatch(true);
        this.config.setContentNegotiationManager(new ContentNegotiationManager());
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

}