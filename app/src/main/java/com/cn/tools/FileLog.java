package com.cn.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class FileLog {
	private static final String TAG = FileLog.class.getSimpleName();
	private static final ReentrantLock mLock = new ReentrantLock(true);
	
	private static FileOutputStream mSystemLog = null;
	private static SimpleDateFormat mFormatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");// 月-日 时:分:秒.毫秒
	private static boolean mIntialized = false;

	public static void log(String tag, String str) {
		log(String.format("[%s] (%s): %s\n", mFormatter.format(new Date()), tag, str).getBytes());
		LogUtil.i(tag, str);
	}
	
	private static void log(byte[] buffer) {		
		if (!mIntialized) {
			try {
				intialize();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		if (null == mSystemLog){
			mIntialized = false;
			return;
		}
		
		FileLock lock = null;
		mLock.lock();

		try {
			lock = mSystemLog.getChannel().lock();
			// ///所有的线程 在这里排队等待锁//////
			if (null == mSystemLog) {
				mIntialized = false;
				return;
			}
			
			mSystemLog.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}finally{
			if (null != lock) {
				try {
					lock.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mLock.unlock();
		}
	}
	
	private static void intialize() throws IOException {
		try {	
			DateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			String fileName = date.format(System.currentTimeMillis());
			File logFile = new File(String.format("%s%s.log", Constants.LOG_PATH, fileName));// 年-月-日.log
			if (!logFile.exists()) {
				deleteOlderLogFile(date);
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			}
			mSystemLog = new FileOutputStream(logFile, true);
			mIntialized = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deleteOlderLogFile(DateFormat date) {
		File logFileDir = new File(Constants.LOG_PATH);
		if (!logFileDir.exists()) {
			return;
		}

		File[] logs = logFileDir.listFiles();
		if (null!=logs && logs.length>0) {
			// 获取离现在之前的过期日期
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -Constants.LOG_EXPIRED_TIME);// 日期减
			String expire = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			String fileName = null;
			for (File file : logs) {
				fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
				if(fileName.compareTo(expire) <= 0){
					file.delete();
				}
			}
		}
	}
	
	private static void close() {
		mIntialized = false;
		if(null != mSystemLog){
			try {
				mSystemLog.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		mSystemLog = null;	
	}	
}
