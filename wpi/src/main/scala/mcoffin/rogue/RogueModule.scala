package mcoffin.rogue

import com.google.inject.AbstractModule
import com.google.inject.Provider

import net.codingwell.scalaguice.ScalaModule

import org.osgi.framework.BundleContext
import org.osgi.framework.Constants
import org.osgi.framework.ServiceReference
import org.osgi.framework.ServiceEvent

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
  val boundClasses = scala.collection.mutable.Buffer[String]()

  private[RogueModule] def bindServiceReference(sr: ServiceReference[_]) {
    val srClassNames = sr.getProperty(Constants.OBJECTCLASS).asInstanceOf[Array[String]]
    val impl = bundleContext.getService(sr)
    val srClasses = srClassNames.map(Class.forName(_))
    for (srClass <- srClasses) {
      val srClassName = srClass.getCanonicalName
      if (!boundClasses.contains(srClassName)) {
        boundClasses += srClassName
        srClass match {
          case clazz: Class[Object] => {
            bind(clazz).toProvider(new OSGIServiceProvider(bundleContext, srClassName))
          }
          case _ => throw new Exception("Illegal service registration not of type Object")
        }
      }
    }
  }

  override def configure {
    bundleContext.getAllServiceReferences(null, null).foreach(sr => bindServiceReference(sr))
  }
}
