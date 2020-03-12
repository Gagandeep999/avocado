package nodes;

import visitor.visitor;

public class funcDeclNode extends node {

    public funcDeclNode(String data){
        super(data);
    }

    public funcDeclNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
