package mcoffin.rogue

import java.util.ServiceLoader

import org.osgi.framework.Bundle
import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory
import org.osgi.framework.wiring.BundleRevision
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

object Launcher extends App {
  implicit class RogueBundle(val internalBundle: Bundle) {
    def isFragment = (internalBundle.adapt(classOf[BundleRevision]).getTypes() & BundleRevision.TYPE_FRAGMENT) != 0
  }

  lazy val logger = LoggerFactory.getLogger(Launcher.getClass())

  def osgiConfig: java.util.Map[String, String] = {
    val cfg: Map[String, String] = Map()
    cfg
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
  val framework = frameworkFactory.newFramework(osgiConfig)

  framework.start()

  val context = framework.getBundleContext()
  args.foreach(startBundleURL(_))
}
