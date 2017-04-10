package cn.edu.njnu.bomb_login_reg;

/**
 * Created by Administrator on 2017/3/24.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncrypt {
    /**
         * 对字符串加密,加密算法使用SHA-256
         *
         * @param strSrc
         *            要加密的字符串
         * @param encName
         *            加密类型
         * @return
         */
    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}