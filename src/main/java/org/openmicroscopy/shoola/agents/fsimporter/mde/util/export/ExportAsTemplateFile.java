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

import org.openmicroscopy.shoola.agents.fsimporter.ImporterAgent;
import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleContent;
import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleController;
import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleTreeElement;
import org.openmicroscopy.shoola.agents.fsimporter.mde.configuration.TagNames;
import org.openmicroscopy.shoola.agents.fsimporter.mde.util.TagData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Export input form values and object tree structure to a xml file that can
 * reloaded as template to fill out fields.
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
public class ExportAsTemplateFile {

    final String MDE_TEMPLATE = "MDETemplate";
    final String ELEM_OBJECT_PRE="ObjectPre";
    final String ELEM_TAGDATA="TagData";
    final String ATTR_ID="ID";
    final String ATTR_NAME="Name";
    final String ATTR_DEFAULT_VAL="DefaultValues";
    final String ATTR_VALUE="Value";
    final String ATTR_VISIBLE="Visible";
    final String ATTR_UNIT="Unit";
    final String ATTR_TYPE="Type";

    String fName;
    public ExportAsTemplateFile(String fileName){
        this.fName=fileName;
    }

    public void export(DefaultMutableTreeNode tree,List<String> filter){
        Document doc=generateXML(tree);
        if(doc!=null) {
            saveXML(doc,fName);
        }
    }

    private void saveXML(Document doc,String fileName) {
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(fileName));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging
            transformer.transform(domSource, streamResult);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    private Document generateXML(DefaultMutableTreeNode tree){
        Document document = null;
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            // root element
            Element root = document.createElement(MDE_TEMPLATE);
            document.appendChild(root);

            // get list of available objects for selected setup
            ModuleController controller = ModuleController.getInstance();
            HashMap<String,ModuleContent> list=controller.getAvailableContent();

            Element nodeElem=nodeToXML(tree,document,list);
            if(nodeElem!=null)
                root.appendChild(nodeElem);
        }catch(Exception e){
            ImporterAgent.getRegistry().getLogger().error(this,"[MDE] Cannot generate template xml");
            e.printStackTrace();
        }
        return document;
    }

    private Element nodeToXML(DefaultMutableTreeNode node,Document doc,HashMap<String,ModuleContent> availableCList){
        if(node == null) {
            return null;
        }
        String objectType=((ModuleTreeElement)node.getUserObject()).getType();
        if(!objectType.equals(TagNames.OME_ROOT) && !availableCList.containsKey(objectType)){
            System.out.println("[MDE][Template] skip "+objectType);
            return null;
        }
        ModuleContentParser mc_parser=new ModuleContentParser();
        Element nodeObj = mc_parser.createXMLElem(((ModuleTreeElement)node.getUserObject()).getData(),
                String.valueOf(((ModuleTreeElement)node.getUserObject()).getChildIndex()),doc,ELEM_OBJECT_PRE);

        ImporterAgent.getRegistry().getLogger().debug(null, "\nadd to template "+ node.getUserObject().toString());
        for(int i = 0 ; i < node.getChildCount(); i++) {
            Element childElem = nodeToXML((DefaultMutableTreeNode) node.getChildAt(i), doc,availableCList);
            if(childElem!=null){
                nodeObj.appendChild(childElem);
            }
        }
        return nodeObj;
    }



}
