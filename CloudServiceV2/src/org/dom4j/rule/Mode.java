package org.dom4j.rule;

import com.wmt.data.LocalAudioAll;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.NodeType;

public class Mode {
    private Map<String, RuleSet> attributeNameRuleSets;
    private Map<String, RuleSet> elementNameRuleSets;
    private EnumMap<NodeType, RuleSet> ruleSets;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$dom4j$NodeType;

        static {
            $SwitchMap$org$dom4j$NodeType = new int[NodeType.values().length];
            try {
                $SwitchMap$org$dom4j$NodeType[NodeType.ELEMENT_NODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SwitchMap$org$dom4j$NodeType[NodeType.ATTRIBUTE_NODE.ordinal()] = 2;
        }
    }

    public Mode() {
        this.ruleSets = new EnumMap(NodeType.class);
    }

    public void addRule(Rule rule) {
        NodeType matchType = rule.getMatchType();
        String name = rule.getMatchesNodeName();
        if (name != null) {
            switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[matchType.ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    this.elementNameRuleSets = addToNameMap(this.elementNameRuleSets, name, rule);
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    this.attributeNameRuleSets = addToNameMap(this.attributeNameRuleSets, name, rule);
                    break;
            }
        }
        if (matchType == NodeType.ANY_NODE) {
            Iterator i$ = this.ruleSets.values().iterator();
            while (i$.hasNext()) {
                RuleSet ruleSet = (RuleSet) i$.next();
                if (ruleSet != null) {
                    ruleSet.addRule(rule);
                }
            }
        }
        getRuleSet(matchType).addRule(rule);
    }

    protected Map<String, RuleSet> addToNameMap(Map<String, RuleSet> map, String name, Rule rule) {
        if (map == null) {
            map = new HashMap();
        }
        RuleSet ruleSet = (RuleSet) map.get(name);
        if (ruleSet == null) {
            ruleSet = new RuleSet();
            map.put(name, ruleSet);
        }
        ruleSet.addRule(rule);
        return map;
    }

    public void applyTemplates(Document document) throws Exception {
        int i = 0;
        int size = document.nodeCount();
        while (i < size) {
            fireRule(document.node(i));
            i++;
        }
    }

    public void applyTemplates(Element element) throws Exception {
        int i = 0;
        int size = element.attributeCount();
        while (i < size) {
            fireRule(element.attribute(i));
            i++;
        }
        i = 0;
        size = element.nodeCount();
        while (i < size) {
            fireRule(element.node(i));
            i++;
        }
    }

    public void fireRule(Node node) throws Exception {
        if (node != null) {
            Rule rule = getMatchingRule(node);
            if (rule != null) {
                Action action = rule.getAction();
                if (action != null) {
                    action.run(node);
                }
            }
        }
    }

    public Rule getMatchingRule(Node node) {
        RuleSet ruleSet;
        Rule answer;
        NodeType matchType = node.getNodeTypeEnum();
        switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[matchType.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
                if (this.elementNameRuleSets != null) {
                    ruleSet = this.elementNameRuleSets.get(node.getName());
                    if (ruleSet != null) {
                        answer = ruleSet.getMatchingRule(node);
                        if (answer != null) {
                            return answer;
                        }
                    }
                }
            case ClassWriter.COMPUTE_FRAMES:
                if (this.attributeNameRuleSets != null) {
                    ruleSet = this.attributeNameRuleSets.get(node.getName());
                    if (ruleSet != null) {
                        answer = ruleSet.getMatchingRule(node);
                        if (answer != null) {
                            return answer;
                        }
                    }
                }
        }
        answer = null;
        ruleSet = (RuleSet) this.ruleSets.get(matchType);
        if (ruleSet != null) {
            answer = ruleSet.getMatchingRule(node);
        }
        if (answer != null || matchType == NodeType.ANY_NODE) {
            return answer;
        }
        ruleSet = this.ruleSets.get(matchType);
        return ruleSet != null ? ruleSet.getMatchingRule(node) : answer;
    }

    protected RuleSet getRuleSet(NodeType matchType) {
        RuleSet ruleSet = (RuleSet) this.ruleSets.get(matchType);
        if (ruleSet == null) {
            ruleSet = new RuleSet();
            this.ruleSets.put(matchType, ruleSet);
            if (matchType != NodeType.ANY_NODE) {
                RuleSet allRules = (RuleSet) this.ruleSets.get(matchType);
                if (allRules != null) {
                    ruleSet.addAll(allRules);
                }
            }
        }
        return ruleSet;
    }

    protected void removeFromNameMap(Map<String, RuleSet> map, String name, Rule rule) {
        if (map != null) {
            RuleSet ruleSet = (RuleSet) map.get(name);
            if (ruleSet != null) {
                ruleSet.removeRule(rule);
            }
        }
    }

    public void removeRule(Rule rule) {
        NodeType matchType = rule.getMatchType();
        String name = rule.getMatchesNodeName();
        if (name != null) {
            switch (AnonymousClass_1.$SwitchMap$org$dom4j$NodeType[matchType.ordinal()]) {
                case LocalAudioAll.SORT_BY_DATE:
                    removeFromNameMap(this.elementNameRuleSets, name, rule);
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    removeFromNameMap(this.attributeNameRuleSets, name, rule);
                    break;
            }
        }
        getRuleSet(matchType).removeRule(rule);
        if (matchType == NodeType.ANY_NODE) {
            getRuleSet(NodeType.ANY_NODE).removeRule(rule);
        }
    }
}