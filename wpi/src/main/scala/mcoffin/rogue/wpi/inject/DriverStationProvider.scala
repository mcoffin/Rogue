package mcoffin.rogue.wpi.inject

import com.google.inject.Provider

import edu.wpi.first.wpilibj.DriverStation

class DriverStationProvider extends Provider[DriverStation] {
  override def get = DriverStation.getInstance
}
