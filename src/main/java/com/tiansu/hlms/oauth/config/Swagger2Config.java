package com.tiansu.hlms.oauth.config;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wangjl
 * @description Swagger2的配置文件
 * @date 2018-01-08 14:54
 **/
@Configuration
@EnableSwagger2  // 启用 Swagger
public class Swagger2Config {

    /**
     * @author wangjl
     * @date 2018/1/8 15:03
     * @description: 设置要显示的API权限
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Class<?> declaringClass = input.declaringClass();
                if (declaringClass == BasicErrorController.class )// 排除
                    return false;
                if (declaringClass.isAnnotationPresent(RestController.class)
                        || declaringClass.isAnnotationPresent(Controller.class)) // 被注解的类
                    return true;
                if (input.isAnnotatedWith(ResponseBody.class)) // 被注解的方法
                    return true;
                return false;
            }
        };
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .select()
                .apis(predicate)
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * @author wujh
     * @date 2018/1/8 15:03
     * @description:  设置API标题和版本号
     * @return ApiInfo API基本详细信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("鉴权中心相关接口API")//大标题
                .description("鉴权中心相关API")
                .contact(new Contact("tiansu-hlms", "", ""))  // 作者
                .version("0.1")//版本
                .build();
    }
}
