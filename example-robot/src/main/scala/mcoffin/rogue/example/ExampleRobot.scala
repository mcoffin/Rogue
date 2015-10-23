package mcoffin.rogue.example

import com.google.inject.Inject

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.SampleRobot
import edu.wpi.first.wpilibj.Preferences

import mcoffin.rogue.wpi.WpiActivator
import mcoffin.rogue.inject.InjectLogger

import org.osgi.framework.BundleContext
import org.slf4j.Logger

class ExampleRobotActivator extends WpiActivator(classOf[ExampleRobot]) {
}

class ExampleRobot @Inject() (val bundleContext: BundleContext) extends SampleRobot {
  @InjectLogger var logger: Logger = null
  @Inject var ds: DriverStation = null
  @Inject var prefs: Preferences = null

  override def robotInit {
    logger.info("Hello, world!")
    logger.debug(s"DriverStation: ${ds}")
    logger.debug(s"Preferences: ${prefs}")
  }
}
