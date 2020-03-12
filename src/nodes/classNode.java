package nodes;

import visitor.visitor;

public class classNode extends node {

    public classNode(String data){
        super(data);
    }

    public classNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
