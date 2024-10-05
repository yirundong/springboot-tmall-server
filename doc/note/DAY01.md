# 1. Spring框架

## 1.1. Spring框架的作用

Spring框架主要解决了创建对象和管理对象的相关问题。

通过Spring创建并管理对象，可以使得开发者不再反复关心对象的创建过程，并且，默认情况下，由Spring创建的对象都是单例的，这是非常有必要的！

> 由Spring创建的对象通常称之为Spring Bean。
>
> 由于Spring会创建并管理很多对象，所以Spring也通常被称之为Spring容器。

## 1.2. Spring框架的依赖项

Spring框架的基础依赖项是：`spring-context`。

## 1.3. 使用Spring框架创建对象

### 1.3.1. 组件扫描

在配置类上，添加`@ComponentScan`注解可以开启组件扫描。

> 在Spring Boot项目中，启用类上的`@SpringBootApplication`注解中包含`@ComponentScan`。
>
> 关于`@SpringBootApplication`注解：
>
> ```java
> @SpringBootApplication
> -- @ComponentScan
> -- @SpringBootConfiguration
> -- -- @Configuration
> ```
>
> 另外，如果没有在`@ComponentScan`上配置扫描的包，默认扫描的是当前配置类所在的包。

在类上添加`@Component`或基于`@Component`的组合注解，即可将类标记为组件类。

> 在Spring框架中，组件注解有：
>
> - `@Component`
> - `@Controller`
> - `@Service`
> - `@Repository`
> - `@Configuration`
>
> 在Spring MVC框架中，还新增了组件注解：
>
> - `@RestController`
> - `@ControllerAdvice`
> - `@RestControllerAdvice`

Spring框架在执行组件扫描时，会扫描所配置的包及其子孙包，并尝试创建所有组件类的对象。

这种做法只适用于自定义的类。

### 1.3.2. @Bean方法

在配置类中，可以自行设计方法，返回所需的对象，并在方法上添加`@Bean`注解，则Spring会自动调用此方法，并管理此方法返回的对象。

示例：

```java
@Configuration
public class BeanConfiguration {
    @Bean
    public UserController userController() {
        return new UserController();
    }
}
```

这种做法适用于所有类型创建对象，但一般用于创建非自定义类的对象。

## 1.4. Spring Bean的作用域

默认情况下，被Spring管理的对象都是单例的，也可以通过`@Scope("prototype")`修改为非单例的（相当于局部变量）。

被Spring管理的单例的Spring Bean，默认情况下，都是预加载的（相当于饿汉式的单例模式），也可以通过`@Lazy`注解修改为懒加载的（相当于懒汉式的单例模式）。

注意：不要把Spring和单例模式这种设计模式直接划等号，只是Spring管理的对象的特征与单例模式的相同而已。

如果使用组件扫描的方式创建对象，则在类上添加`@Scope`或`@Lazy`来配置作用域，如果使用`@Bean`方法的方式创建对象，则在方法上添加这些注解来配置作用域。

## 1.5. Spring Bean的生命周期

仅当Spring Bean是单例的，才有必要讨论生命周期问题。

在自定义的组件类中，可以自定义方法，并在方法添加`@PostConstruct`和`@PreDestroy`注解，将方法标记为初始化方法和销毁方法。

在使用`@Bean`方法的情况下，可以在`@Bean`注解上配置`initMethod`和`destroyMethod`属性，来指定初始化方法和销毁方法的名称。

初始化方法会在创建对象、完成自动装配之后，被Spring框架自动调用。

销毁方法会在Spring框架销毁对象的前一刻被自动调用。

## 1.6. 自动装配

自动装配：当某个属性，或某个被Spring自动调用的方法的参数需要值时，Spring会自动从容器中找到合适的值，为属性或参数注入值。

示例：为属性注入值

```java
@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
}
```

示例：为方法的参数注入值

```java
@RestController
public class UserController {
    private UserMapper userMapper;
    
    //                    ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 自动装配
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
```
如果类中仅有1个构造方法，不需要添加`@Autowired`注解；如果类中有多个构造方法，Spring会尝试调用无参数的构造方法，如果某个构造方法添加了`@Autowired`注解，则必然调用添加了注解的构造方法。

```java
@RestController
public class UserController {
    private UserMapper userMapper;
    
    @Autowired
    //                        ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 自动装配
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
```

```java
@Configuration
public class BeanConfiguration {
    @Bean
    //                                   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 自动装配
    public UserController userController(UserMapper userMapper) {
        UserController userController = new UserController();
        userController.setUserMapper(userMapper);
        return userController;
    }
}
```

除此以外，通过`@Value`注解也适用以上做法！

使用`@Resource`注解也可以实现自动装配，但是，它不可以添加在构造方法上！并且，它是`javax` 包下的注解！从装配机制上，`@Resource`是先根据名称装配，再根据类型装配，而`@Autowired`是先根据类型进行查找，当冲突时再尝试根据名称装配。

关于`@Autowired`的装配机制，它是先根据类型查找匹配类型的Spring Bean的数量，然后：

- 0个：取决于`@Autowired`注解的`required`属性的值
  - `true`（默认）：装配失败，加载Spring时抛出`NoSuchBeanDefinitionException`异常
  - `false`：放弃装配，但是，后续可能出现NPE
- 1个：直接装配，且成功
- 多个：将尝试根据名称进行装配：
  - 存在名称匹配的：装配成功
  - 不存在名称匹配的：装配失败，加载Spring时抛出`NoUniqueBeanDefinitionException`异常

另外，关于名称匹配：

- Spring Bean的名称与属性（或参数）的名称相同
- 属性（或参数）的名称与Spring Bean的名称相同
- 通过`@Resource`的`name`属性指定Spring Bean的名称；通过`@Qulifier`注解指定Spring Bean的名称，结合`@Autowired`的机制一起使用

## 1.7. 概念

IoC（Inversion of Control）：控制反转，指的是将对象的创建、管理等控制权交给了框架

DI（Dependency Injection）：依赖注入，为对象的依赖项（类中的属性）注入值

Spring框架通过DI完善了IoC。

## 1.8. Spring AOP

再议

# 2. Spring MVC框架

## 2.1. Spring MVC框架的作用

Spring MVC框架主要解决了：

- 接收请求
- 响应结果
- 处理异常

## 2.2. Spring MVC框架的依赖项

Spring MVC框架的基础依赖项是：`spring-webmvc`

## 2.3. 接收请求

Spring MVC通过控制器中的方法来接收请求。

仅当添加了`@Controller`注解的组件类会被视为“控制器类”。

需要通过`@RequestMapping`系列注解来配置请求路径，此系列注解包括：

- `@RequestMapping`
- `@GetMapping`，等效于`@RequestMapping(method = RequestMethod.GET)`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PatchMapping`

通常，以上注解都只需要用于配置请求路径即可，如果响应结果中，中文出现乱码，可以配置注解的`produces`属性，例如：

```java
@RequestMapping(value = "/users", produces = "application/json; charset=utf-8")
```

## 2.4. 接收请求参数

### 2.4.1. 接收普通参数

无论是GET请求中在URL中的参数，还是POST请求中在请求体中的参数，你都可以直接声明为处理请求的方法的参数，例如：

```java
@PostMapping("/login")
public void login(String username, String password) {
    // ...
}
```

或者：

```java
@Data
public class UserLoginParam implements Serializable {
    private String username;
    private String password;
}
```

```java
@PostMapping("/login")
public void login(UserLoginParam userLoginParam) {
    // ...
}
```

提示：以上做法都可以接收到请求参数，特别是第2种封装的做法，在过程中，Spring MVC框架就使用到了Spring框架创建对象的机制，把多个请求参数自动创建为参数类型的对象！

### 2.4.2. 接收URL中占位符的参数

也可以在设计URL时，使用`{}`格式的占位符，并结合`@PathVariable`注解获取占位符位置的值，例如：

```java
// http://localhost:8080/users/9527/delete
@PostMapping("/{id}/delete") // 假设类上已经配置了@RequestMapping("/users")
public void delete(@PathVariable Long id) {
    // ...
}
```

在占位符中的自定义名称右侧，可以添加冒号，再编写正则表达式，例如：

```java
@PostMapping("/{id:[0-9]+}/delete")
```

如果请求的URL匹配，则对应的方法可以处理请求，如果请求的URL不匹配，则不会匹配！

另外，如果设计的URL中的占位符名称与方法的参数名称不一致，可以配置注解参数：

```java
@PostMapping("/{id}/delete")
public void delete(@PathVariable("id") Long userId) {
    // ...
}
```

### 2.4.3. 关于`@RequestBody`注解

在处理请求的方法的参数上添加`@RequestBody`，表示客户端提交的请求参数需要是某种特定格式的，例如是JSON格式或XML格式等。

在没有添加`@RequestBody`的情况下，客户端提交的请求参数必须是FormData格式的，例如：`username=test&password=123456`。

结论：

- 有`@RequestBody`：参数必须是某种格式的，如果是FormData，则抛出`HttpMediaTypeNotSupportedException`异常
- 没有`@RequestBody`：参数必须是FormData的，如果是某种格式的，则服务器端接收到的参数均为`null`

### 2.4.4. 关于`@RequestParam`

`@RequestParam`注解的源代码如下：

```java
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default true;

    String defaultValue() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
}
```

## 2.5. 响应结果

在默认情况下，处理请求的方法的返回值表示“处理响应的视图组件的名称及相关数据”，最终的响应是一个嵌入了数据的完整的HTML页面的源代码，这不是前后端分离的做法！

在前后端分离的开发模式下，服务器端应该只响应数据结果，由客户端自行决定如何处理这些数据！

在处理请求的方法上，添加`@ResponseBody`注解，即可使得此方法是“响应正文”的，方法的返回值就是响应到客户端的数据，而不再处理视图！

也可以在控制器类上添加`@ResponseBody`注解，则类中所有方法都是响应正文的！或者，在类上使用`@RestController`注解，它是由`@Controller`和`@ResponseBody`组合而成的注解！

## 2.6. 关于HttpMessageConverter

Spring MVC框架内置了许多消息转换器（MessageConverter），其作用是实现基础数据与对象的相互转换。

在接收请求时，Spring MVC会根据请求头中的`Content-Type`来识别请求参数的文档类型，然后，自动选择合适的消息转换器，将请求参数转换为对象！

在处理响应时，Spring MVC会根据方法的返回值类型来选择合适的消息转换器，例如，当方法的返回值是`String`类型，就会使用`StringHttpMessageConverter`（Spring MVC自带的），如果方法的返回值类型是Spring MVC没有对应的消息转换器的类型时，如果项目中添加了Jackson依赖项时，Spring MVC会自动使用Jackson框架中的消息转换器来处理方法的返回值，而Jackson框架会将返回值转换成JSON格式的数据！

在Spring Boot项目中添加了`spring-boot-starter-web`时，就包括了Jackson相关的依赖项！

## 2.7. 统一处理异常

在基于Spring MVC框架的Web项目中，应该由控制器中的方法捕获并处理异常，但是，处理不同请求时，可能出现相同的异常，则在多个不同的方法都需要使用`try...catch`进行捕获并处理，非常繁琐！Spring MVC框架提供了统一处理异常的机制，每个处理请求的方法都不必处理异常，而是将异常抛出即可，Spring MVC会自动使用统一处理异常的机制对这些抛出的异常进行处理！

注意：统一处理异常的机制，只能处理控制器中的方法抛出的异常！

使用统一处理异常机制时：

- 使用专门的类，在类上添加`@ControllerAdvice`注解，或`@RestControllerAdvice`注解，则此类中的特定的方法（例如添加了`@ExceptionHandler`的方法）就可以作用于整个项目中所有控制器类中的所有方法
- 自定义处理异常的方法，此方法需要添加`@ExceptionHandler`注解，并且，方法的参数中必须有1个异常类型

注意：处理异常的方法，并不像处理请求的方法那样可以自由的添加参数，必须有1个异常类型，并且，可以按需添加少量特定类型的参数，例如`HttpServletRequest`、`HttpServletResponse`等，除了特定类型以外的都不可以设计为处理异常的方法的参数！













