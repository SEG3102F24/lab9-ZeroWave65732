package seg3x02.tempconverterapi.repository

import org.springframework.data.repository.CrudRepository
import seg3x02.tempconverterapi.security.credentials.User
import java.util.*

interface UserRepository: CrudRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean
}