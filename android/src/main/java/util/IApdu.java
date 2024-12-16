/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package util;

/**
 * Apdu interface
 * 
 */
public interface IApdu {

    /**
     * APDU request. CLA(1B) + INS(1B) + P1(1B) + P2(1B) + Lc(0, 1 or 2B) + data(Lc) + Le(0, 1 or 2B). NOTE: By default,
     * Lc is not present if length of request data is 0, call {@link IApduReq#setLcAlwaysPresent()} to make it always
     * present(i.e. Lc is 0 if data length is 0); By default, Le is present, call {@link IApduReq#setLeNotPresent()} to
     * make it not present; By default, Lc and Le occupies 1 byte if present, call
     * {@link IApduReq#setLengthOfLcLeTo2Bytes()} to change it to 2 bytes when needed.
     */
    public interface IApduReq {

        /**
         * By default, Lc &amp; Le occupies 1 byte. By calling this method, change it to 2 bytes
         */
        public void setLengthOfLcLeTo2Bytes();

        /**
         * By default, Lc is not present if length of request data is 0. By calling this method, Lc always present(i.e.
         * Lc is 0 if data length is 0)
         */
        public void setLcAlwaysPresent();

        /**
         * By default, Le is always present. By calling this method, Le is not present
         */
        public void setLeNotPresent();

        public void setCla(byte cla);

        public byte getCla();

        public void setIns(byte ins);

        public byte getIns();

        public void setP1(byte p1);

        public byte getP1();

        public void setP2(byte p2);

        public byte getP2();

        public void setData(byte[] data);

        public byte[] getData();

        public void setLe(short le);

        public short getLe();

        /**
         * pack APDU request
         * 
         * @return packed bytes
         */
        public byte[] pack();
    }

    /**
     * APDU response. data + SW1(1B) + SW2(1B)
     */
    public interface IApduResp {
        public byte[] getData();

        public short getStatus();

        public String getStatusString();
    }

    /**
     * constructor, P1 = 0, P2 = 0, Lc = 0, data = null, Le = 0x00(1B) or 0xFFFF(2B)
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins);

    /**
     * constructor, P1 = 0, P2 = 0, Le = 0x00(1B) or 0xFFFF(2B)
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @param data
     *            [input] data
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins, byte[] data);

    /**
     * constructor, P1 = 0, P2 = 0,
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @param data
     *            [input] data
     * @param le
     *            Le
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins, byte[] data, short le);

    /**
     * constructor, Lc = 0, data = null, Le = 0x00(1B) or 0xFFFF(2B)
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @param p1
     *            P1
     * @param p2
     *            P2
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins, byte p1, byte p2);

    /**
     * constructor, Le = 0x00(1B) or 0xFFFF(2B)
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @param p1
     *            P1
     * @param p2
     *            P2
     * @param data
     *            [input] data
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins, byte p1, byte p2, byte[] data);

    /**
     * constructor
     * 
     * @param cla
     *            CLA
     * @param ins
     *            INS
     * @param p1
     *            P1
     * @param p2
     *            P2
     * @param data
     *            [input] data
     * @param le
     *            Le
     * @return APDU request
     */
    public IApduReq createReq(byte cla, byte ins, byte p1, byte p2, byte[] data, short le);

    /**
     * unpack APDU response
     * 
     * @param resp
     *            [input] apdu response data bytes
     * @return IApdueResp interface
     */
    public IApduResp unpack(byte[] resp);

}
