package org.apache.axis.impl.llom.serialize;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.Vector;
import java.util.Stack;


/**
 * Copyright 2001-2004 The Apache Software Foundation.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class StreamingOMSerializer implements XMLStreamConstants {

    private Stack prefixStack = new Stack();
    private Stack nsCountStack = new Stack();

    public void serialize(Object obj, XMLStreamWriter writer) throws XMLStreamException {

        if (!(obj instanceof XMLStreamReader)) {
            throw new UnsupportedOperationException("Unsupported input object. Must be of the the type XMLStreamReader");
        }

        XMLStreamReader node = (XMLStreamReader) obj;
        serializeNode(node, writer);
    }

    protected void serializeNode(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                serializeElement(reader, writer);
            } else if (event == ATTRIBUTE) {
                serializeAttributes(reader, writer);
            } else if (event == CHARACTERS) {
                serializeText(reader, writer);
            } else if (event == COMMENT) {
                serializeComment(reader, writer);
            } else if (event == CDATA) {
                serializeCData(reader, writer);
            } else if (event == END_ELEMENT) {
                serializeEndElement(writer);
            }
            writer.flush();
        }
    }

    /**

     */
    protected void serializeElement(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        int nsPushCount=0;
        String prefix = reader.getPrefix();
        String nameSpaceName = reader.getNamespaceURI();
        if (prefix != null) {
            writer.writeStartElement(prefix, reader.getLocalName(), nameSpaceName);
            if (serializeNamespace(prefix, nameSpaceName,writer)) nsPushCount++;
        } else {
            writer.writeStartElement(nameSpaceName, reader.getLocalName());
            //add the own namespace
            writer.writeDefaultNamespace(nameSpaceName);
        }

        //add attributes
        serializeAttributes(reader, writer);
        //add the namespaces
        int count = reader.getNamespaceCount();
        for (int i = 0; i < count; i++) {
            if (serializeNamespace(reader.getNamespacePrefix(i) ,reader.getNamespaceURI(i), writer)) nsPushCount++;
        }

        nsCountStack.push(new Integer(nsPushCount));

    }

    protected void serializeEndElement(XMLStreamWriter writer) throws XMLStreamException {
        Integer removeCount = (Integer)nsCountStack.pop();
        int count = removeCount.intValue();
        for (int i=0;i<count;i++)
            prefixStack.pop();
        
        writer.writeEndElement();
    }

    /**
     */
    protected void serializeText(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters(reader.getText());
    }

    protected void serializeCData(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCData(reader.getText());
    }


    protected void serializeComment(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeComment(reader.getText());
    }

    /**
     * @param writer
     * @throws XMLStreamException
     */


    protected void serializeAttributes(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {

        int count = reader.getAttributeCount();
        String prefix = null;
        String namespaceName = null;
        for (int i = 0; i < count; i++) {
            prefix = reader.getAttributePrefix(i);
            namespaceName = reader.getAttributeNamespace(i);
            if (prefix != null && !namespaceName.equals("")) {
                writer.writeAttribute(prefix,
                        namespaceName,
                        reader.getAttributeLocalName(i),
                        reader.getAttributeValue(i));
            } else {
                writer.writeAttribute(reader.getAttributeLocalName(i),
                        reader.getAttributeValue(i));
            }
        }
    }

    private boolean serializeNamespace(String prefix,String URI, XMLStreamWriter writer) throws XMLStreamException {
        boolean nsWritten = false;
        if (!prefixStack.contains(prefix)) {
            writer.writeNamespace(prefix,URI );
            prefixStack.push(prefix);
            nsWritten = true;
        }
        return nsWritten;
    }

}