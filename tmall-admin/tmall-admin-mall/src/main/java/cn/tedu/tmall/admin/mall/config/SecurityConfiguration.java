package cn.tedu.tmall.admin.mall.config;

import cn.tedu.tmall.admin.mall.filter.JwtAuthorizationFilter;
import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.web.JsonResult;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

/**
 * Security配置类
 *
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 18:32
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 允许跨域访问
        http.cors();

        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 将解析JWT的过滤器添加到Spring Security框架的过滤器链中,达成先解析jwt
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 处理无认证信息却尝试访问需要通过认证的资源时的异常
        http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
            String message = "当前未登录，或登录信息已过期，请登录！";
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERROR_UNAUTHORIZED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            response.setContentType("application/json; charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(jsonResultString);
            printWriter.close();
        });

        // 白名单
        String[] urls = {
                "/doc.html",
                "/**/*.js",
                "/**/*.css",
                "/swagger-resources",
                "/v2/api-docs",
                "/favicon.ico",
        };

        // 禁用“防止伪造的跨域攻击”的防御机制（CSRF）
        http.csrf().disable();

        // 配置请求授权
        http.authorizeRequests()
                .mvcMatchers(urls).permitAll()
                .anyRequest().authenticated();
    }

}