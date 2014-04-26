package net.i2p.crypto.eddsa.math;

import java.math.BigInteger;

/**
 * A particular element of the field \Z/(2^255-19).
 * @author str4d
 *
 */
public class FieldElement {
    final Field f;

    final BigInteger bi;

    public FieldElement(Field f, BigInteger bi) {
        this.f = f;
        this.bi = bi;
    }

    /**
     * Decode a FieldElement from its (b-1)-bit encoding.
     * The highest bit is masked out.
     * @param val the (b-1)-bit encoding of a FieldElement.
     * @return the FieldElement represented by 'val'.
     */
    public FieldElement(Field f, byte[] val) {
        if (val.length != f.getb()/8)
            throw new IllegalArgumentException("Not a valid encoding");

        // Convert 'val' to big endian
        byte[] out = new byte[val.length];
        for (int i = 0; i < val.length; i++) {
            out[i] = val[val.length-1-i];
        }

        this.f = f;
        this.bi = new BigInteger(1, out).and(f.getMask());
    }

    /**
     * Encode a FieldElement in its (b-1)-bit encoding.
     * @return the (b-1)-bit encoding of this FieldElement.
     */
    public byte[] toByteArray() {
        byte[] in = bi.and(f.getMask()).toByteArray();
        byte[] out = new byte[f.getb()/8];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[in.length-1-i];
        }
        for (int i = in.length; i < out.length; i++) {
            out[i] = 0;
        }
        return out;
    }

    public boolean isNonZero() {
        return bi.compareTo(BigInteger.ZERO) != 0;
    }

    /**
     * From the Ed25519 paper:
     * x is negative if the (b-1)-bit encoding of x is lexicographically larger
     * than the (b-1)-bit encoding of -x. If q is an odd prime and the encoding
     * is the little-endian representation of {0, 1,..., q-1} then the negative
     * elements of F_q are {1, 3, 5,..., q-2}.
     * @return
     */
    public boolean isNegative() {
        return bi.testBit(0);
    }

    public FieldElement add(FieldElement val) {
        return new FieldElement(f, bi.add(val.bi).mod(f.getQ()));
    }

    public FieldElement addOne() {
        return new FieldElement(f, bi.add(Constants.ONE).mod(f.getQ()));
    }

    public FieldElement subtract(FieldElement val) {
        return new FieldElement(f, bi.subtract(val.bi).mod(f.getQ()));
    }

    public FieldElement subtractOne() {
        return new FieldElement(f, bi.subtract(Constants.ONE).mod(f.getQ()));
    }

    public FieldElement negate() {
        return new FieldElement(f, f.getQ().subtract(bi));
    }
    
    public FieldElement divide(FieldElement val) {
    	return divide(val.bi);
    }
    public FieldElement divide(BigInteger val) {
    	return new FieldElement(f, bi.divide(val).mod(f.getQ()));
    }

    public FieldElement multiply(FieldElement val) {
        return new FieldElement(f, bi.multiply(val.bi).mod(f.getQ()));
    }

    public FieldElement square() {
        return modPow(BigInteger.valueOf(2), f.getQ());
    }

    public FieldElement squareAndDouble() {
        return square().multiply(new FieldElement(f, Constants.TWO));
    }

    public FieldElement invert() {
        return modPow(f.getQm2(), f.getQ());
    }

    public FieldElement modPow(BigInteger e, BigInteger m) {
        return new FieldElement(f, bi.modPow(e, m));
    }
    
    public FieldElement pow(BigInteger i){
    	return modPow(i, f.getQ());
    }
    
    public FieldElement pow(FieldElement e){
    	return pow(e.bi);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FieldElement))
            return false;
        FieldElement fe = (FieldElement) obj;
        return bi.equals(fe.bi);
    }
    
    @Override
    public String toString() {
    	return "[FieldElement val="+bi+"]";
    }
}
