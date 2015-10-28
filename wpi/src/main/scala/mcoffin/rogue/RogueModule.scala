package mcoffin.rogue

import com.google.inject.AbstractModule
import com.google.inject.Provider

import net.codingwell.scalaguice.ScalaModule

import org.osgi.framework.BundleContext
import org.osgi.framework.Constants
import org.osgi.framework.ServiceReference
import org.osgi.framework.ServiceEvent
import org.slf4j.LoggerFactory

import scala.reflect.Manifest

class OSGIServiceProvider (
  val bundleContext: BundleContext,
  val className: String
) extends Provider[Object] {
  private[OSGIServiceProvider] def serviceReference = {
    bundleContext.getServiceReference(className)
  }

  override def get = bundleContext.getService(serviceReference).asInstanceOf[Object]
}

class RogueModule (val bundleContext: BundleContext) extends AbstractModule with ScalaModule {
  val logger = LoggerFactory.getLogger(classOf[RogueModule])

  val boundClasses = scala.collection.mutable.Buffer[String]()

  private[RogueModule] def bindServiceReference(sr: ServiceReference[_]) {
    val srClassNames = sr.getProperty(Constants.OBJECTCLASS).asInstanceOf[Array[String]]
    val impl = bundleContext.getService(sr)
    val srClasses = srClassNames.map(className => {
      try {
        val c = Class.forName(className)
        Some(c)
      } catch {
        case e: ClassNotFoundException => {
          logger.warn("Exception while resolving OSGi service interface: " + e)
          None
        }
      }
    }).collect {
      maybeClass => maybeClass match {
        case Some(c) => c
      }
    }
    for (srClass <- srClasses) {
      val srClassName = srClass.getCanonicalName
      if (!boundClasses.contains(srClassName)) {
        boundClasses += srClassName
        val clazz = srClass.asInstanceOf[Class[Object]]
        bind(clazz).toProvider(new OSGIServiceProvider(bundleContext, srClassName))
      }
    }
  }

  override def configure {
    val serviceReferences = bundleContext.getAllServiceReferences(null, null)
    if (serviceReferences != null) {
      serviceReferences.foreach(bindServiceReference(_))
    }
  }
}
