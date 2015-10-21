# rogue
[![Stories in Ready](https://badge.waffle.io/mcoffin/rogue.svg?label=ready&title=Ready)](http://waffle.io/mcoffin/rogue)

`rogue` is a lightweight OSGi environment for the *FIRST* Robotics Competition.

`rogue` is very much still a **work in progress**. Please treat it as such.

## Deploy Notes

Running a `gradle :rogue-launcher:distTar` will build a distribution tarball that you can put on to your roboRIO with **one** exception.

While you are free to compile with the official version of WPILib, `rogue` requires a *slightly* patched version of WPILib to be able to run multiple robot bundles at the same time. To patch WPILib, run `rogue-wpilib-patcher <original-wpilib-jar> <output-wpilib-jar>`, and then copy the output jar to the `lib` directory of the rogue distribution on your roboRIO.

## Features

Rogue includes a *Launcher* which bootstraps the [Apache Felix](http://felix.apache.org/) container runtime on the roboRIO, and set of utilities in the *wpi* directory for interfacing WPILib robots with OSGi.

## Team Adoption

The following teams are known to be using rogue:
* 662 - Rocky Mountain Robotics

## License

Rogue is released under the Apache 2.0 License. See LICENSE file
