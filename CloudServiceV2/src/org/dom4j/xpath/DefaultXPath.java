package org.dom4j.xpath;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.InvalidXPathException;
import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.XPath;
import org.dom4j.XPathException;
import org.jaxen.FunctionContext;
import org.jaxen.JaxenException;
import org.jaxen.NamespaceContext;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.VariableContext;
import org.jaxen.dom4j.Dom4jXPath;

public class DefaultXPath implements XPath, NodeFilter, Serializable {
    private NamespaceContext namespaceContext;
    private String text;
    private org.jaxen.XPath xpath;

    class AnonymousClass_1 implements Comparator {
        final /* synthetic */ Map val$sortValues;

        AnonymousClass_1(Map map) {
            this.val$sortValues = map;
        }

        public int compare(Object o1, Object o2) {
            o1 = this.val$sortValues.get(o1);
            o2 = this.val$sortValues.get(o2);
            if (o1 == o2) {
                return 0;
            }
            if (o1 instanceof Comparable) {
                return ((Comparable) o1).compareTo(o2);
            }
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }
            return !o1.equals(o2) ? -1 : 0;
        }
    }

    public DefaultXPath(String text) throws InvalidXPathException {
        this.text = text;
        this.xpath = parse(text);
    }

    protected static org.jaxen.XPath parse(String text) {
        try {
            return new Dom4jXPath(text);
        } catch (JaxenException e) {
            throw new InvalidXPathException(text, e.getMessage());
        } catch (RuntimeException e2) {
            throw new InvalidXPathException(text);
        }
    }

    public boolean booleanValueOf(Object context) {
        try {
            setNSContext(context);
            return this.xpath.booleanValueOf(context);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return false;
        }
    }

    public Object evaluate(Object context) {
        try {
            setNSContext(context);
            List answer = this.xpath.selectNodes(context);
            return (answer == null || answer.size() != 1) ? answer : answer.get(0);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return null;
        }
    }

    protected Object getCompareValue(Node node) {
        return valueOf(node);
    }

    public FunctionContext getFunctionContext() {
        return this.xpath.getFunctionContext();
    }

    public NamespaceContext getNamespaceContext() {
        return this.namespaceContext;
    }

    public String getText() {
        return this.text;
    }

    public VariableContext getVariableContext() {
        return this.xpath.getVariableContext();
    }

    protected void handleJaxenException(Exception exception) throws XPathException {
        throw new XPathException(this.text, exception);
    }

    public boolean matches(Node node) {
        boolean z = false;
        try {
            setNSContext(node);
            List answer = this.xpath.selectNodes(node);
            if (answer == null || answer.size() <= 0) {
                return z;
            }
            Object item = answer.get(0);
            return item instanceof Boolean ? ((Boolean) item).booleanValue() : answer.contains(node);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return z;
        }
    }

    public Number numberValueOf(Object context) {
        try {
            setNSContext(context);
            return this.xpath.numberValueOf(context);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return null;
        }
    }

    protected void removeDuplicates(List list, Map sortValues) {
        HashSet distinctValues = new HashSet();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object value = sortValues.get(iter.next());
            if (distinctValues.contains(value)) {
                iter.remove();
            } else {
                distinctValues.add(value);
            }
        }
    }

    public List selectNodes(Object context) {
        try {
            setNSContext(context);
            return this.xpath.selectNodes(context);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return Collections.EMPTY_LIST;
        }
    }

    public List selectNodes(Object context, XPath sortXPath) {
        List answer = selectNodes(context);
        sortXPath.sort(answer);
        return answer;
    }

    public List selectNodes(Object context, XPath sortXPath, boolean distinct) {
        List answer = selectNodes(context);
        sortXPath.sort(answer, distinct);
        return answer;
    }

    public Object selectObject(Object context) {
        return evaluate(context);
    }

    public Node selectSingleNode(Object context) {
        Object obj = null;
        try {
            setNSContext(context);
            Object answer = this.xpath.selectSingleNode(context);
            if (answer instanceof Node) {
                return (Node) answer;
            }
            if (answer == null) {
                return obj;
            }
            throw new XPathException("The result of the XPath expression is not a Node. It was: " + answer + " of type: " + answer.getClass().getName());
        } catch (JaxenException e) {
            handleJaxenException(e);
            return obj;
        }
    }

    public void setFunctionContext(FunctionContext functionContext) {
        this.xpath.setFunctionContext(functionContext);
    }

    protected void setNSContext(Object context) {
        if (this.namespaceContext == null) {
            this.xpath.setNamespaceContext(DefaultNamespaceContext.create(context));
        }
    }

    public void setNamespaceContext(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
        this.xpath.setNamespaceContext(namespaceContext);
    }

    public void setNamespaceURIs(Map map) {
        setNamespaceContext(new SimpleNamespaceContext(map));
    }

    public void setVariableContext(VariableContext variableContext) {
        this.xpath.setVariableContext(variableContext);
    }

    public void sort(List list) {
        sort(list, false);
    }

    protected void sort(List list, Map sortValues) {
        Collections.sort(list, new AnonymousClass_1(sortValues));
    }

    public void sort(List list, boolean distinct) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            Map sortValues = new HashMap(size);
            int i = 0;
            while (i < size) {
                Object object = list.get(i);
                if (object instanceof Node) {
                    Node node = (Node) object;
                    sortValues.put(node, getCompareValue(node));
                }
                i++;
            }
            sort(list, sortValues);
            if (distinct) {
                removeDuplicates(list, sortValues);
            }
        }
    }

    public String toString() {
        return "[XPath: " + this.xpath + "]";
    }

    public String valueOf(Object context) {
        try {
            setNSContext(context);
            return this.xpath.stringValueOf(context);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return "";
        }
    }
}