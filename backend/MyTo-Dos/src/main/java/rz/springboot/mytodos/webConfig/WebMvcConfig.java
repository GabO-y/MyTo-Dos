package rz.springboot.mytodos.webConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig {

    //Configuração CORS personalizada
    //permite requisições do frontend (localhost:5173)
    @Bean
    public WebMvcConfigurer corsConfigurerCustom() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //Permitir requisições de qualquer endpoint 
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //permitir todos os headers
                    .allowedHeaders("*")
                    .allowCredentials(true);            
            }
        };
    }


}
