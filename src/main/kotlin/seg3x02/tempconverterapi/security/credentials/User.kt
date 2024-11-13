package seg3x02.tempconverterapi.security.credentials

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username"])])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @NotBlank
    @Size(max = 20)
    var username: String = ""

    @NotBlank
    @Size(max=120)
    var password: String =""

    @Enumerated(EnumType.STRING)
    var role: ERole = ERole.ROLE_USER

    constructor() {}
    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}