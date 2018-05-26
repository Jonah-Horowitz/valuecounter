package net.hashjellyfish.valuecounter.vb.ops;

public class TimesN implements Operation<Integer,Integer> {
    private int n;

    public TimesN(int aNumber) {
        n = aNumber;
    }

    @Override
    public Integer apply(Integer integer) {
        return integer*n;
    }

    @Override
    public String toString() {
        return "x"+n;
    }

    @Override
    public TimesN makeCopy() {
        return new TimesN(n);
    }
}
