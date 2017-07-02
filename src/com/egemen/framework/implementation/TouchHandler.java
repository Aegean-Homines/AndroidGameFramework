package com.egemen.framework.implementation;

import java.util.List;

import com.egemen.framework.Input.TouchEvent;


import android.view.View.OnTouchListener;

public interface TouchHandler extends OnTouchListener {

	boolean isTouchDown(int pointer);

	int getTouchX(int pointer);

	int getTouchY(int pointer);

	List<TouchEvent> getTouchEvents();

}
