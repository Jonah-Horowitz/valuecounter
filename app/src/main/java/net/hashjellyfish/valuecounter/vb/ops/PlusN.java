package net.hashjellyfish.valuecounter.vb.ops;

public class PlusN implements Operation<Integer,Integer> {
    private int n;

    public PlusN(int aNumber) {
        n=aNumber;
    }

    @Override
    public Integer apply(Integer integer) {
        return integer+n;
    }

    @Override
    public String toString() {
        return "+"+n;
    }

    @Override
    public PlusN makeCopy() {
        return new PlusN(n);
    }
}
