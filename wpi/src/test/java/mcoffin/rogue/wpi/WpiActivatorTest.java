package mcoffin.rogue.wpi;

public class WpiActivatorTest {
   
  @Test (expected = RuntimeException.class)
  public void prestartShouldFail() {
    Object runner = new Object();

    WpiActivator wa = new WpiActivator(runner);
    wa.prestart();
  }

}
