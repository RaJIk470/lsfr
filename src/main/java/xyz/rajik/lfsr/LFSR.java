package xyz.rajik.lfsr;

import javax.swing.text.StyledEditorKit;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LFSR implements Iterator<Boolean> {
    private BitSet state;
    private List<Integer> xorIndexes;
    private Integer size;
    private BitSet key;
    private Integer keySize;

    public LFSR(int initialState, int xorBits, int size) {
        if (xorBits >> (size - 1) > 0) throw new LFSRException("incorrect seed value");
        keySize = 0;
        key = new BitSet();
        state = BitSet.valueOf(new long[]{initialState});
        xorIndexes = new ArrayList<>();
        BitSet.valueOf(new long[]{xorBits}).stream().forEach(xorIndexes::add);
        this.size = size;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Boolean next() {
        Boolean result = state.get(size - 1);
        Boolean xorResult = result;

        System.out.print("key " + keySize + ": ");
        for (Character ch : bitSetToString(state, 24).toCharArray()) {
            System.out.print(ch + " ");
        }

        for (int i = 0; i < xorIndexes.size(); i++)
            xorResult = xorResult ^ state.get(xorIndexes.get(i));


        for (int i = size - 1; i > 0; i--) {
            state.set(i, state.get(i - 1));
        }
        System.out.print(xorResult ? "1" : "0");
        System.out.println();
        state.set(0, xorResult);
        key.set(keySize++, result);
        return result;
    }

    public void next(int n) {
        for (int i = 0; i < n; i++) next();
    }

    @Override
    public String toString() {
        return bitSetToString(state, size);
    }

    public static String bitSetToString(BitSet bitSet, int size) {
        StringBuilder sc = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sc.append(bitSet.get(i) ? '1' : '0');
            /*b.append(bitSet.get(i) ? '1' : '0');
            if ((i + 1) % 8 == 0) {
                sc.append(b.reverse());
                b = new StringBuilder();
            }*/
        }


        return sc.toString();
    }

    public static String byteArrayToBits(byte[] array) {
        return bitSetToString(BitSet.valueOf(array), array.length * 8);
    }

    public String keyToString() {
        return bitSetToString(key, keySize);
    }

    public BitSet getState() {
        return state;
    }

    public void setState(BitSet state) {
        this.state = state;
    }

    public List<Integer> getXorIndexes() {
        return xorIndexes;
    }

    public void setXorIndexes(List<Integer> xorIndexes) {
        this.xorIndexes = xorIndexes;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public BitSet getKey() {
        return key;
    }

    public void setKey(BitSet key) {
        this.key = key;
    }

    public Integer getKeySize() {
        return keySize;
    }

    public void setKeySize(Integer keySize) {
        this.keySize = keySize;
    }
}
