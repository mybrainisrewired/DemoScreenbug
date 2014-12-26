package org.dom4j;

public enum NodeType {
    ;
    private static final NodeType[] byCode;
    private final short code;
    private final String name;

    static {
        boolean z;
        if (NodeType.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        ANY_NODE = new NodeType("ANY_NODE", 0, (short) 0, "Node");
        ELEMENT_NODE = new NodeType("ELEMENT_NODE", 1, (short) 1, "Element");
        ATTRIBUTE_NODE = new NodeType("ATTRIBUTE_NODE", 2, (short) 2, "Attribute");
        TEXT_NODE = new NodeType("TEXT_NODE", 3, (short) 3, "Text");
        CDATA_SECTION_NODE = new NodeType("CDATA_SECTION_NODE", 4, (short) 4, "CDATA");
        ENTITY_REFERENCE_NODE = new NodeType("ENTITY_REFERENCE_NODE", 5, (short) 5, "Entity");
        ENTITY_NODE = new NodeType("ENTITY_NODE", 6, (short) 6, "Entity");
        PROCESSING_INSTRUCTION_NODE = new NodeType("PROCESSING_INSTRUCTION_NODE", 7, (short) 7, "ProcessingInstruction");
        COMMENT_NODE = new NodeType("COMMENT_NODE", 8, (short) 8, "Comment");
        DOCUMENT_NODE = new NodeType("DOCUMENT_NODE", 9, (short) 9, "Document");
        DOCUMENT_TYPE_NODE = new NodeType("DOCUMENT_TYPE_NODE", 10, (short) 10, "DocumentType");
        DOCUMENT_FRAGMENT_NODE = new NodeType("DOCUMENT_FRAGMENT_NODE", 11, (short) 11, "DocumentFragment");
        NOTATION_NODE = new NodeType("NOTATION_NODE", 12, (short) 12, "Notation");
        NAMESPACE_NODE = new NodeType("NAMESPACE_NODE", 13, (short) 13, "Namespace");
        UNKNOWN_NODE = new NodeType("UNKNOWN_NODE", 14, (short) 14, "Unknown");
        $VALUES = new NodeType[]{ANY_NODE, ELEMENT_NODE, ATTRIBUTE_NODE, TEXT_NODE, CDATA_SECTION_NODE, ENTITY_REFERENCE_NODE, ENTITY_NODE, PROCESSING_INSTRUCTION_NODE, COMMENT_NODE, DOCUMENT_NODE, DOCUMENT_TYPE_NODE, DOCUMENT_FRAGMENT_NODE, NOTATION_NODE, NAMESPACE_NODE, UNKNOWN_NODE};
        byCode = new NodeType[values().length];
        NodeType[] arr$ = values();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            NodeType nodeType = arr$[i$];
            byCode[nodeType.code] = nodeType;
            i$++;
        }
    }

    private NodeType(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static NodeType byCode(short code) {
        if (code < (short) 0 || code >= byCode.length) {
            return null;
        }
        NodeType nodeType = byCode[code];
        if ($assertionsDisabled || nodeType != null) {
            return nodeType;
        }
        throw new AssertionError();
    }

    public short getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}