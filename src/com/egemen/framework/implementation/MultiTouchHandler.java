package com.egemen.framework.implementation;

import java.util.ArrayList;
import java.util.List;

import com.egemen.framework.Pool;
import com.egemen.framework.Input.TouchEvent;
import com.egemen.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;

public class MultiTouchHandler implements TouchHandler {
	private static final int MAX_TOUCHPOINTS = 10;
	// if users are normal human beings with 10 fingers and/or they don't have
	// any fantasies regarding to playing the game with their toes, 10 points
	// should be enough

	boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
	int[] touchX = new int[MAX_TOUCHPOINTS];
	int[] touchY = new int[MAX_TOUCHPOINTS];
	int[] id = new int[MAX_TOUCHPOINTS];

	Pool<TouchEvent> touchEventsPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();

	float scaleX, scaleY;

	public MultiTouchHandler(View view, float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {

			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventsPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			if( index < 0 || index >= MAX_TOUCHPOINTS)
				return false;
			else
				return isTouched[index];
		}
	}

	private int getIndex(int pointerId) {
		for(int i = 0; i < MAX_TOUCHPOINTS; i++)
		{
			if (id[i] == pointerId){
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			//TODO: Check if we can simply use event.getActionMasked() instead of this madness
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			//TODO: Again, check if we can use event.getActionIndexMasked()
			int pointerCount = event.getPointerCount();  //# of fingers on screen, always >= 1 (event will occur if at least one finger is touching on the screen)
			TouchEvent touchEvent;
			for(int i = 0 ; i < MAX_TOUCHPOINTS; i++)
			{
				if(i >= pointerCount){
					isTouched[i] = false;
					id[i] = -1;
					continue;
				}
				
				int pointerId = event.getPointerId(i);
				if(event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex){
					//checking if it's an up/down/cancel/out event to mask the id to see if we should process it for this point
					continue;
				}
				
				switch (action){
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					touchEvent = touchEventsPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
					touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
					isTouched[i] = true;
					id[i] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;		
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					touchEvent = touchEventsPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_UP;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
					touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
					isTouched[i] = false;
					id[i] = -1;
					touchEventsBuffer.add(touchEvent);
					break;
				case MotionEvent.ACTION_MOVE:
					touchEvent = touchEventsPool.newObject();
					touchEvent.type = TouchEvent.TOUCH_DRAGGED;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = true;
                    id[i] = pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;
				}
			}
			return true;			
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			if(index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else 
				return touchX[index];
			
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			if(index < 0 || index >= MAX_TOUCHPOINTS)
				return 0;
			else 
				return touchY[index];
			
			
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventsPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
			
		}
	}

}
