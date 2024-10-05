# 3. Spring Boot框架

## 3.1. Spring Boot框架的作用

Spring Boot框架主要解决了：依赖管理，自动配置

Spring Boot被设计为“开箱即用”的，它是一种“约定大于配置”的思想。

## 3.2. 依赖管理

在开发实践中，需要使用到的依赖项很多，而且，添加的某个依赖项可能还依赖了其它依赖项，例如，当添加`spring-webmvc`时，`spring-webmvc`还依赖了`spring-context`，另外，再添加`spring-jdbc`时，`spring-jdbc`也会依赖`spring-context`，如果`spring-webmvc`和`spring-jdbc`依赖的`spring-context`的版本并不相同，则项目是不可用的。

所以，众多依赖项必须是协调的，版本应该兼容且不冲突，Spring Boot项目都使用了`spring-boot-starter-parent`作为父级项目，这个父级项目就管理了许多依赖项的版本，所以，在`pom.xml`中添加依赖时，某些依赖项是不需要写版本号的，并且，它提供大量了`spring-boot-starter-???`的依赖项，例如`spring-boot-starter-web`，这些依赖项会将相关的一组依赖整合在一起，开发者添加依赖时更加方便！

## 3.3. 自动配置

在`spring-boot-starter`依赖项中包含了`spring-boot-autoconfigure`，这个`spring-boot-autoconfigure`就是用于实现自动配置的，其中包含了大量的、预编写的自动配置类，这些自动配置类中使用了`@ConditionalOnXxx`系列的注解，对当前项目的环境（是否添加了哪些依赖、是否创建了哪些对象、是否配置了哪些属性等）进行判断，以决定是否需要启用某些自动配置。

另外，其实需要通过`@EnableAutoConfiguration`注解来开启自动配置，此注解已经被包含在`@SpringBootApplication`中了，所以，并不需要显式的使用此注解！

## 3.4. 相关注解

- `@SpringBootApplication`：添加在启动类上，每个Spring Boot项目只能有1个类添加此注解
- `@SpringBootConfiguration`：包含了`@Configuration`，被包含在`@SpringBootApplication`中
- `@SpringBootTest`：标记某个类是基于Spring Boot的测试类，执行这个类中的测试方法时，会加载Spring Boot的所有环境，包括执行组件扫描、读取配置文件等

# 4. Spring Security框架

## 4.1. Spring Security框架的作用

Spring Security框架主要解决了认证与授权的相关问题。

## 4.2. 相关概念

认证：识别客户端的身份，Spring Security只会根据`SecurityContext`中的认证信息（`Authentication`对象）来识别客户端的身份

授权：控制客户端是否允许访问某个资源

提示：登录是处理认证时非常重要且不可或缺的一个环节，在处理登录时，需要将通过登录验证后的结果（认证信息）存入到`SecurityContext`中，后续，Spring Security会自动从`SecurityContext`中找到认证信息，从而识别客户端的身份

## 4.3. Spring Security框架的基本特点

在Spring Boot项目中，当添加了`spring-boot-starter-security`依赖后，你的项目会发生以下变化：

- 所有请求都是必须通过认证的，否则，会响应`403`，或重定向到默认的登录页面
- 提供了默认的登录页（`/login`）和登出页（`/logout`）
- 提供了默认的登录账号，用户名为`user`，密码为启动项目时的控制台提示的UUID值
- 默认开启了防止伪造的跨域攻击的防御机制，自定义的POST请求无法正常处理

## 4.4. 关于Spring Security的配置类

在项目中，可以自定义类，继承自`WebSecurityConfigurerAdapter`，则此类是Spring Security的配置类，在类中重写`void configure(HttpSecurity http)`方法进行配置。

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public void configure(HttpSecurity http) {
        // 配置请求的授权访问，注意：将使用第一匹配原则
        http.authorizeRequests()
            .mvcMatchers("/user/login", "/user/reg") // 匹配若干个路径，可以使用Ant-Style通配模式
            .permitAll() // 允许直接访问，不需要检查认证信息
            .anyRequest() // 所有请求，也可以视为“除了以上配置过的以外的其它请求”
            .authenticated(); // 要求是已经通过认证的，则需要检查认证信息
        
        // 如果调用以下方法，将启用默认的登录页和登出页，当视为“未通过认证”时，将重定向到登录页
        // 如果不调用以下方法，将不启用默认的登录页和登录页，当视为“未通过认证”时，将响应403
        // 还可以在以下方法的基础上，进一步调用其它方法对登录页和登出页进行配置
        http.formLogin();
        
        // 禁用【防止伪造的跨域攻击的防御机制】，则自定义的POST请求可以正常处理
        http.csrf().disable();
    }
}
```

关于Ant-Style：

- 当使用`*`通配符时，例如：`/user/*`，可以匹配`/user/login`，但不可以匹配`/user/9527/delete`
- 当使用`**`通配符时，例如：`/user/**`，可以匹配`/user/login`，也可以匹配`/user/9527/delete`

关于第一匹配原则：

- 如果某个请求匹配以上配置的多种规则，将以第一次匹配到的为准，根据以上演示代码，`/user/login`匹配到了`mvcMatcher()`方法配置的路径，也匹配了`anyRequest()`，由于`mvcMatcher()`的代码靠前，则`/user/login`请求适用于`permitAll()`，而不会是`authenticated()`，在实际应用中，可以简单的认为：需要将精准的配置写在靠前的位置，需要将模糊的配置写在靠后的位置

## 4.5. 使用Spring Security验证登录

Spring Security提供了`UserDetailsService`接口，接口中定义了`UserDetails loadUserByUsername(String username)`方法，在验证登录时，Spring Security框架会自动使用登录时提交的用户名来调用这个方法，则框架将得到返回的`UserDetails`类型的对象，并自动检查`UserDetails`对象中的用户状态，如果用户状态为不可用（被禁用、已过期等），则抛出异常，然后，还会自动验证登录时提交的密码与`UserDetails`对象中的密码是否匹配，如果不匹配，则抛出异常，如果匹配，则验证完成，将返回`Authentication`类型的结果。

```
假设提交了登录信息：root / 123456

【以下是框架自动执行】
UserDetails userDetails = userDetailsService.loadUserByUsername("root");
检查userDetails对象中包含的用户状态，例如是否禁用、是否过期等
检查userDetails对象中包含的密码，与123456是否匹配
```

当需要自定义登录验证时，需要：

- 在配置类中，使用`@Bean`方法配置密码编码器（`PasswordEncoder`）
  - Spring Security在验证密码是否匹配时，要求`UserDetails`类型的对象中的密码是某种密文格式
  - 一般定义在Spring Security的配置类中即可
- 在Spring Security配置类中，通过重写`AuthenticationManager authenticationManagerBean()`方法，并在方法上添加`@Bean`注解，则后续可以自动装配`AuthenticationManager`对象来执行登录验证
  - 不建议重写`authenticationManager()`方法，此方法在执行某些测试时会出现死循环，从而导致内存溢出

- 自定义组件类，实现`UserDetailsService`接口，并重写`loadUserByUsername()`方法
  - 一旦Spring容器中存在`UserDetailsService`类型的对象，则Spring Security不再启用默认的登录账号，启动项目时控制台也不再显示临时的UUID密码
- 在需要执行登录验证的类中，先自动装配`AuthenticationManager`，然后再调用此对象的`authenticate()`方法即可执行登录验证
  - 提示：验证通过后，应该将`authenticate()`方法返回的结果存入到`SecurityContext`中，以保证后续能够通过认证

# 5. 算法相关

## 5.1. 关于UUID

UUID是全球唯一码，它保证了在同一时空中的值是唯一的！

UUID的算法并不是固定的，各个平台生成的UUID的特征并不相同！

UUID的主要特点：唯一、随机（不可预测）

UUID本质是由128位算法（运算结果是128个二进制位）运算得到的结果，在显示时，通常会转换成十六进制数，长度为32，并且，会在其中添加4个减号（典型格式是8-4-4-4-12），则总长度为36。

另外，128位算法的结果有2的128次方个，2的128次方的值为340282366920938463463374607431768211456。

## 5.2. 关于BCrypt算法

**注意：**所有用于处理密码加密并最终保存到数据库中所使用的算法，都不是加密算法！所有的加密算法都是可以被逆向运算的，加密算法是用于保证传输过程的安全性的！用于处理密码加密并存储的算法，都是单向算法，即使算法、加密参数、密文均被泄密，也不可能通过运算还原出原始的密码（密码的原文）。

早期，典型的用于处理密码加密算法主要有：MD5、SHA-1等，这些算法是可以被穷举手段进行破解的！

BCrypt算法默认使用了随机的盐值，并且，此盐值被保存到密文中，作为密文的一部分，以便于后续验证密码。

BCrypt算法最大的特点就是“慢”！它被故意设计为一种慢速算法，可以有效的防止暴力破解（循环尝试）。

BCrypt的算法的工具类是`BCryptPasswordEncoder`，这个类的构造方法可以传入参数，表示算法的强度值，默认为10，表示将进行2的10次方这么多次数的哈希运算，如果你认为现在执行BCrypt太快而不安全，可以适当的调整这个值，值越大，执行哈希运算的次数就越多，整体运算速度就会越慢！

# 6. 项目的大致开发流程

项目的大致开发流程应该是：立项（确定要做什么项目） > 页面规划 > 数据库与表的设计 > 编写代码。

提示：以上开发流程并不是规范的，更加规范的开发流程可以学习《软件工程》。

# 7. 规划本项目的数据类型与数据属性

商品：ID，创建时间，修改时间，所属类别ID，所属类别名称（冗余），标题，截取的详情（详情表中的数据的前???个字，或单独填入均可，是冗余的数据），图片，价格，关键字列表，是否推荐，审核状态（冗余），上架状态，排序序号，销量（冗余），评论数量（冗余），好评率（冗余），差评率（冗余）

商品详情：ID，创建时间，修改时间，商品ID，详情（图文）

商品审核记录：ID，创建时间，修改时间，审核人，商品ID，审核时间，状态，备注

商品上架下架记录：（暂不实现）

商品类别：ID，创建时间，修改时间，名字，父级ID，层级（冗余），是否无子级（冗余），是否显示在列表中，排序序号，启用状态，关键词列表

购物车：ID，创建时间，修改时间，商品标题，商品价格，商品数量，汇总金额 ，用户ID

收货地址：ID，创建时间，修改时间，用户ID，收货人，收货电话，省编码，省名称，市编码，市名称，区编码，区名称，详细地址，是否为默认的收货地址

订单：ID，创建时间，修改时间，订单号，下单时间，收货人，收货地址，收货电话，订单状态，总金额，用户ID，商品的总数量，物流单号（不实现），支付渠道（不实现），支付方式（不实现）

订单项（订单中的商品）：ID，创建时间，修改时间，所属订单，商品ID，商品封面图，商品标题，商品价格，购买数量

> 备注：某些数据处理的难度较大，本项目将不考虑，至少包括：商品折扣、优惠券、库存等。



```mysq
select * from xx where 父级=1
```

| ID   | 父级 | 名称     | 层级 | 是否无子级 |
| ---- | ---- | -------- | ---- | ---------- |
| 1    | 0    | 家电     | 1    | 否         |
| 2    | 0    | 水果     | 1    |            |
| 3    | 0    | 服装     | 1    |            |
| 4    | 1    | 电视     | 2    |            |
| 5    | 1    | 空调     | 2    | 否         |
| 6    | 5    | 柜机空调 | 3    | 是         |
| 7    | 5    | 挂机空调 | 3    | 是         |
| 8    | 5    | 中央空调 | 3    | 是         |
|      | 7    | xxxx     |      |            |
|      |      |          |      |            |











