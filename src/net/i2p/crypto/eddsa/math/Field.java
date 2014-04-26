package net.i2p.crypto.eddsa.math;

import java.math.BigInteger;

/**
 * An EdDSA finite field. Includes several pre-computed values.
 * @author str4d
 *
 */
public class Field {
    private int b;
    private BigInteger q;
    /**
     * q-2
     */
    private BigInteger qm2;
    /**
     * q-5
     */
    private BigInteger qm5;
    /**
     * q+3
     */
    private BigInteger qp3;
    /**
     * Mask where only the first b-1 bits are set.
     */
    private BigInteger mask;

    public Field(int b, BigInteger q) {
        this.b = b;
        this.q = q;
        this.qm2 = q.subtract(Constants.TWO);
        this.qm5 = q.subtract(Constants.FIVE);
        this.qp3 = q.add(Constants.THREE);
        this.mask = Constants.ONE.shiftLeft(b-1).subtract(Constants.ONE);
    }

    public int getb() {
        return b;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getQm2() {
        return qm2;
    }

    public BigInteger getQm5() {
        return qm5;
    }

    public BigInteger getQp3() {
        return qp3;
    }

    public BigInteger getMask() {
        return mask;
    }
}
