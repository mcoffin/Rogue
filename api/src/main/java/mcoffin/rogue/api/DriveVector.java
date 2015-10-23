package mcoffin.rogue.api;

public class DriveVector {

  private double magnitude, direction;

  public DriveVector() {
    this(0,0);
  }

  public DriveVector(double magnitude, double direction) {
    setMagnitude(magnitude);
    setDirection(direction);
  }

  public void setMagnitude(double mag) {
    magnitude = mag;
  }

  public void setDirection(double dir) {
    direction = dir;
  }

  public double getMagnitude() {
    return magnitude;
  }

  public double getDirection() {
    return direction;
  }
}
