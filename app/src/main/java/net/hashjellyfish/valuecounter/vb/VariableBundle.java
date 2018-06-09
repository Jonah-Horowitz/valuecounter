package net.hashjellyfish.valuecounter.vb;

import android.content.Context;
import android.support.annotation.Nullable;

import net.hashjellyfish.valuecounter.vb.ops.EqualsN;
import net.hashjellyfish.valuecounter.vb.ops.MinusN;
import net.hashjellyfish.valuecounter.vb.ops.Operation;
import net.hashjellyfish.valuecounter.vb.ops.PlusN;
import net.hashjellyfish.valuecounter.vb.ops.TimesN;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class VariableBundle implements Serializable {
    private static final long serialVersionUID = 6353897323909559255L;
    public static final int DEFAULT_LOG_LENGTH = 10;

    private long _id = -1;
    @NotNull private String caption = "";
    private int mainValue = 0;
    @Nullable private Operation<Integer> op1 = null;
    @Nullable private Operation<Integer> op2 = null;
    @Nullable private Operation<Integer> op3 = null;
    private int logLength = DEFAULT_LOG_LENGTH;
    @NotNull private ArrayList<String> log = new ArrayList<>();

    /**
     * Sets the id of this bundle.
     * @param id The new id.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setId(long id) {
        _id = id;
        return this;
    }

    /**
     * Sets the caption of this bundle.
     * @param target The new caption.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setCaption(@NotNull String target) {
        caption = target;
        return this;
    }

    /**
     * Sets the value of this bundle.
     * @param value The new value.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setValue(int value) {
        mainValue = value;
        return this;
    }

    /**
     * Sets one of the <code>Operation</code>s of this bundle.
     * @param pos The position of the new <code>Operation</code>. If outside the range [1,3], nothing happens.
     * @param target The new <code>Operation</code>.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setOp(int pos, @Nullable Operation<Integer> target) {
        switch (pos) {
            case 1: op1 = target; break;
            case 2: op2 = target; break;
            case 3: op3 = target; break;
            default: break;
        }
        return this;
    }

    /**
     * Sets one of the <code>Operation</code>s of this bundle.
     * @param pos The position of the new <code>Operation</code>. If outside the range [1,3], nothing happens.
     * @param desc The description of the new <code>Operation</code>.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setOp(int pos, @Nullable String desc) {
        return setOp(pos, parseOperation(desc));
    }

    /**
     * Sets one of the <code>Operation</code>s of this bundle.
     * @param pos The position of the new <code>Operation</code>. If outside the range [1,3], nothing happens.
     * @param type The type of the new <code>Operation</code>.
     * @param value The numerical value of the new <code>Operation</code>.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setOp(int pos, @Nullable String type, @Nullable String value) {
        return setOp(pos, parseOperation(type, value));
    }

    /**
     * Sets the length of the log associated with this bundle, discarding log entries if necessary.
     * @param length The new length of the log.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setLogLength(int length) {
        logLength = length;
        cullLog();
        return this;
    }

    /**
     * Sets the log associated with this bundle, discarding log entries if necessary.
     * @param newLog The new log.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setLog(@NotNull ArrayList<String> newLog) {
        log = newLog;
        cullLog();
        return this;
    }

    /**
     * Sets the log associated with this bundle, discarding log entries if necessary.
     * @param newLog The new log.
     * @return This bundle.
     */
    @NotNull
    public VariableBundle setLog(@NotNull String[] newLog) {
        return setLog(new ArrayList<String>(Arrays.asList(newLog)));
    }

    /**
     * Retrieves the id of this bundle.
     * @return The id of this bundle.
     */
    public long getId() {
        return _id;
    }

    /**
     * Retrieves the caption of this bundle.
     * @return The caption of this bundle.
     */
    @NotNull
    public String getCaption() {
        return caption;
    }

    /**
     * Retrieves the value of this bundle.
     * @return The value of this bundle.
     */
    public int getValue() {
        return mainValue;
    }

    /**
     * Retrieves an <code>Operation</code> from this bundle.
     * @param pos The position of the <code>Operation</code> to be retrieved.
     * @return The requested <code>Operation</code>, or null if <code>pos</code> is outside the range [1,3].
     */
    @Nullable
    public Operation<Integer> getOperation(int pos) {
        switch (pos) {
            case 1: return op1;
            case 2: return op2;
            case 3: return op3;
            default: return null;
        }
    }

    /**
     * Retrieves the maximum length of the log for this bundle. Note: a result of -1 indicates an infinite maximum length.
     * @return The maximum length of the log for this bundle.
     */
    public int getLogLength() {
        return logLength;
    }

    /**
     * Retrieves the log for this bundle.
     * @return The log for this bundle.
     */
    @NotNull
    public ArrayList<String> getLog() {
        return log;
    }

    /**
     * Creates an <code>Operation</code> of the appropriate type.
     * @param type A <code>String</code> representing the type of <code>Operation</code> returned.
     * @param value A <code>String</code> which can be parsed into an <code>int</code> which represents the <code>Operation</code>'s parameter.
     * @return An instance of a class extending <code>Operation</code>, or null if the <code>type</code> is not recognized.
     */
    @Nullable
    public static Operation<Integer> parseOperation(@Nullable String type, @Nullable String value) {
        if (type==null || value==null) return null;
        int val = value.length()==0 ? 0 : Integer.parseInt(value);
        switch (type) {
            case PlusN.OP_ID: if (val>=0) {
                return new PlusN().setValue(val);
            } else {
                return new MinusN().setValue(-val);
            }
            case MinusN.OP_ID: if (val>=0) {
                return new MinusN().setValue(val);
            } else {
                return new PlusN().setValue(-val);
            }
            case TimesN.OP_ID: return new TimesN().setValue(val);
            case EqualsN.OP_ID: return new EqualsN().setValue(val);
        }
        return null;
    }

    /**
     * Creates an <code>Operation</code> of the appropriate type.
     * @param description The <code>String</code> representation of an <code>Operation</code>, typically obtained by <code>toString()</code>.
     * @return An instance of a class extending <code>Operation</code>, or null if the <code>type</code> is not recognized.
     */
    @Nullable
    public static Operation<Integer> parseOperation(@Nullable String description) {
        if (description==null) return null;
        return parseOperation(description.substring(0,1),description.substring(1));
    }

    /**
     * Creates a deep copy of this <code>VariableBundle</code>.
     * @return The deep copy of this.
     */
    @NotNull
    private VariableBundle makeCopy() {
        VariableBundle v = new VariableBundle();
        v._id = this._id;
        v.caption = this.caption;
        v.mainValue = this.mainValue;
        v.op1 = this.op1==null ? null : this.op1.makeCopy();
        v.op2 = this.op2==null ? null : this.op2.makeCopy();
        v.op3 = this.op3==null ? null : this.op3.makeCopy();
        return v;
    }

    /**
     * Reduces the log to its maximum length. Note: -1 maximum length is interpreted as infinite.
     */
    private void cullLog() {
        if (logLength==-1) return;
        for (int i = log.size(); i > logLength; i--) {
            log.remove(i-1);
        }
    }

    /**
     * Adds the given <code>String</code> to the beginning of this bundle's log.
     * @param newLine The new line to be included.
     */
    public void addToLog(@NotNull String newLine) {
        log.add(0,newLine);
        cullLog();
    }

    /**
     * Applies the appropriate <code>Operation</code> to the value of this bundle.
     * @param pos Which <code>Operation</code> to apply.
     */
    public void applyOperation(int pos) {
        Operation<Integer> op = null;
        switch (pos) {
            case 1: op = op1; break;
            case 2: op = op2; break;
            case 3: op = op3; break;
        }
        if (op!=null) {
            applyOperation(op);
        }
    }

    /**
     * Applies the given <code>Operation</code> to the value of this bundle.
     * @param op The <code>Operation</code> to apply.
     */
    public void applyOperation(@NotNull Operation<Integer> op) {
        addToLog(formatLogEntry(mainValue,op.toString()));
        mainValue = op.apply(mainValue);
    }

    /**
     * Creates a new log entry with the given data.
     * @param oldValue The previous value of the bundle.
     * @param op The operation which was applied, as a <code>String</code>.
     * @return The log entry for the application.
     */
    @NotNull
    public static String formatLogEntry(int oldValue, String op) {
        return String.valueOf(oldValue) + "\t" + op + "\t" + ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
