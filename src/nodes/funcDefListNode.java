package nodes;

import visitor.visitor;

public class funcDefListNode extends node {

    public funcDefListNode(String data){
        super(data);
    }

    public funcDefListNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
