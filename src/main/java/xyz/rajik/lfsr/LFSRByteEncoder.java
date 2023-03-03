package xyz.rajik.lfsr;

import java.util.BitSet;

public class LFSRByteEncoder implements ByteEncoder {
    public final static Integer DEFAULT_STATE = 0xA0_13_7F;
    public final static Integer DEFAULT_BITS = 0x01_F3_31;
    public final static Integer DEFAULT_SIZE = 24;
    private Integer state;
    private Integer xorBits;
    private Integer size;

    private LFSR lfsr;
    private Integer keySize;

    public LFSRByteEncoder(int state, int xorBits, int size) {
        this.state = state;
        this.xorBits = xorBits;
        this.size = size;
        keySize = 0;
        lfsr = new LFSR(state, xorBits, size);
    }

    public LFSRByteEncoder() {
        this(DEFAULT_STATE, DEFAULT_BITS, DEFAULT_SIZE);
    }

    private void prepareLfsr(Integer length) {
        Integer msgLength = length * 8;
        if (keySize < msgLength) {
            lfsr.next(msgLength - keySize);
            keySize = msgLength;
        }
    }
    @Override
    public byte[] encode(byte[] msg) {
        prepareLfsr(msg.length);
        BitSet key = lfsr.getKey().get(0, msg.length * 8);
        BitSet msgBits = BitSet.valueOf(msg);
        System.out.println("msg: " + msgBits);
        msgBits.xor(key);

        return msgBits.toByteArray();
    }

    @Override
    public byte[] decode(byte[] msg) {
        return encode(msg);
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getXorBits() {
        return xorBits;
    }

    public void setXorBits(Integer xorBits) {
        this.xorBits = xorBits;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LFSR getLfsr() {
        return lfsr;
    }

    public void setLfsr(LFSR lfsr) {
        this.lfsr = lfsr;
    }

    public Integer getKeySize() {
        return keySize;
    }

    public void setKeySize(Integer keySize) {
        this.keySize = keySize;
    }
}
