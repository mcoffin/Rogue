package mcoffin.rogue.example

import com.google.inject.Inject

import edu.wpi.first.wpilibj.SampleRobot

import mcoffin.rogue.wpi.WpiActivator

import org.osgi.service.log._
import org.osgi.framework.BundleContext

class ExampleRobotActivator extends WpiActivator(classOf[ExampleRobot]) {
}

class ExampleRobot @Inject() (val logger: LogService, val bundleContext: BundleContext) extends SampleRobot {
  override def robotInit {
    logger.log(LogService.LOG_INFO, "Hello, world!")
  }
}
