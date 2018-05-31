package net.hashjellyfish.valuecounter.vb.ops;

/**
 * Adds a fixed value to the input.
 */
public class PlusN implements Operation<Integer,Integer> {
    private static final long serialVersionUID = 839334479128530865L;
    private int n=0;

    /**
     * Changes the value which is to be added to the input.
     * @param aNumber The new value to be added to the input.
     * @return This operation.
     */
    public PlusN setValue(int aNumber) {
        n = aNumber;
        return this;
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
        return (new PlusN()).setValue(n);
    }

    @Override
    public String opType() {
        return "+";
    }
}
