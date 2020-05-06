package nodes;

import visitor.visitor;

public class idNode extends node {

    public idNode(String data){
        super(data);
    }

    public idNode(String data, node parent){
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
