package com.ez08.compass.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.ez08.compass.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class FileUtils {
	/**
	 * SD����Ŀ¼
	 */
	public String SDCardRoot;
	/**
	 * ��ȡ��չSD���豸״̬
	 */
	private String SDStateString;
	/**
	 * �����map
	 */
	public static HashMap<String, Integer> emo_map = new HashMap<String, Integer>();
	// public static final String[] EMOS = new String[] { "微笑", "呲牙", "色", "发呆",
	// "得意", "大哭", "害羞", "闭嘴", "睡", "流泪", "尴尬", "发怒", "调皮", "大笑", "惊讶",
	// "委屈", "冷汗", "抓狂", "吐", "偷笑", "傲慢", "困", "憨笑", "敲打", "抠鼻", "鼓掌",
	// "坏笑", "鄙视", "委屈", "阴险", "咖啡", "玫瑰", "嘴唇", "爱心", "菜刀", "月亮", "强",
	// "握手", "拥护", "啤酒", "OK" };
//	 public static final String[] EMOS = new String[] { "微笑", "呲牙", "色", "发呆",
//	 "得意", "大哭", "害羞", "闭嘴", "睡", "崇拜", "亲亲", "发怒", "调皮", "大笑", "惊讶",
//	 "委屈", "冷汗", "抓狂", "吐", "阴险", "傲慢", "困", "憨笑", "敲打", "奋斗", "拥护",
//	 "晕", "鄙视", "强", "菜刀", "再见", "玫瑰", "嘴唇", "爱心", "咖啡", "月亮", "礼物",
//	 "握手", "鼓掌", "啤酒", "OK" };

	 public static final String[] EMOS_ALI = new String[] { "微笑", "害羞", "吐舌头",
	 "偷笑 ", "爱慕", "大笑", "跳舞", "飞吻", "慰", "抱抱", "加油", "胜利",
	 "强", "亲亲", "花痴", "露齿笑", "查找", "呼叫", "算账", "财迷",
	 "好主意", "鬼脸", "天使", "再见", "流口水", "享受", "色情狂", "呆",
	 "思考 ", "迷惑", "疑问", "没钱了", "无聊", "怀疑", "嘘", "小样",
	 "摇头", "感冒", "尴尬", "傻笑 ", "不会吧", "无奈", "流汗", "凄凉", "困了",
	 "晕", "忧伤", "委屈", "悲伤", "大哭", "痛哭", "I服了U", "对不起 ",
	 "再见", "皱眉", "好累", "生病", "吐", "背", "惊讶", "惊愕",
	 "闭嘴", "欠扁", "鄙视", "大怒", "生气", "财神 ", "学习雷锋", "恭喜发财",
	 "小二", "老大", "邪恶", "单挑", "CS", "忍者", "炸弹", "惊声尖叫", "漂亮MM",
	 "帅GG", "招财猫", "成绩", "鼓掌", "握手", "红唇", "玫瑰 ", "残花", "爱心",
	 "心碎", "钱", "购物", "礼物", "收邮件", "电话", "举杯庆祝", "时钟", "等待 ",
	 "很晚了 ", "飞机", "支付宝" };

	 public static final String[] SEND_ALI = new String[] { "{znzbqali_0}", "{znzbqali_1}", "{znzbqali_2}",
	 "{znzbqali_3} ", "{znzbqali_4}", "{znzbqali_5}", "{znzbqali_6}", "{znzbqali_7}", "{znzbqali_8}", "{znzbqali_9}", "{znzbqali_10}", "{znzbqali_11}",
	 "{znzbqali_12}", "{znzbqali_13}", "{znzbqali_14}", "{znzbqali_15}", "{znzbqali_16}", "{znzbqali_17}", "{znzbqali_18}", "{znzbqali_19}",
	 "{znzbqali_20}", "{znzbqali_21}", "{znzbqali_22}", "{znzbqali_23}", "{znzbqali_24}", "{znzbqali_25}", "{znzbqali_26}", "{znzbqali_27}",
	 "{znzbqali_28}", "{znzbqali_29}", "{znzbqali_30}", "{znzbqali_31}", "{znzbqali_32}", "{znzbqali_33}", "{znzbqali_34}", "{znzbqali_35}",
	 "{znzbqali_36}", "{znzbqali_37}", "{znzbqali_38}", "{znzbqali_39}", "{znzbqali_40}", "{znzbqali_41}", "{znzbqali_42}", "{znzbqali_43}", "{znzbqali_44}",
	 "{znzbqali_45}", "{znzbqali_46}", "{znzbqali_47}", "{znzbqali_48}", "{znzbqali_49}", "{znzbqali_50}", "{znzbqali_51}", "{znzbqali_52}",
	 "{znzbqali_53}", "{znzbqali_54}", "{znzbqali_55}", "{znzbqali_56}", "{znzbqali_57}", "{znzbqali_58}", "{znzbqali_59}", "{znzbqali_60}",
	 "{znzbqali_61}", "{znzbqali_62}", "{znzbqali_63}", "{znzbqali_64}", "{znzbqali_65}", "{znzbqali_66} ", "{znzbqali_67}", "{znzbqali_68}",
	 "{znzbqali_69}", "{znzbqali_70}", "{znzbqali_71}", "{znzbqali_72}", "{znzbqali_73}", "{znzbqali_74}", "{znzbqali_75}", "{znzbqali_76}", "{znzbqali_77}",
	 "{znzbqali_78}", "{znzbqali_79}", "{znzbqali_80}", "{znzbqali_81}", "{znzbqali_82}", "{znzbqali_83}", "{znzbqali_84}", "{znzbqali_85}", "{znzbqali_86}",
	 "{znzbqali_87}", "{znzbqali_88}", "{znzbqali_89}", "{znzbqali_90}", "{znzbqali_91}", "{znzbqali_92}", "{znzbqali_93}", "{znzbqali_94}", "{znzbqali_95}",
	 "{znzbqali_96}", "{znzbqali_97}", "{znzbqali_98}" };
	 
	 public static final String[] REP_ALI = new String[] { "emoji_01", "emoji_02", "emoji_03",
	 "emoji_04 ", "emoji_05", "emoji_06", "emoji_07", "emoji_08", "emoji_09", "emoji_10", "emoji_11", "emoji_12",
	 "emoji_13", "emoji_14", "emoji_15", "emoji_16", "emoji_17", "emoji_18", "emoji_19", "emoji_20",
	 "emoji_21", "emoji_22", "emoji_23", "emoji_24", "emoji_25", "emoji_26", "emoji_27", "emoji_28",
	 "emoji_29", "emoji_30", "emoji_31", "emoji_32", "emoji_33", "emoji_34", "emoji_35", "emoji_36",
	 "emoji_37", "emoji_38", "emoji_39", "emoji_40", "emoji_41", "emoji_42", "emoji_43", "emoji_44", "emoji_45",
	 "emoji_46", "emoji_47", "emoji_48", "emoji_49", "emoji_50", "emoji_51", "emoji_52", "emoji_53",
	 "emoji_54", "emoji_55", "emoji_56", "emoji_57", "emoji_58", "emoji_59", "emoji_60", "emoji_61",
	 "emoji_62", "emoji_63", "emoji_64", "emoji_65", "emoji_66", "emoji_67", "emoji_68", "emoji_69",
	 "emoji_70", "emoji_71", "emoji_72", "emoji_73", "emoji_74", "emoji_75", "emoji_76", "emoji_77", "emoji_78",
	 "emoji_79", "emoji_80", "emoji_81", "emoji_82", "emoji_83", "emoji_84", "emoji_85", "emoji_86", "emoji_87",
	 "emoji_88", "emoji_89", "emoji_90", "emoji_91", "emoji_92", "emoji_93", "emoji_94", "emoji_95", "emoji_96",
	 "emoji_97", "emoji_98", "emoji_99" };
	 
	public FileUtils() {
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		SDStateString = Environment.getExternalStorageState();

	}

	public static void bindEmotionText(Context context, String text, TextView tv) {
		if (emo_map.size() == 0) {
			for (int i = 0; i < 41; i++) {
				emo_map.put("[" + EMOS_ALI[i] + "]", R.drawable.emoji_01 + i);
			}
		}
		int index1 = text.indexOf("[");
		SpannableStringBuilder sb;
		if (index1 >= 0) {
			sb = new SpannableStringBuilder("");
			replaceString(context, sb, text, (int) tv.getTextSize(),
					(int) tv.getTextSize());
		} else {
			sb = new SpannableStringBuilder(text);
		}
		tv.append(sb);
	}

	public static SpannableStringBuilder getEmotion(Context context, String text) {
		if (emo_map.size() == 0) {
			for (int i = 0; i < 41; i++) {
				emo_map.put("[" + EMOS_ALI[i] + "]", R.drawable.emoji_01 + i);
			}
		}
		int index1 = text.indexOf("[");
		SpannableStringBuilder sb;
		if (index1 >= 0) {
			sb = new SpannableStringBuilder("");
			// 特殊符号前面加一个空格，不会出现不正常的折行现象
//			text = text.replace(" [", "[");
//			text = text.replace("[", " [");
			replaceString(context, sb, text);
		} else {
			sb = new SpannableStringBuilder(text);
		}

		return sb;
	}

	public static void replaceString(Context context,
                                     SpannableStringBuilder sb, String text) {

		int index1 = text.indexOf("[");
		int index2 = text.indexOf("]");
		String remindedString = "";
		String emString = "";
		if (index2 > 0 && index1 < index2) {
			// sb.append(text.substring(0, index1));
			remindedString = text.substring(index2 + 1);
			emString = text.substring(index1, index2 + 1);
			if (emString == null) {
				return;
			}
			String cc = emString.substring(1, emString.length() - 1);
			if (cc != null && !"".equals(cc)
					&& emo_map.containsKey("[" + cc + "]")) {
				// int num = Integer.parseInt(cc);
				// if (num < 0 || num > emo_map.size() - 1) {
				// return;
				// } else {

				int id = emo_map.get("[" + cc + "]");
				Drawable d = context.getResources().getDrawable(id);
				//
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				//
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);

				SpannableString ss = new SpannableString(text.substring(0,
						index2 + 1));
				ss.setSpan(span, index1, index2 + 1,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

				sb.append(ss);
				replaceString(context, sb, remindedString);
				// }
			} else {
				sb.append(text.substring(0, index2 + 1));
				replaceString(context, sb, remindedString);
			}

		} else {
			sb.append(text);
		}
	}

	public static void replaceString(Context context,
                                     SpannableStringBuilder sb, String text, int imgWidth, int imgHeight) {

		int index1 = text.indexOf("[");
		int index2 = text.indexOf("]");
		String remindedString = "";
		String emString = "";
		if (index2 > 0) {
			// sb.append(text.substring(0, index1));
			remindedString = text.substring(index2 + 1);
			emString = text.substring(index1, index2 + 1);
			if (emString == null) {
				return;
			}
			String cc = emString.substring(1, emString.length() - 1);
			if (cc != null && !"".equals(cc)
					&& emo_map.containsKey("[" + cc + "]")) {
				// int num = Integer.parseInt(cc);
				// if (num < 0 || num > emo_map.size() - 1) {
				// return;
				// } else {

				int id = emo_map.get("[" + cc + "]");
				Drawable d = context.getResources().getDrawable(id);
				//
				d.setBounds(0, 0, imgWidth, imgHeight);
				// d.setBounds(0, 0, d.getIntrinsicWidth(),
				// d.getIntrinsicHeight());
				//
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);

				SpannableString ss = new SpannableString(text.substring(0,
						index2 + 1));
				ss.setSpan(span, index1, index2 + 1,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				sb.append(ss);
				replaceString(context, sb, remindedString, imgWidth, imgHeight);
				// }
			}

		} else {
			sb.append(text);
		}
	}

	/**
	 * �ж��Ƿ�������
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @param fileName
	 * @return
	 * @throws IOException
	 */

	public File createFileInSDCard(String dir, String fileName)
			throws IOException {

		creatSDDir(dir);

		File file = new File(SDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @return
	 */

	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir + File.separator);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirFile;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����?
	 * 
	 * @param dir
	 *            Ŀ¼·��
	 * @param fileName
	 *            �ļ����?
	 * @return
	 */

	public boolean isFileExist(String dir, String fileName) {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		return file.exists();
	}

	/**
	 * ��ȡ�ļ���·��
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 */

	public String getFilePath(String dir, String fileName) {
		return SDCardRoot + dir + File.separator + fileName;
	}

	/**
	 * /*** ��ȡSD����ʣ������,��λ��Byte
	 * 
	 * @return /
	 */

	public long getSDAvailableSize() {

		if (SDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			// ȡ��sdcard�ļ�·��
			File pathFile = android.os.Environment
					.getExternalStorageDirectory();
			android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
			// ��ȡSDCard��ÿ��block��SIZE
			long nBlocSize = statfs.getBlockSize();
			// ��ȡ�ɹ�����ʹ�õ�Block������
			long nAvailaBlock = statfs.getAvailableBlocks();
			// ���� SDCard ʣ���СByte
			long nSDFreeSize = nAvailaBlock * nBlocSize;
			return nSDFreeSize;
		}
		return 0;
	}

	/**
	 * ��һ���ֽ��������д�뵽SD����
	 */
	public boolean write2SD(String dir, String fileName, byte[] bytes) {

		if (bytes == null) {
			return false;
		}

		OutputStream output = null;
		try {
			// ӵ�пɶ���дȨ�ޣ��������㹻������
			if (SDStateString.equals(android.os.Environment.MEDIA_MOUNTED)
					&& bytes.length < getSDAvailableSize()) {
				File file = null;
				creatSDDir(dir);
				file = createFileInSDCard(dir, fileName);
				output = new BufferedOutputStream(new FileOutputStream(file));
				output.write(bytes);
				output.flush();
				return true;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * ��sd���ж�ȡ�ļ����������ֽ�������
	 *
	 * @param dir
	 * @param fileName
	 * @return
	 */
	public byte[] readFromSD(String dir, String fileName) {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		if (!file.exists()) {
			return null;
		}
		System.out.println("###file:" + file.getAbsolutePath());
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);
			// file.deleteOnExit();
			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ��һ��InputStream��������д�뵽SD���� ,�������϶�ȡͼƬ
	 */
	public File write2SDFromInput(String dir, String fileName, InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			int size = input.available();
			// ӵ�пɶ���дȨ�ޣ��������㹻������
			if (SDStateString.equals(android.os.Environment.MEDIA_MOUNTED)
					&& size < getSDAvailableSize()) {
				creatSDDir(dir);
				file = createFileInSDCard(dir, fileName);
				output = new BufferedOutputStream(new FileOutputStream(file));
				byte buffer[] = new byte[4 * 1024];
				int temp;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
				}
				output.flush();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * bitmap ���byte����
	 *
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBitmapByte(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e(TAG, "transform byte exception");
		}
		return out.toByteArray();
	}

	/**
	 * �������bitmap
	 *
	 * @param temp
	 * @return
	 */
	public static Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			// Bitmap bitmap=BitmapFactory.decodeResource(getResources(),
			// R.drawable.contact_add_icon);
			return null;
		}
	}

	/**
	 * ��һ���ֽ�������ݱ��file
	 */
	public File bytesToFile(byte[] bytes) {

		if (bytes == null) {
			return null;
		}
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		// ��ȡ��չSD���豸״̬
		SDStateString = Environment.getExternalStorageState();
		OutputStream output = null;
		try {
			// ӵ�пɶ���дȨ�ޣ��������㹻������
			if (SDStateString.equals(android.os.Environment.MEDIA_MOUNTED)
					&& bytes.length < getSDAvailableSize()) {
				File file = null;
				file = createFileInSDCard("test.t", "temppicture");
				output = new BufferedOutputStream(new FileOutputStream(file));
				output.write(bytes);
				output.flush();
				return file;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param bytes
	 * @param time
	 * @return
	 */
	public File saveImg(byte[] bytes, String time) {

		if (bytes == null) {
			return null;
		}
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		// ��ȡ��չSD���豸״̬
		SDStateString = Environment.getExternalStorageState();
		OutputStream output = null;
		try {
			// ӵ�пɶ���дȨ�ޣ��������㹻������
			if (SDStateString.equals(android.os.Environment.MEDIA_MOUNTED)
					&& bytes.length < getSDAvailableSize()) {
				File file = null;
				// 保存图片名为时间+targetCid的格式，targetCid用于区分来自不同cid的图片
				file = createFileInSDCard("imImage", time + ".jpg");
				output = new BufferedOutputStream(new FileOutputStream(file));
				output.write(bytes);
				output.flush();
				return file;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void assetToFile(Context context, String name, File file) {
		AssetManager assetManager = context.getAssets();

		InputStream is;
		try {
			is = assetManager.open(name);
			java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();

			byte[] bufferByte = new byte[1024];
			int l = -1;
			while ((l = is.read(bufferByte)) > -1) {
				bout.write(bufferByte, 0, l);
			}
			byte[] rBytes = bout.toByteArray();
			bout.close();
			is.close();

			if (!file.exists()) {
				file.createNewFile();
			}

			DataOutputStream dos = new DataOutputStream(new FileOutputStream(
					file));
			dos.write(rBytes);
			dos.flush();
			dos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
