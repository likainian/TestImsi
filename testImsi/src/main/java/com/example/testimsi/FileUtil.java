package com.example.testimsi;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {
	private static final String TAG = "FileUtilttt";
	//写入文件
	public static void saveFile(String str,String path,String name,boolean append){
		try {
			File file = new File(path);
			if (!file.exists()) {  //判断文件是否存在
				file.mkdirs(); //创建文件夹
			}
			FileOutputStream fos = new FileOutputStream(path+File.separator+name,append);
			BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			bos.write(str);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//读出文件
	public static String readFile(String filePath){
		Log.i(TAG, "readFile: ");
		try {
			FileInputStream fis = new FileInputStream(filePath);

			byte[] b = new byte[1024];
			int num = -1;
			StringBuilder sb = new StringBuilder();
			while ((num = fis.read(b)) != -1) {
				sb.append(new String(b,0,num));
			}
			Log.i(TAG, "===== 外部存储读取到的数据位： "+sb.toString());
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
