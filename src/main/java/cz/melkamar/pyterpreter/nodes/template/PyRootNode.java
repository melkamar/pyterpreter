package cz.melkamar.pyterpreter.nodes.template;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Created by Martin Melka (martin.melka@gmail.com) on 03.11.2017 10:03.
 */
public class PyRootNode extends RootNode {
//    public PyRootNode() {
//        super(TruffleLanguage.class, null, null);
//        children = new ArrayList<>();
//    }

    @Override
    public Object execute(VirtualFrame frame) {
        throw new NotImplementedException();
    }
}
