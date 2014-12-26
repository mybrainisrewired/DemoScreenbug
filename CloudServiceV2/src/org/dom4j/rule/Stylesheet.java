package org.dom4j.rule;

import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

public class Stylesheet {
    private String modeName;
    private RuleManager ruleManager;

    public Stylesheet() {
        this.ruleManager = new RuleManager();
    }

    public void addRule(Rule rule) {
        this.ruleManager.addRule(rule);
    }

    public void applyTemplates(Object input) throws Exception {
        applyTemplates(input, this.modeName);
    }

    public void applyTemplates(Object input, String mode) throws Exception {
        Mode mod = this.ruleManager.getMode(mode);
        int i;
        int size;
        if (input instanceof Element) {
            Element element = (Element) input;
            i = 0;
            size = element.nodeCount();
            while (i < size) {
                mod.fireRule(element.node(i));
                i++;
            }
        } else if (input instanceof Document) {
            Document document = (Document) input;
            i = 0;
            size = document.nodeCount();
            while (i < size) {
                mod.fireRule(document.node(i));
                i++;
            }
        } else if (input instanceof List) {
            List list = (List) input;
            i = 0;
            size = list.size();
            while (i < size) {
                Object object = list.get(i);
                if (object instanceof Element) {
                    applyTemplates((Element) object, mode);
                } else if (object instanceof Document) {
                    applyTemplates((Document) object, mode);
                }
                i++;
            }
        }
    }

    public void applyTemplates(Object input, XPath xpath) throws Exception {
        applyTemplates(input, xpath, this.modeName);
    }

    public void applyTemplates(Object input, XPath xpath, String mode) throws Exception {
        Mode mod = this.ruleManager.getMode(mode);
        Iterator it = xpath.selectNodes(input).iterator();
        while (it.hasNext()) {
            mod.fireRule((Node) it.next());
        }
    }

    public void applyTemplates(Object input, org.jaxen.XPath xpath) throws Exception {
        applyTemplates(input, xpath, this.modeName);
    }

    public void applyTemplates(Object input, org.jaxen.XPath xpath, String mode) throws Exception {
        Mode mod = this.ruleManager.getMode(mode);
        Iterator it = xpath.selectNodes(input).iterator();
        while (it.hasNext()) {
            mod.fireRule((Node) it.next());
        }
    }

    public void clear() {
        this.ruleManager.clear();
    }

    public String getModeName() {
        return this.modeName;
    }

    public Action getValueOfAction() {
        return this.ruleManager.getValueOfAction();
    }

    public void removeRule(Rule rule) {
        this.ruleManager.removeRule(rule);
    }

    public void run(Object input) throws Exception {
        run(input, this.modeName);
    }

    public void run(Object input, String mode) throws Exception {
        if (input instanceof Node) {
            run((Node) input, mode);
        } else if (input instanceof List) {
            run((List) input, mode);
        }
    }

    public void run(List list) throws Exception {
        run(list, this.modeName);
    }

    public void run(List list, String mode) throws Exception {
        int i = 0;
        int size = list.size();
        while (i < size) {
            Object object = list.get(i);
            if (object instanceof Node) {
                run((Node) object, mode);
            }
            i++;
        }
    }

    public void run(Node node) throws Exception {
        run(node, this.modeName);
    }

    public void run(Node node, String mode) throws Exception {
        this.ruleManager.getMode(mode).fireRule(node);
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public void setValueOfAction(Action valueOfAction) {
        this.ruleManager.setValueOfAction(valueOfAction);
    }
}