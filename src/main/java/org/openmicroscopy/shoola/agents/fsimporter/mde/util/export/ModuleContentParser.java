package org.openmicroscopy.shoola.agents.fsimporter.mde.util.export;

import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleContent;
import org.openmicroscopy.shoola.agents.fsimporter.mde.util.TagData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class ModuleContentParser {

    final String ATTR_ID="ID";
    final String ATTR_TYPE="Type";

    final String ELEM_TAGDATA="TagData";



    /**
     * Builds the tag for a certain instrument from {@link ModuleContent} object
     * <pre>{@code
     * <ObjectPre Type="" ATTR_ID="" >
     * 		<TagData...>
     *
     * </ObjectPre>
     * }</pre>
     * @param c    {@link ModuleContent} object holds instrument values
     * @param idx value for attribute id
     * @param doc
     * @param elemName name of generated element
     */
    public Element createXMLElem(ModuleContent c, String idx, Document doc,String elemName) {
        if(c==null) {
            return null;
        }
        Element result = doc.createElement(elemName);
        result.setAttribute(ATTR_ID, idx);
        result.setAttribute(ATTR_TYPE, c.getType());

        List<TagData> list= c.getTagList();
        if(list == null)
            return result;
        //add tagData
        TagDataParser td_parser=new TagDataParser();
        for(int i=0;i<list.size();i++) {
            Element child = td_parser.createXMLElem(list.get(i), doc,ELEM_TAGDATA);
            if(child!=null)
                result.appendChild(child);
        }
        return result;
    }
}
