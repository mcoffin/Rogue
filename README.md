# rogue
[![Build Status](https://travis-ci.org/mcoffin/rogue.svg?branch=master)](https://travis-ci.org/mcoffin/rogue)
[![Coverage Status](https://coveralls.io/repos/mcoffin/rogue/badge.svg?branch=master&service=github)](https://coveralls.io/github/mcoffin/rogue?branch=master)
[![Stories in Ready](https://badge.waffle.io/mcoffin/rogue.svg?label=ready&title=Ready)](http://waffle.io/mcoffin/rogue)

`rogue` is a lightweight OSGi environment for the *FIRST* Robotics Competition.

`rogue` is very much still a **work in progress**. Please treat it as such.

## Deploying

Running a `gradle :rogue-karaf:dist` will build a distribution tarball that you can put on to your roboRIO.

## Caveats

When using Google Guice injection to receive your dependencies on other OSGI services, you'll need to use `Provider<T>` instead of `T` itself if you want to get the correct version of the dependency once the service changes behind the scenes (via a module hot-swap or something similar).

If you want to keep an always-up-to-date version of an injected module **do not cache** the instance given to you by the provider. You must access the instance via the provider every time.

## Team Adoption

The following teams are known to be using rogue:
* 662 - Rocky Mountain Robotics

## License

Rogue is released under the Apache 2.0 License. See LICENSE file
