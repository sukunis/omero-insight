package org.openmicroscopy.shoola.agents.fsimporter.mde.util.inout;

import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleTreeElement;
import org.w3c.dom.Element;

import javax.swing.tree.DefaultMutableTreeNode;

public class ModuleTreeParser {
    final String ATTR_TYPE="Type";
    final String ATTR_ID="ID";
    public Element createXMLElement(){return null;}

    public ModuleTreeElement getModuleTreeElement(Element eElem,DefaultMutableTreeNode parent)
    {
        String type = eElem.getAttribute(ATTR_TYPE);
        String index = eElem.getAttribute(ATTR_ID);
        ModuleContentParser mc_parser = new ModuleContentParser();
        return new ModuleTreeElement(type,null,index,mc_parser.parseFromConfig(eElem,type,true,null),parent);
    }
}
