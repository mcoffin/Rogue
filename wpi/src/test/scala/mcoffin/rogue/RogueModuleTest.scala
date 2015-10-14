package mcoffin.rogue

import com.google.inject.Guice
import com.google.inject.Inject

import net.codingwell.scalaguice.InjectorExtensions._

import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito
import org.osgi.framework.BundleContext
import org.osgi.framework.Constants
import org.osgi.framework.ServiceReference

trait RogueModuleTestService {
}

class RogueModuleTestServiceImpl extends RogueModuleTestService {
}

case class RogueModuleTestInjected @Inject() (val svc: RogueModuleTestService) {
}

class RogueModuleTest {
  lazy val testServiceName = classOf[RogueModuleTestService].getCanonicalName

  @Test def bindsService {
    val ctx = Mockito.mock(classOf[BundleContext])
    val sr = Mockito.mock(classOf[ServiceReference[RogueModuleTestService]])
    Mockito.doReturn(Array(testServiceName).asInstanceOf[Object]).when(sr).getProperty(Constants.OBJECTCLASS)

    val svcArr: Array[ServiceReference[_]] = Array(sr)
    Mockito.when(ctx.getAllServiceReferences(null, null)).thenReturn(svcArr)
    val svcImpl = new RogueModuleTestServiceImpl
    Mockito.when(ctx.getService(sr)).thenReturn(svcImpl.asInstanceOf[RogueModuleTestService])
    Mockito.doReturn(sr).when(ctx).getServiceReference(testServiceName)

    val module = new RogueModule(ctx)
    val injector = Guice.createInjector(module)
    val injected = injector.instance[RogueModuleTestInjected]

    assertNotNull(injected.svc)
  }
}
