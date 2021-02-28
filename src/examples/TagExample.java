/***************************************************************************
 *   Copyright (C) 2021 by Kyle Hayes                                      *
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

import io.github.libplctag.Tag;

public class TagExample {
    static final int TIMEOUT = 5000;
    static int lastState = -1;

    public static void main(String argv[]) {
        int rc = Tag.PLCTAG_STATUS_OK;

        // check the library version.
        if(!Tag.checkLibraryVersion(2, 3, 4)) {
            System.err.println("This version of the library does not support the required version of 2.3.4!");
            System.exit(1);
        }

        // set the debug level.
        Tag.setDebugLevel(Tag.PLCTAG_DEBUG_DETAIL);

        // get the library version
        int version_major = Tag.getLibraryIntAttribute("version_major", 0);
        int version_minor = Tag.getLibraryIntAttribute("version_minor", 0);
        int version_patch = Tag.getLibraryIntAttribute("version_patch", 0);

        System.err.println("Using library version " + version_major + "." + version_minor + "." + version_patch + ".");

        // read a PLC-5 tag
        Tag tag = new Tag("protocol=ab-eip&gateway=10.206.1.38&plc=PLC5&elem_size=2&elem_count=1&name=N7:0", TIMEOUT);
        rc = tag.getStatus();
        if(rc != Tag.PLCTAG_STATUS_OK) {
            System.err.println("Unable to create the tag, got error " + Tag.decodeError(rc) + "!");
            System.exit(1);
        }

        // FIXME - one of the two callbacks should show a lambda example.
        Tag.EventCallbackInterface eventCallback = new Tag.EventCallbackInterface(){
            public void invoke(int tag_id, int event, int status) {
                String eventMsg;

                switch(event) {
                    case Tag.PLCTAG_EVENT_ABORTED:
                        eventMsg = "operation aborted";
                        lastState = Tag.PLCTAG_EVENT_ABORTED;
                        break;

                    case Tag.PLCTAG_EVENT_DESTROYED:
                        eventMsg = "being destroyed";
                        lastState = Tag.PLCTAG_EVENT_DESTROYED;
                        break;

                    case Tag.PLCTAG_EVENT_READ_COMPLETED:
                        eventMsg = "read completed";
                        lastState = Tag.PLCTAG_EVENT_READ_COMPLETED;
                        break;

                    case Tag.PLCTAG_EVENT_READ_STARTED:
                        eventMsg = "read started";
                        lastState = Tag.PLCTAG_EVENT_READ_STARTED;
                        break;

                    case Tag.PLCTAG_EVENT_WRITE_COMPLETED:
                        eventMsg = "write completed";
                        lastState = Tag.PLCTAG_EVENT_WRITE_COMPLETED;
                        break;

                    case Tag.PLCTAG_EVENT_WRITE_STARTED:
                        eventMsg = "write started";
                        lastState = Tag.PLCTAG_EVENT_WRITE_STARTED;
                        break;

                    default:
                        eventMsg = "unknown event!";
                        break;
                }

                System.err.println("Tag " + tag_id + " " + eventMsg + " with status " + Tag.decodeError(status) + ".");
            }
        };

        rc = tag.registerEventCallback(eventCallback);
        if(rc != Tag.PLCTAG_STATUS_OK) {
            System.err.println("Unable to register a callback on the tag, got error " + Tag.decodeError(rc) + "!");
            System.exit(1);
        }

        // read the tag.
        rc = tag.read(TIMEOUT);
        if(rc != Tag.PLCTAG_STATUS_OK) {
            System.err.println("Unable to read the tag, got error " + Tag.decodeError(rc) + "!");
            System.exit(1);
        }

        // get the tag element size and count.
        int elem_size = tag.getIntAttribute("elem_size", -1);
        if(elem_size == -1) {
            System.err.println("Unable to get the tag's element size!");
            System.exit(1);
        }

        int elem_count = tag.getIntAttribute("elem_count", -1);
        if(elem_size == -1) {
            System.err.println("Unable to get the tag's element count!");
            System.exit(1);
        }

        // print out the values.
        for(int index = 0; index < elem_count; index++) {
        	int val = 0;
        	
            switch (elem_size) {
                case 1:
                    val = tag.getInt8(index * elem_size);
                    System.err.println("data[" + index + "] = " + val);
                    break;

                case 2:
                    val = tag.getInt16(index * elem_size);
                    System.err.println("data[" + index + "] = " + val);
                    break;

                case 4:
                    val = tag.getInt32(index * elem_size);
                    System.err.println("data[" + index + "] = " + val);
                    break;

                default:
                    System.err.println("Unsupported data size " + elem_size + "!");
                    break;
            }
        }

        // done with the tag.
        tag.unregisterEventCallback();
        tag.close();

        // read a Logix tag
        tag = new Tag("protocol=ab-eip&gateway=10.206.1.39&path=1,5&plc=ControlLogix&elem_count=1&name=TestStringArray", TIMEOUT);
        rc = tag.getStatus();
        if(rc != Tag.PLCTAG_STATUS_OK) {
            System.err.println("Unable to create the tag, got error " + Tag.decodeError(rc) + "!");
            System.exit(1);
        }

        // get the string length
        int str_length = tag.getStringLength(0);
        if(str_length < 0) {
            System.err.println("Unable to get the string length, got error " + Tag.decodeError(str_length) + "!");
            System.exit(1);
        }

        System.err.println("String length " + str_length);

        // get the string
        String str = tag.getString(0);
        if(str == null) {
            rc = tag.getStatus();
            System.err.println("Unable to get the string, got error " + Tag.decodeError(rc) + "!");
            System.exit(1);
        }

        System.err.println("String: \"" + str + "\".");

        // done with the tag
        tag.close();

        System.err.println("Done.");
    }
}
