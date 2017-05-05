package com.cn.tools;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherUtilities {
    private static final String TAG = OtherUtilities.class.getSimpleName();
    private static Toast mToast;
    /**
     * 读取本地文件
     *
     * @param filename
     * @return
     */
    public static Spanned getLocalContent(String filename) {
        Spanned content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(BaseApplication.myContext.getAssets().open(filename), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            content = Html.fromHtml(sb.toString());
        } catch (IOException e) {
            LogUtil.w(TAG, "getLocalContent -- exception, info=" + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.w(TAG, "getLocalContent -- br close exception, info=" + e.getMessage());
                }
            }
        }
        return content;
    }

    /**
     * 创建头像在本地的缓存文件名称
     *
     * @param uid
     * @param version 服务器端的版本号
     * @param isThumb 是否为缩略图
     * @return
     */
    public static File createAvatarFile(long uid, long version, boolean isThumb) {
        String postfix = ImageUtil.IMGJSUFFIX;
        String filename = null;
        if (isThumb) {
            filename = String.format("%s_thumb", uid);
        } else {
            filename = String.format("%s", uid);
        }
        // 文件根目录如果不存在则创建
        File rootDir = new File(Constants.AVATAR_PATH);
        if (!rootDir.exists()) {
            boolean createRes = rootDir.mkdirs();
            if (!createRes) {
                return null;
            }
        }

        // 先判断文件是否存在，如果存在，则先删除
        File checkFile = new File(String.format("%s%s%s", Constants.AVATAR_PATH, filename, postfix));
        if (checkFile.exists()) {
            checkFile.delete();
        }

        return checkFile;
    }

    /**
     * @param numbers String类型的列表
     * @return String
     * @function joinNumbers 连接手机号码，中间用半角逗号分隔
     */
    public static String join(List<String> numbers, String separate) {
        if (null == numbers || numbers.size() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String num : numbers) {
            sb.append(num).append(separate);
        }
        return sb.delete(sb.length() - separate.length(), sb.length()).toString();
    }

    /**
     * @param values   String 数组
     * @param separate 分隔符
     * @return String
     * @function 用分隔符连接字符串
     */
    public static String join(String[] values, String separate) {
        if (null == values || values.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String num : values) {
            sb.append(num).append(separate);
        }
        return sb.delete(sb.length() - separate.length(), sb.length()).toString();
    }

    /**
     * @param values   String 数组
     * @param separate 分隔符
     * @return String
     * @function 用分隔符连接字符串
     */
    public static String join(char[] values, String separate) {
        if (null == values || values.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (char num : values) {
            sb.append(num).append(separate);
        }
        return sb.delete(sb.length() - separate.length(), sb.length()).toString();
    }

    /**
     * 重复一个字符若干次,并用逗号链接
     */
    public static String repeatChar(char tChar, int count) {
        char[] charArray = new char[count];
        Arrays.fill(charArray, tChar);
        return join(charArray, ",");
    }

    /**
     * 生成n位的随机字符串
     *
     * @param n 长度
     * @return string类型的随机数
     */
    public static String getRandomString(int n) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < n; i++) {
            char c = (char) (0x30 + rand.nextInt(10));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 读取短信中的pincode
     *
     * @param smsContent
     * @return
     */
    public static String getPincode(String smsContent) {
        String regex = ":(\\d{6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(smsContent);

        String str = null;
        while (matcher.find()) {
            str = matcher.group(1);
        }
        return str;
    }

    /**
     * 过滤特殊字符
     *
     * @param content
     * @return
     */
    public static String filterSpecialChars(String content) {
        return content.replace("-", "").replace(" ", "");
    }

    /**
     * 过滤特殊字符
     */
    public static String filterSpecialStr(String content) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？_]";
        String newStr = "";
        try {
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            newStr = mat.replaceAll("").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * 只让输入数字、字母、汉字
     */
    public static String filterInputStr(String content) {
        String regEx = "^[0-9A-Za-z\u4e00-\u9fa5]{0,}";
        String newStr = "";
        try {
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            if (mat.matches()) {
                newStr = content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * 只让输入数字、字母、汉字、常用标点符号
     */
    public static String filterInputStr2(String content) {
        String regEx = "^[0-9A-Za-z\u4e00-\u9fa5 \n ，。！？（）：；@、.]{0,}";
        String newStr = "";
        try {
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(content);
            if (mat.matches()) {
                newStr = content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newStr;
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param content
     * @return
     */
    public static String replaceBlank(String content) {
        if (!TextUtils.isEmpty(content)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(content);
            content = "";
            content = m.replaceAll(" ");
        }
        return content;
    }

    public static boolean checkAlphaDigits(String content) {
        String reg = "^[0-9a-zA-Z]*$";
        if (TextUtils.isEmpty(content) || !content.matches(reg)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkMobile(String content) {
        String phoneReg = "^1[034578][0-9]{9}$";
        if (!TextUtils.isEmpty(content) && content.matches(phoneReg)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 匹配6位纯数字
     *
     * @param content
     * @return
     */
    public static boolean checkSixFigure(String content) {
        String phoneReg = "^[0-9]{6}$";
        if (content.matches(phoneReg)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 过滤手机号码
     *
     * @param content
     * @return
     */
    public static String filterMobile(String content) {
        content = filterSpecialChars(content);
        if (content.length() < 11) {
            return null;
        }
        // 如果是以国码开头则先去掉相关的国码信息，如果是以ip(17911/17951/17909)开头则先去掉ip电话
        if (content.startsWith("0086")) {
            content = content.substring(4);
        } else if (content.startsWith("+86")) {
            content = content.substring(3);
        } else if (content.startsWith("17911") || content.startsWith("17951") || content.startsWith("17909")) {
            content = content.substring(5);
        }

        String phoneReg = "^1[034578][0-9]{9}$";
        if (content.matches(phoneReg)) {
            return content;
        }

        return null;
    }

    public static boolean checkRealName(String content) {
        String reg = "";
        if (TextUtils.isEmpty(content) || content.length() > 20) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPwd(String content) {
        String pwdReg = "^[0-9a-zA-Z]*$";
        if (TextUtils.isEmpty(content) || content.length() < 8 || content.length() > 20 || !content.matches(pwdReg)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPincode(String content) {
        String numberReg = "^[0-9]{6}$";
        if (TextUtils.isEmpty(content) || !content.matches(numberReg)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkEmail(String content) {
        String emailReg = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT)$";
        if (TextUtils.isEmpty(content) || !content.matches(emailReg) || content.length() > 50) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Url匹配
     *
     * @param content
     * @return
     */
    public static boolean patternMatcherUrl(String content) {
        content = content.trim();
        if (!content.startsWith("http://") && !content.startsWith("https://")) {
            return false;
        }
        String regex = "(http|https)://((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String buildSQL(String... args) {
        if (args.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString();
    }


    /**
     * 去除list中的重复元素
     */
    public static ArrayList singleElement(List oldList) {
        ArrayList newList = new ArrayList();
        for (Object obj : oldList) {
            if (!newList.contains(obj)) {
                newList.add(obj);
            }
        }
        return newList;
    }

    /**
     * 比较这两个list中包含的值是否相等
     */
    public static <T extends Comparable<T>> boolean compareList(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * 保存asset下的图片文件到指定的目录下
     */
    public static void savePicture(String assetFileName, String sharePicPath) {
        AssetManager am = BaseApplication.myContext.getResources().getAssets();
        Bitmap sharePicture = null;
        try {
            sharePicture = BitmapFactory.decodeStream(am.open(assetFileName));
            File file = new File(sharePicPath);
            if (file.exists())
                file.delete();
            ImageUtil.saveBitmap(sharePicture, sharePicPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ImageUtil.safeReleaseBitmap(sharePicture);
        }
    }

    /**
     * 调用打电话接口
     *
     * @param context
     * @param tleNum
     */
    public static void startTelPhone(Context context, String tleNum) {
        if (!TextUtils.isEmpty(tleNum) && checkMobile(tleNum)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + tleNum));
            context.startActivity(intent);
        }
    }

    /**
     * 判断给定时间与当前时间的先后 String strTime 给定的时间 return true：给定时间在当前时间之前 ；
     * false：给定时间在当前时间之后
     */
    public static boolean timeOrder(String strTime) {
        boolean isRight = true;
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd-HH").parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        long currTime = System.currentTimeMillis();
        if (time >= currTime) {
            isRight = false;
        }
        return isRight;
    }

    /**
     * 获取用户隐藏的名字，如：刘**
     *
     * @param userNickName 用户昵称
     * @return
     */
    public static String getHideName(String userNickName) {
        if (!TextUtils.isEmpty(userNickName)) {
            return userNickName.substring(0, 1) + "**";
        }
        return userNickName;
    }

    /**
     * 将数字转化成想要的格式
     *
     * @param num
     * @param isResTwoDec 是否保留兩位小數
     * @return
     */
    public static String getFormatNum(String num, boolean isResTwoDec) {
        String retResult = "";
        try {
            if (isResTwoDec) {
                double dnum = Double.parseDouble(num);
                if (dnum < 10000) {
                    String formatStr = String.format(Locale.getDefault(), "%.2f", dnum);
                    retResult = num;
                } else if (dnum > 9999) {
                    double ddnum = dnum / 10000d;
                    String formatStr = String.valueOf(((int) (ddnum * 100)) / 100.0);
                    retResult = formatStr + "万";
                }
            } else {
                int inum = Integer.parseInt(num);
                if (inum < 10000) {
                    retResult = num + "";
                } else if (inum > 9999) {
                    double dnum = inum / 10000d;
                    String formatStr = String.format(Locale.getDefault(), "%.1f", dnum);
                    retResult = formatStr + "万";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retResult;
    }

    /**
     * 将数字转化成想要的格式
     *
     * @param num
     * @return
     */
    public static String getFormatNumNoDf(String num) {
        String retResult = "";
        try {
            double dnum = Double.parseDouble(num);
            if (dnum < 10000) {
                retResult = String.valueOf((int) dnum);
            } else if (dnum > 9999) {
                double ddnum = dnum / 10000d;
                String formatStr = String.valueOf(((int) (ddnum * 100)) / 100.0);
                retResult = formatStr + "万";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 将数字转化成想要的格式
     *
     * @param num
     * @return
     */
    public static String getFormatNum(String num) {
        String retResult = "";
        try {
            int inum = Integer.valueOf(num);
            if (inum < 10000) {
                retResult = num + "";
            } else if (inum > 9999) {
                double dnum = inum / 10000d;
                String formatStr = String.format(Locale.getDefault(), "%.1f", dnum);
                retResult = formatStr + "万";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retResult;
    }

    /**
     * 根据Key 读取Value
     *
     * @param key
     * @return
     */
    public static String readData(Context mContext, String key, int resId) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(mContext.getResources().openRawResource(resId));
            props.load(in);
            in.close();
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查网络连接
     *
     * @return
     */
    public static boolean checkInternetConnection(Context context) {
        if (NetworkUtil.isNetworkConnected(context)) {
            if (NetworkUtil.isNetworkAvailable(context)) {
                return true;
            } else {
                String msg = BaseApplication.myContext.getString(R.string.str_network_not_use);
                showNewToast(BaseApplication.myContext, R.mipmap.icon_network_not_ok, msg, Toast.LENGTH_LONG);
                return false;
            }
        } else {
            String msg = BaseApplication.myContext.getString(R.string.str_network_off);
            showNewToast(BaseApplication.myContext, R.mipmap.icon_network_not_ok, msg, Toast.LENGTH_LONG);
            return false;
        }
    }

    public static void showToastText(String msg) {
        if (BaseApplication.myContext != null) {
            showNewToast(BaseApplication.myContext, 0, msg, Toast.LENGTH_LONG);
        }
    }



    /**
     * 弹出Toast
     *
     * @param context
     * @param resId
     * @param text     提示内容
     * @param duration
     */
    private static void showNewToast(Context context, int resId, CharSequence text, int duration) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.iv_icon);
        TextView textView = (TextView) layout.findViewById(R.id.tv_content);
        if (resId > 0) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(resId);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        } else {
            textView.setText("");
        }
        if (null == mToast) {
            mToast = new Toast(context);
        }
        mToast.setView(layout);
        mToast.setDuration(duration);
        mToast.show();
    }

    /**
     * 向指定电话号码发送短信
     *
     * @param content
     */
    public static void sendMSM(String phone, String content) {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(content);  //因为一条短信有字数限制，因此要将长短信拆分
        for (String text : list) {
            manager.sendTextMessage(phone, null, text, null, null);
        }
    }


    /**
     * 判断是否是多次点击
     */
    private static long lastClickTime;

    public static boolean isFastMultipleClick(long duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < duration) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断身份证号是否合规
     *
     * @param id
     * @return
     */
    public static boolean checkIdcard(String id) {
        String phoneReg = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        if (!TextUtils.isEmpty(id) && id.matches(phoneReg)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查qq
     *
     * @param qq
     * @return
     */
    public static boolean checkQQNumber(String qq) {
        boolean isMatch = false;
        if (TextUtils.isEmpty(qq)) {
            return isMatch;
        }
        String qqReg = "[0-9]{5,10}";
        isMatch = qq.matches(qqReg);
        return isMatch;
    }

    /**
     * 中文GB2312编码
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 中文GB2312解码
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    /**
     * 中文GB2312解码
     */
    public static String gBDecodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
