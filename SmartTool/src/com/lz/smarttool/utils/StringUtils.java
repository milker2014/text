package com.lz.smarttool.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 处理字符串工具类
 * 
 * @author:CuiWei
 * @see:
 * @since:
 * @copyright © soufun.com
 * @Date:2014年5月22日
 */
public class StringUtils {

	/**
	 * 判断是否为空
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNullOrEmpty(String text) {
		if (text == null || "".equals(text.trim()) || text.trim().length() == 0 || "null".equals(text.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得MD5加密字符串
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			UtilLog.w("AOS", e.toString());
		} catch (UnsupportedEncodingException e) {
			UtilLog.w("AOS", e.toString());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 获取更新的时间
	 * 
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String getDateString(Date date) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (calendar.get(Calendar.YEAR) - (date.getYear() + 1900) > 0) {
			return sdf.format(date);
		} else if (calendar.get(Calendar.MONTH) - date.getMonth() > 0) {
			return sdf.format(date);
		} else if (calendar.get(Calendar.DAY_OF_MONTH) - date.getDate() > 6) {
			return sdf.format(date);
		} else if ((calendar.get(Calendar.DAY_OF_MONTH) - date.getDate() > 0) && (calendar.get(Calendar.DAY_OF_MONTH) - date.getDate() < 6)) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - date.getHours();
			return i + "天前";
		} else if (calendar.get(Calendar.HOUR_OF_DAY) - date.getHours() > 0) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - date.getHours();
			return i + "小时前";
		} else if (calendar.get(Calendar.MINUTE) - date.getMinutes() > 0) {
			int i = calendar.get(Calendar.MINUTE) - date.getMinutes();
			return i + "分钟前";
		} else if (calendar.get(Calendar.SECOND) - date.getSeconds() > 0) {
			int i = calendar.get(Calendar.SECOND) - date.getSeconds();
			return i + "秒前";
		} else if (calendar.get(Calendar.SECOND) - date.getSeconds() == 0) {
			return "刚刚";
		} else {
			return sdf.format(date);
		}
	}

	/**
	 * 对流转化成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getContentByString(InputStream is) {
		try {
			if (is == null)
				return null;
			byte[] b = new byte[1024];
			int len = -1;
			StringBuilder sb = new StringBuilder();
			while ((len = is.read(b)) != -1) {
				sb.append(new String(b, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			UtilLog.w("AOS", e.toString());
		}
		return null;
	}

	/**
	 * 对流转化成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getStringByStream(InputStream is) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line + "\n");
			}
			return buffer.toString().replaceAll("\n\n", "\n");
		} catch (Exception e) {
			UtilLog.w("AOS", e.toString());
		}
		return null;
	}

	/**
	 * 截取字符串，去掉sign后边的
	 * 
	 * @param source
	 *            原始字符串
	 * @param sign
	 * @return
	 */
	public static String splitByIndex(String source, String sign) {
		String temp = "";
		if (isNullOrEmpty(source)) {
			return temp;
		}
		int length = source.indexOf(sign);
		if (length > -1) {
			temp = source.substring(0, length);
		} else {
			return source;
		}
		return temp;
	}

	//
	// /**
	// * 截取字符串，返回sign分隔的字符串
	// *
	// */
	// public static String splitNumAndStr(String res, String sign) {
	// StringBuffer buffer;
	// String reg = "\\d+";
	// Pattern p = Pattern.compile(reg);
	// Matcher m = p.matcher(res);
	// if (m.find()) {
	// buffer = new StringBuffer();
	// String s = m.group();
	// buffer.append(s);
	// buffer.append(sign);
	// buffer.append(res.replace(s, ""));
	// return buffer.toString();
	// }
	// return null;
	// }
	//
	// /**
	// * 保留小数点后一位
	// *
	// * @param d
	// * @return
	// * @throws Exception
	// */
	// public static String formatNumber(double d) {
	// try {
	// DecimalFormat df = new DecimalFormat("#,##0.0");
	// return df.format(d);
	// } catch (Exception e) {
	// }
	// return "";
	// }
	//
	// public static String formatNumber(String d) {
	// return formatNumber(Double.parseDouble(d));
	// }
	//
	// /**
	// * 把对象放进map里
	// *
	// * @param o
	// * 实体
	// */
	// public static Map<String, String> getMapForEntry(Object o) {
	// Map<String, String> map = new HashMap<String, String>();
	// try {
	// Field[] fields = o.getClass().getFields();
	// for (Field f : fields) {
	// String key = f.getName();
	// try {
	// String value = (String) f.get(o);
	// if (StringUtils.isNullOrEmpty(value)
	// || value.indexOf("全部") > -1) {
	// continue;
	// }
	// map.put(key, value);
	// } catch (Exception e) {
	// UtilsLog.w("AOS",e.toString());
	// }
	// }
	// } catch (Exception e) {
	// }
	// return map;
	// }
	//
	// /**
	// * map 转化为实体
	// *
	// * @param <T>
	// * @param map
	// * @param clazz
	// * @return
	// */
	// public static <T> T setMapForEntry(Map<String, String> map, Class<T> clazz) {
	// T t = null;
	// try {
	// t = clazz.newInstance();
	// for (Entry<String, String> entry : map.entrySet()) {
	// String key = entry.getKey();
	// Field field = t.getClass().getField(key);
	// field.set(t, entry.getValue());
	// }
	// } catch (Exception e) {
	// }
	// return t;
	// }
	//
	// /**
	// * 实体转化
	// *
	// * @param o
	// * @return
	// */
	// public static <T> T convertEntry(Object o) {
	// T t = null;
	// try {
	// t = (T) o.getClass().newInstance();
	// Field[] fields = o.getClass().getFields();
	// for (Field f : fields) {
	// try {
	// String value = (String) f.get(o);
	// if (StringUtils.isNullOrEmpty(value)
	// || value.indexOf("全部") > -1) {
	// f.set(t, "");
	// } else {
	// f.set(t, value);
	// }
	// } catch (Exception e) {
	// UtilsLog.w("AOS",e.toString());
	// }
	// }
	// } catch (Exception e) {
	// }
	// return t;
	// }

	/*
	 * 获取字符串格式的当前时间
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	//
	// /*
	// * 保存收藏数据库时间
	// */
	// public static Long getStoreTime(Long currentTime, String time2) {
	// UtilsLog.v("currentTime", "currentTime==" + currentTime);
	// UtilsLog.v("currentTime", "time2==" + time2);
	// long ret = 0;
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//
	// try {
	// Date d2 = format.parse(time2);
	// // 数据库格式问题 Math.abs(currentTime - d2.getTime())
	// ret = Math.abs(currentTime - d2.getTime());
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// UtilsLog.w("AOS",e.toString());
	// }
	//
	// UtilsLog.v("currentTime", "ret==" + ret);
	// return ret;
	// }
	//
	// /*
	// * 时间格式转换，yyyy-MM-dd xx:xx:xx为：yyyy-MM-dd
	// */
	// public static String getStringDate(String date) {
	//
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	// Date d = new Date();
	// try {
	// d = formatter.parse(date);
	// } UtilsLog.w("AOS",e.toString()); (Exception e) {
	// UtilsLog.w("AOS",e.toString());
	// }
	// String dateString = formatter.format(d);
	// return dateString;
	//
	// }
	//
	// /*
	// * 截取sign后边的数字
	// */
	// public static String getStringNum(String str, String sign) {
	// String reg = ":split:";
	// return str.replace(sign, reg).replaceAll(reg + "\\d+", "")
	// .replaceAll(" ", "").trim();
	//
	// }
	//
	// public static String getRegText(String xml, String tag) {
	// Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">",
	// Pattern.UNICODE_CASE | Pattern.DOTALL);
	// Matcher m = pattern.matcher(xml);
	// if (m.find()) {
	// xml = m.group(1);
	// xml = xml.replace("<![CDATA[", "").replace("<![cdata[", "")
	// .replace("]]>", "");
	// return xml;
	// } else {
	// return null;
	// }
	// }
	//
	// /*
	// * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	// */
	// public static String getStringForDate(long date) {
	//
	// SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
	// Date d = new Date(date);
	// String dateString = f.format(d);
	// return dateString;
	//
	// }

	// /*
	// * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	// */
	// public static String getStringForDate_yyyy_MM_dd() {
	//
	// SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	// Date d = new Date(System.currentTimeMillis());
	// String dateString = f.format(d);
	// return dateString;
	//
	// }

	// /*
	// * 时间格式转换，yyyy-MM-dd xx:xx:xx为：MM-dd xx:xx 不要年和秒
	// */
	// public static String getStringForDate(String date) {
	//
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	// SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
	// Date d = new Date();
	// try {
	// d = formatter.parse(date);
	// } catch (Exception e) {
	// UtilsLog.w("AOS",e.toString());
	// }
	// String dateString = f.format(d);
	// return dateString;
	//
	// }
	//
	// /*
	// * 判断价格是否为0或空
	// */
	// public static boolean isPriceZero(String price) {
	// if (isNullOrEmpty(price)) {
	// return true;
	// }
	// price = splitByIndex(price, ".");
	// if ("0".equals(price)) {
	// return true;
	// }
	// return false;
	//
	// }
	//
	// /**
	// * 取价格的整数，去掉单位
	// *
	// * @param price
	// * @return
	// */
	// public static String getPrice(String price) {
	// Pattern p = Pattern.compile("^\\d+");
	// Matcher m = p.matcher(price);
	// if (m.find()) {
	// return m.group();
	// }
	// return "";
	// }

	/**
	 * 判断是否全为数字
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isAllNumber(String content) {

		if (isNullOrEmpty(content)) {
			return false;
		}
		Pattern p = Pattern.compile("\\-*\\d+");
		Matcher m = p.matcher(content);
		return m.matches();
	}

	// /**
	// * 整数转字节数组
	// *
	// * @param i
	// * @return
	// */
	// public static byte[] intToByte(int i) {
	// byte[] bt = new byte[4];
	// bt[0] = (byte) (0xff & i);
	// bt[1] = (byte) ((0xff00 & i) >> 8);
	// bt[2] = (byte) ((0xff0000 & i) >> 16);
	// bt[3] = (byte) ((0xff000000 & i) >> 24);
	// return bt;
	// }

	// /**
	// * 字节数组转整数
	// *
	// * @param bytes
	// * @return
	// */
	// public static int bytesToInt(byte[] bytes) {
	// int num = bytes[0] & 0xFF;
	// num |= ((bytes[1] << 8) & 0xFF00);
	// num |= ((bytes[2] << 16) & 0xFF0000);
	// num |= ((bytes[3] << 24) & 0xFF000000);
	// return num;
	// }

	/**
	 * 获得个人发布过的城市、手机号与发布类别的对应关系
	 * 
	 * @return
	 */
	// public static HashMap<String, String> getPublishInfo() {
	// HashMap<String, String> map = new HashMap<String, String>();
	// List<PublishInfo> list =
	// SoufunApp.getSelf().getDb().getList(PublishInfo.class,
	// "select type,city,phone from  " + PublishInfo.class.getSimpleName() +
	// " group by type,city,phone");
	// if (list != null) {
	// for (PublishInfo info : list) {
	// String key = info.type;
	// String str = "";
	// if (map.containsKey(key)) {
	// str = map.get(key) + ";" + info.city + "&" + info.phone;
	// map.put(key, str);
	// } else {
	// map.put(key, info.city + "&" + info.phone);
	// }
	// }
	// }
	// return map;
	// }

	// /**
	// * 拼接短信内容
	// */
	// public static String getMSG(String district, String title, String room,
	// String mj, String price, String type) {
	// StringBuffer sb = new StringBuffer();
	// if (!isNullOrEmpty(district))
	// sb.append(district + ",");
	// if (!isNullOrEmpty(title))
	// sb.append("" + title + ",");
	// if (!isNullOrEmpty(room))
	// sb.append("" + room + ",");
	// if (!isNullOrEmpty(price))
	// sb.append("价格为" + price.replace("/套", "").replace("/月", "") + "/月的");
	// if (!isNullOrEmpty(mj) && !"暂无资料".equals(mj) && !"暂无信息".equals(mj))
	// sb.append("" + mj + "平的房源");
	// if (!isNullOrEmpty(type))
	// sb.append(type);
	// if (!isNullOrEmpty(sb.toString())) {
	// if (sb.toString().endsWith(",")) {
	// String str = sb.toString().substring(0,
	// sb.toString().length() - 1);
	// return str;
	// }
	// }
	// return sb.toString();
	// }
	//
	// /**
	// * 判断是否属于48个城市
	// */
	// public static boolean is_48_city(String city) {
	// // String[]
	// //
	// citys={"北京","上海","广州","深圳","成都","重庆","杭州","南京","天津","武汉","苏州","石家庄","郑州","济南","大连","沈阳","长春","太原","青岛","潍坊","烟台","呼和浩特","哈尔滨","唐山","秦皇岛","珠海","无锡","西安","昆明","福州","海南","佛山","常州","惠州","厦门","合肥","南宁","长沙","贵阳","宁波","中山","昆山","扬州","兰州","徐州","三亚","南昌","东莞"};
	// String[] citys = { "安阳", "鞍山", "蚌埠", "包头", "保定", "北海", "北京", "滨州",
	// "沧州", "长春", "长沙", "常熟", "常州", "成都", "大连	", "大庆", "德阳", "东莞",
	// "防城港", "佛山", "福州", "赣州", "广州	", "贵阳", "桂林", "哈尔滨", "海南",
	// "	邯郸	", "杭州", "合肥", "衡水", "衡阳", "呼和浩特", "湖州", "淮安", "惠州", "吉林",
	// "济南", "济宁", "嘉兴", "江门", "江阴", "金华", "九江", "昆明", "昆山", "兰州",
	// "廊坊", "乐山", "连云港", "聊城", "临沂", "柳州", "泸州", "洛阳", "马鞍山", "梅州",
	// "绵阳", "内江", "南昌", "南充", "南京", "南宁", "南通", "宁波", "秦皇岛", "青岛",
	// "清远", "泉州", "三亚", "汕头", "商丘", "上海", "绍兴", "深圳", "沈阳", "石家庄",
	// "苏州", "遂宁", "太原", "泰安", "泰州", "唐山", "天津", "威海", "潍坊", "温州",
	// "乌鲁木齐", "无锡", "吴江", "芜湖", "武汉", "西安", "西宁", "厦门", "咸阳", "湘潭",
	// "襄阳", "徐州", "烟台", "盐城", "扬州", "宜昌", "银川", "岳阳", "漳州", "肇庆",
	// "镇江", "郑州", "中山", "重庆", "舟山", "株洲", "珠海", "淄博" };
	// for (int i = 0; i < citys.length; i++) {
	// if (city.equals(citys[i])) {
	// return true;
	// }
	// }
	// return false;
	//
	// }

	/**
	 * 对小数点后两位及两位以内的数据进行取整
	 */
	public static String getInt(String string) {

		return (int) (Double.parseDouble(string) * 100) / 10 / 10 + "";

	}

	/**
	 * 对小数点后的数据如果是零取整不是返回原值
	 */
	public static String round(String str) {

		float f = Float.parseFloat(str);
		int i = (int) f;
		if (f == i) {
			return i + "";
		} else {
			return f + "";

		}
	}

	/**
	 * 号码的宅电和手机判定
	 * 
	 * @param number
	 * @return
	 */
	public static boolean phoneOrMobile(String number) {
		if (number == null || number.equals("")) {
			return true;
		} else if (number.length() > 11) {
			return true;
		} else {
			Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$");
			Matcher m = p.matcher(number);
			return m.matches();
		}
	}

	/**
	 * 半角字符转化为全角字符
	 * 
	 * @param input
	 *            要转化的字符
	 * @return 转化后的结果
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 *            要转化的字符
	 * @return 转化后的结果
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
}
