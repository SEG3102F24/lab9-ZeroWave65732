package seg3x02.tempconverterapi.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import seg3x02.tempconverterapi.repository.UserRepository

@Service
class UserDetailsServiceImpl(val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username:String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow{ UsernameNotFoundException("User $username not found") }
        return build(user)
    }
}