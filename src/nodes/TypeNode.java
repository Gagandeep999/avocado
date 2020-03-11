package nodes;

import visitor.visitor;

public class TypeNode extends node {

    public TypeNode(String data){
        super(data);
    }

    public TypeNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
