package mcoffin.rogue.inject

import com.google.inject.Guice

import org.junit.Test
import org.slf4j.Logger

case class Slf4jLoggerModuleTestInjected () {
  @InjectLogger var logger: Logger = null
}

class Slf4jLoggerModuleTest {
  @Test def injectsLogger {
    import net.codingwell.scalaguice.InjectorExtensions._
    import org.junit.Assert._

    val injector = Guice.createInjector(Slf4jLoggerModule)
    val injected = injector.instance[Slf4jLoggerModuleTestInjected]

    assertNotNull(injected.logger)
  }
}
