package com.leepresswood.geonode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class CrossFadeActivity
{

	private View start;
	private View end;
	private int duration;

	public CrossFadeActivity(View start, View end)
	{
		this.start = start;
		this.end = end;
	}

	public void crossfade()
	{
		// Set the content view to 0% opacity but visible, so that it is visible
		// (but fully transparent) during the animation.
		end.setAlpha(0f);
		end.setVisibility(View.VISIBLE);

		// Animate the content view to 100% opacity, and clear any animation
		// listener set on the view.
		end.animate()
			.alpha(1f)
			.setDuration(duration)
			.setListener(null);

		// Animate the loading view to 0% opacity. After the animation ends,
		// set its visibility to GONE as an optimization step (it won't
		// participate in layout passes, etc.)
		start.animate()
			.alpha(0f)
			.setDuration(duration)
			.setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					start.setVisibility(View.GONE);
				}
			});
	}
}