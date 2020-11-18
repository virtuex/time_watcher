# Java性能耗时监控工具（基于Spring的StopWatch监控工具）
## 特点
- 改编自Spring的StopWatch，功能增强，原理不变
- 代码实现简单，使用更简单
- 支持Slf4j日志打印，可使用Log4j，Logback等日志库
- 统一归纳，展示每项任务耗时与占用总时间的百分比，展示结果直观
- 性能消耗相对较小，并且最大程度的保证了start与stop之间的时间记录的准确性
- 可在start时直接指定任务名字，从而更加直观的显示记录结果
- 支持嵌套，可在一个任务中监控另一个任务
- 单独发布jar，不需要依赖spring相关的jar

## 使用实例
- 添加依赖
> 已发布到`jitpack`，可直接使用
```
allprojects {
    repositories {
        mavenCentral()
        maven {
            url "https://jitpack.io"
        }
    }
}

dependencies {
    implementation 'com.github.virtuex:time_watcher:1.0'
    implementation("org.slf4j:slf4j-log4j12:1.6.6")
    implementation("org.slf4j:slf4j-api:1.7.26")
}

```
- 测试代码

```java

public class LogingStopWatchTest {
    private static Logger logger = LoggerFactory.getLogger(LogingStopWatchTest.class);

    public static void main(String[] args) {
        initWatch("ROOT");
        startWatch("TEST");
        startWatch("TEST2-1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch();
        startWatch("TEST2-2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch();
        stopWatch();
        outAndReleaseWatch();
    }


    /**
     * 开始监控任务，与stopWatch成对出现
     */
    protected static void startWatch(String taskName) {
        if (logger.isDebugEnabled()) {
            StopWatchContext.startWatch(taskName, -1);
        }
    }

    /**
     * 结束监控任务，与startWatch成对出现
     */
    protected static void stopWatch() {
        if (logger.isDebugEnabled()) {
            StopWatchContext.stopWatch();
        }
    }

    /**
     * 初始化监控
     */
    private static void initWatch(String watchName) {
        if (logger.isDebugEnabled()) {
            StopWatch stopWatch = new Slf4jStopWatch(watchName, true, -1);
            StopWatchContext.setCurrentStopWatch(new StopWatchPack(stopWatch));
            // 开始根任务
            startWatch("--");
        }
    }


    /**
     * 始放监控资源
     */
    private static void outAndReleaseWatch() {
        if (logger.isDebugEnabled()) {
            // 结束根任务
            stopWatch();
            // 打印详细
            StopWatchContext.prettyPrint();
            StopWatchContext.releaseStopWatch();
        }
    }
}
```

- 运行结果

运行结果中很直观的展示了任务耗时，及其子任务所占的百分比

```
2020-11-18 23:31:16 [DEBUG] slf4j.Slf4jStopWatch (log:49) -   
Task Name 'ROOT': Running Time = 4s 1ms 
  -----------------------------------------
  time        %     Task name
  -----------------------------------------
  4s 1ms   100%  --
    Task Name 'TEST': Running Time = 4s 1ms 
    -----------------------------------------
    time        %     Task name
    -----------------------------------------
    4s 1ms   100%  TEST
      Task Name 'TEST2-1': Running Time = 4s 1ms 
      -----------------------------------------
      time        %     Task name
      -----------------------------------------
      3s   075%  TEST2-1
      1s 1ms   025%  TEST2-2
```


## Github地址
- 工具代码地址  
[点击这里](https://github.com/virtuex/time_watcher)

- Demo地址  
[点击这里](https://github.com/virtuex/time_watcher/tree/demo)