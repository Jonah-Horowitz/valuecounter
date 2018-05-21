package net.hashjellyfish.valuecounter.vb;

import net.hashjellyfish.valuecounter.vb.ops.MinusN;
import net.hashjellyfish.valuecounter.vb.ops.Operation;
import net.hashjellyfish.valuecounter.vb.ops.PlusN;

import java.util.ArrayList;

public class VariableBundle {
    public String caption = "";
    public int mainValue = 0;
    public Operation<Integer,Integer> op1 = null;
    public Operation<Integer,Integer> op2 = null;
    public Operation<Integer,Integer> op3 = null;

    public VariableBundle() {
        // TODO
    }

    public static ArrayList<VariableBundle> loadTestBundles() {
        ArrayList<VariableBundle> tempList = new ArrayList();
        VariableBundle v1 = new VariableBundle();
        v1.caption = "Test1";
        v1.op1 = new PlusN(3);
        v1.op2 = new MinusN(1);
        v1.op3 = new PlusN(1);
        tempList.add(v1);
        VariableBundle v2 = v1.makeCopy();
        v2.caption = "Test2";
        v2.op1=null;
        tempList.add(v2);
        VariableBundle v3 = v1.makeCopy();
        v3.mainValue=7;
        v3.caption="Test3";
        tempList.add(v3);
        VariableBundle v4 = v1.makeCopy();
        v4.caption = System.getProperty("user.home");
        tempList.add(v4);
        tempList.add(v1.makeCopy());
        return tempList; // TODO
    }

    public static Operation<Integer,Integer> parseOperation(String target) {
        return null; // TODO
    }

    public VariableBundle makeCopy() {
        VariableBundle v = new VariableBundle();
        v.caption = this.caption;
        v.mainValue = this.mainValue;
        v.op1 = this.op1==null ? null : this.op1.makeCopy();
        v.op2 = this.op2==null ? null : this.op2.makeCopy();
        v.op3 = this.op3==null ? null : this.op3.makeCopy();
        return v; // TODO
    }
}
