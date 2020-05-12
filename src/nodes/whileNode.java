package nodes;

import visitor.visitor;

public class whileNode extends node {

    public whileNode(String data){
        super(data);
    }

    public whileNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}