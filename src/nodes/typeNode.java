package nodes;

import visitor.visitor;

public class typeNode extends node {

    public typeNode(String data){
        super(data);
    }

    public typeNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
