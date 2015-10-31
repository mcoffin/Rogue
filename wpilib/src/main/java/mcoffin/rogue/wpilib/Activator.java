package mcoffin.rogue.wpilib;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGi bundle activator for WPILib
 */
public class Activator implements BundleActivator {
  /**
   * Version file to write the WPILib version to
   */
  private static final String WPILIB_VERSION_FILE = "/tmp/frc_versions/FRC_Lib_Version.ini";

  private final Logger logger = LoggerFactory.getLogger(Activator.class);

  /**
   * Performs one-time initialization of NetworkTable
   */
  private void initializeNetworkTable() {
    NetworkTable.setServerMode();
    NetworkTable.getTable("");
    NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~").putBoolean("LW Enabled", false);
  }

  /**
   * Writes the WPILib version to a file to emulate
   * behavior of the real WPILib
   */
  protected void writeWPILibVersion() throws IOException {
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new FileWriter(WPILIB_VERSION_FILE));
      out.write("2015 Java 1.2.0");
      out.flush();
    } finally {
      try {
        out.close();
      } catch (Exception e) {}
    }
  }

  @Override
  public void start(BundleContext ctx) throws IOException {
    initializeNetworkTable();
    RobotBase.initializeHardwareConfiguration();
    writeWPILibVersion();

    logger.debug("Initialized WPILib");
  }

  @Override
  public void stop(BundleContext ctx) {
  }
}
