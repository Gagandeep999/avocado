package nodes;

import visitor.visitor;

public class varDeclNode extends node {

    public varDeclNode(String data){
        super(data);
    }

    public varDeclNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
