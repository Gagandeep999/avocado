package nodes;

import visitor.visitor;

public class funcCallNode extends node {

    public funcCallNode(String data){
        super(data);
    }

    public funcCallNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}