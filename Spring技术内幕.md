### 　IOC容器的实现
    每个对象都需要其他对象的引用，也就是每个对象需要负责维护具有依赖和联系的对象，
    那么这个对象的责任就有点大而且导致代码高度耦合，难以测试。
    依赖反转后使对象直接直接的关系交给框架就好，需要申明好，spring负责注入相关依赖.
    使得使用与管理分离,代码更容编写和维护。
    注入方式：接口注入 setter注入 构造器注入
    
    BeanFactory 与 ApplicationContext
    BeanFactory 接口定义简单容器
    ApplicationContext 应用上下问，作为容器的高级形态
    
    HierarchicalBeanFactory 具备了双亲ioc容器的管理功能
    ConfigurableBeanFactory 定义了对BeanFactory配置功能 类加载器 类转化 属性编辑器
    ListableBeanFactory 可以列出工厂生产的所有实例
    AutowireCapableBeanFactory 使得工厂具有自动装配功能
    AbstractBeanFactory别名管理,单例创建与注册,工厂方法FactoryBean支持.
    ConfigurableListableBeanFactory 提供bean definition的解析,注册功能,再对单例来个预加载(解决循环依赖问题).
    
    关于ApplicationContext 上下文接口为核心的接口设计
    ConfigurableApplicationContext 该接口提供了根据配置创建、获取bean的一些方法
    ClassPathXmlApplicationContext、FileSystemXmlApplicationContext等。提供了通过各种途径去加载实例化bean的手段。
    
    XmlBeacFactory 与DefaultListableBeanFactory 具体实现，具体的容器产品
    使用ioc容器，需要以下几个步骤：
    1) 创建ioc配置文件的抽象资源 这个抽象资源包含了BeanDefinition定义信息
    2）创建一个BeanFactory 使用DefaultListableBeanFactory
    ①new AnnotationConfigApplication();//构造函数触发开始
    ②this.beanFactory=new DefaultListBeanFactory();//factory 实例
    ③this.resourcePatterResolver= getResourcePatternResolver();//资源解析器
    ④ return new PathMachingResourcePatternResolver();//资源解析器
    ⑤ this.classLoader=ClassUtils.getDefaultClassLoader();//类加载器
    ⑥this.reader= new AnnotatedBeanDefinitionReader(this);//创建读取注解的Bean定义读取器 
    ->并初始化spring内部BeanDefinition的注册(主要时后置处理器)
    ->beanDefinitionMap beanDefinitionNames
    ⑦this.scanner=new ClassPathBeanDefinitionScanner(this);//创建Bean定义扫描器
    ⑧register(annotatedClasses) //开始注册
    ->doRegisterBean(annotatedClass)
    ->processCommonDefinitionAnnotations//检查通用的注解 并添加数据到BeanDefinition中
    
    
    以下为refresh函数操作：
        prepareRefresh();
        prepareBeanFactory()
        
        postProcessBeanFactory();
        invokeBeanFactoryPostProcessors();
            BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
            BeanFactoryPostProcessor 接口 postProcessBeanFactory  
            BeanDefinitionRegistryPostProcessor 接口 postProcessBeanDefinitionRegistry
            前者允许修改ioc实现类的bean 后者允许向容器注册bean 后者比前者先执行
            ConfigurationClassPostProcessor();
            
            先扫描系统初步加载的BeanDefinitionRegistryPostProcessor
            AbstractApplication:invokeBaenFactoryPostProcess 
            代理给PostProcessorRegistrationDelegate:invokeBeanFactoryPostProcessors执行
            参数 beanFactory List<BeanFactoryPostProcessor>
            解析上述方法：①判断是否是BeanDefinitionRegistry(Bean定义注册器) 的实现若是走②
                        ②如果是BeannDefinitionRegistryPostProcessor实例 
                        则执行registryProcessor的postProcessBeanDefinitionRegistry
                        方法并添加到registryProcessor列表中,
                        若不是 则添加到常规的regularPostProcessor列表中，后续使用
                        ③从容器中获取所有的BeanDefinitionRegitryPostProcessor的名字列表(数组)
                        ④如果是PriorityOrdered类型的实现
                        则添加到currentRegistryProcessors 数组中且添加到processBeans中
                        ⑤排序currentRegistryProcessors  添加到registryProcessors中
                        然后触发执行invokeBeanDefinitonRegistryPostProcessors
                        ⑥如果是Ordered类型的实现 重复上述过程
                        ⑦触发其他的BeanDefinitonRegistryPostProcessors
                        ⑧触发registryProcessors中BeanFactoryPostProcessor：postProcessBeanFactory方法
                        ⑨触发regularProcessors中BeanFactoryPostProcessor：postProcessBeanFactory方法
                        ⑩结束上述过程
            总结便是执行BeanFactoyPostProcessor时 首选找到系统内置的BeanDefinitionRegistryPostProcessor
            也就是ConfigurationClassPostProcessor 负责加载用户自定义的Bean BeanFactoryPostProcessor 或者
            BeanDefinitionRegistryPostProcessor等 先执行 PriorityOrdered 再执行 Ordered 再执行其他的
            BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry 处理完成后还需执行
            postProcessBeanFactory方法 应为BeanDefinitionRegistryPostProcessor 
            也实现了BeanFactoryPostProcessor接口，最后执行常规的BeanFactoryPostProcessor：postProcessBeanFactory
            然后执行用户自定义BeanFactoryPostProcessor们;完结撒花。
        registerBeanPostProcessors(beanFactory)解析
            PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory,this)
            所以只需要看registerBeanPostProcessors(beanFactory,this)就可
            从BeanFactory
            读取系统内置的BeanProcessor 分为PriorityOrdered ordered non三种
            然后分别注册到factory registerBeanPostProcessors 注册并创建对应实例
        initMessageSource();
            初始化消息源
        initApplicationEventMulticater();
            初始化事件传播器
        onRefresh();
            初始化其他特殊的bean 目前是个空方法
        registerListeners()
            注册监听器
        finishBeanFactoryInitialization(beanFactory)
            完成实例化非懒加载的单例(包括factoryBean)
        先解决bean的后置处理器执行时间问题：
        那么关于依赖注入是在哪里产生的？
            getBean()->doGetBean()->getSingleton()->AbstractAutowireCapableBeanFactory:createBean
            ->doCreateBean->populateBean
            ->AuthowiredAnnotationBeanProcessor:postProcessProperty()
            ->metadata.inject(bean,beanName,pvs)->element.inject(target,beanName,pvs)
            ->beanFactory.resolveDependency(desc,beanName,autowiredBeanNames,typeConverter)
            ->doResolveDependency()->DependencyDescriptor:resolveCandidate()->getBean()
        
                
            
                
            
            
            
        
           
     
                  
                    
            
            
    