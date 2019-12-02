package org.openmicroscopy.shoola.env.ui;

import java.io.File;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.model.DownloadArchivedActivityParam;
import org.openmicroscopy.shoola.env.data.model.DownloadProjectActivityParam;

import omero.gateway.SecurityContext;

public class DownloadProjectActivity 	
	extends ActivityComponent
	{

		/** The description of the activity when finished. */
		private static final String DESCRIPTION_CREATED = "Project " +
				"downloaded : Dataset ";
		
		/** The description of the activity when cancelled. */
		private static final String DESCRIPTION_CANCEL = "Download Project " +
				"cancelled";
		
		/** The description of the activity when no archived files found. */
		private static final String DESCRIPTION_NO_ARCHIVED = "No Original " +
				"Images available";
		
		/** The description of the activity when no archived files found. */
		private static final String OPTION_NO_ARCHIVED = "You can download the " +
				"Images as OME-TIFF";
		
		/** The parameters to download. */
		private DownloadProjectActivityParam parameters; 
		
		/**
		 * Creates a new instance.
		 * 
		 * @param viewer The viewer this data loader is for.
		 * Mustn't be <code>null</code>.
	     * @param registry Convenience reference for subclasses.
	     * @param ctx The security context.
		 * @param parameters The object hosting information about the original
		 * image.
		 */
		DownloadProjectActivity(UserNotifier viewer, Registry registry,
				SecurityContext ctx, DownloadProjectActivityParam parameters) 
		{
			super(viewer, registry, ctx);
			if (parameters == null)
				throw new IllegalArgumentException("No parameters");
			this.parameters = parameters;
			setNumberOfFiles(parameters.getNumImages());
			initialize("Downloading Project: Dataset "+parameters.getLocation().getName(), 
					parameters.getIcon());
			File f = parameters.getLocation();
			if (f.isFile() || !f.exists()) f = f.getParentFile();
			messageLabel.setText("in "+f.getAbsolutePath());
			this.parameters = parameters;
		}
		
		/**
		 * Creates a concrete loader.
		 * @see ActivityComponent#createLoader()
		 */
		protected UserNotifierLoader createLoader()
		{
		    File f = parameters.getLocation();
			loader = new ArchivedLoader(viewer, registry, ctx,
			        parameters.getImages(), f, parameters.isOverride(), parameters.isZip(), 
			        parameters.isKeepOriginalPaths(), this);
			return loader;
		}

		/**
		 * Modifies the text of the component. 
		 * @see ActivityComponent#notifyActivityCancelled()
		 */
		protected void notifyActivityCancelled()
		{
			type.setText(DESCRIPTION_CANCEL);
		}

		/**
		 * Modifies the text of the component.
		 * @see ActivityComponent#notifyActivityEnd()
		 */
		protected void notifyActivityEnd()
		{
		    List<File> files = (List<File>) result;
		    //Handle no file returned.
		    if (CollectionUtils.isEmpty(files)) {
		        type.setText(DESCRIPTION_NO_ARCHIVED);
		        messageLabel.setText(OPTION_NO_ARCHIVED);
		        return;
		    }
		    type.setText(DESCRIPTION_CREATED+" "+parameters.getLocation().getName());
		    StringBuffer buffer = new StringBuffer();
		    buffer.append("as ");
		    buffer.append(parameters.getLocation().getAbsolutePath());
		    messageLabel.setText(buffer.toString());
		}

		/** 
		 * No-operation in this case.
		 * @see ActivityComponent#notifyActivityError()
		 */
		protected void notifyActivityError() {}

		private void setNumberOfFiles(int size){
			numOfFiles=size;
}

}
