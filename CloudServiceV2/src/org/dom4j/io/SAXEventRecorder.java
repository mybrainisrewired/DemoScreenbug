package org.dom4j.io;

import android.support.v4.util.TimeUtils;
import com.wmt.data.LocalAudioAll;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class SAXEventRecorder extends DefaultHandler implements LexicalHandler, DeclHandler, DTDHandler, Externalizable {
    private static final String EMPTY_STRING = "";
    private static final byte NULL = (byte) 2;
    private static final byte OBJECT = (byte) 1;
    private static final byte STRING = (byte) 0;
    private static final String XMLNS = "xmlns";
    public static final long serialVersionUID = 1;
    private List events;
    private Map prefixMappings;

    static class SAXEvent implements Externalizable {
        static final byte ATTRIBUTE_DECL = (byte) 17;
        static final byte CHARACTERS = (byte) 8;
        static final byte COMMENT = (byte) 15;
        static final byte ELEMENT_DECL = (byte) 16;
        static final byte END_CDATA = (byte) 14;
        static final byte END_DOCUMENT = (byte) 5;
        static final byte END_DTD = (byte) 10;
        static final byte END_ELEMENT = (byte) 7;
        static final byte END_ENTITY = (byte) 12;
        static final byte END_PREFIX_MAPPING = (byte) 3;
        static final byte EXTERNAL_ENTITY_DECL = (byte) 19;
        static final byte INTERNAL_ENTITY_DECL = (byte) 18;
        static final byte PROCESSING_INSTRUCTION = (byte) 1;
        static final byte START_CDATA = (byte) 13;
        static final byte START_DOCUMENT = (byte) 4;
        static final byte START_DTD = (byte) 9;
        static final byte START_ELEMENT = (byte) 6;
        static final byte START_ENTITY = (byte) 11;
        static final byte START_PREFIX_MAPPING = (byte) 2;
        public static final long serialVersionUID = 1;
        protected byte event;
        protected List parms;

        SAXEvent(byte event) {
            this.event = event;
        }

        void addParm(Object parm) {
            if (this.parms == null) {
                this.parms = new ArrayList(3);
            }
            this.parms.add(parm);
        }

        Object getParm(int index) {
            return (this.parms == null || index >= this.parms.size()) ? null : this.parms.get(index);
        }

        public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
            this.event = in.readByte();
            if (in.readByte() != (byte) 2) {
                this.parms = (List) in.readObject();
            }
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeByte(this.event);
            if (this.parms == null) {
                out.writeByte(ClassWriter.COMPUTE_FRAMES);
            } else {
                out.writeByte(1);
                out.writeObject(this.parms);
            }
        }
    }

    public SAXEventRecorder() {
        this.events = new ArrayList();
        this.prefixMappings = new HashMap();
    }

    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 17);
        saxEvent.addParm(eName);
        saxEvent.addParm(aName);
        saxEvent.addParm(type);
        saxEvent.addParm(valueDefault);
        saxEvent.addParm(value);
        this.events.add(saxEvent);
    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 8);
        saxEvent.addParm(ch);
        saxEvent.addParm(new Integer(start));
        saxEvent.addParm(new Integer(end));
        this.events.add(saxEvent);
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 15);
        saxEvent.addParm(ch);
        saxEvent.addParm(new Integer(start));
        saxEvent.addParm(new Integer(end));
        this.events.add(saxEvent);
    }

    public void elementDecl(String name, String model) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 16);
        saxEvent.addParm(name);
        saxEvent.addParm(model);
        this.events.add(saxEvent);
    }

    public void endCDATA() throws SAXException {
        this.events.add(new SAXEvent((byte) 14));
    }

    public void endDTD() throws SAXException {
        this.events.add(new SAXEvent((byte) 10));
    }

    public void endDocument() throws SAXException {
        this.events.add(new SAXEvent((byte) 5));
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        QName elementName;
        SAXEvent saxEvent = new SAXEvent((byte) 7);
        saxEvent.addParm(namespaceURI);
        saxEvent.addParm(localName);
        saxEvent.addParm(qName);
        this.events.add(saxEvent);
        if (namespaceURI != null) {
            elementName = new QName(localName, Namespace.get(namespaceURI));
        } else {
            elementName = new QName(localName);
        }
        List prefixes = (List) this.prefixMappings.get(elementName);
        if (prefixes != null) {
            Iterator itr = prefixes.iterator();
            while (itr.hasNext()) {
                SAXEvent prefixEvent = new SAXEvent((byte) 3);
                prefixEvent.addParm(itr.next());
                this.events.add(prefixEvent);
            }
        }
    }

    public void endEntity(String name) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 12);
        saxEvent.addParm(name);
        this.events.add(saxEvent);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 3);
        saxEvent.addParm(prefix);
        this.events.add(saxEvent);
    }

    public void externalEntityDecl(String name, String publicId, String sysId) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 19);
        saxEvent.addParm(name);
        saxEvent.addParm(publicId);
        saxEvent.addParm(sysId);
        this.events.add(saxEvent);
    }

    public void internalEntityDecl(String name, String value) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 18);
        saxEvent.addParm(name);
        saxEvent.addParm(value);
        this.events.add(saxEvent);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 1);
        saxEvent.addParm(target);
        saxEvent.addParm(data);
        this.events.add(saxEvent);
    }

    public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
        if (in.readByte() != (byte) 2) {
            this.events = (List) in.readObject();
        }
    }

    public void replay(ContentHandler handler) throws SAXException {
        Iterator itr = this.events.iterator();
        while (itr.hasNext()) {
            SAXEvent saxEvent = (SAXEvent) itr.next();
            switch (saxEvent.event) {
                case LocalAudioAll.SORT_BY_DATE:
                    handler.processingInstruction((String) saxEvent.getParm(0), (String) saxEvent.getParm(1));
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    handler.startPrefixMapping((String) saxEvent.getParm(0), (String) saxEvent.getParm(1));
                    break;
                case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                    handler.endPrefixMapping((String) saxEvent.getParm(0));
                    break;
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    handler.startDocument();
                    break;
                case JsonWriteContext.STATUS_EXPECT_NAME:
                    handler.endDocument();
                    break;
                case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                    AttributesImpl attributes = new AttributesImpl();
                    List attParmList = (List) saxEvent.getParm(JsonWriteContext.STATUS_OK_AFTER_SPACE);
                    if (attParmList != null) {
                        Iterator attsItr = attParmList.iterator();
                        while (attsItr.hasNext()) {
                            String[] attParms = (String[]) attsItr.next();
                            attributes.addAttribute(attParms[0], attParms[1], attParms[2], attParms[3], attParms[4]);
                        }
                    }
                    handler.startElement((String) saxEvent.getParm(0), (String) saxEvent.getParm(1), (String) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES), attributes);
                    break;
                case Type.LONG:
                    handler.endElement((String) saxEvent.getParm(0), (String) saxEvent.getParm(1), (String) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES));
                    break;
                case Type.DOUBLE:
                    handler.characters((char[]) saxEvent.getParm(0), ((Integer) saxEvent.getParm(1)).intValue(), ((Integer) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES)).intValue());
                    break;
                case Type.ARRAY:
                    ((LexicalHandler) handler).startDTD((String) saxEvent.getParm(0), (String) saxEvent.getParm(1), (String) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES));
                    break;
                case Type.OBJECT:
                    ((LexicalHandler) handler).endDTD();
                    break;
                case Opcodes.T_LONG:
                    ((LexicalHandler) handler).startEntity((String) saxEvent.getParm(0));
                    break;
                case Opcodes.FCONST_1:
                    ((LexicalHandler) handler).endEntity((String) saxEvent.getParm(0));
                    break;
                case Opcodes.FCONST_2:
                    ((LexicalHandler) handler).startCDATA();
                    break;
                case Opcodes.DCONST_0:
                    ((LexicalHandler) handler).endCDATA();
                    break;
                case Opcodes.DCONST_1:
                    ((LexicalHandler) handler).comment((char[]) saxEvent.getParm(0), ((Integer) saxEvent.getParm(1)).intValue(), ((Integer) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES)).intValue());
                    break;
                case Segment.TOKENS_PER_SEGMENT:
                    ((DeclHandler) handler).elementDecl((String) saxEvent.getParm(0), (String) saxEvent.getParm(1));
                    break;
                case Opcodes.SIPUSH:
                    ((DeclHandler) handler).attributeDecl((String) saxEvent.getParm(0), (String) saxEvent.getParm(1), (String) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES), (String) saxEvent.getParm(JsonWriteContext.STATUS_OK_AFTER_SPACE), (String) saxEvent.getParm(JsonWriteContext.STATUS_EXPECT_VALUE));
                    break;
                case Opcodes.LDC:
                    ((DeclHandler) handler).internalEntityDecl((String) saxEvent.getParm(0), (String) saxEvent.getParm(1));
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                    ((DeclHandler) handler).externalEntityDecl((String) saxEvent.getParm(0), (String) saxEvent.getParm(1), (String) saxEvent.getParm(ClassWriter.COMPUTE_FRAMES));
                    break;
                default:
                    throw new SAXException("Unrecognized event: " + saxEvent.event);
            }
        }
    }

    public void startCDATA() throws SAXException {
        this.events.add(new SAXEvent((byte) 13));
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 9);
        saxEvent.addParm(name);
        saxEvent.addParm(publicId);
        saxEvent.addParm(systemId);
        this.events.add(saxEvent);
    }

    public void startDocument() throws SAXException {
        this.events.add(new SAXEvent((byte) 4));
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        QName qName;
        SAXEvent saxEvent = new SAXEvent((byte) 6);
        saxEvent.addParm(namespaceURI);
        saxEvent.addParm(localName);
        saxEvent.addParm(qualifiedName);
        if (namespaceURI != null) {
            qName = new QName(localName, Namespace.get(namespaceURI));
        } else {
            qName = new QName(localName);
        }
        if (attributes != null && attributes.getLength() > 0) {
            List attParmList = new ArrayList(attributes.getLength());
            int i = 0;
            while (i < attributes.getLength()) {
                String attLocalName = attributes.getLocalName(i);
                if (attLocalName.startsWith(XMLNS)) {
                    String prefix = attLocalName.length() > 5 ? attLocalName.substring(FragmentManagerImpl.ANIM_STYLE_FADE_EXIT) : EMPTY_STRING;
                    SAXEvent prefixEvent = new SAXEvent((byte) 2);
                    prefixEvent.addParm(prefix);
                    prefixEvent.addParm(attributes.getValue(i));
                    this.events.add(prefixEvent);
                    List prefixes = (List) this.prefixMappings.get(qName);
                    if (prefixes == null) {
                        prefixes = new ArrayList();
                        this.prefixMappings.put(qName, prefixes);
                    }
                    prefixes.add(prefix);
                } else {
                    attParmList.add(new String[]{attributes.getURI(i), attLocalName, attributes.getQName(i), attributes.getType(i), attributes.getValue(i)});
                }
                i++;
            }
            saxEvent.addParm(attParmList);
        }
        this.events.add(saxEvent);
    }

    public void startEntity(String name) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 11);
        saxEvent.addParm(name);
        this.events.add(saxEvent);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        SAXEvent saxEvent = new SAXEvent((byte) 2);
        saxEvent.addParm(prefix);
        saxEvent.addParm(uri);
        this.events.add(saxEvent);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if (this.events == null) {
            out.writeByte(ClassWriter.COMPUTE_FRAMES);
        } else {
            out.writeByte(1);
            out.writeObject(this.events);
        }
    }
}