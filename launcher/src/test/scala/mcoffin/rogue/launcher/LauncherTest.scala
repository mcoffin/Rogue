package mcoffin.rogue.launcher

import org.junit.Assert._
import org.junit.Test

class LauncherTest {
  @Test def loadsOSGiPropertiesFromResource {
    val props = Launcher.osgiProperties
    assertEquals("./deploy", props("felix.fileinstall.dir"))
  }

  @Test def loadsBootstrapConfigFromResource {
    assertTrue(Launcher.config.containsKey("bundles"))
  }
}
