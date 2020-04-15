package nodes;

import visitor.visitor;

public class VarDeclNode extends node {

    public VarDeclNode(String data){
        super(data);
    }

    public VarDeclNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
