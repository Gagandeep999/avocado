package nodes;

import visitor.visitor;

public class mainNode extends node {

    public mainNode(String data){
        super(data);
    }

    public mainNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
