package mcoffin.rogue.wpi.inject

import com.google.inject.AbstractModule

import edu.wpi.first.wpilibj.DriverStation

import net.codingwell.scalaguice.ScalaModule

class WPIModule extends AbstractModule with ScalaModule {
  override def configure {
    bind[DriverStation].toProvider[DriverStationProvider]
  }
}
