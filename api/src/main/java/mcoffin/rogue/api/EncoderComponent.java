package mcoffin.rogue.api;

public interface EncoderComponent {
  public DriveVector getEncoderPosition();
  public void resetEncoders();
}
