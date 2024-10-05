# 处理验证登录失败的异常

使用Spring Security验证登录时，如果失败，Spring Security会抛出对应的异常，并且，Spring Security也有处理这些异常的机制，默认的做法是响应`403`，可以自行定义全局异常处理器来接管处理这些异常，相关的异常有：

```
AuthenticationException
-- BadCredentialsException 密码错误
-- AuthenticationServiceException
-- -- InternalAuthenticationServiceException 用户名错误
-- AccountStatusException
-- -- DisabledException 账号被禁用
```

则使用全局异常处理器处理以上相关异常：

```java
@ExceptionHandler({
        InternalAuthenticationServiceException.class,
        BadCredentialsException.class
})
public JsonResult handleAuthenticationException(AuthenticationException e) {
    log.debug("全局异常处理器开始处理AuthenticationException");
    String message = "登录失败，用户名或密码错！";
    return JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED, message);
}

@ExceptionHandler
public JsonResult handleDisabledException(DisabledException e) {
    log.debug("全局异常处理器开始处理DisabledException");
    String message = "登录失败，账号已经被禁用！";
    return JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED_DISABLED, message);
}
```

另外，无认证信息却尝试访问需要通过认证的资源时的异常需要在Spring Security的配置类中的`void configurer(HttpSecurity http)`方法中处理：

```java
// 处理无认证信息却尝试访问需要通过认证的资源时的异常
http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
    Integer state = ServiceCode.ERROR_UNAUTHORIZED.getValue();
    String message = "当前未登录，或登录信息已过期，请登录！";
    response.setContentType("application/json; charset=utf-8");
    PrintWriter printWriter = response.getWriter();
    printWriter.println("{\"state\": " + state + ", \"message\": \"" + message + "\"}");
    printWriter.close();
});
```

# Token

Token：票据，令牌

当某个客户端第1次向服务器端发起请求后，服务器端会向客户端响应一个Token，后续，当客户端向服务器端发起请求时，将携带此Token，服务器端将根据Token来识别客户端的身份！

与Session机制不同，Token是由服务器端的程序发出的（需要程序员编写代码向客户端响应Token数据），并且，客户端程序需要自行携带Token（需要程序员编写代码携带Token数据发起请求），甚至，识别客户端身份也是需要程序员编写相关代码来实现的！

Token数据与Session不同，Session ID本质是一个UUID值，所以，是非常随机的、不可预测的值，并且，没有实际的数据含义，而Token是一段有意义的数据！

基于Token的特点，它适用于集群或分布式系统，只要每个服务器端都具备解读这个Token数据的能力，就可以根据Token识别客户端的身份！

在使用Token时，由于服务器不再需要在内存中保存用户的信息，所以，可以为Token数据设置较长的有效期！

# JWT

**JWT**：**J**SON **W**eb **T**oken

JWT的官网：https://jwt.io

每个JWT数据都是由3个部分组成，最终使用算法进行编码得到的数据！组成部分包括：

- Header：头部信息，用于声明算法（alg）与Token类型（typ）
- Payload：载荷，用于存放数据
- Verify Signature：验证签名，用于配置算法与secretKey

在JWT的官网，列举了常用的JWT编程的库：https://jwt.io/libraries?language=Java

以使用`jjwt`为例，需要在项目中添加依赖：

```xml
<jjwt.version>0.9.1</jjwt.version>
```

```xml
<!-- JJWT（Java JWT） -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>${jjwt.version}</version>
</dependency>
```

测试使用：

```java
package cn.tedu.tmall.passport;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTests {

    String secretKey = "k4^&32flj5Ss(Jf&*(5%DK3da";

    @Test
    void generate() {
        Date date = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 9527);
        claims.put("username", "root");

        String jwt = Jwts.builder()
                // Header
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload：Claims
                .setClaims(claims)
                .setExpiration(date)
                // Signature
                .signWith(SignatureAlgorithm.HS256, secretKey)
                // compact
                .compact();
        System.out.println(jwt);
    }

    @Test
    void parse() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6OTUyNywiZXhwIjoxNjg5MzMxOTU0LCJ1c2VybmFtZSI6InJvb3QifQ.61NgtudAvpOHk_CTcam2PO2dZHAtSfqcJWpcxfzgAyU";
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        Object id = claims.get("id");
        Object username = claims.get("username");
        System.out.println("id = " + id);
        System.out.println("username = " + username);
    }

}
```

尝试解析JWT时，如果JWT已过期，则抛出异常：

```
io.jsonwebtoken.ExpiredJwtException: JWT expired at 2023-07-14T18:52:34Z. Current time: 2023-08-03T11:55:38Z, a difference of 1702984297 milliseconds.  Allowed clock skew: 0 milliseconds.
```

如果使用的`secretKey`与生成时的不一致，或JWT的编码结果中某个值的错误，则抛出异常：

```
io.jsonwebtoken.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
```

如果JWT数据错误，导致无法正确解析，则抛出异常：

```
io.jsonwebtoken.MalformedJwtException: Unable to read JSON value: {"id":9527,"MM|b�\�":"root"}
```

**注意：**JWT本身并不对数据进行保密处理，在不知道`secretKey`的情况下仍是可以被解析出其中的数据的（某个编程API在`secretKey`不匹配时直接抛出异常，并不显示解析结果），所以，不要在JWT中存放敏感数据。

# 基于JWT识别客户端的身份 -- 分析

当需要在项目中实现“基于JWT识别客户端的身份”，大致需要：

- 当某个客户端向服务器端发起登录请求后，如果服务器端验证通过，会向客户端响应一个JWT
- 后续，当客户端向服务器端发起请求时，将携带此JWT，服务器端将尝试解析JWT，并将成功解析的结果创建为`Authentication`对象存入到`SecurityContext`中

# 向客户端响应JWT数据

在`UserLoginInfoVO`类中添加`id`属性；

在`UserMapper.xml`中查询的字段列表中补充查询`id`属性；

创建`CustomUserDetails`类，继承自`User`类，在类中扩展出`id`属性；

调整`UserDetailsServiceImpl`中的实现，将返回`CustomUserDetails`类型的结果；

调整`UserServiceImpl`中的实现，当通过登录验证后，返回的`Authentication`对象中的当事人（通过调用`getPrinciple()`得到）就是`UserDetailsServiceImpl`中返回的`CustomUserDetails`，则可以获取用户的`id`和`username`等数据，再将这些数据用于生成JWT并返回（需要修改`IUserService`和实现类中登录方法的返回值类型）；

调整`UserController`，调用Service的登录方法时获取返回值，并响应到客户端。

以上代码请参考GIT提交记录：

- 在项目中生成JWT（2023/8/3 at 14:37）
- 在项目中生成JWT（2023/8/3 at 15:24）

# 处理验证登录的流程

```mermaid
sequenceDiagram
    participant Client
    participant UserController
    participant UserService
    participant AuthenticationManager
    participant UserDetailsService
    participant IUserRepository
    participant UserMapper
  
    Client->>UserController: 发起请求，提交username与password
    UserController->>UserController: 封装为UserLoginInfoParam
    UserController->>UserService: 传入UserLoginInfoParam参数
    UserService->>AuthenticationManager: 处理UserLoginInfoParam为Authentication
    AuthenticationManager->>UserDetailsService: 调用loadUserByUsername()方法并传入用户名
    UserDetailsService->>IUserRepository: 调用IUserRepository
    IUserRepository->>UserMapper: 调用UserMapper
    UserMapper-->>IUserRepository: 返回UserLoginInfoVO
    IUserRepository-->>UserDetailsService: 返回UserLoginInfoVO
    UserDetailsService-->>AuthenticationManager: 返回UserDetails
    AuthenticationManager-->>UserService: 返回Authentication对象，其中当事人是UserDetails对象
    UserService->>UserController: 基于Authentication生成JWT并返回
    UserController-->>Client: 返回JWT
```

# 解析客户端携带的JWT

Spring Security框架使用了大量的过滤器（`Filter`）处理认证与授权！

过滤器是Java EE中的核心组件之一，是最早处理请求的组件，其大致工作流程如下：

```
---(request)---> Filter(s) ------> Servlet ------> Controller
```

在每个项目中，都可以按需创建若干个过滤器，形成过滤器链，每个过滤器都可以对请求进行“阻止”或“放行”，任何请求必须被所有过滤器“放行”才可以后续的组件被处理！

Spring Security处理`SecurityContext`也是通过过滤器组件来实现的，则处理JWT必须在Spring Security处理`SecurityContext`之前进行，就可以实现“先解析JWT并向`SecurityContext`中存入认证信息，然后框架读取到认证信息”，则必须使用过滤器解析JWT，不可以使用其它组件，并且用于解析JWT的过滤器必须在Spring Security的某些过滤器之前执行！

自行创建过滤器类，可以直接实现`Filter`接口（不推荐），或继承自`OncePerRequestFilter`类，例如：

```java
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public JwtAuthorizationFilter() {
        log.debug("创建过滤器对象：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthorizationFilter.doFilterInternal()");

        // 放行
        filterChain.doFilter(request, response);
    }

}
```

并且，一定要在Spring Security的配置类中的`void configurer(HttpSecurity http)`中将自定义的过滤器添加在框架的过滤器链中的某个位置（否则，你的过滤器将在框架的过滤器之后执行，则没有意义）：

![image-20230803163719997](images/image-20230803163719997.png)

关于使用Knife4j配置请求头中的参数：

![image-20230803165002477](images/image-20230803165002477.png)

处理JWT的过滤器大致是：

```java
// 此过滤器的主要职责：
// 1. 尝试接收客户端携带的JWT
// 2. 尝试解析JWT
// 3. 将解析结果创建为Authentication并存入到SecurityContext
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // TODO 尚未解决的问题
    // 1. 未处理解析JWT时可能抛出的异常
    // 2. 未合理的处理“当事人”
    // 3. 未正确的存入“权限”

    public JwtAuthorizationFilter() {
        log.debug("创建过滤器对象：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthorizationFilter.doFilterInternal()");
        // 根据业内的惯例，客户端提交的JWT会放在请求头（Request Header）中名为Authorization的属性中
        String jwt = request.getHeader("Authorization");
        log.debug("获取客户端携带的JWT：{}", jwt);

        // 检查JWT的基本有效性
        if (!StringUtils.hasText(jwt)) {
            // 放行
            filterChain.doFilter(request, response);
            // 返回，避免代码继续向下执行（当前类中剩余的代码）
            return;
        }

        // 尝试解析JWT
        String secretKey = "k4^&32flj5Ss(Jf&*(5%DK3da";
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        log.debug("id = {}", id);
        log.debug("username = {}", username);

        // 创建认证信息（Authentication）
        Object principal = username; // 当事人，可以是任何类型的数据
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("山寨权限");
        authorities.add(authority);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, null, authorities);

        // 将Authentication存入到SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 放行
        filterChain.doFilter(request, response);
    }

}
```

由于默认情况下`SecurityContext`是基于Session的，当成功解析JWT且将认证信息存入到`SecurityContext`中以后，只要Session未过期，接下来的访问即使不携带JWT，也可以通过认证。

可以进行配置，使得Spring Security永不使用Session：

![image-20230803175003787](images/image-20230803175003787.png)











