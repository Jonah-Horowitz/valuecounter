package net.hashjellyfish.valuecounter.vb.ops;

/**
 * Outputs a fixed value.
 */
public class EqualsN implements Operation<Integer,Integer> {
    private static final long serialVersionUID = -9175502930396860316L;
    private int n=0;

    /**
     * Sets the fixed output value to the given number.
     * @param aNumber The new output value.
     * @return This operation.
     */
    public EqualsN setValue(int aNumber) {
        n = aNumber;
        return this;
    }

    @Override
    public Integer apply(Integer target) {
        return n;
    }

    @Override
    public String toString() {
        return "="+n;
    }

    @Override
    public EqualsN makeCopy() {
        return (new EqualsN()).setValue(n);
    }

    @Override
    public String opType() {
        return "=";
    }
}
