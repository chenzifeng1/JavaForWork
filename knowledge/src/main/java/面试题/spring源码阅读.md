## Spring 

AbstractApplicationContext::rfresh()

```java
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    public void refresh() throws BeansException, IllegalStateException {
        synchronized (this.startupShutdownMonitor) {
            this.prepareRefresh();
            ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
            this.prepareBeanFactory(beanFactory);

            try {
                // 扩展使用 使用了模板方法的思想
                this.postProcessBeanFactory(beanFactory);
                // PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, this.getBeanFactoryPostProcessors());
                // 委托者模式  会实例化和调用所有 BeanFactoryPostProcessor
                this.invokeBeanFactoryPostProcessors(beanFactory);
                // 注册BeanPostProcessor，
                this.registerBeanPostProcessors(beanFactory);
                // 初始化国际文件 
                this.initMessageSource();
                // 初始化事件广播器，先看看有没有 applicationEventMulticaster 这个bean，有的话获取
                // 没有的话就创建一个simpleApplicationEventMulticaster
                this.initApplicationEventMulticaster();
                // 还是一个空客方法，留给子类去实现
                this.onRefresh();
                // 注册监听器
                this.registerListeners();
                // 结束bean工厂的初始化
                this.finishBeanFactoryInitialization(beanFactory);
                //结束refresh 
                // 1. 清除资源缓存 2. 初始化LifeCycleProcessor 3.调用这个Processor的refresh方法 4.发布事件
                this.finishRefresh();
            } catch (BeansException var9) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var9);
                }

                this.destroyBeans();
                this.cancelRefresh(var9);
                throw var9;
            } finally {
                this.resetCommonCaches();
            }

        }
    }
    
    
}

```