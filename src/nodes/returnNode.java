package nodes;

import visitor.visitor;

public class returnNode extends node {

    public returnNode(String data){
        super(data);
    }

    public returnNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
