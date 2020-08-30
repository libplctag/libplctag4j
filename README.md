# libplctag4j

Java wrapper for [libplctag](https://github.com/libplctag/libplctag).

## PLC Access Library for Java

This library is a single JAR including all the needed parts to communicate with Allen-Bradley and some Modbus PLCs.   It uses the C-based project [libplctag](https://github.com/libplctag/libplctag) for the underlying protocol emulation.

### Tier 1 Platforms

This library primarily supports the following platforms:

- Linux x86 and x64, Ubuntu/Debian
- Microsoft Windows 10 x86 and x64
- Apple macOS x64

The C library will build on many of the platforms on which Java is available so support for other operating systems and CPUs is possible but not directly part of this library.   Patches and help gladly accepted!

### License

This code is released under a dual MPL (Mozilla Public License) 2.0/LGPL (Lesser/Library GNU Public License) 2+ license.   See the license files for more information.

The Native Library Loader project's license is detailed in the NativeLibLoader-License.txt file.

### Full Release Requirements

Several things need to be completed before this can be considered a full release.

- [x] Get Tag.java compiling.
- [x] Move to Gradle-based build.
- [x] Separate out Java wrapper into separate repo.
- [x] Include native DLL, JNA into single libplctag.java JAR.
- [x] Rename repo/project to libplctag4j.
- [ ] Create examples
   - [ ] tag_rw clone
   - [ ] Tag listing.
- [ ] CI integration.

### Future Work   

- [ ] Add wrappers and examples for callback functions.
- [ ] support external DLLs more easily (to support tier 2 platforms).
