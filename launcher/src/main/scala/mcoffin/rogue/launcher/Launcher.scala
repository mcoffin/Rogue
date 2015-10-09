package mcoffin.rogue.launcher

import java.util.Properties
import java.util.ServiceLoader

import org.osgi.framework.Bundle
import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory
import org.osgi.framework.wiring.BundleRevision
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.collection.JavaConversions._

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

  val config = {
    val yaml = new Yaml
    yaml.load(getClass.getResourceAsStream("launcher.yml")).asInstanceOf[java.util.Map[String, Object]]
  }

  private[Launcher] def maybeStartBundle(b: Bundle) {
    val name = b.getSymbolicName
    if (!b.isFragment) {
        logger.debug("Starting bundle " + name)
        b.start()
    } else {
      logger.debug("Bundle " + name + " is a fragment bundle.")
    }
  }

  def startBundleGroup(bundles: Iterable[String]) {
    bundles.map(name => {
      logger.debug("Installing bundle " + name + "")
      context.installBundle(name)
    }).foreach(maybeStartBundle)
  }

  lazy val frameworkFactory = ServiceLoader.load(classOf[FrameworkFactory]).iterator.next
  val framework = frameworkFactory.newFramework(osgiProperties)

  framework.start()

  val context = framework.getBundleContext()
  config.get("bundles").asInstanceOf[java.util.List[Object]].foreach(cfg => {
    startBundleGroup(cfg match {
      case l: java.util.List[_] => {
        logger.debug("Installing bundle group " + l)
        l.map(x => x.asInstanceOf[String])
      }
      case url: String => Seq(url)
      case _ => throw new IllegalArgumentException("Invalid bundle group configuration")
    })
  })
}
