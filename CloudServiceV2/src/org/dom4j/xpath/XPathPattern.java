package org.dom4j.xpath;

import java.util.ArrayList;
import org.dom4j.InvalidXPathException;
import org.dom4j.Node;
import org.dom4j.NodeType;
import org.dom4j.XPathException;
import org.dom4j.rule.Pattern;
import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.SimpleVariableContext;
import org.jaxen.VariableContext;
import org.jaxen.XPathFunctionContext;
import org.jaxen.dom4j.DocumentNavigator;
import org.jaxen.pattern.PatternParser;
import org.jaxen.saxpath.SAXPathException;

public class XPathPattern implements Pattern {
    private Context context;
    private org.jaxen.pattern.Pattern pattern;
    private String text;

    public XPathPattern(String text) {
        this.text = text;
        this.context = new Context(getContextSupport());
        try {
            this.pattern = PatternParser.parse(text);
        } catch (SAXPathException e) {
            throw new InvalidXPathException(text, e.getMessage());
        } catch (RuntimeException e2) {
            throw new InvalidXPathException(text);
        }
    }

    public XPathPattern(org.jaxen.pattern.Pattern pattern) {
        this.pattern = pattern;
        this.text = pattern.getText();
        this.context = new Context(getContextSupport());
    }

    protected ContextSupport getContextSupport() {
        return new ContextSupport(new SimpleNamespaceContext(), XPathFunctionContext.getInstance(), new SimpleVariableContext(), DocumentNavigator.getInstance());
    }

    public NodeType getMatchType() {
        return NodeType.byCode(this.pattern.getMatchType());
    }

    public String getMatchesNodeName() {
        return this.pattern.getMatchesNodeName();
    }

    public double getPriority() {
        return this.pattern.getPriority();
    }

    public String getText() {
        return this.text;
    }

    public Pattern[] getUnionPatterns() {
        org.jaxen.pattern.Pattern[] patterns = this.pattern.getUnionPatterns();
        if (patterns == null) {
            return null;
        }
        int size = patterns.length;
        XPathPattern[] answer = new XPathPattern[size];
        int i = 0;
        while (i < size) {
            answer[i] = new XPathPattern(patterns[i]);
            i++;
        }
        return answer;
    }

    protected void handleJaxenException(Exception exception) throws XPathException {
        throw new XPathException(this.text, exception);
    }

    public boolean matches(Node node) {
        try {
            ArrayList<Node> list = new ArrayList(1);
            list.add(node);
            this.context.setNodeSet(list);
            return this.pattern.matches(node, this.context);
        } catch (JaxenException e) {
            handleJaxenException(e);
            return false;
        }
    }

    public void setVariableContext(VariableContext variableContext) {
        this.context.getContextSupport().setVariableContext(variableContext);
    }

    public String toString() {
        return "[XPathPattern: text: " + this.text + " Pattern: " + this.pattern + "]";
    }
}