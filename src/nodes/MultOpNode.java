package nodes;

import visitor.visitor;

public class MultOpNode extends node {

    public MultOpNode(String data){
        super(data);
    }

    public MultOpNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
