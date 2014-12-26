package org.dom4j.rule;

import java.util.HashMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.rule.pattern.NodeTypePattern;

public class RuleManager {
    private int appearenceCount;
    private HashMap modes;
    private Action valueOfAction;

    class AnonymousClass_1 implements Action {
        final /* synthetic */ Mode val$mode;

        AnonymousClass_1(Mode mode) {
            this.val$mode = mode;
        }

        public void run(Node node) throws Exception {
            if (node instanceof Element) {
                this.val$mode.applyTemplates((Element) node);
            } else if (node instanceof Document) {
                this.val$mode.applyTemplates((Document) node);
            }
        }
    }

    public RuleManager() {
        this.modes = new HashMap();
    }

    protected void addDefaultRule(Mode mode, Pattern pattern, Action action) {
        mode.addRule(createDefaultRule(pattern, action));
    }

    protected void addDefaultRules(Mode mode) {
        Action applyTemplates = new AnonymousClass_1(mode);
        Action valueOf = getValueOfAction();
        addDefaultRule(mode, NodeTypePattern.ANY_DOCUMENT, applyTemplates);
        addDefaultRule(mode, NodeTypePattern.ANY_ELEMENT, applyTemplates);
        if (valueOf != null) {
            addDefaultRule(mode, NodeTypePattern.ANY_ATTRIBUTE, valueOf);
            addDefaultRule(mode, NodeTypePattern.ANY_TEXT, valueOf);
        }
    }

    public void addRule(Rule rule) {
        int i = this.appearenceCount + 1;
        this.appearenceCount = i;
        rule.setAppearenceCount(i);
        Mode mode = getMode(rule.getMode());
        Rule[] childRules = rule.getUnionRules();
        if (childRules != null) {
            int i2 = 0;
            int size = childRules.length;
            while (i2 < size) {
                mode.addRule(childRules[i2]);
                i2++;
            }
        } else {
            mode.addRule(rule);
        }
    }

    public void clear() {
        this.modes.clear();
        this.appearenceCount = 0;
    }

    protected Rule createDefaultRule(Pattern pattern, Action action) {
        Rule rule = new Rule(pattern, action);
        rule.setImportPrecedence(-1);
        return rule;
    }

    protected Mode createMode() {
        Mode mode = new Mode();
        addDefaultRules(mode);
        return mode;
    }

    public Rule getMatchingRule(String modeName, Node node) {
        Mode mode = (Mode) this.modes.get(modeName);
        if (mode != null) {
            return mode.getMatchingRule(node);
        }
        System.out.println("Warning: No Mode for mode: " + mode);
        return null;
    }

    public Mode getMode(String modeName) {
        Mode mode = (Mode) this.modes.get(modeName);
        if (mode != null) {
            return mode;
        }
        mode = createMode();
        this.modes.put(modeName, mode);
        return mode;
    }

    public Action getValueOfAction() {
        return this.valueOfAction;
    }

    public void removeRule(Rule rule) {
        Mode mode = getMode(rule.getMode());
        Rule[] childRules = rule.getUnionRules();
        if (childRules != null) {
            int i = 0;
            int size = childRules.length;
            while (i < size) {
                mode.removeRule(childRules[i]);
                i++;
            }
        } else {
            mode.removeRule(rule);
        }
    }

    public void setValueOfAction(Action valueOfAction) {
        this.valueOfAction = valueOfAction;
    }
}