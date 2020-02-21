/*
 * Copyright (C) <2019> University of Dundee & Open Microscopy Environment.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openmicroscopy.shoola.agents.fsimporter.mde.util.export;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * import input form values and object tree structure from a xml file .
 * <MDETemplate>
 *     <ObjectPre ID=<ObjectID> Name=<ObjectName>>
 *         <TagData../>
 *         <!--Child objects-->
 *         <ObjectPre ID=<ObjectID> Name=<ObjectName>>
 *             <TagData.../>
 *         </ObjectPre>
 *         ....
 *     </ObjectPre>
 *     ....
 * </MDETemplate>
 * @author Susanne Kunis<susannekunis at gmail dot com>
 */
public class ImportFromTemplateFile {
    public ImportFromTemplateFile(String fName) {
    }

    //public void import(DefaultMutableTreeNode tree, List<String> filter){ }
}
