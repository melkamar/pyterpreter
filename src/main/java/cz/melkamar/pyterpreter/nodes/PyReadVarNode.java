package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.*;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;
import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PyReadVarNode extends PyExpressionNode {
    protected abstract FrameSlot getSlot();

    public String getVarName() {
        return (String) getSlot().getIdentifier();
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
        return readUpStack(Frame::getLong, frame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame frame) throws FrameSlotTypeException {
        return readUpStack(Frame::getBoolean, frame);
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
        return readUpStack(Frame::getObject, frame);
    }

    @Specialization(replaces = {"readLong", "readBoolean", "readObject"})
    protected Object read(VirtualFrame virtualFrame) {
        try {
            return this.readUpStack(Frame::getValue, virtualFrame);
        } catch (FrameSlotTypeException e) {
            throw new NotImplementedException("This should never happen.");
        }
    }

    protected boolean isLong(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Long;
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }

    @Override
    public void print(int indent) {
        printIndented("READ " + getVarName() + " (desc " + getSlot().getFrameDescriptor() + ")", indent);
    }

    private Frame getScope(Frame frame) {
        Object[] args = frame.getArguments();
        if (args == null || args.length == 0) return null;
        return (Frame) args[0];
    }

    public static interface FrameGet<T> {
        public T get(Frame frame, FrameSlot slot) throws FrameSlotTypeException;
    }

    public <T> T readUpStack(FrameGet<T> getter, Frame frame) throws FrameSlotTypeException {
        T value = getter.get(frame, this.getSlot());

        while (value == null) {
            frame = this.getScope(frame);
            if (frame == null) {
                CompilerDirectives.transferToInterpreter();
                throw new UndefinedVariableException(getVarName());
            }
//            value = getter.get(frame, this.getSlot());
            /*
            TODO this part is reallllly slow - but no better idea about how to go through parent frames to find
            a local variable. An attempt was made in branch variable-read-caching, but it was still slow.

            Saving a reference to the scope where the variable was found does not work because of loops -
            the same part of AST can be iterated over multiple times. In the first iteration the variable
            will be cached to refer to its scope A (i.e. AST node will be rewritten, pointing to A) - but during
            a second execution (over another frame, B), the readnode will still point to now-obsolete scope A.

            The dirty solution is to save the parent scope whenever caching a node and always check we are in the
            same scope when cache-reading. That effectively speeds up reads for the slow-down price of equality
            check with EVERY variable read.
            */
            CompilerDirectives.transferToInterpreter();
            FrameSlot slot = frame.getFrameDescriptor().findFrameSlot(getVarName());
            if (slot != null) value = getter.get(frame, slot);
        }
        return value;
    }
}
