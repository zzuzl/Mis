package cn.zzuzl.common.util;

import cn.zzuzl.model.Activity;
import cn.zzuzl.model.Student;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public class StringUtil {
    private static final String Algorithm = "DESede";
    private static final String PASSWORD_CRYPT_KEY = "2016zzuzlMis672399171";

    // json转换为activity的数组
    public static List<Activity> json2Activity(String json) throws IOException {
        List<Activity> activityList = new ArrayList<Activity>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Activity[] activities = objectMapper.readValue(json, Activity[].class);
        activityList = Arrays.asList(activities);
        return activityList;
    }

    // 获取当前年份
    public static Integer getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    // 字符串加密
    private static byte[] encryptMode(byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);    //生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm);    //实例化负责加密/解密的Cipher工具类
            c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    // 根据字符串生成秘钥字节数组
    private static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组

        if (key.length > temp.length) {
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    // 解密
    private static byte[] decryptMode(byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 加密字符串，返回加密后的base64字符串
     *
     * @param s 源字符串
     * @return 加密后的base64字符串
     */
    public static String encrypt(String s) {
        String result = "";
        if (s != null) {
            byte[] secretArr = encryptMode(s.getBytes());
            result = Base64.encodeBase64String(secretArr);
        }

        return result;
    }

    /**
     * 解密字符串
     *
     * @param s 加密后的字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String s) {
        if (s != null) {
            byte[] myMsgArr = decryptMode(Base64.decodeBase64(s));
            if (myMsgArr == null) {
                return "";
            } else {
                return new String(myMsgArr);
            }
        }

        return "";
    }

    public static List<Student> decryptStudentList(List<Student> list) {
        if (list != null && list.size() > 0) {
            for (Student student : list) {
                student.decrypt();
            }
        }

        return list;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String msg = "410183199410012076";
        System.out.println("【加密前】：" + msg);

        //加密
        String s = encrypt(msg);
        System.out.println("【加密后】：" + s);

        //解密
        System.out.println("【解密后】：" + decrypt(s));
    }
}
