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

package libplctag.java;

import org.libplctag.Tag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TagTest {
	static final int TIMEOUT = 5000;
	
    @Test public void testTag() {
        // set the debug level.
        Tag.setDebugLevel(Tag.PLCTAG_DEBUG_DETAIL);
       
        Tag tag = new Tag("make=system&family=library&name=debug", TIMEOUT);
        assertEquals("Tag was not created correctly!", tag.status(), Tag.PLCTAG_STATUS_OK);
        
        // write the DEBUG_DETAIL log value.
        assertEquals("Unable to update debug tag!", tag.setInt32(0, Tag.PLCTAG_DEBUG_INFO), Tag.PLCTAG_STATUS_OK);
        
        // write the tag.
        assertEquals("Unable to write debug tag!", tag.write(TIMEOUT), Tag.PLCTAG_STATUS_OK);
        
        // attempt read the DEBUG log value.
        assertEquals("Debug value not set!", Tag.getLibraryAttribute("debug", -1), Tag.PLCTAG_DEBUG_INFO);
        
        tag.close();
    }
}
