package com.example.browser;


import com.example.memory10.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;



public class ZoomTutorial {
	
	final private int mAnimationDuration = 300;
	private Animator mCurrentAnimator;
	
	private View mContainView;
	private ViewGroup mThumbViewParent;
	private View mExpandedView;
	
	private Rect startBounds;
	private float startScale;
	private float startScaleFinal;
	
	public ZoomTutorial(View containerView,View expandedView) {
		mContainView = containerView;
		mExpandedView = expandedView;
	}
	
	
	public void zoomImageFromThumb(final View thumbView) {
		mThumbViewParent = (ViewGroup) thumbView.getParent();
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		thumbView.getGlobalVisibleRect(startBounds);
		mContainView.getGlobalVisibleRect(finalBounds, globalOffset);

		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		set_Center_crop(finalBounds);
		
		mExpandedView.setVisibility(View.VISIBLE);
		
		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(1);
		animSet.play(ObjectAnimator.ofFloat(mExpandedView, "pivotX", 0f))
		.with(ObjectAnimator.ofFloat(mExpandedView, "pivotY", 0f))
		.with(ObjectAnimator.ofFloat(mExpandedView, "alpha", 1.0f));
		animSet.start();
 
        startZoomAnim(mExpandedView, startBounds, finalBounds, startScale);
		startScaleFinal = startScale;
	}
	
	
	private void set_Center_crop(Rect finalBounds) {
		if ((float) finalBounds.width() / finalBounds.height() > (float) 
				startBounds.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
	}

	
	public void startZoomAnim(View v, Rect startBounds, Rect finalBounds, float startScale) {
		AnimatorSet set = new AnimatorSet();
		set.play(
			ObjectAnimator.ofFloat(v, "x", startBounds.left, finalBounds.left))
			.with(ObjectAnimator.ofFloat(v, "y", startBounds.top, finalBounds.top))
			.with(ObjectAnimator.ofFloat(v, "scaleX", startScale, 1f))
			.with(ObjectAnimator.ofFloat(v, "scaleY", startScale, 1f));
		
		set.setDuration(mAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
				if (listener != null) {
					listener.onExpanded();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
				if (listener != null) {
					listener.onExpanded();
				}
			}
		});
		set.start();
		mCurrentAnimator = set;
	}

	
	public boolean getScaleFinalBounds(int position) {
		
		int firstPosition = ((AdapterView<?>) mThumbViewParent).getFirstVisiblePosition();
		//int firstPosition=1;
		View childView = mThumbViewParent.getChildAt(position - firstPosition);
		
		startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();
		
		try {
			
			childView.getGlobalVisibleRect(startBounds);
		} catch (Exception e) {
			return false;
		}
		mContainView.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);
		set_Center_crop(finalBounds);
		
		startScaleFinal = startScale;
		return true;
	}
	
	public void closeZoomAnim(int position) {
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}
		AnimatorSet set = new AnimatorSet();
		
		boolean isInBound = getScaleFinalBounds(position);
		if (isInBound) {
			set.play(ObjectAnimator.ofFloat(mExpandedView, "x", startBounds.left))
				.with(ObjectAnimator.ofFloat(mExpandedView, "y", startBounds.top))
				.with(ObjectAnimator.ofFloat(mExpandedView, "scaleX", startScaleFinal))
				.with(ObjectAnimator.ofFloat(mExpandedView, "scaleY", startScaleFinal));
		} else {
			
			ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mExpandedView, "alpha", 0.1f);
			set.play(alphaAnimator);
		}
		set.setDuration(mAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mExpandedView.clearAnimation();
				mExpandedView.setVisibility(View.GONE);
				mCurrentAnimator = null;
				if (listener != null) {
					listener.onThumbed();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mExpandedView.clearAnimation();
				mExpandedView.setVisibility(View.GONE);
				mCurrentAnimator = null;
				if (listener != null) {
					listener.onThumbed();
				}
			}
		});
		set.start();
		mCurrentAnimator = set;
	}
	
	private OnZoomListener listener;
	
	public void setOnZoomListener(OnZoomListener l) {
		listener = l;
	}
	
	public interface OnZoomListener {
		public void onExpanded();
		public void onThumbed();
	}
	
}
