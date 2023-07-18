package firework.config;

import de.hybris.platform.swagger.ApiDocInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/swagger.properties")
@ImportResource({"classpath*:/swagger/swaggerintegration/web/spring/*-web-spring.xml"})
public class WebConfig {
    public WebConfig() {
    }

    @Bean({"apiDocInfo"})
    public ApiDocInfo apiDocInfo() {
        return () -> "firework";
    }
}
