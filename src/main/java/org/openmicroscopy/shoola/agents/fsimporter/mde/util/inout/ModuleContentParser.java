package org.openmicroscopy.shoola.agents.fsimporter.mde.util.inout;

import org.openmicroscopy.shoola.agents.fsimporter.mde.components.ModuleContent;
import org.openmicroscopy.shoola.agents.fsimporter.mde.util.TagData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedHashMap;
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
    public Element createXMLElem(ModuleContent c, String idx, Document doc,String elemName,boolean saveVal) {
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
            Element child = td_parser.createXMLElem(list.get(i), doc,ELEM_TAGDATA,saveVal);
            if(child!=null)
                result.appendChild(child);
        }
        return result;
    }

    public ModuleContent parseFromConfig(Element eElement,String type,boolean pre,String[] parents) {
        ModuleContent mc = new ModuleContent(
                elementsToTagDataList(eElement.getElementsByTagName(ELEM_TAGDATA), type,pre), type, parents);

        return mc;
    }

    /**
     * Parse list of {@link TagData} from given NodeList
     * {@code
     * <TagData Name="" Type="" Visible="" Value="" Unit="" DefaultValues="">
     * }
     * @param nodeList list of elements TAGDATA
     * @param parent owned object
     * @param pre is true if TagData element is part of predefinitions (objPre), else false (disable value specification for objectConf)
     * @return
     */
    private LinkedHashMap<String,TagData> elementsToTagDataList(NodeList nodeList, String parent, boolean pre){
        if(nodeList==null)
            return null;
        LinkedHashMap<String,TagData> list = new LinkedHashMap<>();
        for(int i=0; i<nodeList.getLength();i++) {
            Node n=nodeList.item(i);
            TagData t=new TagDataParser().parseFromConfig(n,pre,parent);
            if(t!=null){
                list.put(t.getTagName(),t);
            }
        }
        return list;
    }

}
