package visitor;

import nodes.*;

public abstract class visitor {
    public abstract void visit(AddOpNode node);
    public abstract void visit(AssignStatNode node);
    public abstract void visit(ClassListNode node);
    public abstract void visit(ClassNode node);
    public abstract void visit(FuncDeclNode node);
    public abstract void visit(FuncDefListNode node);
    public abstract void visit(FuncDefNode node);
    public abstract void visit(GeneralNode node);
    public abstract void visit(IdNode node);
    public abstract void visit(MultOpNode node);
    public abstract void visit(node node);
    public abstract void visit(NumNode node);
    public abstract void visit(ProgNode node);
    public abstract void visit(StatBlockNode node);
    public abstract void visit(TypeNode node);
    public abstract void visit(VarDeclNode node);
}
