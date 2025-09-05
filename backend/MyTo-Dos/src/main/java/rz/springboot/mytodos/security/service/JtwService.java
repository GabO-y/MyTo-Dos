package rz.springboot.mytodos.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

//serviço para gerar tokens JWT
@Service
public class JtwService {

    //atributo final para o codificador JWT
    private final JwtEncoder encoder;

    public JtwService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    //método para gerar um token JWT a partir da autenticação do usuário
    public String generateToken(Authentication authentication){

        //pega o instante atual e define o tempo de expiração do token
        Instant now = Instant.now();
        long inspiredAt = 300L; //5 minutos

        //cria a string de escopo (roles) do usuário
        String scope = authentication.getAuthorities().stream()
            .map(a -> "ROLE_" + a.getAuthority())
            .collect(Collectors.joining(" "));

        //vai retornar tipo: [ROLE_BASIC, ROLE_ADMIN]

        //cria as reivindicações (claims) do token JWT
        //Obs: reindicações sao as informações contidas no token
        //Exmplo: emissor, assunto, data de emissão, expiração, escopo, etc
        var claims = JwtClaimsSet.builder()
            .issuer("myToDos-api")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(inspiredAt))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();

        //codifica e retorna o token JWT como uma string    
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }
}
