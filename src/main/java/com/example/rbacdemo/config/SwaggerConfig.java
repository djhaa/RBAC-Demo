package com.example.rbacdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置类
 * 官方文档参考: http://springfox.github.io/springfox/docs/current/#quick-start-guides
 *
 * @author djhaa
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final Tag UmsAdminTag = new Tag("UmsAdminController", "后台用户管理Controller");
    private final Tag UmsRoleTag = new Tag("UmsRoleController", "后台用户角色管理Controller");
    private final Tag UmsResourceTag = new Tag("UmsResourceController", "后台资源管理Controller");
    private final Tag UmsResourceCategoryTag = new Tag("UmsResourceCategoryController", "后台资源分类管理Controller");
    private final Tag UmsMenuTag = new Tag("UmsMenuController", "后台菜单管理Controller");

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.rbacdemo.controller"))
                .paths(PathSelectors.any())
                .build()
                //配置swagger使用apikey验证
                .securitySchemes(apiKeys())
                .securityContexts(securityContexts())
                .tags(UmsAdminTag, UmsRoleTag, UmsResourceTag, UmsResourceCategoryTag, UmsMenuTag);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RBAC Demo")
                .description("RBAC Demo's document")
                .version("1.0")
                .contact(new Contact("djhaa", "xxx", "xxx"))
                .build();
    }

    private List<ApiKey> apiKeys() {
        List<ApiKey> res = new ArrayList<>();
        res.add(new ApiKey("API_Authorization", "Authorization", "header"));
        return res;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> res = new ArrayList<>();
        SecurityContext target = SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/*/.*")).build();
        res.add(target);
        return res;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> res = new ArrayList<>();
        res.add(new SecurityReference("API_Authorization", authorizationScopes));
        return res;
    }
}
