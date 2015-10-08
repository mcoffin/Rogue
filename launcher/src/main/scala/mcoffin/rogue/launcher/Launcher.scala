package mcoffin.rogue.launcher

import java.util.Properties
import java.util.ServiceLoader

import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.io.Source

object Launcher extends App {
  lazy val logger = LoggerFactory.getLogger(Launcher.getClass())

  private[Launcher] def osgiProperties = {
    val props = new Properties()
    props.load(getClass.getResourceAsStream("osgi.properties"))
    logger.debug("Loaded OSGi properties: " + props)
    val pMap: scala.collection.mutable.Map[String, String] = props
    pMap
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
  val framework = frameworkFactory.newFramework(osgiProperties)

  framework.start()

  val context = framework.getBundleContext()
  args.foreach(start(_))
}
