/***************************************************************************
 *   Copyright (C) 2020 by Kyle Hayes                                      *
 *   Author Kyle Hayes  kyle.hayes@gmail.com                               *
 *                                                                         *
 * This software is available under either the Mozilla Public License      *
 * version 2.0 or the GNU LGPL version 2 (or later) license, whichever     *
 * you choose.                                                             *
 *                                                                         *
 * MPL 2.0:                                                                *
 *                                                                         *
 *   This Source Code Form is subject to the terms of the Mozilla Public   *
 *   License, v. 2.0. If a copy of the MPL was not distributed with this   *
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.              *
 *                                                                         *
 *                                                                         *
 * LGPL 2:                                                                 *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/


package org.libplctag;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Tag {
    public static final String JNA_LIBRARY_NAME = "plctag";
    private static NativeLibrary nativeLib = null;

    static {
        /* DEBUG - output the Java system path */
        String property = System.getProperty("java.library.path");
        StringTokenizer parser = new StringTokenizer(property, ":");

        // while (parser.hasMoreTokens()) {
        //     System.err.println("Path search segment: " + parser.nextToken());
        // }

        try {
            nativeLib = NativeLibrary.getInstance(Tag.JNA_LIBRARY_NAME);
            // System.err.println("Found library in system path!");
        } catch(UnsatisfiedLinkError e1) {
            // System.err.println("Unable to find library in system, will try DLL.");

            try {
                // try to extract the library from the DLL.
                File libFile = Native.extractFromResourcePath(Tag.JNA_LIBRARY_NAME, Tag.class.getClassLoader());

                try {
                    nativeLib = NativeLibrary.getInstance(libFile.getAbsolutePath());
                    // System.err.println("Loaded library from native DLL in \"" + libFile.getAbsolutePath() + "\".");
                } catch(UnsatisfiedLinkError e3) {
                    // System.err.println("Unable to load native DLL from path \"" + libFile.getAbsolutePath() + "\"!");
                    // System.exit(Tag.PLCTAG_ERR_NOT_FOUND);
                    throw e3;
                }
            } catch(IOException e2) {
                // System.err.println("Unable to extract library \"" + Tag.JNA_LIBRARY_NAME + "\" from JAR!");
                //System.exit(Tag.PLCTAG_ERR_NOT_FOUND);
                //System.err.println("Unable to extract library, got IOException: " + e2.getMessage());
                //e2.printStackTrace();
                throw new RuntimeException("Unable to extract library \"" + Tag.JNA_LIBRARY_NAME + "\" from JAR!");
            }
        }

        // we found the library, try to set it up for JNA
        try {
            // map all native in this class to the library.
            Native.register(Tag.JNA_LIBRARY_NAME);
        } catch(Exception e4) {
            e4.printStackTrace();
            //System.exit(Tag.PLCTAG_ERR_NOT_FOUND);
            throw e4;
        }
    }

    // error codes
    public static final int PLCTAG_STATUS_PENDING      = (1);
    public static final int PLCTAG_STATUS_OK           = (0);

    public static final int PLCTAG_ERR_ABORT           = (-1);
    public static final int PLCTAG_ERR_BAD_CONFIG      = (-2);
    public static final int PLCTAG_ERR_BAD_CONNECTION  = (-3);
    public static final int PLCTAG_ERR_BAD_DATA        = (-4);
    public static final int PLCTAG_ERR_BAD_DEVICE      = (-5);
    public static final int PLCTAG_ERR_BAD_GATEWAY     = (-6);
    public static final int PLCTAG_ERR_BAD_PARAM       = (-7);
    public static final int PLCTAG_ERR_BAD_REPLY       = (-8);
    public static final int PLCTAG_ERR_BAD_STATUS      = (-9);
    public static final int PLCTAG_ERR_CLOSE           = (-10);
    public static final int PLCTAG_ERR_CREATE          = (-11);
    public static final int PLCTAG_ERR_DUPLICATE       = (-12);
    public static final int PLCTAG_ERR_ENCODE          = (-13);
    public static final int PLCTAG_ERR_MUTEX_DESTROY   = (-14);
    public static final int PLCTAG_ERR_MUTEX_INIT      = (-15);
    public static final int PLCTAG_ERR_MUTEX_LOCK      = (-16);
    public static final int PLCTAG_ERR_MUTEX_UNLOCK    = (-17);
    public static final int PLCTAG_ERR_NOT_ALLOWED     = (-18);
    public static final int PLCTAG_ERR_NOT_FOUND       = (-19);
    public static final int PLCTAG_ERR_NOT_IMPLEMENTED = (-20);
    public static final int PLCTAG_ERR_NO_DATA         = (-21);
    public static final int PLCTAG_ERR_NO_MATCH        = (-22);
    public static final int PLCTAG_ERR_NO_MEM          = (-23);
    public static final int PLCTAG_ERR_NO_RESOURCES    = (-24);
    public static final int PLCTAG_ERR_NULL_PTR        = (-25);
    public static final int PLCTAG_ERR_OPEN            = (-26);
    public static final int PLCTAG_ERR_OUT_OF_BOUNDS   = (-27);
    public static final int PLCTAG_ERR_READ            = (-28);
    public static final int PLCTAG_ERR_REMOTE_ERR      = (-29);
    public static final int PLCTAG_ERR_THREAD_CREATE   = (-30);
    public static final int PLCTAG_ERR_THREAD_JOIN     = (-31);
    public static final int PLCTAG_ERR_TIMEOUT         = (-32);
    public static final int PLCTAG_ERR_TOO_LARGE       = (-33);
    public static final int PLCTAG_ERR_TOO_SMALL       = (-34);
    public static final int PLCTAG_ERR_UNSUPPORTED     = (-35);
    public static final int PLCTAG_ERR_WINSOCK         = (-36);
    public static final int PLCTAG_ERR_WRITE           = (-37);
    public static final int PLCTAG_ERR_PARTIAL         = (-38);
    public static final int PLCTAG_ERR_BUSY            = (-39);



    /**
     * Create a new tag based on the passed attributed string.  The attributes
     * are protocol-specific.  The only required part of the string is the key-
     * value pair "protocol=XXX" where XXX is one of the supported protocol
     * types.
     *
     * Wait for timeout milliseconds for the tag to finish the creation process.
     * If this is zero, return immediately.  The application program will need to
     * poll the tag status with plc_tag_status() while the status is PLCTAG_STATUS_PENDING
     * until the status changes to PLCTAG_STATUS_OK if the creation was successful or
     * another PLCTAG_ERR_xyz if it was not.
     *
     * An opaque integer handle is returned. If the value is greater than zero, then
     * the operation was a success.  If the value is less than zero then the
     * tag was not created and the failure error is one of the PLCTAG_ERR_xyz
     * errors.
     *
     * @param attributes - the tag attributes in a string.
     * @param timeout - the number of milliseconds to wait for the tag to be created.  If
     *     this value is zero, then the function will return immediately and the application
     *     must call status() to wait for the tag to complete.
     */

    public Tag(String attributes, int timeout) {
    	tag_id = Tag.plc_tag_create(attributes, timeout);
    }

    private static native int plc_tag_create(String attrib_str, int timeout);


    /**
     * Close the tag.
     *
     * This provides programatic control over the resources uses in the native library.  Note that
     * @return a status code of the operation.
     */

    public int close() {
        int rc = Tag.PLCTAG_ERR_NULL_PTR;

        // destroy the underlying tag, if any.
        if(this.tag_id > 0) {
            rc = Tag.plc_tag_destroy(this.tag_id);
        }

        // make sure no one uses this again.
        this.tag_id = Tag.PLCTAG_ERR_NULL_PTR;

        return rc;
    }

    private static native int plc_tag_destroy(int tag_id);

    /**
     * Decode the passed status code into a string.
     *
     * @param rc a result/status code
     * @return a String indicating the error type.
     */

    public static String decodeError(int rc) {
    	return Tag.plc_tag_decode_error(rc);
    }

    private static native String plc_tag_decode_error(int rc);



    /**
     * abort
     *
     * Abort any outstanding IO to the PLC.  If there is something in flight, then
     * it is marked invalid.  Note that this does not abort anything that might
     * be still processing in the report PLC.
     *
     * @return The return value will be PLCTAG_STATUS_OK unless there is an error such as
     * a null pointer.
     */

     public int abort() {
        return Tag.plc_tag_abort(this.tag_id);
    }

    private static native int plc_tag_abort(int tag_id);


   /**
    * read
    *
    * Start a read of the tag in the PLC.
    *
    * @param timeout If the timeout value is not zero, then wait timeout milliseconds until the read
    * completes or the timeout occurs, whichever is first.  Return the status.   If the timeout is
    * zero then the function returns as soon as the read request is queued within the library.
    *
    * @return If the timeout value is zero, then plc_tag_read will normally return
    * PLCTAG_STATUS_PENDING.  Unless there is an error.
    */

    public int read(int timeout) {
        return Tag.plc_tag_read(this.tag_id, timeout);
    }

   private static native int plc_tag_read(int tag_id, int timeout);



   /**
    * getStatus
    *
    * Return the current status of the tag.  This will be PLCTAG_STATUS_PENDING if there is
    * an uncompleted IO operation.  It will be PLCTAG_STATUS_OK if everything is fine.  Other
    * errors will be returned as appropriate.
    *
    * @return Returns one of the tag statuses above PLCTAG_STATUS_OK, ... PLCTAG_ERR_NULL_PTR...
    */

    public int getStatus() {
        return Tag.plc_tag_status(this.tag_id);
    }
    private static native int plc_tag_status(int tag_id);


   /**
    * write
    *
    * Start a write of the tag in the PLC.
    *
    * @param timeout If the timeout value is not zero, then wait timeout milliseconds until the write
    * completes or the timeout occurs, whichever is first.  Return the status.   If the timeout is
    * zero then the function returns as soon as the write request is queued within the library.
    *
    * @return If the timeout value is zero, then write will normally return
    * PLCTAG_STATUS_PENDING.  Unless there is an error.
    */

    public int write(int timeout) {
        return Tag.plc_tag_write(this.tag_id, timeout);
    }

    private static native int plc_tag_write(int tag_id, int timeout);

    // debug levels
    public static final int PLCTAG_DEBUG_NONE          = (0);
    public static final int PLCTAG_DEBUG_ERROR         = (1);
    public static final int PLCTAG_DEBUG_WARN          = (2);
    public static final int PLCTAG_DEBUG_INFO          = (3);
    public static final int PLCTAG_DEBUG_DETAIL        = (4);
    public static final int PLCTAG_DEBUG_SPEW          = (5);

    private static final int PLCTAG_DEBUG_LAST         = (6);

    public static void setDebugLevel(int level) {
        if(level >= 0 && level < PLCTAG_DEBUG_LAST) {
            Tag.plc_tag_set_debug_level(level);
        }
    }

    private static native void plc_tag_set_debug_level(int debug_level);



    /**
    * Check that the library supports the required API version.
    *
    * The version is passed as integers.   The three arguments are:
    *
    * @param major_ver - the major version of the library.  This must be an exact match.
    * @param minor_ver - the minor version of the library.   The library must have a minor
    *             version greater than or equal to the requested version.
    * @param patch_ver - the patch version of the library.   The library must have a patch
    *             version greater than or equal to the requested version if the minor
    *             version is the same as that requested.   If the library minor version
    *             is greater than that requested, any patch version will be accepted.
    *
    * @return true is returned if the version is compatible.  If it does not,
    * false is returned.
    *
    * Examples:
    *
    * To match version 2.1.4, call Tag.checkLibraryVersion(2, 1, 4).
    */

    public static boolean checkLibraryVersion(int major_ver, int minor_ver, int patch_ver) {
    	int res = Tag.plc_tag_check_lib_version(major_ver, minor_ver, patch_ver);

    	if(res == Tag.PLCTAG_STATUS_OK) {
    		return true;
    	} else {
    		return false;
    	}
    }

    private static native int plc_tag_check_lib_version(int req_major, int req_minor, int req_patch);


    /**
     * getLibraryIntAttribute
     *
     * Get an attribute of the library as an integer.
     *
     * @param attrib the name of the attribute to get.
     * @param default_val the value to return if the attribute is not found.
     * @return the integer value of the attribute or the default value if not found.
     */
	public static int getLibraryIntAttribute(String attrib, int default_val) {
		return Tag.plc_tag_get_int_attribute(0, attrib, default_val);
	}

    /**
     * getIntAttribute
     *
     * Get an attribute of the tag as an integer.
     *
     * @param attrib the name of the attribute to get.
     * @param default_val the value to return if the attribute is not found.
     * @return the integer value of the attribute or the default value if not found.
     */

     public int getIntAttribute(String attrib, int default_val) {
		return Tag.plc_tag_get_int_attribute(this.tag_id, attrib, default_val);
	}

    private static native int plc_tag_get_int_attribute(int tag_id, String attrib_name, int default_value);


    /**
     * setLibraryIntAttribute
     *
     * Set an attribute of the library as an integer.
     *
     * @param attrib the name of the attribute to get.
     * @param new_val the new value for the attribute.
     * @return the status of the operation.   PLCTAG_STATUS_OK if all went well.  PLCTAG_ERR_UNSUPPORTED
     *     will be returned if the attribute name is not found.
     */

	public static int setLibraryIntAttribute(String attrib, int new_val) {
		return Tag.plc_tag_set_int_attribute(0, attrib, new_val);
	}


    /**
     * setIntAttribute
     *
     * Set an attribute of the tag as an integer.
     *
     * @param attrib the name of the attribute to get.
     * @param new_val the new value for the attribute.
     * @return the status of the operation.   PLCTAG_STATUS_OK if all went well.  PLCTAG_ERR_UNSUPPORTED
     *     will be returned if the attribute name is not found.
     */

	public int setIntAttribute(String attrib, int new_val) {
		return Tag.plc_tag_set_int_attribute(this.tag_id, attrib, new_val);
	}

    private static native int plc_tag_set_int_attribute(int tag_id, String attrib_name, int new_value);


    /**
     * size
     *
     * Get the size of the tag in bytes.   This is the size of the internal buffer for the tag.
     *
     * @return return the size of the tag in bytes.  Will return negative values for errors.
     */

    public int size() {
        return Tag.plc_tag_get_size(this.tag_id);
    }

    private static native int plc_tag_get_size(int tag_id);


    /* data routines */

    /**
     * getBit
     *
     * Get the bit at the specified bit offset.
     *
     * @param bit_offset
     * @return The bit value as 0 or 1, or an error (negative) if there was a problem.
     */

    public int getBit(int bit_offset) {
    	return Tag.plc_tag_get_bit(this.tag_id, bit_offset);
    }

    private static native int plc_tag_get_bit(int tag_id, int offset_bit);


    /**
     * setBit
     *
     * set the bit at the specified bit offset to the passed value.
     *
     * @param bit_offset - the bit within the bytes of the tag.
     * @param new_val if new_val is zero then the bit will be set to zero, otherwise the bit will
     *     be set to one.
     * @return PLCTAG_STATUS_OK or an error (negative) if there was a problem.
     */

    public int setBit(int bit_offset, int new_val) {
    	return Tag.plc_tag_set_bit(this.tag_id, bit_offset, new_val);
    }

    private static native int plc_tag_set_bit(int tag_id, int offset_bit, int val);


    /**
     * getInt64
     *
     * Return a signed 64-bit value from the given offset in the tag buffer.
     *
     * @param offset the byte offset at which to start getting the value.
     * @return INT64_MAX if there is an error otherwise the value.
     */

     public long getInt64(int offset) {
        return Tag.plc_tag_get_int64(this.tag_id, offset);
    }

    private static native long plc_tag_get_int64(int tag_id, int offset);


    public int setInt64(int offset, long val) {
        return Tag.plc_tag_set_int64(this.tag_id, offset, val);
    }

    private static native int plc_tag_set_int64(int tag_id, int offset, long val);


    /**
     * getUInt32
     *
     * Return an unsigned 32-bit value from the given offset in the tag buffer.
     *
     * Note that Java does not have unsigned values that can be used here.   We
     * fake this by using a long and adding an offset to negative values.
     *
     * @param offset
     * @return UINT32_MAX if there is an error otherwise the value.
     */
    public long getUInt32(int offset) {
        long tmp_res = Tag.plc_tag_get_uint32(this.tag_id, offset);

        if(tmp_res < 0) {
            tmp_res += 0x100000000L;
        }

        return tmp_res;
    }

    private static native int plc_tag_get_uint32(int tag_id, int offset);


    public int setUInt32(int offset, long val) {
        return Tag.plc_tag_set_uint32(this.tag_id, offset, (int)val);
    }

    private static native int plc_tag_set_uint32(int tag_id, int offset, int val);



    public int getInt32(int offset) {
        return Tag.plc_tag_get_int32(this.tag_id, offset);
    }

    private static native int plc_tag_get_int32(int tag_id, int offset);


    public int setInt32(int offset, int val) {
        return Tag.plc_tag_set_int32(this.tag_id, offset, val);
    }

    private static native int plc_tag_set_int32(int plc_tag1, int offset, int val);



    public int getUInt16(int offset) {
        return Tag.plc_tag_get_uint16(this.tag_id, offset);
    }

    private static native short plc_tag_get_uint16(int tag_id, int offset);

    public int setUInt16(int offset, int val) {
        return Tag.plc_tag_set_uint16(this.tag_id, offset, (short)val);
    }

    private static native int plc_tag_set_uint16(int tag_id, int offset, short val);



    public int getInt16(int offset) {
        return Tag.plc_tag_get_int16(this.tag_id, offset);
    }

    private static native short plc_tag_get_int16(int tag_id, int offset);

    public int setInt16(int offset, int val) {
        return Tag.plc_tag_set_int16(this.tag_id, offset, (short)val);
    }

    private static native int plc_tag_set_int16(int plc_tag1, int offset, short val);



    public int getUInt8(int offset) {
        return Tag.plc_tag_get_uint8(this.tag_id, offset);
    }

    private static native byte plc_tag_get_uint8(int tag_id, int offset);

    public int setUInt8(int offset, int val) {
        return Tag.plc_tag_set_uint8(this.tag_id, offset, (byte)val);
    }

    private static native int plc_tag_set_uint8(int tag_id, int offset, byte val);



    public int getInt8(int offset) {
        return Tag.plc_tag_get_int8(this.tag_id, offset);
    }

    private static native byte plc_tag_get_int8(int tag_id, int offset);

    public int setInt8(int offset, int val) {
        return Tag.plc_tag_set_int8(this.tag_id, offset, (byte)val);
    }

    private static native int plc_tag_set_int8(int plc_tag1, int offset, byte val);



    public double getFloat64(int offset) {
        return Tag.plc_tag_get_float64(this.tag_id, offset);
    }

    private static native double plc_tag_get_float64(int tag_id, int offset);

    public int setFloat64(int offset, double val) {
        return Tag.plc_tag_set_float64(this.tag_id, offset, val);
    }

    private static native int plc_tag_set_float64(int tag_id, int offset, double val);


    public float getFloat32(int offset) {
        return Tag.plc_tag_get_float32(this.tag_id, offset);
    }

    private static native float plc_tag_get_float32(int tag_id, int offset);

    public int setFloat32(int offset, float fVal) {
        return Tag.plc_tag_set_float32(this.tag_id, offset, fVal);
    }

    private static native int plc_tag_set_float32(int tag_id, int offset, float val);



    /**
    * shutdownLibrary
    *
    * Some systems may not call the atexit() handlers.  In those cases, wrappers should
    * call this function before unloading the library or terminating.   Most OSes will cleanly
    * recover all system resources when a process is terminated and this will not be necessary.
    *
    * THIS IS NOT THREAD SAFE!   Do not call this if you have multiple threads running against
    * the library.  You have been warned.   Close all tags first with plc_tag_destroy() and make
    * sure that nothing can call any library functions until this function returns.
    *
    * Normally you do not need to call this function.   This is only for certain wrappers or
    * operating environments that use libraries in ways that prevent the normal exit handlers
    * from working.
    */

    public static void shutdownLibrary() {
        Tag.plc_tag_shutdown();
    }

    private static native void plc_tag_shutdown();


    /**
     * registerEventCallback
     *
     * This function registers the passed callback function with the tag.  Only one callback function
     * may be registered on a tag at a time!
     *
     * Once registered, any of the following operations on or in the tag will result in the callback
     * being called:
     *
     *      * starting a tag read operation.
     *      * a tag read operation ending.
     *      * a tag read being aborted.
     *      * starting a tag write operation.
     *      * a tag write operation ending.
     *      * a tag write being aborted.
     *      * a tag being destroyed
     *
     * The callback is called outside of the internal tag mutex so it can call any tag functions safely.   However,
     * the callback is called in the context of the internal tag helper thread and not the client library thread(s).
     * This means that YOU are responsible for making sure that all client application data structures the callback
     * function touches are safe to access by the callback!
     *
     * Do not do any operations in the callback that block for any significant time.   This will cause library
     * performance to be poor or even to start failing!
     *
     * When the callback is called with the PLCTAG_EVENT_DESTROY_STARTED, do not call any tag functions.  It is
     * not guaranteed that they will work and they will possibly hang or fail.
     *
     * Return values:
     *
     * If there is already a callback registered, the function will return PLCTAG_ERR_DUPLICATE.   Only one callback
     * function may be registered at a time on each tag.
     *
     * If all is successful, the function will return PLCTAG_STATUS_OK.
     */


    public static final int PLCTAG_EVENT_READ_STARTED      = (1);
    public static final int PLCTAG_EVENT_READ_COMPLETED    = (2);
    public static final int PLCTAG_EVENT_WRITE_STARTED     = (3);
    public static final int PLCTAG_EVENT_WRITE_COMPLETED   = (4);
    public static final int PLCTAG_EVENT_ABORTED           = (5);
    public static final int PLCTAG_EVENT_DESTROYED         = (6);

    public interface EventCallbackInterface extends Callback {
        public void invoke(int tag_id, int event, int status);
    }

    // Keep a reference here so that GC does not get it.
    private EventCallbackInterface eventCallback;

    public int registerEventCallback(EventCallbackInterface callback) {
        int rc = Tag.plc_tag_register_callback(this.tag_id, callback);

        if(rc == Tag.PLCTAG_STATUS_OK) {
            this.eventCallback = callback;
        }

        return rc;
    }

    private static native int plc_tag_register_callback(int tag_id, EventCallbackInterface callback);


    /*
    * unregisterEventCallback
    *
    * This function removes the callback already registered on the tag.
    *
    * Return values:
    *
    * The function returns PLCTAG_STATUS_OK if there was a registered callback and removing it went well.
    * An error of PLCTAG_ERR_NOT_FOUND is returned if there was no registered callback.
    */

    public int unregisterEventCallback() {
        this.eventCallback = null;
        return Tag.plc_tag_unregister_callback(this.tag_id);
    }

    private static native int plc_tag_unregister_callback(int tag_id);




    /**
    * registerLogger
    *
    * This function registers the passed callback function with the _library_.  Only one callback function
    * may be registered with the library at a time!
    *
    * Once registered, the function will be called with any logging message that is normally printed due
    * to the current log level setting.
    *
    * WARNING: the callback will usually be called when the internal tag API mutex is held.   You cannot
    * call any tag functions within the callback!
    *
    * Return values:
    *
    * If there is already a callback registered, the function will return PLCTAG_ERR_DUPLICATE.   Only one callback
    * function may be registered at a time on each tag.
    *
    * If all is successful, the function will return PLCTAG_STATUS_OK.
    */

    public interface LoggingCallbackInterface extends Callback {
        public void invoke(int tag_id, int debugLevel, String msg);
    }

    /* as with the event callback, we need to keep this to prevent GC from getting it. */
    private static LoggingCallbackInterface loggingCallback;

    public static synchronized int registerLoggerCallback(LoggingCallbackInterface callback) {
        int rc = Tag.plc_tag_register_logger(callback);

        if(rc == Tag.PLCTAG_STATUS_OK) {
            Tag.loggingCallback = callback;
        }

        return rc;
    }

    private static native int plc_tag_register_logger(LoggingCallbackInterface callback);


    /*
    * unregisterLogger
    *
    * This function removes the logger callback already registered for the library.
    *
    * Return values:
    *
    * The function returns PLCTAG_STATUS_OK if there was a registered callback and removing it went well.
    * An error of PLCTAG_ERR_NOT_FOUND is returned if there was no registered callback.
    */

    public static synchronized int unregisterLogger() {
        Tag.loggingCallback = null;
        return Tag.plc_tag_unregister_logger();
    }

    private static native int plc_tag_unregister_logger();




    // The only piece of instance state.
    private int tag_id = 0;



    /**
     * finalize
     *
     * This should never do anything.  But, just in case close() is not
     * called first, this will catch the problem and free up the
     * tag's memory, eventually.
     *
     * Note that finalize() is deprecated!
     */
    protected void finalize() {
        if(tag_id > 0) {
            close();
        }
    }

}
