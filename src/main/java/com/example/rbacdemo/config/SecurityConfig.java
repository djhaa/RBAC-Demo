package com.example.rbacdemo.config;

import com.example.rbacdemo.component.AdminAccessDeniedHandler;
import com.example.rbacdemo.component.AdminAuthenticationEntryPoint;
import com.example.rbacdemo.component.AdminLogoutSuccessHandler;
import com.example.rbacdemo.component.JwtAuthenticationTokenFilter;
import com.example.rbacdemo.service.UmsAdminService;
import com.example.rbacdemo.service.impl.UserDetailsServiceImpl;
import com.example.rbacdemo.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author djhaa
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private AdminLogoutSuccessHandler adminLogoutSuccessHandler;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private IgnoreUrlsConfig ignoreUrls;
    @Autowired
    private AdminAuthenticationEntryPoint adminAuthenticationEntryPoint;
    @Autowired
    private AdminAccessDeniedHandler adminAccessDeniedHandler;

    /**
    * 与请求相关配置
    * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //无需权限便可访问的路径
                .authorizeRequests().antMatchers(ignoreUrls.getUrls()).permitAll()
                //任何请求都需要身份验证
                .and().authorizeRequests().anyRequest().authenticated()
                //允许跨域请求
                .and().cors()
                //关闭CSRF
                .and().csrf().disable()
                //不使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //自定义权限拒绝处理类
                .and().exceptionHandling().authenticationEntryPoint(adminAuthenticationEntryPoint).accessDeniedHandler(adminAccessDeniedHandler)
                //登出，配置的logoutUrl对应前端请求的登出url.当前端请求该url时会被Security的LogoutFilter拦截
                .and().logout().logoutUrl("/admin/logout").logoutSuccessHandler(adminLogoutSuccessHandler)
                //添加JWT过滤器
                .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
    * 用户认证相关配置
    * */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
    * UserDetailsService是一个函数式接口，可通过通过Lambda表达式实现其loadUserByUsername()抽象方法
    * 或者通过实现类UserDetailsServiceImpl实现其loadUserByUsername()抽象方法
    * */
    @Override
    public UserDetailsService userDetailsService() {
//        return username -> umsAdminService.loadUserByUsername(username);
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }
}
