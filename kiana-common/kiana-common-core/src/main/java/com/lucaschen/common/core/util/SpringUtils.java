package com.lucaschen.common.core.util;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类 方便在非 spring 管理环境中获取 bean
 */
@Component
public final class SpringUtils implements BeanFactoryPostProcessor {

    /**
     * Spring 应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    private static void postProcessBeanFactoryStatic(final ConfigurableListableBeanFactory beanFactory) {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 获取对象
     *
     * @param name Bean 名称
     * @return Object 一个以所给名字注册的 Bean 的实例
     * @throws BeansException BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取类型为 RequiredType 的对象
     *
     * @param requiredType Bean 的类型
     * @return Object 一个符合所给类型的的 Bean 的实例
     * @throws BeansException BeansException
     */
    public static <T> T getBean(final Class<T> requiredType) throws BeansException {
        return beanFactory.getBean(requiredType);
    }

    /**
     * 如果 BeanFactory 包含一个与所给名称匹配的 Bean 定义，则返回 true
     *
     * @param name Bean 名称
     * @return boolean 是否包含
     */
    public static boolean containsBean(final String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的 Bean 定义是一个 singleton 还是一个 prototype。
     * 如果与给定名字相应的 Bean 定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name Bean 名称
     * @return boolean 是否是 singleton
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(final String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name Bean 名称
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public static Class<?> getType(final String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的 Bean 名字在 Bean 定义中有别名，则返回这些别名
     *
     * @param name Bean 名称
     * @return String[] 别名列表
     * @throws NoSuchBeanDefinitionException NoSuchBeanDefinitionException
     */
    public static String[] getAliases(final String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取 AOP 代理对象
     *
     * @param invoker 对象类型
     * @return <T> 被代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(final T invoker) {
        return (T) AopContext.currentProxy();
    }

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        postProcessBeanFactoryStatic(beanFactory);
    }
}