package seg3x02.tempconverterapi.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

@Service
class JwtUtils(private val encoder: JwtEncoder) {
    fun generateJwtToken(authentication: Authentication): String {
        val now = Instant.now()
        val scope: String = authentication.authorities.stream()
            .map{ obj: GrantedAuthority -> obj.authority}
            .collect(Collectors.joining(""))
        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.name)
            .claim("scope", scope)
            .build()
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}