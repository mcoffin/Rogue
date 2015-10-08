package mcoffin.rogue.launcher

import java.util.Properties
import java.util.ServiceLoader

import org.osgi.framework.Bundle
import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory
import org.osgi.framework.wiring.BundleRevision
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.io.Source

object Launcher extends App {
  implicit class RogueBundle(val internalBundle: Bundle) {
    def isFragment = (internalBundle.adapt(classOf[BundleRevision]).getTypes() & BundleRevision.TYPE_FRAGMENT) != 0
  }

  lazy val logger = LoggerFactory.getLogger(Launcher.getClass())

  private[Launcher] def osgiProperties = {
    val props = new Properties()
    props.load(getClass.getResourceAsStream("osgi.properties"))
    logger.debug("Loaded OSGi properties: " + props)
    val pMap: scala.collection.mutable.Map[String, String] = props
    pMap
  }

  def startBundleURL(name: String) {
    logger.info("Installing bundle at url \"" + name + "\"")
    val b = context.installBundle(name)
    if (!b.isFragment) {
        logger.info("Starting bundle at url \"" + name + "\"")
        b.start()
    } else {
      logger.debug("Bundle at url \"" + name + "\" is a fragment bundle.")
    }
  }

  lazy val frameworkFactory = ServiceLoader.load(classOf[FrameworkFactory]).iterator.next
  val framework = frameworkFactory.newFramework(osgiProperties)

  framework.start()

  val context = framework.getBundleContext()
  args.foreach(startBundleURL(_))
}
