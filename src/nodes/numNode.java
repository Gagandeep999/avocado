package nodes;

import visitor.visitor;

public class numNode extends node {

    public numNode(String data){
        super(data);
    }

    public numNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
