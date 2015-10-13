package mcoffin.rogue.wpi

import com.google.inject.Guice
import com.google.inject.util.Modules

import edu.wpi.first.wpilibj.RobotBase

import java.io.BufferedWriter
import java.io.FileWriter

import mcoffin.rogue.RogueModule
import mcoffin.rogue.util.InjectUtils
import mcoffin.rogue.wpi.inject.WPIModule

import org.ops4j.peaberry.Peaberry
import org.osgi.framework.BundleActivator
import org.osgi.framework.BundleContext

/**
 * Wrapper for static WPIlib initialization actions
 */
object WPILib {
  var initialized = false;

  /**
   * Initializes the NI FPGA ensuring to only do it once,
   * and preventing against race conditions
   */
  def initializeHardwareConfiguration() {
    if (initialized) {
      return
    }

    WPILib.synchronized {
      if (!initialized) {
        RobotBase.initializeHardwareConfiguration()
        initialized = true
      }
    }
  }

  /**
   * Emulates the behavior of RobotBase.main in writing
   * the WPILib version to a tmp file
   */
  def writeWPILibVersion() {
    var out: BufferedWriter = null
    try {
      out = new BufferedWriter(new FileWriter("/tmp/frc_versions/FRC_Lib_Version.ini"))
      out.write("2015 Java 1.2.0")
      out.flush()
    } finally {
      if (out != null) {
        try {
          out.close()
        } catch {
          case e: Exception => {}
        }
      }
    }
  }
}

case class WpiActivator(robotClass: Class[_]) extends BundleActivator {
  var bundleContext: BundleContext = null

  lazy val robotBase = {
    val module = InjectUtils.createChainedOverrideModule(Array(new RogueModule(bundleContext)), Array(Peaberry.osgiModule(bundleContext), new WPIModule))
    val injector = Guice.createInjector(module)
    injector.getInstance(robotClass).asInstanceOf[RobotBase]
  }

  private[wpi] def prestart {
    prestart(robotClass)
  }

  private[wpi] def prestart(runner: Class[_]) {
    // Because the prestart() method in robot base is protected...
    // for... reasons
    try {
      val prestartMethod = runner.getDeclaredMethod("prestart") // TODO: Traverse class hierarchy from robotClass instead of starting at RobotBase
      prestartMethod.setAccessible(true)
      prestartMethod.invoke(robotBase);
    } catch {
      case e: NoSuchMethodException => {
        val newRunner = runner.getSuperclass
        if (newRunner != null) {
          prestart(newRunner)
        } else {
          throw new RuntimeException("Could not find prestart() method in robot class hierarchy")
        }
      }
    }
  }

  lazy val thread = new Thread() {
    override def run = robotBase.startCompetition
  }

  override def start(ctx: BundleContext) {
    bundleContext = ctx

    WPILib.initializeHardwareConfiguration()
    prestart

    WPILib.writeWPILibVersion()

    // Finally, run the competition loop
    thread.start()
  }

  override def stop(ctx: BundleContext) {
    thread.interrupt()
    thread.join()
  }
}
