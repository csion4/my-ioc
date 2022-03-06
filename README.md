# 基于一个玩具制作流程抽象出来的IOC容器
## (手动置顶)6，总结
一个很简单的Demo项目，代码量很少，很短时间把代码写完并完成调试的，所以还有很多不足的流程+bug；
简单既是他的劣势又是他的优势，我特意将整个流程抽象出来很多不同的包和类出来，
摒弃了Spring IOC流程中很多复杂的流程（比如多层继承+复杂的后置处理器扩展+三级缓存和AOP）回归IOC容器本质，可以让我们直观的了解啥是
对象容器，啥是IOC（DI）以及他们的简单实现，让我们在看到Spring源码时不至于那么恐慌和排斥；   

## 1，前言
IOC容器并没有你想象的那么复杂，关键思想也就是那个被面试官问烂了的话题：控制反转依赖注入；
但是如果看了Spring源码的童鞋就会被那些大佬们写的迷宫绕的走不出来，只能苦苦背诵那些概念以为了可以和面试官聊两句（[Spring启动源码解析可参看](https://github.com/csion4/xml_spring-context-5.3.5-sources)）；
我承认Spring IOC代码设计性和扩展性很高，但是个人感觉他功能实现的过多并且太侧重于高扩展了，也可能是多个版本迭代和多模式
（从单纯的xml到如今全注解）支持使得它整个流程太过于复杂；他厉害的地方也是这点，能将如此庞大的功能集包装成一个黑盒并且提供最简单
的两行代码new ApplicationContext().getBean(xxx);这也就是Spring的魅力吧；

## 2，简介
这个小Demo则是从一个名叫Csion的大叔制作玩具的过程抽象出来的一个IOC容器，他或许可以让你能从另外一个角度看IOC，让我们一起经入Csion大叔的世界吧；

## 3，故事开始
我们今天的主角是一个叫Csion的大叔，他是一个富二代，最近继承了他家的祖业--一家玩具工厂，据他说规模超越了Lego了；
他的家族提供根据玩具名称或类型定制玩具的能力（Uncle.class）；来让我们看看整个流程吧：
1. 首先我们如果要定制玩具，需要提供蓝图（bluePrint）；
2. 大叔会指派一个蓝图收集者（DrawingGatherer）收集所有的蓝图；
3. 根据蓝图绘制出所有的玩具设计图纸（ToyDrawing），并存到文件夹中（DrawingFolder）
4. 开启工厂，提供存有所有设计图的文件夹，生产玩具
5. 将所有生产的玩具放到盒子中（ToyBox）
6. 对有需要的玩具进行上色（Painting）
7. 生产完成，等待别人来根据玩具名称或者玩具类型获取玩具吧（坐等数钱）
怎么样，资本家赚钱有时候就是这么简单；

## 4，抽象分析
结合Spring IOC进行抽象分析：
1. 我们这里的Uncle对应的是Spring IOC中的ApplicationContext;
2. justDoIt()方法对应refresh()方法
3. bluePrint则是基于注解的IOC的扫描基准类
4. DrawingGatherer对应ConfigurationClassPostProcessor这个后置处理器了（这是基于注解的，如果是xml则没有这个而是解析xml流程获取beanDefinition）
5. ToyDrawing对应BeanDefinition啦
6. DrawingFolder对应beanDefinitionMap
7. ToyFactory对应BeanFactory
8. ToyBox对应Spring IOC中的一级缓存，singletonObjects
9. Painting对应populateBean()，属性填充
10. 貌似缺少了玩具启动流程来对应initializeBean()实例化方法了，这个后面我再补充了
11. @AToy注解对应@Component，@Assemble注解对应@Autowired

从这个抽象关系中我们可以看出，整个Spring Framework如果只是抽离出来IOC模块其实对应的东西很少，但是为什么他却是那么的复杂，从ApplicationContext接口的继承
关系中就可以看出来了，他不仅仅实现了BeanFactory的功能，还包括了很多，使得他整体过于庞大；
 
 
    public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
    		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {}

## 5，说了这么多，我们应该如何体验一下富二代Csion Uncle的乐趣呢？
1. 获取这个demo源码后编译打包后install到本地maven仓库
2. 创建一个测试maven项目，引入依赖

       <dependency>
          <groupId>com.csion</groupId>
          <artifactId>my-ioc</artifactId>
          <version>1.0-SNAPSHOT</version>
       </dependency>

3. 编写测试代码
        
        - controller
           -- Controller.java
        - service
           -- impl
              --- ServiceImpl.java
           -- Service.java
        - Main.java
    Service接口：
        
        public interface Service {
            void test();
        }
        
    ServiceImpl实现类:
    
        @AToy   // 使用容器注解
        public class ServiceImpl implements Service {
            @Override 
            public void test() {
                System.out.println("i am test");
            }
        }
     
    Controller类：
    
        @AToy   // 使用容器注解
        public class Controller {
            @Assemble   //使用自动注入注解
            private Service service1;
        
            public void test() {
                service1.test();
            }
        }

    Main启动类：
    
        public class Main {
            public static void main(String[] args) {
                Uncle uncle = new CsionUncle(Main.class);
                Controller c1 = uncle.getToy(Controller.class);     // 根据类型获取
                Controller c2 = (Controller) uncle.getToy("controller");    // 根据名称获取
                c1.test();  // 输出：i am test
                c2.test();  // 输出：i am test
                System.out.println(c1 == c2);   // 输出：true
            }
        }

## 6，总结
一个很简单的Demo项目，代码量很少，很短时间把代码写完并完成调试的，所以还有很多不足的流程+bug；
简单既是他的劣势又是他的优势，我特意将整个流程抽象出来很多不同的包和类出来，
摒弃了Spring IOC流程中很多复杂的流程（比如多层继承+复杂的后置处理器扩展+三级缓存和AOP）回归IOC容器本质，可以让我们直观的了解啥是
对象容器，啥是IOC（DI）以及他们的简单实现，让我们在看到Spring源码时不至于那么恐慌和排斥；

## for me：
第一：实现一个ioc容器
第二：提升抽象能力
