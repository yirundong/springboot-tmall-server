# Spring AOP

AOP：面向切面的编程

AOP是由AspectJ提供的技术，Spring框架很好的支持了AOP。

AOP主要解决了横切关注的问题（若干个不同的方法都需要解决的问题），典型的应用场景有：错误的处理、事务管理、安全检查、日志等等。

在Spring Boot项目中，需要添加`spring-boot-starter-aop`依赖，才可以进行AOP编程。

演示示例：

```java
package cn.tedu.tmall.admin.mall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 统计各Service方法的执行耗时的切面类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Aspect // 标记此类是一个切面类，Spring AOP会将此类编织到匹配的连接点的执行过程中
@Component // 标记此类是一个组件类
public class TimerAspect {

    // 连接点（JoinPoint）：程序执行过程中的某个节点，可能是方法的调用，或抛出异常
    // 切入点（Point Cut）：是匹配1个或多个连接点的表达式
    // 通知（Advice）：包含了切入点表达式与执行的方法
    // -------------------------------------------------------------
    // Advice的种类（使用不同Advice时，方法的参数列表将不同）：
    // -- @Before：前置，你的代码将在连接点【之前】执行
    // -- @After：后置，你的代码将在连接点【之后】执行
    // -- @AfterReturning：返回之后，你的代码将在连接点【成功返回之后】执行
    // -- @AfterThrowing：抛出异常之后，你的代码将在连接点【抛出异常之后】执行
    // -- @Around：环绕，你的代码可以在连接点【之前和之后】执行
    // -------------------------------------------------------------
    // 各Advice的执行节点大致是：
    // @Around：开始
    // try {
    //      @Before
    //      连接点
    //      @AfterReturning
    // } catch (Throwable e) {
    //      @AfterThrowing
    // } finally {
    //      @After
    // }
    // @Around：结束
    // -------------------------------------------------------------
    // 关于切入点表达式：
    // -- 完整的切入点表达式格式为：[修饰符] 返回值类型 [包名]类名.方法名(参数列表) [异常]
    // -- 可以使用1个星号作为通配符，用于匹配任意内容，只会且必须匹配1次
    // -- 可以使用2个连接的小数点作为通配符，可以匹配若干次（0~n次），只能用于包名和参数列表
    //                 ↓ 返回值类型
    //                   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 包名
    //                                                    ↓ 类型名
    //                                                      ↓ 方法名
    @Around("execution(* cn.tedu.tmall.admin.mall.service.*.*(..))")
    //     ↓↓↓↓↓↓ 方法的返回值类型必须是Object【适用于@Around】
    //            ↓↓↓↓↓ 切面方法的名称可以完全自定义
    //                  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 处理连接点的对象，方法参数必须是此类型【适用于@Around】
    public Object timer(ProceedingJoinPoint pjp) throws Throwable {
        // 获取连接点的相关信息
        String className = pjp.getTarget().getClass().getName(); // 获取当前连接点所归属的对象
        String methodName = pjp.getSignature().getName(); // 获取当前连接点方法的名称
        Object[] args = pjp.getArgs(); // 获取当前连接点方法执行的参数列表
        System.out.println("className = " + className);
        System.out.println("methodName = " + methodName);
        System.out.println("args = " + Arrays.toString(args));

        long start = System.currentTimeMillis();

        // 执行连接点
        // 注意-1：调用连接点时，必须获取返回值，且作为当前切面的返回值
        // 注意-2：调用连接点时，必须将异常抛出（或者，使用try...catch处理时，在catch代码块再次抛出异常）
        Object result = pjp.proceed();

        long end = System.currentTimeMillis();
        System.out.println("执行耗时：" + (end - start));
        return result;
    }

}
```











