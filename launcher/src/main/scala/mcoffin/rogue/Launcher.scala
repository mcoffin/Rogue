package mcoffin.rogue

import java.util.ServiceLoader

import org.osgi.framework.BundleException
import org.osgi.framework.launch.FrameworkFactory

import scala.collection.JavaConversions._

object Launcher extends App {
  def osgiConfig: java.util.Map[String, String] = {
    val cfg: Map[String, String] = Map()
    cfg
  }

  def start(name: String) {
    println("Starting \"" + name + "\"")
    val b = context.installBundle(name)
    try {
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
