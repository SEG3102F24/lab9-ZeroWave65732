package seg3x02.tempconverterapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import seg3x02.tempconverterapi.security.RsaKeyProperties

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties::class)
class TempConverterApiApplication

fun main(args: Array<String>) {
	runApplication<TempConverterApiApplication>(*args)
}
