package mcoffin.rogue.wpi

import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

import edu.wpi.first.wpilibj.RobotBase

class RobotActivator extends BundleActivator {
  lazy val thread = new Thread() {
    override def run() = RobotBase.main(Array())
  }

  override def start(ctx: BundleContext) {
    thread.start()
  }

  override def stop(ctx: BundleContext) {
    thread.interrupt()
    thread.join()
  }
}
