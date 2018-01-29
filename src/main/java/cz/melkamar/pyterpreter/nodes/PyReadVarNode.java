package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.UndefinedVariableException;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PyReadVarNode extends PyExpressionNode {
    protected abstract FrameSlot getSlot();

    public String getVarName() {
        return (String) getSlot().getIdentifier();
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return FrameUtil.getLongSafe(frame, getSlot());
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, getSlot());
    }

    @Specialization(replaces = {"readLong", "readBoolean"})
    protected Object readObject(VirtualFrame frame) {
        if (!frame.isObject(getSlot())) {
            CompilerDirectives.transferToInterpreter();
            Object result = frame.getValue(getSlot());
            frame.setObject(getSlot(), result);
            return result;
        }

        Object result = FrameUtil.getObjectSafe(frame, getSlot());
        if (result == null){ // null means variable not found, for None there is PyNoneType to avoid clashing
            CompilerDirectives.transferToInterpreter();
            throw new UndefinedVariableException(getVarName());
        }
        return result;
    }

    protected boolean isLong(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Long;
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }

    @Override
    public void print(int indent) {
        printIndented("READ "+getVarName(), indent);
    }
}
