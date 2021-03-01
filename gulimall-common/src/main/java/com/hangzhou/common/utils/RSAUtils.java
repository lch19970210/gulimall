package com.hangzhou.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.Charset;
import java.security.PrivateKey;

/**
 * @Author linchenghui
 * @Date 2021/3/1
 */
public class RSAUtils {

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAz+OJAbzGZAjPvBkgpcSxcHRzZ+pGSkEPChv3siNL7jEo8GggB0jD+ZD/Kl48uXoS74yWqvTe4x1w6A9NjMCLz/df1KZ7vWPzESrm50Gu8dSINACv/ILJKRK/1wquKdLXgnb2+fLoioyMUwv2A0gnVy6VnMtHSukfbfOgxcoIgQIDAQAB";

    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMDP44kBvMZkCM+8GSClxLFwdHNn6kZKQQ8KG/eyI0vuMSjwaCAHSMP5kP8qXjy5ehLvjJaq9N7jHXDoD02MwIvP91/Upnu9Y/MRKubnQa7x1Ig0AK/8gskpEr/XCq4p0teCdvb58uiKjIxTC/YDSCdXLpWcy0dK6R9t86DFygiBAgMBAAECgYAGIVXxpYgpTUWTx+jgnIShbkWK5HM9Yt+oqRX0jR8JgKbiz1Sa2BXHLVMf2b//Ru0LrGltjOfrgjsNu5ho9csAA3rUSpeM6ovEtt3LApFeW2QzefviPzMqWoAysfMroo1kp0Gr8Yi2NlJhsUtMLjTJhMqeTCk1z/Czn+19VQetMQJBAO7q/vuueUj34Ed2736SLN1GzNePex1ExxzzQZ0PTeXHr+bcmvPx5+eq7vIJn09sxh/2p9PFJ35oMAtXeXkikRUCQQDOmP/mYUeICWJyPvpFtK1wQwIQolKDFlaIzKlAGbuLTJK0AyJ/4f2u+LXzLFbtZohLzRx/0ZWP/kPgciTxNDy9AkA3EOCeH5OCtubxcb83W/eNNKFH2aEVmOGPkZ2A1qMsn76yJgxsx0edBK+4B3G5e6eafF4oGBvHLbpNEudDfS0VAkEAhnCLvcg5+y1YMc4HoMqlPnNsaZSnqwQjRZwg20SjhU9L+/Y6Qgu2wGC6TkbhPlVFhkYwMIEbGXu1gd3laky65QJBANpNMzprvyieEG7w26z0h+rwYcMkWktwIJ8M9QVJwGihADQAPGVG43K2k0JyNIBAiySC7+sx8h47I8NW5Yxoh6o=";

    private static RSA rsa = new RSA(privateKey, publicKey);

    public static String decodeStr(String str){
        byte[] decrypt = rsa.decrypt(str, KeyType.PublicKey);
        String decodeStr = StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
        return decodeStr;
    }

}
