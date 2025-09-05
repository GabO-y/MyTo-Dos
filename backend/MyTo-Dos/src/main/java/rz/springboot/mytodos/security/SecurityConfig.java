package rz.springboot.mytodos.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rz.springboot.mytodos.model.User;
import rz.springboot.mytodos.model.enums.Role;
import rz.springboot.mytodos.repository.UserRepository;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/*
 * como um arquivo de configuração XML, mas escrito em código Java.
 * Em vez de usar XML para dizer ao Spring quais objetos criar e como 
 * eles se relacionam, você usa o código. Isso é uma prática comum no 
 * desenvolvimento moderno com Spring
 * 
 * Os beans são objetos gerenciados pelo Spring, e que podem ser injetados
 * em outras partes da aplicação onde forem necessários.
*/

//Classe de configuração de segurança
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * as chaves foram commitas juntos por não serem chaves sensíveis
     * e facilitar o uso do projeto
     * mas eu estou ciente que em um projeto real, elas devem ser armazenadas em um local seguro
     */

    //injeção das chaves pública e privada do JWT via application.properties
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    //configuração da cadeia de filtros de segurança
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //configurações de segurança HTTP
        http
            //o Customizer.withDefaults() aplica as configurações padrão do Spring Security 
            .cors(Customizer.withDefaults()) 
            //o csrf é desabilitado para simplificar o uso da API
            .csrf(AbstractHttpConfigurer::disable)
            //habilita o console do H2 (banco de dados em memória) desabilitando algumas proteções
            //apenas para fins de desenvolvimento e testes
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            //configura as regras de autorização
            .authorizeHttpRequests(auth -> auth
            //permite acesso sem autenticação aos endpoints de autenticação e ao console do H2
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            //configura a autenticação via HTTP Basic e OAuth2 com JWT  
            .httpBasic(Customizer.withDefaults())
            .oauth2ResourceServer(conf -> conf.jwt(Customizer.withDefaults()));

        //retorna a cadeia de filtros configurada
        return http.build();
    }

    //configuração do gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        //esse retorno se refere ao AuthenticationManager padrão do Spring
        return config.getAuthenticationManager();
    }

    //inicializa um usuário admin padrão no banco de dados se ele não existir
    //útil para testes iniciais e administração do sistema
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        //retorna uma função que será executada na inicialização da aplicação
        return args -> {
            //verifica se o usuário admin já existe
            if (userRepository.findByUsername("admin").isEmpty()) {
                //se não existir, cria um novo usuário admin com senha codificada
                //username: admin
                //password: admin123
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                //sendo o primeiro e ADMIN
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            }
        };
    }

    //configuração dos beans para codificação de senha e JWT
    //usando RSA para assinar e verificar os tokens JWT
    @Bean
    JwtDecoder decoder(){
        //decodificador de JWT usando a chave pública
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }


    //codificador de JWT usando o par de chaves RSA
    @Bean
    JwtEncoder encoder(){
        //constrói o conjunto de chaves JWK com a chave pública e privada
        var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    //bean para codificação de senhas usando BCrypt
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
