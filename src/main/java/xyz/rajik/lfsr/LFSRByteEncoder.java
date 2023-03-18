package xyz.rajik.lfsr;

import java.util.BitSet;

public class LFSRByteEncoder implements ByteEncoder {
    public final static Integer DEFAULT_STATE = 0xFF_FF_FF;
    public final static Integer DEFAULT_BITS = 0b100000000000000000001101;
    public final static Integer DEFAULT_SIZE = 24;
    private LFSR lfsr;
    private Integer keySize;

    public LFSRByteEncoder(int state, int xorBits, int size) {
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
        msgBits.xor(key);

        return msgBits.toByteArray();
    }

    @Override
    public byte[] decode(byte[] msg) {
        return encode(msg);
    }

    public LFSR getLfsr() {
        return lfsr;
    }
}
