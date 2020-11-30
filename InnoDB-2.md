### 第一章 mysql 体系结构和存储引擎
    1.1 定义数据库和实例
    1.2 mysqld体系结构
    组成；
        连接池
        管理服务和工具组件
        sql接口组件
        查询分析器组件
        优化器组件
        缓冲组件
        插件式存储引擎
        物理文件
    1.3 mysql 存储引擎
        1.3.1 innodb存储引擎
            插入缓冲
            二次写
            自适应哈希索引
            预读
### 第二章 InnoDB 存储引擎
    2.1 InnoDB存储引擎概述
    2.2 InnoDB存储引擎的版本
    2.3 InnoDB 体系结构
        维护所有进程 线程需要访问的多个内存数据结构
        缓存磁盘上的数据 方便快速的度曲
        重做日志缓冲
        2.3.1 后台线程
            1.master thread 负责将缓冲池的数据异步刷新到磁盘 保证数据的一致性 包括脏页的刷新 合并插入缓冲（insert buffer）
            undo页面的回收 
            2.IO thread
                大量使用了AIO来处理IO请求 这样可以加大提高数据库的性能，而IO thread 主要负责这些io请求的回调
            3.purge thread
                事务提交后 undolog可能不再需需要 因此需要Puger thread 来回收已经使用并分配的undo页
                是离散的读取undo页
            4. page clean thread
                将之前版本脏页的刷新都放到单独的线程中来完成
        2.3.2 内存
            1.缓冲池
            首选将磁盘读取到的页放到缓冲池中.对于修改操作，首先修改缓冲池中的页= 然后以一定频率刷新到磁盘上。
            缓冲池存储的数据：索引页 数据页 undo页 插入缓冲 自适应呢哈希索引 锁信息 数据字典信息
            2. lru list free list flush list
            3. 重做日志缓冲 redo log buffer
                innodb引擎首先将重做日志放入这个缓冲区 然后以一定频率刷新到重做日志文件,
                一般每秒钟都会将重做日志刷新到日志文件，因此只需要保证每秒产生的事务量在这个缓冲大小之内便可以。
            4.额外的内存池
    2.4 checkPoint
        解决问题： 
        缩短数据库恢复时间 
        缓冲池不够用时 
        将脏页刷新到磁盘 
        重做日志不可用时 
        刷新脏页面
        两种checkPoint 
        sharp CheckPoint 发生在数据库关闭时将所有的脏页都刷新到磁盘  
        Fuzzy CheckPoint  
            Master Thread CheckPoint
            FLUST_LRU_LIST_CHECKPOINT
            Asynv/Sync flush CheckPoint
            Dirty Page to much CheckPoint
    2.5 Master Thread 工作方式
    2.6 Innodb 关键特性
        插入缓冲(Insert Buffer)
        两次写(Double write)
        自适应的哈希索引
        异步IO
        刷新临接页
        2.6.1 插入缓冲
            对于非聚集的索引的插入或者更新操作，不是每一次插入到索引页面 ，而是先判断插入的非索引页是否存在缓冲池
            如在，则直接插入，若不存在则先放入到一个insert buffer对象中，然后以一定的频率和情况惊醒insert 
            buffer 和辅助索引页子节点的merge操作
        2.6.2 两次写
            doubleWrite 带给Innodb存储引擎的是数据页的可靠性
            当对缓冲池中的脏页进行刷新时，并不直接写磁盘，而是通过memcpy函数将脏页先复制到内存中的doublewrite buffer 
            然后马上调用fsync函数 同步磁盘，避免缓冲带来的问题
        2.6.3 自适应的哈希索引
            innodb存储你引擎会监控对表上各索引页的查询，如果观察到建立哈希索引可以带来速度提升，则建立哈希索引
            adaptive hash index 
        2.6.4 异步IO
        2.6.5 刷新邻接页
            当刷新一个脏页 innodb存储引擎会检测该页所在区的所有页,如果是脏页 那么一起刷新
    2.7  启动 关闭与恢复
        innodb_fast_shutdown 影响着表的存储引擎的行为
        0 当数据库关闭时，innodb需要完成所有的full purge 和 merge insert buffer
        并且将所有的脏页属性回磁盘
        1 表示不需要full purge和merge insert buffer
        2 表示不完成 full purge merge insert buffer
         
                       
        
        
        
                                                                  
           
                   
      
        
        
        