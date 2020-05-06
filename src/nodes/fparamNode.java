package nodes;

import visitor.visitor;

public class fparamNode extends node {

    public fparamNode(String data){
        super(data);
    }

    public fparamNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
