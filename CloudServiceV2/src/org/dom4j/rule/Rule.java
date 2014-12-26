package org.dom4j.rule;

import org.dom4j.Node;
import org.dom4j.NodeType;

public class Rule implements Comparable {
    private Action action;
    private int appearenceCount;
    private int importPrecedence;
    private String mode;
    private Pattern pattern;
    private double priority;

    public Rule() {
        this.priority = 0.5d;
    }

    public Rule(Pattern pattern) {
        this.pattern = pattern;
        this.priority = pattern.getPriority();
    }

    public Rule(Pattern pattern, Action action) {
        this(pattern);
        this.action = action;
    }

    public Rule(Rule that, Pattern pattern) {
        this.mode = that.mode;
        this.importPrecedence = that.importPrecedence;
        this.priority = that.priority;
        this.appearenceCount = that.appearenceCount;
        this.action = that.action;
        this.pattern = pattern;
    }

    public int compareTo(Object that) {
        return that instanceof Rule ? compareTo((Rule) that) : getClass().getName().compareTo(that.getClass().getName());
    }

    public int compareTo(Rule that) {
        int answer = this.importPrecedence - that.importPrecedence;
        if (answer != 0) {
            return answer;
        }
        answer = (int) Math.round(this.priority - that.priority);
        return answer == 0 ? this.appearenceCount - that.appearenceCount : answer;
    }

    public boolean equals(Object that) {
        return that instanceof Rule && compareTo((Rule) that) == 0;
    }

    public Action getAction() {
        return this.action;
    }

    public int getAppearenceCount() {
        return this.appearenceCount;
    }

    public int getImportPrecedence() {
        return this.importPrecedence;
    }

    public final NodeType getMatchType() {
        return this.pattern.getMatchType();
    }

    public final String getMatchesNodeName() {
        return this.pattern.getMatchesNodeName();
    }

    public String getMode() {
        return this.mode;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public double getPriority() {
        return this.priority;
    }

    public Rule[] getUnionRules() {
        Pattern[] patterns = this.pattern.getUnionPatterns();
        if (patterns == null) {
            return null;
        }
        int size = patterns.length;
        Rule[] answer = new Rule[size];
        int i = 0;
        while (i < size) {
            answer[i] = new Rule(this, patterns[i]);
            i++;
        }
        return answer;
    }

    public int hashCode() {
        return this.importPrecedence + this.appearenceCount;
    }

    public final boolean matches(Node node) {
        return this.pattern.matches(node);
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setAppearenceCount(int appearenceCount) {
        this.appearenceCount = appearenceCount;
    }

    public void setImportPrecedence(int importPrecedence) {
        this.importPrecedence = importPrecedence;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public String toString() {
        return super.toString() + "[ pattern: " + getPattern() + " action: " + getAction() + " ]";
    }
}