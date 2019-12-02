/*
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2016 University of Dundee. All rights reserved.
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.treeviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.openmicroscopy.shoola.agents.metadata.MetadataViewerAgent;
import org.openmicroscopy.shoola.agents.treeviewer.DataTreeViewerLoader;
import org.openmicroscopy.shoola.agents.treeviewer.TreeViewerAgent;
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;
import org.openmicroscopy.shoola.agents.util.browser.TreeImageDisplay;

import omero.gateway.SecurityContext;

import org.openmicroscopy.shoola.env.data.views.CallHandle;
import org.openmicroscopy.shoola.env.ui.UserNotifier;

import omero.gateway.model.DataObject;
import omero.gateway.model.DatasetData;
import omero.gateway.model.ImageData;
import omero.gateway.model.ProjectData;

/** 
 * Loads the datasets/images contained in the project hosted by the passed 
 * node. This class calls the <code>loadHierarchy</code> method in the
 * <code>HierarchyBrowsingView</code>. 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * @since OME3.0
 */
public class ProjectsLoader 
	extends org.openmicroscopy.shoola.agents.treeviewer.DataTreeViewerLoader
{

	/** Handle to the asynchronous call so that we can cancel it. */
    private CallHandle  		handle;
    
    /** Reference to the node hosting the project to browse. */
    private TreeImageDisplay 	node;
    
    /** The user's identifier. */
    private long 				userID;
    
    /** Destination directory for download project data. */
    private String downloadPath;
    
    /**
     * Creates a new instance.
     * 
     * @param viewer The viewer this loader is for. Mustn't be <code>null</code>.
     * @param ctx The security context.
     * @param node   The node hosting the project to browse.
     *               Mustn't be <code>null</code>.
     * @param userID The user's identifier.
     * @param downloadPath destination where the data should be stored.
     */
    public ProjectsLoader(TreeViewer viewer, SecurityContext ctx, 
    		TreeImageDisplay node, long userID,String downloadPath)
	{
		super(viewer, ctx);
		if (node == null)
			throw new IllegalArgumentException("No node of reference.");
		this.node = node;
		this.userID = userID;
        this.downloadPath=downloadPath;
	}
	
	 /**
     * Retrieves the data.
     * @see org.openmicroscopy.shoola.agents.treeviewer.DataTreeViewerLoader#load()
     */
    public void load()
    {
    	long userID = TreeViewerAgent.getUserDetails().getId();
    	long id = node.getUserObjectId();
    	List<Long> ids = new ArrayList<Long>();
    	ids.add(id);
    	handle = hiBrwView.loadHierarchy(ctx, ProjectData.class, ids, userID,
    			this);
    }

    /**
     * Cancels the data loading.
     * @see org.openmicroscopy.shoola.agents.treeviewer.DataTreeViewerLoader#cancel()
     */
    public void cancel() { handle.cancel(); }

    /**
     * Feeds the result back to the viewer.
     * @see DataTreeViewerLoader#handleResult(Object)
     */
    public void handleResult(Object result)
    {
        if (viewer.getState() == TreeViewer.DISCARDED) return;  //Async cancel.
        // load data of project
        if(downloadPath==null){
        viewer.browseHierarchyRoots(node, (Collection) result);
        }else{//download data of project
            for (Object o : (Collection) result) {
                if (o instanceof ProjectData) {
                    ProjectData proj = (ProjectData) o;
                    String projPath = downloadPath + "/" + proj.getName();

                    boolean success =true;
                    if(!proj.getDatasets().isEmpty() && !(new File(projPath).exists()))
                        success = (new File(projPath)).mkdir();
                    if(success){
                        for (DatasetData ds : proj.getDatasets()) {
                            String path = projPath + "/" + ds.getName();
                            downloadDataset(ds,path);
    }
                    }else{
                        UserNotifier un = MetadataViewerAgent.getRegistry().getUserNotifier();
                        un.notifyError("Download Project Error", "Can't create directory for "+proj.getName());
                    }
                }
            }
        }
    }
    
    /**
     * Load and download image collection of given dataset.
     * @param ds given dataset
     * @param path download destination
     */
    private void downloadDataset(DatasetData ds, String path)
    {
        List<DataObject> imageSets = new ArrayList<DataObject>();
        List<Long> filesetIds = new ArrayList<Long>();
        Iterator it=ds.getImages().iterator();
        long id;
        ImageData image;
        while(it.hasNext()){
            image=(ImageData) it.next();
            if (image.isArchived()) {
                id = image.getFilesetId();
                if (id < 0) imageSets.add(image);
                else if (!filesetIds.contains(id)) {
                    imageSets.add(image);
                    filesetIds.add(id);
}
            }
        }
        if(!imageSets.isEmpty()){
            // create dataset directory
            File target =new File(path);
            if(!target.exists())
                target.mkdir();
        }

        viewer.downloadImageCollection(path,imageSets,ds.getImages().size());
    }


}
