package org.dom4j.swing;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;
import org.dom4j.Branch;
import org.dom4j.CharacterData;
import org.dom4j.Node;

public class BranchTreeNode extends LeafTreeNode {
    protected List children;

    public BranchTreeNode(TreeNode parent, Branch xmlNode) {
        super(parent, xmlNode);
    }

    public BranchTreeNode(Branch xmlNode) {
        super(xmlNode);
    }

    public Enumeration children() {
        return new Enumeration() {
            private int index;

            {
                this.index = -1;
            }

            public boolean hasMoreElements() {
                return this.index + 1 < BranchTreeNode.this.getChildCount();
            }

            public Object nextElement() {
                BranchTreeNode branchTreeNode = BranchTreeNode.this;
                int i = this.index + 1;
                this.index = i;
                return branchTreeNode.getChildAt(i);
            }
        };
    }

    protected List createChildList() {
        Branch branch = getXmlBranch();
        int size = branch.nodeCount();
        List childList = new ArrayList(size);
        int i = 0;
        while (i < size) {
            Node node = branch.node(i);
            if (node instanceof CharacterData) {
                String text = node.getText();
                if (text != null) {
                }
                i++;
            }
            childList.add(createChildTreeNode(node));
            i++;
        }
        return childList;
    }

    protected TreeNode createChildTreeNode(Node xmlNode) {
        return xmlNode instanceof Branch ? new BranchTreeNode(this, (Branch) xmlNode) : new LeafTreeNode(this, xmlNode);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) getChildList().get(childIndex);
    }

    public int getChildCount() {
        return getChildList().size();
    }

    protected List getChildList() {
        if (this.children == null) {
            this.children = createChildList();
        }
        return this.children;
    }

    public int getIndex(TreeNode node) {
        return getChildList().indexOf(node);
    }

    protected Branch getXmlBranch() {
        return (Branch) this.xmlNode;
    }

    public boolean isLeaf() {
        return getXmlBranch().nodeCount() <= 0;
    }

    public String toString() {
        return this.xmlNode.getName();
    }
}