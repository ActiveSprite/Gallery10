package com.example.timefragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class ImageScanner {
	private Context mContext;
	
	public ImageScanner(Context context){
		this.mContext = context;
	}
	
	/**
	 * ����ContentProviderɨ���ֻ��е�ͼƬ���˷��������������߳���
	 */
	public void scanImages(final ScanCompleteCallBack callback) {
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				callback.scanComplete((Cursor)msg.obj);
			}
		};
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext.getContentResolver();

				// ֻ��ѯjpeg��png��ͼƬ
//				Cursor mCursor = mContentResolver.query(mImageUri, null,
//						MediaStore.Images.Media.MIME_TYPE + "=? or "
//								+ MediaStore.Images.Media.MIME_TYPE + "=?",
//						new String[] { "image/jpeg", "image/png" },
//						MediaStore.Images.Media.DATE_MODIFIED);
				
				
				 String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				 Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				 Media.SIZE, Media.DATE_ADDED};
				 String selection=Media.DATA+" like ?";
				 String[] selectionargs={"%/storage/emulated/0/DCIM/%"};
				 Cursor mCursor = mContentResolver.query(mImageUri,columns,selection, selectionargs,
							Media.DATE_MODIFIED+" desc");
				Log.i("isnull",String.valueOf(mCursor==null));
	//			Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, null);
				
				//����Handler֪ͨ�����߳�
				Message msg = mHandler.obtainMessage();
				msg.obj = mCursor;
				mHandler.sendMessage(msg);
			}
		}).start();

	}
	
	
	public static interface ScanCompleteCallBack{
		public void scanComplete(Cursor cursor);
	}


}
