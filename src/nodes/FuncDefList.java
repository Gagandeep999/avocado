package nodes;

import visitor.visitor;

public class FuncDefList extends node {

    public FuncDefList(String data){
        super(data);
    }

    public FuncDefList(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
