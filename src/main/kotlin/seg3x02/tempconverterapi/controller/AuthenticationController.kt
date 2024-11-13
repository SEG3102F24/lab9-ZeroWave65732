package seg3x02.tempconverterapi.controller

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import seg3x02.tempconverterapi.controller.payload.AuthResponse
import seg3x02.tempconverterapi.controller.payload.MessageResponse
import seg3x02.tempconverterapi.controller.payload.SignInData
import seg3x02.tempconverterapi.controller.payload.SignUpData
import seg3x02.tempconverterapi.repository.UserRepository
import seg3x02.tempconverterapi.security.UserDetailsImpl
import seg3x02.tempconverterapi.security.credentials.ERole
import seg3x02.tempconverterapi.security.credentials.User
import seg3x02.tempconverterapi.security.jwt.JwtUtils

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("/auth")
class AuthenticationController(val authenticationManager: AuthenticationManager,
                               val userRepository: UserRepository,
                               val encoder: PasswordEncoder,
                               val jwtUtils: JwtUtils) {
    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: @Valid SignInData): ResponseEntity<*> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val role = userDetails.authorities.elementAtOrNull(0)
        return ResponseEntity.ok<Any>(AuthResponse(jwt,
            userDetails.id,
            userDetails.username,
            role!!.authority))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: @Valid SignUpData): ResponseEntity<*> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                .badRequest()
                .body<Any>(MessageResponse("Error: Username is already taken!"))
        }
        val user = User(signUpRequest.username,
            encoder.encode(signUpRequest.password))
        user.role = if ("admin" == signUpRequest.role) ERole.ROLE_ADMIN else ERole.ROLE_USER
        userRepository.save(user)
        return ResponseEntity.ok<Any>(MessageResponse("User registered successfully!"))
    }
}