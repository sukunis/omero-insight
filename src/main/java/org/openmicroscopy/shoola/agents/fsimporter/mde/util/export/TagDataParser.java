package org.openmicroscopy.shoola.agents.fsimporter.mde.util.export;

import org.openmicroscopy.shoola.agents.fsimporter.mde.util.TagData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TagDataParser {
    final String ATTR_ID="ID";
    final String ATTR_NAME="Name";
    final String ATTR_DEFAULT_VAL="DefaultValues";
    final String ATTR_VALUE="Value";
    final String ATTR_VISIBLE="Visible";
    final String ATTR_UNIT="Unit";
    final String ATTR_TYPE="Type";

    /**
     * Builds {@link TagData} element with his properties as attributes.
     * {@code
     * <TagData Name="" Type="" Visible="" Value="" Unit="" DefaultValues="">
     * }
     * @param t
     * @param doc
     * @param elemName
     * @return
     */
    public Element createXMLElem(TagData t, Document doc, String elemName) {
        if(t==null) {
            return null;
        }
        Element result=doc.createElement(elemName);

        result.setAttribute(ATTR_NAME, t.getTagName());
        result.setAttribute(ATTR_TYPE, String.valueOf(t.getTagType()));
        result.setAttribute(ATTR_VISIBLE,String.valueOf( t.isVisible()));
        result.setAttribute(ATTR_VALUE, t.getTagValue());
        result.setAttribute(ATTR_UNIT, t.getTagUnitString());
        result.setAttribute(ATTR_DEFAULT_VAL, t.getDefaultValuesAsString());
        //result.setAttribute(ATTR_REQ,t.isRequired());

        return result;
    }
}
