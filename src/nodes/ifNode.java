package nodes;

import visitor.visitor;

public class ifNode extends node {

    public ifNode(String data){
        super(data);
    }

    public ifNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
