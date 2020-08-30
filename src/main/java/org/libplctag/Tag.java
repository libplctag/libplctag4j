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

import java.io.IOException;

import org.scijava.nativelib.NativeLoader;

import com.sun.jna.*;

public class Tag {
    public static final String JNA_LIBRARY_NAME = "plctag";
    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(Tag.JNA_LIBRARY_NAME);

    static {
    	try {
    		NativeLoader.loadLibrary("plctag");

    		Native.register(Tag.JNA_LIBRARY_NAME);
            if(!Tag.checkLibraryVersion(2, 1, 16)) {
                System.err.println("Library must be compatible with version 2.1.16!");
                System.exit(Tag.PLCTAG_ERR_UNSUPPORTED);
            }
    	} catch(IOException ioException) {
    		System.err.println("Unable to load native library!");
    		System.exit(Tag.PLCTAG_ERR_NOT_FOUND);
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
  

    
    
    public Tag(String attributes, int timeout) {
    	tag_id = plc_tag_create(attributes, timeout);
    }
    
    public int close() {
        int rc = Tag.PLCTAG_ERR_NULL_PTR;

        if(this.tag_id > 0) {
            rc = Tag.plc_tag_destroy(this.tag_id);
        }

        // make sure no one uses this again.
        this.tag_id = 0;
        this.status = Tag.PLCTAG_ERR_NULL_PTR;

        return rc;
    }

    public static String decodeError(int rc) {
    	return Tag.plc_tag_decode_error(rc);
    }


    public int abort() {
        return Tag.plc_tag_abort(this.tag_id);
    }


    public int read(int timeout) {
        return Tag.plc_tag_read(this.tag_id, timeout);
    }

    public int status() {
        // check if we had a create problem.
        if(this.status < 0) {
            return this.status;
        } else {
            return Tag.plc_tag_status(this.tag_id);
        }
    }

    public int write(int timeout) {
        return Tag.plc_tag_write(this.tag_id, timeout);
    }


    /* static helper methods. */

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

	static boolean checkLibraryVersion(int major_ver, int minor_ver, int patch_ver) {
    	int res = Tag.plc_tag_check_lib_version(major_ver, minor_ver, patch_ver);
    	
    	if(res == Tag.PLCTAG_STATUS_OK) {
    		return true;
    	} else {
    		return false;
    	}
    }
	
	public static int getLibraryAttribute(String attrib, int default_val) {
		return Tag.plc_tag_get_int_attribute(0, attrib, default_val);
	}
	
	public static int setLibraryAttribute(String attrib, int new_val) {
		return Tag.plc_tag_set_int_attribute(0, attrib, new_val);
	}
	
	public int getAttribute(String attrib, int default_val) {
		return plc_tag_get_int_attribute(this.tag_id, attrib, default_val);
	}
	
	public int setAttribute(String attrib, int new_val) {
		return Tag.plc_tag_set_int_attribute(this.tag_id, attrib, new_val);
	}

    public int size() {
        return Tag.plc_tag_get_size(this.tag_id);
    }


    /* data routines */
    // Java does not have a 64-bit unsigned type. */
    
    public int getBit(int bit_offset) {
    	return Tag.plc_tag_get_bit(this.tag_id, bit_offset);
    }
    
    public int setBit(int bit_offset, int new_val) {
    	return Tag.plc_tag_set_bit(this.tag_id, bit_offset, new_val);
    }
   

    public long getInt64(int offset) {
        return Tag.plc_tag_get_int64(this.tag_id, offset);
    }

    public int setInt64(int offset, long val) {
        return Tag.plc_tag_set_int64(this.tag_id, offset, val);
    }



    public long getUInt32(int offset) {
        return Tag.plc_tag_get_uint32(this.tag_id, offset);
    }

    public int setUInt32(int offset, long val) {
        return Tag.plc_tag_set_uint32(this.tag_id, offset, (int)val);
    }



    public int getInt32(int offset) {
        return Tag.plc_tag_get_int32(this.tag_id, offset);
    }

    public int setInt32(int offset, int val) {
        return Tag.plc_tag_set_int32(this.tag_id, offset, val);
    }



    public int getUInt16(int offset) {
        return Tag.plc_tag_get_uint16(this.tag_id, offset);
    }

    public int setUInt16(int offset, int val) {
        return Tag.plc_tag_set_uint16(this.tag_id, offset, (short)val);
    }



    public int getInt16(int offset) {
        return Tag.plc_tag_get_int16(this.tag_id, offset);
    }

    public int setInt16(int offset, int val) {
        return Tag.plc_tag_set_int16(this.tag_id, offset, (short)val);
    }



    public int getUInt8(int offset) {
        return Tag.plc_tag_get_uint8(this.tag_id, offset);
    }

    public int setUInt8(int offset, int val) {
        return Tag.plc_tag_set_uint8(this.tag_id, offset, (byte)val);
    }



    public int getInt8(int offset) {
        return Tag.plc_tag_get_int8(this.tag_id, offset);
    }

    public int setInt8(int offset, int val) {
        return Tag.plc_tag_set_int8(this.tag_id, offset, (byte)val);
    }



    public double getFloat64(int offset) {
        return Tag.plc_tag_get_float64(this.tag_id, offset);
    }

    public int setFloat64(int offset, double val) {
        return Tag.plc_tag_set_float64(this.tag_id, offset, val);
    }

    public float getFloat32(int offset) {
        return Tag.plc_tag_get_float32(this.tag_id, offset);
    }

    public int setFloat32(int offset, float fVal) {
        return Tag.plc_tag_set_float32(this.tag_id, offset, fVal);
    }


    /*
     * PRIVATE data and routines below this point!
     */

    private int tag_id = 0;
    private int status = Tag.PLCTAG_STATUS_OK;


    /*
     * Private native functions from the C library. 
     */

        /**
     * decode error codes into strings.
     */

    private static native String plc_tag_decode_error(int rc);



    /**
     * Set the debug level.   See the PLCTAG_DEBUG_xxx values above.
     */

    private static native void plc_tag_set_debug_level(int debug_level);


    /**
    * Check that the library supports the required API version.
    *
    * The version is passed as integers.   The three arguments are:
    *
    * ver_major - the major version of the library.  This must be an exact match.
    * ver_minor - the minor version of the library.   The library must have a minor
    *             version greater than or equal to the requested version.
    * ver_patch - the patch version of the library.   The library must have a patch
    *             version greater than or equal to the requested version if the minor
    *             version is the same as that requested.   If the library minor version
    *             is greater than that requested, any patch version will be accepted.
    *
    * PLCTAG_STATUS_OK is returned if the version is compatible.  If it does not,
    * PLCTAG_ERR_UNSUPPORTED is returned.
    *
    * Examples:
    *
    * To match version 2.1.4, call plc_tag_check_lib_version(2, 1, 4).
    *
    */

    private static native int plc_tag_check_lib_version(int req_major, int req_minor, int req_patch);


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
     * Original signature : <code>int32_t plc_tag_create(const char*, int)</code><br>
     * <i>native declaration : line 75</i>
     */
    private static native int plc_tag_create(String attrib_str, int timeout);


    /**
    * plc_tag_shutdown
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

    private static native void plc_tag_shutdown();


    /**
     * plc_tag_register_callback
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


/********** FIXME
 * This needs to be set up when I figure out how to do callbacks in JNA.
 
    #define PLCTAG_EVENT_READ_STARTED       (1)
    #define PLCTAG_EVENT_READ_COMPLETED     (2)

    #define PLCTAG_EVENT_WRITE_STARTED      (3)
    #define PLCTAG_EVENT_WRITE_COMPLETED    (4)

    #define PLCTAG_EVENT_ABORTED            (5)

    #define PLCTAG_EVENT_DESTROYED          (6)

    private static native int plc_tag_register_callback(int32_t tag_id, void (*tag_callback_func)(int32_t tag_id, int event, int status));
*/

    /*
    * plc_tag_unregister_callback
    *
    * This function removes the callback already registered on the tag.
    *
    * Return values:
    *
    * The function returns PLCTAG_STATUS_OK if there was a registered callback and removing it went well.
    * An error of PLCTAG_ERR_NOT_FOUND is returned if there was no registered callback.
    */

    private static native int plc_tag_unregister_callback(int tag_id);




    /*
    * plc_tag_register_logger
    *
    * This function registers the passed callback function with the library.  Only one callback function
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


/*
 * FIXME
 * This should be enable when I figure out how to do callbacks in JNA.

    private static native int plc_tag_register_logger(void (*log_callback_func)(int32_t tag_id, int debug_level, const char *message));

*/

    /*
    * plc_tag_unregister_logger
    *
    * This function removes the logger callback already registered for the library.
    *
    * Return values:
    *
    * The function returns PLCTAG_STATUS_OK if there was a registered callback and removing it went well.
    * An error of PLCTAG_ERR_NOT_FOUND is returned if there was no registered callback.
    */

    private static native int plc_tag_unregister_logger();




    /**
     * plc_tag_abort
     *
     * Abort any outstanding IO to the PLC.  If there is something in flight, then
     * it is marked invalid.  Note that this does not abort anything that might
     * be still processing in the report PLC.
     *
     * The status will be PLCTAG_STATUS_OK unless there is an error such as
     * a null pointer.
     *
     * This is a function provided by the underlying protocol implementation.
     *
     * Original signature : <code>int plc_tag_abort(int32_t)</code>
     * <i>native declaration : line 98</i>
     */
    private static native int plc_tag_abort(int tag_id);

    /*
    * plc_tag_destroy
    *
    * This frees all resources associated with the tag.  Internally, it may result in closed
    * connections etc.   This calls through to a protocol-specific function.
    *
    * This is a function provided by the underlying protocol implementation.
    *
    * Original signature : <code>int plc_tag_destroy(int32_t)</code>
    * <i>native declaration : line 111</i>
    */
   private static native int plc_tag_destroy(int tag_id);

   /**
    * plc_tag_read
    *
    * Start a read.  If the timeout value is not zero, then wait until the read
    * completes or the timeout occurs, whichever is first.  Return the status.
    *
    * If the timeout value is zero, then plc_tag_read will normally return
    * PLCTAG_STATUS_PENDING.  Unless there is an error.
    *
    * This is a function provided by the underlying protocol implementation.
    *
    * Original signature : <code>int plc_tag_read(int32_t, int)</code>
    * <i>native declaration : line 128</i>
    */
   private static native int plc_tag_read(int tag_id, int timeout);

   /**
    * plc_tag_status
    *
    * Return the current status of the tag.  This will be PLCTAG_STATUS_PENDING if there is
    * an uncompleted IO operation.  It will be PLCTAG_STATUS_OK if everything is fine.  Other
    * errors will be returned as appropriate.
    *
    * This is a function provided by the underlying protocol implementation.
    *
    * Original signature : <code>int plc_tag_status(int32_t)</code>
    * <i>native declaration : line 142</i>
    */
   private static native int plc_tag_status(int tag_id);

   /**
    * plc_tag_write
    *
    * Start a write.  If the timeout value is zero, then wait until the write
    * returns or the timeout occurs, whichever is first.  Return the status.
    *
    * If the timeout value is zero, then plc_tag_write will usually return
    * PLCTAG_STATUS_PENDING.  The write is considered done when it has been
    * written to the socket.
    *
    * This is a function provided by the underlying protocol implementation.
    *
    * Original signature : <code>int plc_tag_write(int32_t, int)</code>
    * <i>native declaration : line 160</i>
    */
   private static native int plc_tag_write(int tag_id, int timeout);


    /**
     * plc_tag_get_int_attribute
     * @param tag_id
     * @param attrib_name
     * @param default_value
     * 
     * Look up the passed attribute and return the integer value.  If the attribute is not
     * found, return the default value.
     * 
     * Note that some attributes are library-level not tag level, and use a tag ID of zero (0).
     * 
     * @return the integer value of the attribute.
     */

    private static native int plc_tag_get_int_attribute(int tag_id, String attrib_name, int default_value);



    /**
     * plc_tag_set_int_attribute
     * @param tag_id
     * @param attrib_name
     * @param new_value
     * 
     * Look up the passed attribute and set the new integer value.
     * 
     * Note that some attributes are library-level not tag level, and use a tag ID of zero (0).
     * 
     * @return PLCTAG_STATUS_OK on success, PLCTAG_ERR_NOT_FOUND/PLCTAG_ERR_UNSUPPORTED when it does not
     * exist or the operation is no supported.
     */

     private static native int plc_tag_set_int_attribute(int tag_id, String attrib_name, int new_value);
    
    

    /**
     * plc_tag_get_size
     *
     * This function returns the size, in bytes, of the data that the tag
     * can read or write.
     *
     * Original signature : <code>int plc_tag_get_size(int32_t)</code>
     * <i>native declaration : line 169</i>
     */
    private static native int plc_tag_get_size(int tag_id);

    /**
     * plc_tag_get_bit
     * 
     * Get the bit at the specified bit offset.   T
     * 
     * @param tag_id
     * @param offset_bit
     * @return The bit value as 0 or 1, or an error (negative) if there was a problem.  
     */

    private static native int plc_tag_get_bit(int tag_id, int offset_bit);


    /**
     * plc_tag_get_bit
     * 
     * Set the bit at the specified offset.   The passed value, val, is used to determine
     * the bit value to set.   A value of zero will result in setting the bit to zero (0).   Any
     * other value will result in the bit being set to one (1).
     * 
     * @param tag_id
     * @param offset_bit
     * @param val
     * @return The bit value as 0 or 1, or an error (negative) if there was a problem.  
     */

    private static native int plc_tag_set_bit(int tag_id, int offset_bit, int val);


    /**
     * Original signature : <code>int64_t plc_tag_get_int64(int32_t, int)</code>
     * <i>native declaration : line 174</i>
     */
    private static native long plc_tag_get_int64(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_int64(int32_t, int, int32_t)</code>
     * <i>native declaration : line 175</i>
     */
    private static native int plc_tag_set_int64(int tag_id, int offset, long val);

    /**
     * plc_tag_get_uint32
     *
     * Return an unsigned 32-bit integer value from the passed offset in the
     * tag's data.  UINT_MAX is returned if the offset is out of bounds.
     *
     * Original signature : <code>uint32_t plc_tag_get_uint32(int32_t, int)</code>
     * <i>native declaration : line 171</i>
     */
    private static native long plc_tag_get_uint32(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_uint32(int32_t, int, uint32_t)</code>
     * <i>native declaration : line 172</i>
     */
    private static native int plc_tag_set_uint32(int tag_id, int offset, int val);

    /**
     * Original signature : <code>int32_t plc_tag_get_int32(int32_t, int)</code>
     * <i>native declaration : line 174</i>
     */
    private static native int plc_tag_get_int32(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_int32(int32_t, int, int32_t)</code>
     * <i>native declaration : line 175</i>
     */
    private static native int plc_tag_set_int32(int plc_tag1, int offset, int val);

    /**
     * Original signature : <code>uint16_t plc_tag_get_uint16(int32_t, int)</code>
     * <i>native declaration : line 178</i>
     */
    private static native short plc_tag_get_uint16(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_uint16(int32_t, int, uint16_t)</code>
     * <i>native declaration : line 179</i>
     */
    private static native int plc_tag_set_uint16(int tag_id, int offset, short val);

    /**
     * Original signature : <code>int16_t plc_tag_get_int16(int32_t, int)</code>
     * <i>native declaration : line 181</i>
     */
    private static native short plc_tag_get_int16(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_int16(int32_t, int, int16_t)</code>
     * <i>native declaration : line 182</i>
     */
    private static native int plc_tag_set_int16(int plc_tag1, int offset, short val);

    /**
     * Original signature : <code>uint8_t plc_tag_get_uint8(int32_t, int)</code>
     * <i>native declaration : line 185</i>
     */
    private static native byte plc_tag_get_uint8(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_uint8(int32_t, int, uint8_t)</code>
     * <i>native declaration : line 186</i>
     */
    private static native int plc_tag_set_uint8(int tag_id, int offset, byte val);

    /**
     * Original signature : <code>int8_t plc_tag_get_int8(int32_t, int)</code>
     * <i>native declaration : line 188</i>
     */
    private static native byte plc_tag_get_int8(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_int8(int32_t, int, int8_t)</code>
     * <i>native declaration : line 189</i>
     */
    private static native int plc_tag_set_int8(int plc_tag1, int offset, byte val);

    /**
     * Original signature : <code>float plc_tag_get_float32(int32_t, int)</code>
     * <i>native declaration : line 192</i>
     */
    private static native double plc_tag_get_float64(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_float32(int32_t, int, float)</code>
     * <i>native declaration : line 193</i>
     */
    private static native int plc_tag_set_float64(int tag_id, int offset, double val);

    /**
     * Original signature : <code>float plc_tag_get_float32(int32_t, int)</code>
     * <i>native declaration : line 192</i>
     */
    private static native float plc_tag_get_float32(int tag_id, int offset);

    /**
     * Original signature : <code>int plc_tag_set_float32(int32_t, int, float)</code>
     * <i>native declaration : line 193</i>
     */
    private static native int plc_tag_set_float32(int tag_id, int offset, float val);


    /**
     * finalize
     *
     * This should never do anything.  But, just in case close() is not
     * called first, this will catch the problem and free up the
     * tag's memory, eventually.
     */
    protected void finalize() {
        if(tag_id > 0) {
            close();
        }
    }

}
