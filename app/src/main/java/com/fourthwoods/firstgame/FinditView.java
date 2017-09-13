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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;

import com.fourthwoods.firstgame.shapes.FaceShape;
import com.fourthwoods.firstgame.shapes.Letter;
import com.fourthwoods.firstgame.shapes.Point;
import com.fourthwoods.firstgame.shapes.Shape;

public class FinditView extends GameView {
	private List<Shape> shapes = new ArrayList<Shape>();
	private static final Random random = new SecureRandom();

	private static final int PAUSED = 1;
	private static final int STARTED = 2;
	
	private int state = PAUSED;
	private Shape target;
	Rect rect = new Rect();

	Bitmap background = null;
	
	private int idle = 500;
	private int tries = 0;
	
	public FinditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	    Resources res = getResources();
	    background = BitmapFactory.decodeResource(res, R.drawable.chalkboard);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
	}

	public void resume()
	{
		if(state == STARTED)
			playTarget();
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
		idle = 0;
		
		if(state == PAUSED)
			restart();
		else if(state == STARTED)
			hitTest(new Point(event.getX(), event.getY()));
		
		return super.onTouchEvent(event);
	}
	
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
	
	private void hitTest(Point p)
	{
		synchronized(shapes)
		{
			for(Shape shape : shapes)
			{
				if(shape.hitTest(p))
				{
					if(shape == target)
					{
						shape.setScale(4.0);
						state = PAUSED;
						playSuccess();
						
						idle = 400;
					}
					else
					{
						if(shape instanceof FaceShape)
						{
							FaceShape s = (FaceShape)shape;
							s.sayNo();
						}
						
						playTryAgain();
					}
				}
			}
		}
	}
	
	public void restart()
	{
		int rows = 2;
		int cols = 4;
		
		switch(Settings.difficulty)
		{
		case 1: 
			rows = 2;
			cols = 2;
			break;
		case 2:
			rows = 2;
			cols = 3;
			break;
		case 3:
			rows = 2;
			cols = 4;
			break;
		default:
			break;
		}

		int num = rows * cols;
		
		double x = getWidth() / cols;
		double y = getHeight() / rows;
		Point p = new Point(getWidth() / 2, getHeight() / 2);
		
		synchronized(shapes)
		{
			shapes.clear();
			
			target = getShape(new Point(0, 0), (Shape)null);
			
			Shape s;
			
			for(int i = 0; i < rows; i++)
			{
				for(int j = 0; j < cols; j++)
				{
					do
					{
						s = getShape(p, target);
						
						if((Settings.difficulty == 1) && (s.getFillColor() ==  target.getFillColor()))
							continue;
						else if(Settings.difficulty == 2)
						{
							if(target instanceof com.fourthwoods.firstgame.shapes.Number)
							{
								com.fourthwoods.firstgame.shapes.Number n = (com.fourthwoods.firstgame.shapes.Number)s;
								com.fourthwoods.firstgame.shapes.Number t = (com.fourthwoods.firstgame.shapes.Number)target;
								
								if(t.getNumber() == n.getNumber())
								{
									continue;
								}
							}
							else if(target instanceof Letter)
							{
								Letter l = (Letter)s;
								Letter t = (Letter)target;
								
								if(t.getLetter() == l.getLetter())
								{
									continue;
								}
							}
							else if(target.getClass() == s.getClass())
							{
								continue;
							}
						}
						else if(Settings.difficulty == 3)
						{
							if(s.getFillColor() !=  target.getFillColor())
								break;
							
							if(target instanceof com.fourthwoods.firstgame.shapes.Number)
							{
								com.fourthwoods.firstgame.shapes.Number n = (com.fourthwoods.firstgame.shapes.Number)s;
								com.fourthwoods.firstgame.shapes.Number t = (com.fourthwoods.firstgame.shapes.Number)target;
								
								if(t.getNumber() == n.getNumber())
									continue;
							}
							else if(target instanceof Letter)
							{
								Letter l = (Letter)s;
								Letter t = (Letter)target;
								
								if(t.getLetter() == l.getLetter())
									continue;
							}
							else if(target.getClass() == s.getClass())
								continue;
						}
						break;
					}
					while(true);
						
					s.setScale(3.0);
					
					s.setDx((x * j - p.x + x / 2) / 30.0);
					s.setDy((y * i - p.y + y / 2) / 30.0);
					
					shapes.add(s);
				}
			}
			
			num = random.nextInt(num);
			
			s = shapes.get(num);
			target.setScale(s.getScale());
			target.setCenter(s.getCenter());
			target.setDx(s.getDx());
			target.setDy(s.getDy());
			
			shapes.remove(s);
			shapes.add(target);
		}
		
		state = STARTED;
		playTarget();
	}
	
	private Shape getShape(Point p, Shape target)
	{
		Shape s = null;
		int rand;
		
		do
		{
			if(target == null)
			{
				rand = random.nextInt(3);
			}
			else
			{
				if(target instanceof Letter)
					rand = 2;
				else if(target instanceof com.fourthwoods.firstgame.shapes.Number)
					rand = 1;
				else
					rand = 0;
			}
			switch(rand)
			{
			case 0:
				s = getShape(p, Shape.Animation.FINDIT);
				break;
			case 1:
				s = getNumber(p, Shape.Animation.FINDIT);
				break;
			case 2:
			default:
				s = getLetter(p, Shape.Animation.FINDIT);
				break;
			}
		} while(s == null);

		return s;
	}
	
	private void playIdle()
	{
		Sounds.killAllSounds();
		
		final int rand = random.nextInt(2);
		final int touch;
		final int start;
		
		switch(rand)
		{
		case 0:
			touch = R.raw.kara_touch_the_screen;
			start = R.raw.kara_to_start;
			break;
		default:
			touch = R.raw.lexie_touch_the_screen;
			start = R.raw.lexie_to_start;
			break;
		}
		
		MediaPlayer mp;
		if (state == PAUSED)
		{
			mp = Sounds.create(getContext(), touch);
		}
		else if(state == STARTED)
		{
			playTarget();
			return;
		}
		else
		{
			return;
		}
		
		if(mp != null)
		{
			mp.setOnCompletionListener(new OnCompletionListener(){
				public void onCompletion(MediaPlayer mp)
				{
					MediaPlayer mp2;
					mp2 = Sounds.create(getContext(), start);
					mp2.start();
				}
			});

			mp.start();
		}
	}
	
	private void playTarget()
	{
		Sounds.killAllSounds();
		
		final int rand = random.nextInt(2);
		final int findthe;
		final int color;
		final int shape;
		final int letter;
		final int number;
		final SparseIntArray numbers;
		final SparseIntArray letters;

		switch(rand)
		{
		case 0:
			findthe = R.raw.kara_find_the;
			color = Sounds.karaColors.get(target.getFillColor());
			shape = R.raw.kara_shape;
			letter = R.raw.kara_letter;
			number = R.raw.kara_number;
			letters = Sounds.karaLetters;
			numbers = Sounds.karaNumbers;
			break;
		default:
			findthe = R.raw.lexie_find_the;
			color = Sounds.lexieColors.get(target.getFillColor());
			shape = R.raw.lexie_shape;
			letter = R.raw.lexie_letter;
			number = R.raw.lexie_number;
			letters = Sounds.lexieLetters;
			numbers = Sounds.lexieNumbers;
			break;
		}
		
		MediaPlayer mp = Sounds.create(getContext(), findthe);
		if(mp != null)
		{
			if(Settings.difficulty == 1) // color
			{
				mp.setOnCompletionListener(new OnCompletionListener()
				{
					public void onCompletion(MediaPlayer mp)
					{
						MediaPlayer mp2 = Sounds.create(getContext(), color);
						mp2.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer mp)
							{
								MediaPlayer mp2;
								if(target instanceof com.fourthwoods.firstgame.shapes.Number)
								{
									mp2 = Sounds.create(getContext(), number);
								}
								else if(target instanceof Letter)
								{
									mp2 = Sounds.create(getContext(), letter);
								}
								else
								{
									mp2 = Sounds.create(getContext(), shape);
								}

								mp2.start();
							}
						});
						mp2.start();
					}
				});
			}
			if(Settings.difficulty == 2) // shape
			{
				mp.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						MediaPlayer mp2;
						if(target instanceof com.fourthwoods.firstgame.shapes.Number)
						{
							mp2 = Sounds.create(getContext(), number);
							mp2.setOnCompletionListener(new OnCompletionListener(){
								public void onCompletion(MediaPlayer mp)
								{
									int resid = numbers.get(((com.fourthwoods.firstgame.shapes.Number)target).getNumber());
									MediaPlayer mp2 = Sounds.create(getContext(), resid);

									mp2.start();
								}
							});
						}
						else if(target instanceof Letter)
						{
							mp2 = Sounds.create(getContext(), letter);
							mp2.setOnCompletionListener(new OnCompletionListener(){
								public void onCompletion(MediaPlayer mp)
								{	
									int resid = letters.get(((Letter)target).getLetter());
									MediaPlayer mp2 = Sounds.create(getContext(), resid);

									mp2.start();
								}
							});
						}
						else
						{
							int shape;
							switch(rand)
							{
							case 0:
								shape = Sounds.karaShapes.get(target.getClass());
								break;
							default:
								shape = Sounds.lexieShapes.get(target.getClass());
								break;
							}
							
							mp2 = Sounds.create(getContext(), shape);
						}

						mp2.start();
					}
				});
			}
			else if(Settings.difficulty == 3) // color and shape
			{
				mp.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						MediaPlayer mp2 = Sounds.create(getContext(), color);
						mp2.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer mp)
							{
								MediaPlayer mp2;
								if(target instanceof com.fourthwoods.firstgame.shapes.Number)
								{
									mp2 = Sounds.create(getContext(), number);
									mp2.setOnCompletionListener(new OnCompletionListener(){
										public void onCompletion(MediaPlayer mp)
										{
											int resid = numbers.get(((com.fourthwoods.firstgame.shapes.Number)target).getNumber());
											MediaPlayer mp2 = Sounds.create(getContext(), resid);

											mp2.start();
										}
									});
								}
								else if(target instanceof Letter)
								{
									mp2 = Sounds.create(getContext(), letter);
									mp2.setOnCompletionListener(new OnCompletionListener(){
										public void onCompletion(MediaPlayer mp)
										{	
											int resid = letters.get(((Letter)target).getLetter());
											MediaPlayer mp2 = Sounds.create(getContext(), resid);

											mp2.start();
										}
									});
								}
								else
								{
									int shape;
									switch(rand)
									{
									case 0:
										shape = Sounds.karaShapes.get(target.getClass());
										break;
									default:
										shape = Sounds.lexieShapes.get(target.getClass());
										break;
									}
									
									mp2 = Sounds.create(getContext(), shape);
								}
								
								mp2.start();
							}
						});
						mp2.start();
					}
				});
			}
			
			mp.start();
		}
	}

	private void playTryAgain()
	{
		Sounds.killAllSounds();
		
		int rand = random.nextInt(Sounds.no.size());
		int resid = Sounds.no.get(rand);
		MediaPlayer mp = Sounds.create(getContext(), resid);
		mp.setOnCompletionListener(new OnCompletionListener(){
			public void onCompletion(MediaPlayer mp)
			{
				tries++;
				if(tries == 3)
				{
					playTarget();
					tries = 0;
				}
			}
		});
		
		mp.start();
	}
	
	private void playSuccess()
	{
		Sounds.killAllSounds();
		
		int rand = random.nextInt(Sounds.yes.size());
		int resid = Sounds.yes.get(rand);
		MediaPlayer mp = Sounds.create(getContext(), resid);
		mp.start();
		tries = 0;
	}
}
