package org.dom4j.rule;

import java.util.ArrayList;
import java.util.Collections;
import org.dom4j.Node;

public class RuleSet {
    private Rule[] ruleArray;
    private ArrayList rules;

    public RuleSet() {
        this.rules = new ArrayList();
    }

    public void addAll(RuleSet that) {
        this.rules.addAll(that.rules);
        this.ruleArray = null;
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
        this.ruleArray = null;
    }

    public Rule getMatchingRule(Node node) {
        Rule[] matches = getRuleArray();
        int i = matches.length - 1;
        while (i >= 0) {
            Rule rule = matches[i];
            if (rule.matches(node)) {
                return rule;
            }
            i--;
        }
        return null;
    }

    protected Rule[] getRuleArray() {
        if (this.ruleArray == null) {
            Collections.sort(this.rules);
            this.ruleArray = new Rule[this.rules.size()];
            this.rules.toArray(this.ruleArray);
        }
        return this.ruleArray;
    }

    public void removeRule(Rule rule) {
        this.rules.remove(rule);
        this.ruleArray = null;
    }

    public String toString() {
        return super.toString() + " [RuleSet: " + this.rules + " ]";
    }
}