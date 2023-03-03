package xyz.rajik.lfsr;

public interface ByteEncoder {
    byte[] encode(byte[] msg);
    byte[] decode(byte[] msg);
}
