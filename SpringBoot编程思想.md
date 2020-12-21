### SpringBoot编程思想-小马哥
#### 第一章 初览springboot

#### 第二章  理解独立的spring应用
     web应用
     servlet and Spring webmvc Reactive Web(Spring 5.0 webflux)
     非web应用
     ①服务提供②调度任务③消息处理
     JarLauncher
     
#### 第三章 理解固化的Maven依赖
      dependencyManagement管理统一项目依赖
#### 第四章 理解嵌入式的web容器
    
#### 第五章 理解自动装配
 
    理解springbootApplication 注解语义(开启自动配置 激活扫描 配置类)
    SpringApplication 引导类
    @SpringbootApplication
      ①@EnableAutoConfiguration 激活自动装配
      ②@ComponentScan 激活扫描
      ③@Configuration 配置类
      Spring Framework 提供Bean 生命周期管理 和spring编程模型
      
      
      EmbeddedDatabaseConnection是枚举,get方法判断当前类path下有没目标类 有的话返回，没有则为null
      DataSourceAutoConfiguration.EmbeddedDatabaseCondition类
#### 第六章production-ready特性

    springboot的注解驱动
    @Configuration 注解 @Import @Bean
    @ComponnetScan @Conditional(条件装配)
    五大特性：
        ①springapplication
        ②自动装配
        ③外部化配置
        ④springBoot  Actuator
        ⑤嵌入式Web容器
    springCloud核心特性
        分布式配置
        服务注册和发现
        路由
        负载均衡
        熔断机制
        分布式消息
        
#### 第二部分 走向自动装配
    spring 注解编程模型
        元注解
            如果一个注解标注在其他注解上，那么它就是元注解 
            例如@Ducument @Inherited @Repeatable @Component
        spring模式注解
            @Component派生的注解 采用元标注来实现注解的"派生"
        spring组合注解
            
        spring注解属性别名和覆盖
        
        @Enable模块驱动
    条件装配
    @Profile
    @Conditinal
    条件装配 
    
    springboot 自动装配原理
    
    @EnableAutoCOnfiguration 与@Import相关
    @Import与impoortSelector相关 或者ImportBeanDefinitionRwegistar的实现类
    查看AutoConfigurationImportSelector
    
    @EnableAutoConfiguration 读取候选装配组件
    读取流程
    ①通过SpringFactoriesLoader#loadFactoryNames(Class,ClassLoader)
    方法读取所有META-INF/spring.factries 资源中@EnableAutoConfiguration所关联的自动装配的class集合
    ②读取@EnableAutoConfiguration属性的exclude和excluedeName并与spring.autoconfigure.exclude配置属性合并为自动装配的class排除集合
    ③检查自动装配class排除集合是否合法
    ④排除候选自动装配class集合的排除名单
    ⑤再次过滤候选自动装配Class集合中Class不存在的成员
    
    AutoConfigurationImportListener 接口监听 AutoConfigurationImportEvent
       
        
               
              
    
    


