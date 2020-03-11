package nodes;

import visitor.visitor;

public class FuncDeclNode extends node {

    public FuncDeclNode(String data){
        super(data);
    }

    public FuncDeclNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
