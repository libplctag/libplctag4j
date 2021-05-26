# libplctag4j

- [libplctag4j](#libplctag4j)
  - [PLC Access Library for Java](#plc-access-library-for-java)
  - [Dependencies](#dependencies)
  - [Contact and Support](#contact-and-support)
    - [libplctag Forum](#libplctag-forum)
    - [GitHub](#github)
  - [How to Use libplctag4j](#how-to-use-libplctag4j)
    - [Build it Yourself](#build-it-yourself)
  - [Tier 1 Platforms](#tier-1-platforms)
  - [Tier 2 Platforms](#tier-2-platforms)
  - [Android](#android)
  - [License](#license)
  - [TODO](#todo)

Java wrapper for [libplctag](https://github.com/libplctag/libplctag).

## PLC Access Library for Java

This library is a single JAR including all the needed parts to communicate with Allen-Bradley and some Modbus PLCs.   It uses the C-based project [libplctag](https://github.com/libplctag/libplctag) for the underlying protocol emulation.

## Dependencies

None if you use a [Tier 1 platforms](#tier-1-platforms)!  The full JAR library includes all needed Java classes and includes backup copies of the native C libraries for the major Tier 1 platforms below.  Just drop it in your project and start building your application.

If you want to include the only the minimum, there is a second JAR file with just the classes for Tag.java and its inner classes built by the Gradle build.

## Contact and Support

There are two ways to ask for help or contact us.

### libplctag Forum

If you have general questions or comments about the
library, its use, or about one of the wrapper libraries, please join the Google group
[libplctag](https://groups.google.com/forum/#!forum/libplctag)!

The forum is open to all, but is by request only to keep the spammers down.  The traffic is fairly
light with usually a small number of emails per month.  It is our primary means for users to
ask questions and for discussions to happen.   Announcements about releases happen on the forum.

### GitHub

If you find bugs or need specific features, please file them on GitHub's issue tracker for
this project.

## How to Use libplctag4j

You can either take one of the releases from GitHub, unpack the ZIP file, and use the JAR files directly, or you can build it all yourself.   See the section below about Android for more information on how to use the library there.

There are two JAR files.  The main one includes all of JNA and all the binaries required to use the library on the all the Tier 1 platforms except Android.   The JAR `libplctag_min_*.jar` includes only the minimum class files required.

### Build it Yourself

If you want to build the entire Java library yourself, you will need Git, Gradle 6+, the JDK of at least version 8, and an Internet connection for the first build.  Gradle will load everything you need.  Once all the dependencies are loaded, you can drop the Internet connection.

For Intel/AMD-based Windows, Linux or macOS:

1. Install Git, JDK 8+ and Gradle for your OS platform.
2. Checkout the source with Git: `git clone https://github.com/libplctag/libplctag4j`
3. In a command line window enter the directory/folder in which you checked out the project.
4. Run `gradle -b build-jar.gradle clean shadowJar`
5. You will find the JAR files in `build/libs`.

You can use an IDE such as IntelliJ, Eclipse or Visual Studio Code.  All of these either natively handle the Gradle build files or have plugins that will support Gradle.  Note the non-standard Gradle command above.  You may need to edit the IDE configuration to support that directly.  I build on the command line for simplicity.

## Tier 1 Platforms

This library has full support for the following platforms:

- Linux x86 and x64, Ubuntu/Debian
- Microsoft Windows 10 x86 and x64
- Apple macOS x64
- [Android](https://github.com/libplctag/libplctag4android)

The C library will build on many of the platforms on which Java is available so support for other operating systems and CPUs is possible but not directly part of this library.  See the section on [Tier 2 Platforms](#tier-2-platforms).   Patches and help gladly accepted!

Note that using the library on Android is a more complicated process.  To make it easier, there is an example project for [Android](https://github.com/libplctag/libplctag4android) contributed by @GitHubDragonFly.

## Tier 2 Platforms

Tier 2 platforms are those on which the core library will build and Java and JNA are supported.  An example is the RaspberryPI series of small computers.  Any platform that supports either the Windows socket APIs or the POSIX networking and threading APIs will likely support the library, though perhaps with some effort.

For these platforms:

- Build the C library first and install it.
- Install the JDK for your platform, at least version 8.
- Install JNA for your platform.
- Use the minimal JAR file with only the Tag classes.

If you want to build the JAR files on your Tier 2 platform, you will also need to install Gradle.

## Android

There is another project in the libplctag organization for Android thanks to user @GitHubDragonFly.   Please see the [libplctag4android](https://github.com/libplctag/libplctag4android) project for a minimal example Android application.   This example shows how to include libplctag into your projects.

## License

This code is released under the same dual MPL (Mozilla Public License) 2.0/LGPL (Lesser/Library GNU Public License) 2+ license as the C library.  See the license files for more information.

## TODO

There are a few things left to do before this can be considered version 1.0.

- [x] Set up multiple JAR build.
- [ ] Create stand-alone Java examples.
- [ ] Export to Maven Central and/or JCentral.
