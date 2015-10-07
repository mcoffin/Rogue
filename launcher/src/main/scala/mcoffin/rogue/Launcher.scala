package mcoffin.rogue

import java.util.ServiceLoader

import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

object Launcher extends App {
  lazy val logger = LoggerFactory.getLogger(Launcher.getClass())

  def osgiConfig: java.util.Map[String, String] = {
    val cfg: Map[String, String] = Map()
    cfg
  }

  def start(name: String) {
    logger.info("Installing bundle at url \"" + name + "\"")
    val b = context.installBundle(name)
    try {
      logger.info("Starting bundle at url \"" + name + "\"")
      b.start()
    } catch {
      case e: BundleException => {
        e.printStackTrace()
      }
    }
  }

  lazy val frameworkFactory = ServiceLoader.load(classOf[FrameworkFactory]).iterator.next
  val framework = frameworkFactory.newFramework(osgiConfig)

  framework.start()

  val context = framework.getBundleContext()
  args.foreach(start(_))
}
