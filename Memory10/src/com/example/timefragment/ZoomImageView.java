package com.example.timefragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.ImageView.ScaleType;


public class ZoomImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener,
View.OnTouchListener , ViewTreeObserver.OnGlobalLayoutListener{
/**
* �������Ƶļ��
*/
private ScaleGestureDetector mScaleGestureDetector;
/**
* ��������
*/
private GestureDetector mGestureDetector;
/**
* ��ͼƬ��������ƽ�Ƶ�Matrix
*/
private Matrix mScaleMatrix;
/**
* ��һ�μ���ͼƬʱ����ͼƬ���ű�����ʹͼƬ�Ŀ���߸߳�����Ļ
*/
private boolean mFirst;
/**
* ͼƬ�ĳ�ʼ������
*/
private float mInitScale=1;
/**
* ͼƬ��������
*/
private float mMaxScale=4;
/**
* ˫��ͼƬ�Ŵ�ı���
*/
private float mMidScale=(float) 2.0;
/**
* ��С���ű���
*/
private float mMinScale=(float)0.25;
/**
* ������ֵ
*/
private float mMaxOverScale=6;

/**
* �Ƿ������Զ��Ŵ������С
*/
private boolean isAutoScale;

//-----------------------------------------------
/**
* ��һ�δ��ص������
*/
private int mLastPointerCount;
/**
* �Ƿ�����϶�
*/
private boolean isCanDrag;
/**
* ��һ�λ�����x��y����
*/
private float mLastX;
private float mLastY;
/**
* �ɻ������ٽ�ֵ
*/
private int mTouchSlop;
/**
* �Ƿ��ü�����ұ߽�
*/
private boolean isCheckLeftAndRight;
/**
* �Ƿ��ü�����±߽�
*/
private boolean isCheckTopAndBottom;
/**
* �ٶ�׷����
*/
private VelocityTracker mVelocityTracker;
private FlingRunnable mFlingRunnable;


public ZoomImageView(Context context) {
this(context, null, 0);
}

public ZoomImageView(Context context, AttributeSet attrs) {
this(context, attrs, 0);
}

public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
super(context, attrs, defStyleAttr);
//һ��Ҫ��ͼƬ��ScaleType���ó�Matrix���͵�
//setScaleType(ScaleType.MATRIX);
setScaleType(ScaleType.FIT_CENTER);
//��ʼ���������Ƽ�����
mScaleGestureDetector = new ScaleGestureDetector(context,this);
//��ʼ������
mScaleMatrix = new Matrix();

Log.i("matrix","hh");
setOnTouchListener(this);
mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//��ʼ�����Ƽ����������˫���¼�
mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //����������Զ����ţ���ֱ�ӷ��أ������д���
    	
        if (isAutoScale) return true;
        //�õ����������
        float x = e.getX();
        float y = e.getY();
        float u;
        Log.i("matrix",String.valueOf(getScale()));
       
        
       
        //�����ǰͼƬ������ֵС��ָ����˫������ֵ
        if (getScale()< mMidScale){
            //�����Զ��Ŵ�
        	//  Log.i("auto1",String.valueOf(isAutoScale));
            post(new AutoScaleRunnable(mMidScale,x,y));
        }else{
            //��ǰͼƬ������ֵ���ڳ�������ֵ�����Զ���С
            post(new AutoScaleRunnable(mInitScale,x,y));
        }
        return true;
    }
});



}

/**
* ��view��ӵ�windowʱ���ã�����onGlobalLayout����˿���������ע�������
*/
@Override
protected void onAttachedToWindow() {
super.onAttachedToWindow();
getViewTreeObserver().addOnGlobalLayoutListener(this);
}

/**
* ��view��window���Ƴ�ʱ���ã���˿����������Ƴ�������
*/
@Override
protected void onDetachedFromWindow() {
super.onDetachedFromWindow();
getViewTreeObserver().removeGlobalOnLayoutListener(this);
}

/**
* �������������仯ʱ����ô˷��������ǿ����ڴ˷����л�ÿؼ��Ŀ�͸�
*/
@Override
public void onGlobalLayout() {
//ֻ�е���һ�μ���ͼƬ��ʱ��Ż���г�ʼ������һ������mFirst����
//if (!mFirst){
//    mFirst = true;
//    //�õ��ؼ��Ŀ�͸�
//    int width = getWidth();
//    int height = getHeight();
//    //�õ���ǰImageView�м��ص�ͼƬ
//    Drawable d = getDrawable();
//    //Bitmap b=getBitmap();
//    if(d == null){//���û��ͼƬ����ֱ�ӷ���
//        return;
//    }
//    //�õ���ǰͼƬ�Ŀ�͸ߣ�ͼƬ�Ŀ�͸߲�һ�����ڿؼ��Ŀ�͸�
//    //���������Ҫ��ͼƬ�Ŀ�͸���ؼ���͸߽����ж�
//    //��ͼƬ��������ʾ����Ļ��
//    int dw = d.getIntrinsicWidth();
//    int dh = d.getIntrinsicHeight();
//    //���Ƕ���һ����ʱ����������ͼƬ��ؼ��Ŀ�߱�������ȷ�������������ֵ
//    float scale = 1.0f;
//    //���ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶�С�ڿؼ��߶�
//    if (dw>width && dh<height){
//        //������Ҫ��ͼƬ�����С����С���ؼ��Ŀ��
//        //����ΪʲôҪ�������㣬���ǿ���������
//        //���ǵ���matrix.postScale��scale,scale��ʱ����͸߶�Ҫ����scale��
//        //��ǰ���ǵ�ͼƬ�����dw��dw*scale=dw*��width/dw��=width,�����͵��ڿؼ������
//        //���ǵĸ߶�ͬʱҲ����scale�������ܹ���֤ͼƬ�Ŀ�߱Ȳ��ı䣬ͼƬ������
//        scale = width * 1.0f / dw;
//
//    }
//    //���ͼƬ�Ŀ��С�ڿؼ���ȣ�ͼƬ�߶ȴ��ڿؼ��߶�
//    if (dw<width && dh>height){
//        //���Ǿ�Ӧ�ý�ͼƬ�ĸ߶���С����С���ؼ��ĸ߶ȣ����㷽��ͬ��
//        scale = height * 1.0f / dh;
//    }
//    //���ͼƬ�Ŀ��С�ڿؼ���ȣ��߶�С�ڿؼ��߶�ʱ������Ӧ�ý�ͼƬ�Ŵ�
//    //����ͼƬ����ǿؼ���ȵ�1/2 ��ͼƬ�߶��ǿؼ��߶ȵ�1/4
//    //������ǽ�ͼƬ�Ŵ�4������ͼƬ�ĸ߶��ǺͿؼ��߶�һ���ˣ�����ͼƬ��Ⱦͳ����ؼ������
//    //�������Ӧ��ѡ��һ����Сֵ���Ǿ��ǽ�ͼƬ�Ŵ�2������ʱͼƬ��ȵ��ڿؼ����
//    //ͬ�����ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶ȴ��ڿؼ��߶ȣ�����Ӧ�ý�ͼƬ��С
//    //��С�ı���ҲӦ��Ϊ�Ǹ���Сֵ
//    if ((dw < width && dh < height) || (dw > width && dh > height)){
//        scale = Math.min(width * 1.0f / dw , height * 1.0f / dh);
//    }
//
//    //���ǻ�Ӧ�ö�ͼƬ����ƽ�Ʋ�������ͼƬ�ƶ�����Ļ�ľ���λ��
//    //�ؼ���ȵ�һ���ȥͼƬ��ȵ�һ�뼴ΪͼƬ��Ҫˮƽ�ƶ��ľ���
//    //�߶�ͬ����ҿ��Ի���ͼ��һ��
//    int dx = width/2 - dw/2;
//    int dy = height/2 - dh/2;
//    //��ͼƬ����ƽ�ƣ�dx��dy�ֱ��ʾˮƽ����ֱ�ƶ��ľ���
//    mScaleMatrix.postTranslate(dx, dy);
//    //��ͼƬ�������ţ�scaleΪ���ŵı���������������Ϊ���ŵ����ĵ�
//    mScaleMatrix.postScale(scale, scale, width / 2, height / 2);
//    //���������������ǵ�ͼƬ�ϣ�ͼƬ�����õ���ƽ�ƺ�����
//    setImageMatrix(mScaleMatrix);
//
//    //��ʼ��һ�����ǵļ������ŵı߽�ֵ
//    mInitScale = scale;
//    //������Ϊ��ʼ������4��
//    mMaxScale = mInitScale * 4;
//    //˫���Ŵ����Ϊ��ʼ��������2��
//    mMidScale = mInitScale * 2;
//    //��С���ű���Ϊ���Ա�����1/4��
//    mMinScale = mInitScale / 4;
//    //������ֵΪ���ֵ��5��
//    mMaxOverScale = mMaxScale * 5;
//
//}
}

/**
* ���ͼƬ��ǰ�����ű���ֵ
*/
private float getScale(){
//MatrixΪһ��3*3�ľ���һ��9��ֵ
float[] values = new float[9];
//��Matrix��9��ֵӳ�䵽values������
mScaleMatrix.getValues(values);
//�õ�Matrix�е�MSCALE_X��ֵ�����ֵΪͼƬ��ȵ����ű�������ΪͼƬ�߶�
//�����ű����Ϳ�ȵ����ű���һ�£�����ȡһ���Ϳ�����
//���ǻ����� return values[Matrix.MSCALE_Y];
return values[Matrix.MSCALE_X];
}

/**
* ������ź�ͼƬ���������������Լ����
*/
private RectF getMatrixRectF(){
//��õ�ǮͼƬ�ľ���
Matrix matrix = mScaleMatrix;
//����һ���������͵ľ���
RectF rectF = new RectF();
//�õ���ǰ��ͼƬ
Drawable d = getDrawable();
if (d != null){
    //ʹ������εĿ�͸�ͬ��ǰͼƬһ��
    rectF.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
    //������ӳ�䵽�������棬֮�����ǿ���ͨ����ȡ��������������������Լ����
    //���õ����ź�ͼƬ��������������Ϳ��
    matrix.mapRect(rectF);
}
return rectF;
}

/**
* ������ʱ���߽粢��ʹͼƬ����
*/
private void checkBorderAndCenterWhenScale(){
if (getDrawable() == null){
    return;
}
//��ʼ��ˮƽ����ֱ�����ƫ����
float deltaX = 0.0f;
float deltaY = 0.0f;
//�õ��ؼ��Ŀ�͸�
int width = getWidth();
int height = getHeight();
//�õ���ǰͼƬ��Ӧ�ľ���
RectF rectF = getMatrixRectF();
//�����ǰͼƬ�Ŀ�ȴ��ڿؼ���ȣ���ǰͼƬ���ڷŴ�״̬
if (rectF.width() >= width){
    //���ͼƬ��������Ǵ���0�ģ�˵��ͼƬ�����ؼ������һ�����룬
    //��߻����һ��С�ױ�
    if (rectF.left > 0){
        //���ǽ�ͼƬ������ƶ�
        deltaX = -rectF.left;
    }
    //���ͼƬ�ұ�����С�ڿؼ���ȣ�˵��ͼƬ�ұ���ؼ��ұ���һ�����룬
    //�ұ߻����һ��С�ױ�
    if (rectF.right <width){
        //���ǽ�ͼƬ���ұ��ƶ�
        deltaX = width - rectF.right;
    }
}
//�����ǵ�����ȣ����ǵ����߶�
if (rectF.height() >= height){
    //����������С�ױߣ��������ƶ�
    if (rectF.top > 0){
        deltaY = -rectF.top;
    }
    //����������С�ױߣ��������ƶ�
    if (rectF.bottom < height){
        deltaY = height - rectF.bottom;
    }
}
//���ͼƬ�Ŀ��С�ڿؼ��Ŀ�ȣ�����Ҫ��ͼƬ��һ��ˮƽ�ľ���
if (rectF.width() < width){
    deltaX = width/2f - rectF.right + rectF.width()/2f;
}

//���ͼƬ�ĸ߶�С�ڿؼ��ĸ߶ȣ�����Ҫ��ͼƬ��һ����ֱ����ľ���
if (rectF.height() < height){
    deltaY = height/2f - rectF.bottom + rectF.height()/2f;
}
//��ƽ�Ƶ�ƫ�������õ�������
mScaleMatrix.postTranslate(deltaX, deltaY);
}

/**
* ƽ��ʱ����������ұ߽�
*/
private void checkBorderWhenTranslate() {
//������ź�ͼƬ����Ӧ����
RectF rectF = getMatrixRectF();
//��ʼ��ˮƽ����ֱ�����ƫ����
float deltaX = 0.0f;
float deltaY = 0.0f;
//�õ��ؼ��Ŀ��
int width = getWidth();
//�õ��ؼ��ĸ߶�
int height = getHeight();
//�������Ҫ�������ұ߽�
if (isCheckLeftAndRight){
    //�����߳��ֵİױ�
    if (rectF.left > 0){
        //����ƫ��
        deltaX = -rectF.left;
    }
    //����ұ߳��ֵİױ�
    if (rectF.right < width){
        //����ƫ��
        deltaX = width - rectF.right;
    }
}
//�������Ҫ����Ϻ��±߽�
if (isCheckTopAndBottom){
    //���������ְױ�
    if (rectF.top > 0){
        //����ƫ��
        deltaY = -rectF.top;
    }
    //���������ְױ�
    if (rectF.bottom < height){
        //����ƫ��
        deltaY = height - rectF.bottom;
    }
}

mScaleMatrix.postTranslate(deltaX,deltaY);
}


/**
* �Զ��Ŵ���С���Զ����ŵ�ԭ����ʹ��View.postDelay()������ÿ��16ms����һ��
* run�����������Ӿ����γ�һ�ֶ�����Ч��
*/


private class AutoScaleRunnable implements Runnable{
//�Ŵ������С��Ŀ�����
private float mTargetScale;
//������BIGGER,Ҳ������SMALLER
private float tempScale;
//�Ŵ���С�����ĵ�
private float x;
private float y;
//��1��΢��һ�㣬���ڷŴ�
private final float BIGGER = 1.07f;
//��1��΢Сһ�㣬������С
private final float SMALLER = 0.93f;
//���췽������Ŀ��������������ĵ㴫�룬�����ж���Ҫ�Ŵ�����С
public AutoScaleRunnable(float targetScale , float x , float y){
    this.mTargetScale = targetScale;
    this.x = x;
    this.y = y;
   
    //�����ǰ���ű���С��Ŀ�������˵��Ҫ�Զ��Ŵ�
    if (getScale()< mTargetScale){
        //����ΪBigger
        tempScale = BIGGER;
        //Log.i("temp","keson");
    }
    //�����ǰ���ű�������Ŀ�������˵��Ҫ�Զ���С
    if (getScale()> mTargetScale){
        //����ΪSmaller
        tempScale = SMALLER;
    }
}
@Override
public void run() {
    //�������ŵı����ǳ�С��ֻ����΢��1��һ����߱�1Сһ��ı���
    //���ǵ�ÿ16ms���Ŵ������Сһ����ʱ�򣬶���Ч���ͳ�����
    mScaleMatrix.postScale(tempScale, tempScale, x, y);
    Log.i("tempdd",String.valueOf(tempScale));
    Log.i("tempdd","keson");
    //ÿ�ν��������õ�ͼƬ֮ǰ�������һ�±߽�
     checkBorderAndCenterWhenScale();
    //���������õ�ͼƬ��
    setImageMatrix(mScaleMatrix);
    //�õ���ǰͼƬ������ֵ
    float currentScale ;
   
    currentScale=getScale();
   
    //�����ǰ��Ҫ�Ŵ󣬲��ҵ�ǰ����ֵС��Ŀ������ֵ
    //����  ��ǰ��Ҫ��С�����ҵ�ǰ����ֵ����Ŀ������ֵ
    if ((tempScale > 1.0f) && currentScale < mTargetScale
            ||(tempScale < 1.0f) && currentScale > mTargetScale){
        //ÿ��16ms�͵���һ��run����
        postDelayed(this,16);
    }else {
        //current*scale=current*(mTargetScale/currentScale)=mTargetScale
        //��֤ͼƬ���յ�����ֵ��Ŀ������ֵһ��
        float scale = mTargetScale / currentScale;
        mScaleMatrix.postScale(scale, scale, x, y);
        checkBorderAndCenterWhenScale();
        setImageMatrix(mScaleMatrix);
        //�Զ����Ž�������Ϊfalse
        isAutoScale = false;
    }
}
}
public void startAnimation(float startscale,float endscale){
	
	ValueAnimator anim=ValueAnimator.ofFloat(startscale,endscale);
	anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		
		@Override
		public void onAnimationUpdate(ValueAnimator arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	
	
}
/**
* ���Ի���
*/
private class FlingRunnable implements Runnable{
private Scroller mScroller;
private int mCurrentX , mCurrentY;

public FlingRunnable(Context context){
    mScroller = new Scroller(context);
}

public void cancelFling(){
    mScroller.forceFinished(true);
}

/**
 * ���������Ҫ�Ǵ�onTouch�л�õ���ǰ������ˮƽ����ֱ������ٶ�
 * ����scroller.fling��������������ڲ��ܹ��Զ�������Ի���
 * ��x��y�ı仯�ʣ���������仯�����ǾͿ��Զ�ͼƬ����ƽ����
 */
public void fling(int viewWidth , int viewHeight , int velocityX ,
                  int velocityY){
    RectF rectF = getMatrixRectF();
    if (rectF == null){
        return;
    }
    //startXΪ��ǰͼƬ��߽��x����
    final int startX = Math.round(-rectF.left);
    final int minX , maxX , minY , maxY;
    //���ͼƬ��ȴ��ڿؼ����
    if (rectF.width() > viewWidth){
        //����һ��������Χ[minX,maxX]���������ͼ
        minX = 0;
        maxX = Math.round(rectF.width() - viewWidth);
    }else{
        //���ͼƬ���С�ڿؼ���ȣ���������
        minX = maxX = startX;
    }
    //���ͼƬ�߶ȴ��ڿؼ��߶ȣ�ͬ��
    final int startY = Math.round(-rectF.top);
    if (rectF.height() > viewHeight){
        minY = 0;
        maxY = Math.round(rectF.height() - viewHeight);
    }else{
        minY = maxY = startY;
    }
    mCurrentX = startX;
    mCurrentY = startY;

    if (startX != maxX || startY != maxY){
        //����fling������Ȼ�����ǿ���ͨ������getCurX��getCurY����õ�ǰ��x��y����
        //�������ļ�����ģ��һ�����Ի�������������ģ����Ǹ������x��y�ı仯����ģ��
        //��ͼƬ�Ĺ��Ի���
        mScroller.fling(startX,startY,velocityX,velocityY,minX,maxX,minY,maxY);
    }

}

/**
 * ÿ��16ms�������������ʵ�ֹ��Ի����Ķ���Ч��
 */
@Override
public void run() {
    if (mScroller.isFinished()){
        return;
    }
    //�������true��˵����ǰ�Ķ�����û�н��������ǿ��Ի�õ�ǰ��x��y��ֵ
    if (mScroller.computeScrollOffset()){
        //��õ�ǰ��x����
        final int newX = mScroller.getCurrX();
        //��õ�ǰ��y����
        final int newY = mScroller.getCurrY();
        //����ƽ�Ʋ���
        mScaleMatrix.postTranslate(mCurrentX-newX , mCurrentY-newY);
        checkBorderWhenTranslate();
        setImageMatrix(mScaleMatrix);

        mCurrentX = newX;
        mCurrentY = newY;
        //ÿ16ms����һ��
        postDelayed(this,16);
    }
}
}

/**
* �����OnScaleGestureListener�еķ�������������������ǿ��Զ�ͼƬ���зŴ���С
*/
@Override
public boolean onScale(ScaleGestureDetector detector) {
//������������ָ���зֿ�����ʱ��˵��������Ҫ�Ŵ����scaleFactor��һ����΢����1����ֵ
//������������ָ���бպϲ���ʱ��˵��������Ҫ��С�����scaleFactor��һ����΢С��1����ֵ
float scaleFactor = detector.getScaleFactor();
//�������ͼƬ��ǰ������ֵ
float scale = getScale();
//�����ǰû��ͼƬ����ֱ�ӷ���
if (getDrawable() == null){
    return true;
}
//���scaleFactor����1��˵����Ŵ󣬵�ǰ�����ű�������scaleFactor֮��С��
//�������ű���ʱ������Ŵ�
//���scaleFactorС��1��˵������С����ǰ�����ű�������scaleFactor֮�����
//��С�����ű���ʱ��������С
if ((scaleFactor > 1.0f && scale * scaleFactor < mMaxOverScale)
        || scaleFactor < 1.0f && scale * scaleFactor > mMinScale){
    //�߽���ƣ������ǰ���ű�������scaleFactor֮��������������ű���
    if (scale * scaleFactor > mMaxOverScale + 0.01f){
        //��scaleFactor���ó�mMaxScale/scale
        //���ٽ���matrix.postScaleʱ
        //scale*scaleFactor=scale*(mMaxScale/scale)=mMaxScale
        //���ͼƬ�ͻ�Ŵ���mMaxScale���ű����Ĵ�С
        scaleFactor = mMaxOverScale / scale;
    }
    //�߽���ƣ������ǰ���ű�������scaleFactor֮��С������С�����ű���
    //���ǲ���������С
    if (scale * scaleFactor < mMinScale + 0.01f){
        //���㷽��ͬ��
        scaleFactor = mMinScale / scale;
    }
    //ǰ�������������ŵı�������һ����΢����1������΢С��1�������γ�һ��������ָ�Ŵ�
    //������С��Ч��
    //detector.getFocusX()��detector.getFocusY()�õ����Ƕ�㴥�ص��е�
    //��������ʵ��������ͼƬ��ĳһ���ֲ��Ŵ��Ч��
    mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
    //��ΪͼƬ�����ŵ㲻��ͼƬ�����ĵ��ˣ�����ͼƬ�����ƫ�Ƶ��������Խ���һ�α߽�ļ��;��в���
    checkBorderAndCenterWhenScale();
    //���������õ�ͼƬ��
    setImageMatrix(mScaleMatrix);
}
return true;
}

/**
* һ��Ҫ����true
*/
@Override
public boolean onScaleBegin(ScaleGestureDetector detector) {
	
	
	
return true;
}

@Override
public void onScaleEnd(ScaleGestureDetector detector) {

}

@Override
public boolean onTouch(View v, MotionEvent event) {
//��˫������ʱ���������ƶ�ͼƬ��ֱ�ӷ���true
	if (!mFirst){
	    mFirst = true;
	    //�õ��ؼ��Ŀ�͸�
	    int width = getWidth();
	    int height = getHeight();
	    //�õ���ǰImageView�м��ص�ͼƬ
	    Drawable d = getDrawable();
	    //Bitmap b=getBitmap();
	    if(d == null){//���û��ͼƬ����ֱ�ӷ���
	        return true;
	    }
	    //�õ���ǰͼƬ�Ŀ�͸ߣ�ͼƬ�Ŀ�͸߲�һ�����ڿؼ��Ŀ�͸�
	    //���������Ҫ��ͼƬ�Ŀ�͸���ؼ���͸߽����ж�
	    //��ͼƬ��������ʾ����Ļ��
	    int dw = d.getIntrinsicWidth();
	    int dh = d.getIntrinsicHeight();
	    //���Ƕ���һ����ʱ����������ͼƬ��ؼ��Ŀ�߱�������ȷ�������������ֵ
	    float scale = 1.0f;
	    //���ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶�С�ڿؼ��߶�
	    if (dw>width && dh<height){
	        //������Ҫ��ͼƬ�����С����С���ؼ��Ŀ��
	        //����ΪʲôҪ�������㣬���ǿ���������
	        //���ǵ���matrix.postScale��scale,scale��ʱ����͸߶�Ҫ����scale��
	        //��ǰ���ǵ�ͼƬ�����dw��dw*scale=dw*��width/dw��=width,�����͵��ڿؼ������
	        //���ǵĸ߶�ͬʱҲ����scale�������ܹ���֤ͼƬ�Ŀ�߱Ȳ��ı䣬ͼƬ������
	        scale = width * 1.0f / dw;

	    }
	    //���ͼƬ�Ŀ��С�ڿؼ���ȣ�ͼƬ�߶ȴ��ڿؼ��߶�
	    if (dw<width && dh>height){
	        //���Ǿ�Ӧ�ý�ͼƬ�ĸ߶���С����С���ؼ��ĸ߶ȣ����㷽��ͬ��
	        scale = height * 1.0f / dh;
	    }
	    //���ͼƬ�Ŀ��С�ڿؼ���ȣ��߶�С�ڿؼ��߶�ʱ������Ӧ�ý�ͼƬ�Ŵ�
	    //����ͼƬ����ǿؼ���ȵ�1/2 ��ͼƬ�߶��ǿؼ��߶ȵ�1/4
	    //������ǽ�ͼƬ�Ŵ�4������ͼƬ�ĸ߶��ǺͿؼ��߶�һ���ˣ�����ͼƬ��Ⱦͳ����ؼ������
	    //�������Ӧ��ѡ��һ����Сֵ���Ǿ��ǽ�ͼƬ�Ŵ�2������ʱͼƬ��ȵ��ڿؼ����
	    //ͬ�����ͼƬ��ȴ��ڿؼ���ȣ�ͼƬ�߶ȴ��ڿؼ��߶ȣ�����Ӧ�ý�ͼƬ��С
	    //��С�ı���ҲӦ��Ϊ�Ǹ���Сֵ
	    if ((dw < width && dh < height) || (dw > width && dh > height)){
	        scale = Math.min(width * 1.0f / dw , height * 1.0f / dh);
	    }

	    //���ǻ�Ӧ�ö�ͼƬ����ƽ�Ʋ�������ͼƬ�ƶ�����Ļ�ľ���λ��
	    //�ؼ���ȵ�һ���ȥͼƬ��ȵ�һ�뼴ΪͼƬ��Ҫˮƽ�ƶ��ľ���
	    //�߶�ͬ����ҿ��Ի���ͼ��һ��
	    int dx = width/2 - dw/2;
	    int dy = height/2 - dh/2;
	    //��ͼƬ����ƽ�ƣ�dx��dy�ֱ��ʾˮƽ����ֱ�ƶ��ľ���
        mScaleMatrix.postTranslate(dx, dy);
	    //��ͼƬ�������ţ�scaleΪ���ŵı���������������Ϊ���ŵ����ĵ�
	    mScaleMatrix.postScale(scale, scale, width / 2, height / 2);
	    //���������������ǵ�ͼƬ�ϣ�ͼƬ�����õ���ƽ�ƺ�����
	    setImageMatrix(mScaleMatrix);

	    //��ʼ��һ�����ǵļ������ŵı߽�ֵ
	    mInitScale = scale;
	    //������Ϊ��ʼ������4��
	    mMaxScale = mInitScale * 4;
	    //˫���Ŵ����Ϊ��ʼ��������2��
	    mMidScale = mInitScale * 2;
	    //��С���ű���Ϊ���Ա�����1/4��
	    mMinScale = mInitScale / 4;
	    //������ֵΪ���ֵ��5��
	    mMaxOverScale = mMaxScale * 5;
	    Log.i("scaless",String.valueOf(scale));
	}

	setScaleType(ScaleType.MATRIX);
if (mGestureDetector.onTouchEvent(event)){
    return true;
}
//���¼����ݸ�ScaleGestureDetector
mScaleGestureDetector.onTouchEvent(event);
//���ڴ洢��㴥�ز���������
float x = 0.0f;
float y = 0.0f;
//�õ���㴥�صĸ���
int pointerCount = event.getPointerCount();
//�����д��ص�������ۼ�����
for(int i=0 ; i<pointerCount ; i++){
    x += event.getX(i);
    y += event.getY(i);
}
//ȡƽ��ֵ���õ��ľ��Ƕ�㴥�غ�������Ǹ��������
x /= pointerCount;
y /= pointerCount;
//������ص���������ˣ�����Ϊ���ɻ���
if (mLastPointerCount != pointerCount){
    isCanDrag = false;
    mLastX = x;
    mLastY = y;
}
mLastPointerCount = pointerCount;
RectF rectF = getMatrixRectF();
switch (event.getAction()){
    case MotionEvent.ACTION_DOWN:
        //��ʼ���ٶȼ����
        mVelocityTracker = VelocityTracker.obtain();
        if (mVelocityTracker != null){
            //����ǰ���¼���ӵ��������
            mVelocityTracker.addMovement(event);
        }
        //����ָ�ٴε����ͼƬʱ��ֹͣͼƬ�Ĺ��Ի���
        if (mFlingRunnable != null){
            mFlingRunnable.cancelFling();
            mFlingRunnable = null;
        }
        isCanDrag = false;
        //��ͼƬ���ڷŴ�״̬ʱ����ֹViewPager�����¼������¼����ݸ�ͼƬ�������϶�
        if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01f){
            if (getParent() instanceof ViewPager){
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        break;
    case MotionEvent.ACTION_MOVE:
        //��ͼƬ���ڷŴ�״̬ʱ����ֹViewPager�����¼������¼����ݸ�ͼƬ�������϶�
        if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01f){
            if (getParent() instanceof ViewPager){
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
        //�õ�ˮƽ����ֱ�����ƫ����
        float dx = x - mLastX;
        float dy = y - mLastY;
        //�����ǰ�ǲ��ɻ�����״̬���ж�һ���Ƿ��ǻ����Ĳ���
        if (!isCanDrag){
            isCanDrag = isMoveAction(dx,dy);
        }
        //����ɻ���
        if (isCanDrag){
            if (getDrawable() != null){

                if (mVelocityTracker != null){
                    //����ǰ�¼���ӵ��������
                    mVelocityTracker.addMovement(event);
                }

                isCheckLeftAndRight = true;
                isCheckTopAndBottom = true;
                //���ͼƬ���С�ڿؼ����
                if (rectF.width() < getWidth()){
                    //���Ҳ��ɻ���
                    dx = 0;
                    //���Ҳ��ɻ�����Ҳ�Ͳ��ü�����ҵı߽���
                    isCheckLeftAndRight = false;
                }
                //���ͼƬ�ĸ߶�С�ڿؼ��ĸ߶�
                if (rectF.height() < getHeight()){
                    //���²��ɻ���
                    dy = 0;
                    //���²��ɻ�����Ҳ�Ͳ��ü�����±߽���
                    isCheckTopAndBottom = false;
                }
            }
            mScaleMatrix.postTranslate(dx,dy);
            //��ƽ��ʱ������������ұ߽�
            checkBorderWhenTranslate();
            setImageMatrix(mScaleMatrix);
        }
        mLastX = x;
        mLastY = y;
        break;
    case MotionEvent.ACTION_UP:
        //����ָ̧��ʱ����mLastPointerCount��0��ֹͣ����
        mLastPointerCount = 0;
        //�����ǰͼƬ��СС�ڳ�ʼ����С
        if (getScale() < mInitScale){
            //�Զ��Ŵ�����ʼ����С
            post(new AutoScaleRunnable(mInitScale,getWidth()/2,getHeight()/2));
        }
        //�����ǰͼƬ��С�������ֵ
        if (getScale() > mMaxScale){
            //�Զ���С�����ֵ
            post(new AutoScaleRunnable(mMaxScale,getWidth()/2,getHeight()/2));
        }
        if (isCanDrag){//�����ǰ���Ի���
            if (mVelocityTracker != null){
                //����ǰ�¼���ӵ��������
                mVelocityTracker.addMovement(event);
                //���㵱ǰ���ٶ�
                mVelocityTracker.computeCurrentVelocity(1000);
                //�õ���ǰx�����ٶ�
                final float vX = mVelocityTracker.getXVelocity();
                //�õ���ǰy������ٶ�
                final float vY = mVelocityTracker.getYVelocity();
                mFlingRunnable = new FlingRunnable(getContext());
                //����fling����������ؼ���ߺ͵�ǰx��y�᷽����ٶ�
                //����õ���vX��vY��scroller��Ҫ��velocityX��velocityY�ĸ��������෴
                //���Դ���һ����ֵ
                mFlingRunnable.fling(getWidth(),getHeight(),(int)-vX,(int)-vY);
                //ִ��run����
                post(mFlingRunnable);
            }
        }
        break;
    case MotionEvent.ACTION_CANCEL:
        //�ͷ��ٶȼ����
        if (mVelocityTracker != null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        break;
}
return true;
}


/**
* �ж��Ƿ����ƶ��Ĳ���
*/
private boolean isMoveAction(float dx , float dy){
//���ɶ����ж�б���Ƿ���ڿɻ�����һ���ٽ�ֵ
return Math.sqrt(dx*dx + dy*dy) > mTouchSlop;
}
}
