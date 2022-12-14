package top.viclau.magicbox.java.examples.springdata.jpa

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional


@ExtendWith(SpringExtension::class)
@ContextConfiguration("classpath:top/viclau/magicbox/java/examples/springdata/jpa/applicationContext.xml")
@Transactional
open class SpringDataJPAUnitTest
