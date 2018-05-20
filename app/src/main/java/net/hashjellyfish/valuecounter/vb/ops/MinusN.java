package net.hashjellyfish.valuecounter.vb.ops;

public class MinusN implements Operation<Integer,Integer>, Cloneable {
    private int n;

    public MinusN(int aNumber) {
        n=aNumber;
    }

    @Override
    public Integer apply(Integer target) {
        return target-n;
    }

    @Override
    public String toString() {
        return "-"+n;
    }

    @Override
    public MinusN makeCopy() {
        return new MinusN(n);
    }
}
