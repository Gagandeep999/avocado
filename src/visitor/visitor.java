package visitor;

import nodes.*;

public abstract class visitor {
    public abstract void visit(ProgNode node);
    public abstract void visit(ClassListNode node);
    public abstract void visit(FuncDefList node);
    public abstract void visit(ClassNode node);
    public abstract void visit(IdNode node);
    public abstract void visit(FuncDefNode node);
    public abstract void visit(node node);
    public abstract void visit(GeneralNode node);
}
