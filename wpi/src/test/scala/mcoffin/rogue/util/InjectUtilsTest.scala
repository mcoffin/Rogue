package mcoffin.rogue.util

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject

import net.codingwell.scalaguice.ScalaModule

import org.junit.Test
import org.junit.Assert._

trait InjectUtilsTestService {
  def getName: String
}

class InjectUtilsTestServiceImpl1 extends InjectUtilsTestService {
  override def getName = "1"
}

class InjectUtilsTestServiceImpl2 extends InjectUtilsTestService {
  override def getName = "2"
}

case class InjectUtilsTestInjected @Inject() (val svc: InjectUtilsTestService) {
  def doAssert {
    assertEquals(svc.getName, "2")
  }
}

class InjectUtilsTest {
  class InjectUtilsTestModule[T] (val impl: T)(implicit val m: Manifest[T]) extends AbstractModule with ScalaModule {
    override def configure() {
      bind[T].toInstance(impl)
    }
  }

  @Test def bindOverridesBindInChain {
    import net.codingwell.scalaguice.InjectorExtensions._

    val mod1 = new InjectUtilsTestModule[InjectUtilsTestService](new InjectUtilsTestServiceImpl1)
    val mod2 = new InjectUtilsTestModule[InjectUtilsTestService](new InjectUtilsTestServiceImpl2)
    val injector = Guice.createInjector(InjectUtils.createChainedOverrideModule(Array(mod1), Array(mod2)))

    val injected = injector.instance[InjectUtilsTestInjected]
    injected.doAssert
  }
}
