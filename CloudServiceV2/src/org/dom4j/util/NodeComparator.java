package org.dom4j.util;

import com.wmt.data.LocalAudioAll;
import java.util.Comparator;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;

public class NodeComparator implements Comparator<Node> {

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_NODE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.TEXT_NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.CDATA_SECTION_NODE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ENTITY_REFERENCE_NODE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.PROCESSING_INSTRUCTION_NODE.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.COMMENT_NODE.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.DOCUMENT_TYPE_NODE.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.NAMESPACE_NODE.ordinal()] = 10;
        }
    }

    public int compare(String o1, String o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        return o2 == null ? 1 : o1.compareTo(o2);
    }

    public int compare(Attribute n1, Attribute n2) {
        int answer = compare(n1.getQName(), n2.getQName());
        return answer == 0 ? compare(n1.getValue(), n2.getValue()) : answer;
    }

    public int compare(CharacterData t1, CharacterData t2) {
        return compare(t1.getText(), t2.getText());
    }

    public int compare(Document n1, Document n2) {
        int answer = compare(n1.getDocType(), n2.getDocType());
        return answer == 0 ? compareContent(n1, n2) : answer;
    }

    public int compare(DocumentType o1, DocumentType o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        int answer = compare(o1.getPublicID(), o2.getPublicID());
        if (answer != 0) {
            return answer;
        }
        answer = compare(o1.getSystemID(), o2.getSystemID());
        return answer == 0 ? compare(o1.getName(), o2.getName()) : answer;
    }

    public int compare(Element n1, Element n2) {
        int answer = compare(n1.getQName(), n2.getQName());
        if (answer == 0) {
            int c1 = n1.attributeCount();
            answer = c1 - n2.attributeCount();
            if (answer == 0) {
                int i = 0;
                while (i < c1) {
                    Attribute a1 = n1.attribute(i);
                    answer = compare(a1, n2.attribute(a1.getQName()));
                    if (answer != 0) {
                        return answer;
                    }
                    i++;
                }
                answer = compareContent(n1, n2);
            }
        }
        return answer;
    }

    public int compare(Entity n1, Entity n2) {
        int answer = compare(n1.getName(), n2.getName());
        return answer == 0 ? compare(n1.getText(), n2.getText()) : answer;
    }

    public int compare(Namespace n1, Namespace n2) {
        int answer = compare(n1.getURI(), n2.getURI());
        return answer == 0 ? compare(n1.getPrefix(), n2.getPrefix()) : answer;
    }

    public int compare(Node n1, Node n2) {
        NodeType nodeType1 = n1.getNodeTypeEnum();
        NodeType nodeType2 = n2.getNodeTypeEnum();
        if (nodeType1 != nodeType2) {
            return nodeType1.getCode() - nodeType2.getCode();
        }
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[nodeType1.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                return compare((Element) n1, (Element) n2);
            case ClassWriter.COMPUTE_FRAMES:
                return compare((Document) n1, (Document) n2);
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return compare((Attribute) n1, (Attribute) n2);
            case JsonWriteContext.STATUS_EXPECT_VALUE:
                return compare((Text) n1, (Text) n2);
            case JsonWriteContext.STATUS_EXPECT_NAME:
                return compare((CDATA) n1, (CDATA) n2);
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
                return compare((Entity) n1, (Entity) n2);
            case Type.LONG:
                return compare((ProcessingInstruction) n1, (ProcessingInstruction) n2);
            case Type.DOUBLE:
                return compare((Comment) n1, (Comment) n2);
            case Type.ARRAY:
                return compare((DocumentType) n1, (DocumentType) n2);
            case Type.OBJECT:
                return compare((Namespace) n1, (Namespace) n2);
            default:
                throw new RuntimeException("Invalid node types. node1: " + n1 + " and node2: " + n2);
        }
    }

    public int compare(ProcessingInstruction n1, ProcessingInstruction n2) {
        int answer = compare(n1.getTarget(), n2.getTarget());
        return answer == 0 ? compare(n1.getText(), n2.getText()) : answer;
    }

    public int compare(QName n1, QName n2) {
        int answer = compare(n1.getNamespaceURI(), n2.getNamespaceURI());
        return answer == 0 ? compare(n1.getQualifiedName(), n2.getQualifiedName()) : answer;
    }

    public int compareContent(Branch b1, Branch b2) {
        int c1 = b1.nodeCount();
        int answer = c1 - b2.nodeCount();
        if (answer == 0) {
            int i = 0;
            while (i < c1) {
                answer = compare(b1.node(i), b2.node(i));
                if (answer == 0) {
                    i++;
                }
            }
        }
        return answer;
    }
}