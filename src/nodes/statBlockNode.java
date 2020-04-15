package nodes;

import visitor.visitor;

public class StatBlockNode extends node {

    public StatBlockNode(String data){
        super(data);
    }

    public StatBlockNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
