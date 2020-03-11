package nodes;

import visitor.visitor;

public class IdNode extends node {

    public IdNode(String data){
        super(data);
    }

    public IdNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
