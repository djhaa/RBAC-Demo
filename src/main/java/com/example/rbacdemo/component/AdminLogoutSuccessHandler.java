package com.example.rbacdemo.component;

import cn.hutool.json.JSONUtil;
import com.example.rbacdemo.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登出成功处理器类
 *
 * @author djhaa
 */
@Component
public class AdminLogoutSuccessHandler implements LogoutSuccessHandler {
    @Value("${jwt.tokenHeader}")
    private String header;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            //清除Security中保存的已登录用户信息
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        //清除Header中的JWT信息
        response.setHeader(header, "");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.success(null)));
        response.getWriter().flush();
    }
}
