package mcoffin.rogue.wpi;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.osgi.framework.BundleContext;

import org.mockito.Mockito;

public class RobotActivatorTest {
  
  RobotActivator ra;

  @Before
  public void initialize() {
    ra = new RobotActivator();
  }

  @After
  public void interruptThread() {
    if (ra.thread.isAlive()) {
      ra.thread.interrupt();
      ra.thread.join();
    }
  }
  
  @Test
  public void startShouldStartThread() {
    BundleContext bc = Mockito.mock(BundleContext.class);

    ra.start(bc);
    assertTrue("RobotActivator thread should be alive.", ra.thread.isAlive());
  }

  @Test
  public void stopShouldStopThread() {
    BundleContext bc = Mockito.mock(BundleContext.class);

    ra.start(bc);

    ra.stop(bc);

    assertTrue("RobotActivator thread should be dead", !ra.thread.isAlive());
  }
}
