package nodes;

import visitor.visitor;

public class ClassNode extends node {

    public ClassNode(String data){
        super(data);
    }

    public ClassNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
