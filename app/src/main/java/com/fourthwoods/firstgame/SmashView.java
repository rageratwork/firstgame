package com.fourthwoods.firstgame;

/**
 * Copyright (c) 2013 David J. Rager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;

import com.fourthwoods.firstgame.shapes.Letter;
import com.fourthwoods.firstgame.shapes.Point;
import com.fourthwoods.firstgame.shapes.Shape;

public class SmashView extends GameView {
	private List<Shape> shapes = new ArrayList<Shape>();
	Rect rect = new Rect();

	Bitmap background = null;
	
	private int idle = 1000;
	private boolean started = false;
	
	public SmashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	    Resources res = getResources();
	    Options options = new BitmapFactory.Options();
	    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    background = BitmapFactory.decodeResource(res, R.drawable.chalkboard);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
	}

	@Override
	public void render(Canvas canvas)
	{
		if(canvas == null)
			return;
		
		if(idle > 500)
		{
			playIdle();
			idle = 0;
		}
		idle++;
		
		rect.left = 0;
		rect.top = 0;
		rect.right = getWidth();
		rect.bottom = getHeight();
		
		if(background != null)
			canvas.drawBitmap(background, null, rect, null);
		
		synchronized(shapes)
		{
			for(Shape shape : shapes)
			{
				shape.draw(canvas);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Point p = new Point((int)event.getX(), (int)event.getY());
		Shape s = getShape(p);
		idle = 0;
		
		if(s != null)
		{
			playSound(s);
			
			synchronized(shapes)
			{
				shapes.add(s);
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void advanceFrame()
	{
		List<Shape> remove = new ArrayList<Shape>();
		
		synchronized(shapes)
		{
			for(Shape shape : shapes)
			{
				shape.advanceFrame();
				if(!shape.isVisible())
					remove.add(shape);
			}
			
			if(!remove.isEmpty())
				shapes.removeAll(remove);
		}
	}

	private Shape getShape(Point p)
	{
		Shape s = null;
		do
		{
			int rand = random.nextInt(3);
			switch(rand)
			{
			case 0:
				s = getShape(p, Shape.Animation.SMASH);
				break;
			case 1:
				s = getNumber(p, Shape.Animation.SMASH);
				break;
			case 2:
			default:
				s = getLetter(p, Shape.Animation.SMASH);
				break;
			}
		} while(s == null);

		return s;
	}
	
	private void playSound(final Shape s)
	{
		int rand = random.nextInt(3);
		if(Settings.voice_laughter == false)
			rand++;
		
		switch(rand)
		{
		case 0:
			playGiggle();
			break;
		default:
			{
				final SparseIntArray colors;
				final SparseIntArray numbers;
				final SparseIntArray letters;
				@SuppressWarnings("rawtypes")
				final Map<Class, Integer> shapes;
				
				rand = random.nextInt(2);
				switch(rand)
				{
				case 0:
					colors = Sounds.karaColors;
					shapes = Sounds.karaShapes;
					numbers = Sounds.karaNumbers;
					letters = Sounds.karaLetters;
					break;
				default:
					colors = Sounds.lexieColors;
					shapes = Sounds.lexieShapes;
					numbers = Sounds.lexieNumbers;
					letters = Sounds.lexieLetters;
					break;
				}
				
				int color = colors.get(s.getFillColor());
				MediaPlayer mpColor = Sounds.create(getContext(), color);
				
				int resid = -1;
				if(s instanceof com.fourthwoods.firstgame.shapes.Number)
				{
					if(Settings.voice_numbers)
						resid = numbers.get(((com.fourthwoods.firstgame.shapes.Number)s).getNumber());
				}
				else if(s instanceof Letter)
				{
					if(Settings.voice_letters)
						resid = letters.get(((Letter)s).getLetter());
				}
				else
				{
					if(Settings.voice_shapes)
						resid = shapes.get(s.getClass());
				}
				
				if(Settings.voice_colors)
				{
					if(resid != -1)
					{
						final int item = resid;
						mpColor.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer mp)
							{
								MediaPlayer mpItem = Sounds.create(getContext(), item);
								mpItem.start();
							}
						});
					}
					mpColor.start();
				}
				else if(resid != -1)
				{
					MediaPlayer mpItem = Sounds.create(getContext(), resid);
					mpItem.start();
				}
				else if(Settings.voice_laughter)
				{
					playGiggle();
				}
			}
			break;
		}
	}
	
	private void playIdle()
	{
		final int rand = random.nextInt(2);
		int resid;
		switch(rand)
		{
		case 0:
			resid = R.raw.kara_touch_the_screen;
			break;
		default:
			resid = R.raw.lexie_touch_the_screen;
			break;
		}
		
		MediaPlayer mp = Sounds.create(getContext(), resid);
		if(mp != null)
		{
			if(started == false)
			{
				mp.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						int resid;
						switch(rand)
						{
						case 0:
							resid = R.raw.kara_to_start;
							break;
						default:
							resid = R.raw.lexie_to_start;
							break;
						}
						MediaPlayer s = Sounds.create(getContext(), resid);
						s.start();
					}
				});
				started = true;
			}
			mp.start();
		}
	}
	
	private void playGiggle()
	{
		int rand = random.nextInt(Sounds.giggles.size());
		int resid = Sounds.giggles.get(rand);
		MediaPlayer mp = Sounds.create(getContext(), resid);
		mp.start();
	}
}
