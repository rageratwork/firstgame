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
import java.util.Map;
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

import com.fourthwoods.firstgame.shapes.Cursor;
import com.fourthwoods.firstgame.shapes.Droplet;
import com.fourthwoods.firstgame.shapes.FaceShape;
import com.fourthwoods.firstgame.shapes.Letter;
import com.fourthwoods.firstgame.shapes.Line;
import com.fourthwoods.firstgame.shapes.Point;
import com.fourthwoods.firstgame.shapes.Shape;
import com.fourthwoods.firstgame.shapes.Shape.Animation;
import com.fourthwoods.firstgame.shapes.Word;

public class SlashView extends GameView {
	private List<Shape> shapes = new ArrayList<Shape>();
	private List<Shape> cursor = new ArrayList<Shape>();
	private List<Shape> splash = new ArrayList<Shape>();
	
	private Word targetLabel = null;
	private Word scoreLabel = null;
	
	private Shape target = null;
	private com.fourthwoods.firstgame.shapes.Number score = null;
	
	private static final int PAUSED = 1;
	private static final int STARTING = 2;
	private static final int STARTED = 3;
	private static final int STOPPING = 4;
	
	private int state = PAUSED;
	
	protected static final double	DY_SCALE = 40.0;
	protected static final double	DY_RANGE = 4.0;
	Rect rect = new Rect();

	Bitmap background = null;
	
	private static final Random random = new SecureRandom();
	private Point start;
	private boolean slashing = false;
	private int lastPop = -1;
	private int lastGiggle = -1;
	private int idle = 500;
	
	private long timer = 0;
	private static final long DURATION = 30 * 1000;
	
	public SlashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	    Resources res = getResources();
	    background = BitmapFactory.decodeResource(res, R.drawable.chalkboard);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	}

	public void resume()
	{
		// if the home button is pressed before we've completed the STARTING state
		// we restart the STARTING state when resuming the app...
		if(state == STARTING)
		{
			playTarget();
			timer = System.currentTimeMillis() + DURATION;
		}
		else if(state == STARTED)
			playTarget(); // remind the player
		else if(state == STOPPING)
			state = PAUSED; // home button pressed before completing the STOPPING state, just return to PAUSED.
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

		synchronized(cursor)
		{
			for (Shape shape : cursor) {
				shape.draw(canvas);
			}
		}

		for (Shape shape : shapes) {
			shape.draw(canvas);
		}

		for (Shape shape : splash) {
			shape.draw(canvas);
		}
		
		if(targetLabel != null)
			targetLabel.draw(canvas);
		if(scoreLabel != null)
			scoreLabel.draw(canvas);
		if(target != null)
			target.draw(canvas);
		if(score != null)
			score.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(state == PAUSED)
		{
			state = STARTING;
			
			if(Settings.difficulty > 1)
			{
				targetLabel = new Word("Pop It", Animation.FINDIT);
				targetLabel.setScale(1.75);
				targetLabel.setCenter(new Point(160 * Settings.scale_factor, 25 * Settings.scale_factor));
				
				scoreLabel = new Word("Score", Animation.FINDIT);
				scoreLabel.setScale(1.75);
				scoreLabel.setCenter(new Point(540 * Settings.scale_factor, 30 * Settings.scale_factor));
				
				target = getShape(new Point(300 * Settings.scale_factor, 50 * Settings.scale_factor));
				target.setScale(1.75);
				target.setDx(0);
				target.setDy(0);
				target.setAnimation(Animation.FINDIT);

				score = new com.fourthwoods.firstgame.shapes.Number(Animation.SLASH, true);
				score.setScale(1.75);
				score.setCenter(new Point(685 * Settings.scale_factor, 30 * Settings.scale_factor));
				score.setNumber(0);
				
				playTarget();
				timer = System.currentTimeMillis() + DURATION;
			}
			else
			{
				target = null;
				score = null;
				targetLabel = null;
				scoreLabel = null;
				state = STARTED;
			}
		}
		else if(state == STARTED)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				start = new Point(event.getX(), event.getY());
				if(Settings.difficulty > 1 && target.hitTest(start))
					playTarget();
				
				slashing = true;
			}
			else if (event.getAction() == MotionEvent.ACTION_MOVE && slashing == true)
			{
				Point end = new Point(event.getX(), event.getY());
							
				Cursor c = new Cursor(Shape.Animation.SLASH);
				c.setStart(start);
				c.setEnd(end);
				synchronized(cursor)
				{
					cursor.add(c);
				}
				
				start = end;
				idle = 0;
			}
			else if (event.getAction() == MotionEvent.ACTION_UP)
			{
				slashing = false;
			}
		}
		
		return true;
	}

	@Override
	public void advanceFrame() {
		List<Shape> remove = new ArrayList<Shape>();
		if(getWidth() < 0)
			return;
		
		if((Settings.difficulty > 1) && (state == STARTED) && (timer < System.currentTimeMillis()))
		{
			playFinished();
			state = STOPPING;
		}
		
		if ((state == STARTED) && (random.nextInt(25) == 0) && (shapes.size() < 3)) {
			Shape s = getTargetShape(new Point(random.nextInt(getWidth()), getHeight()));
			s.setScreenWidth(getWidth());
			s.setScale(2.5);
			double dy = -random.nextFloat() * DY_RANGE - getHeight() / DY_SCALE;
			s.setDy(dy);

			shapes.add(s);
			
			playLaunch();
		}

		synchronized(cursor)
		{
			Line line = null;
			if(slashing == true &&  cursor.size() >= 1)
			{
				Cursor c = (Cursor)cursor.get(cursor.size() - 1);
				Point begin = c.getStart();
				Point end = c.getEnd();
				line = new Line(begin, end);
			}
			
			for (Shape shape : shapes) {
				if((state == STARTED) && (line != null) && shape.isVisible() && (shape.hitTest(line)))
				{
					int rand;
					do
					{
						rand = random.nextInt(Sounds.pop.size());
					}
					while(rand == lastPop);
					lastPop = rand;
					int resid = Sounds.pop.get(rand);
					MediaPlayer mp = Sounds.create(getContext(), resid);
					mp.start();
					
					splash.addAll(this.getDroplets(shape));
					shape.adaptivePop();
					
					if(Settings.difficulty == 2)
					{
						if(shape.getFillColor() == target.getFillColor())
						{
							if(target instanceof Letter && shape instanceof Letter)
							{
								incScore();
							}
							else if(target instanceof com.fourthwoods.firstgame.shapes.Number && shape instanceof com.fourthwoods.firstgame.shapes.Number)
							{
								incScore();
							}
							else if(!(target instanceof Letter || target instanceof com.fourthwoods.firstgame.shapes.Number))
							{
								// target is Shape
								if(!(shape instanceof Letter || shape instanceof com.fourthwoods.firstgame.shapes.Number))
									incScore();
								else
									decScore();
							}
							else
								decScore();
						}
						else
							decScore();
					}
					else if(Settings.difficulty == 3)
					{
						if(shape.getClass() == target.getClass())
						{
							if(target instanceof com.fourthwoods.firstgame.shapes.Number)
							{
								if(((com.fourthwoods.firstgame.shapes.Number)shape).getNumber() == ((com.fourthwoods.firstgame.shapes.Number)target).getNumber())
									incScore();
								else
									decScore();;

							}
							else if(target instanceof Letter)
							{
								if(((Letter)shape).getLetter() == ((Letter)target).getLetter())
									incScore();
								else
									decScore();

							}
							else
								incScore();
						}
						else
							decScore();
					}
				}
				
				shape.advanceFrame();
				if(!shape.isVisible() || (shape.getCenter().y > getHeight()))
				{
					shape.adapt();
					remove.add(shape);
				}
			}
	
			if (!remove.isEmpty())
				shapes.removeAll(remove);
			
			remove.clear();
			
			for (Shape shape : cursor) {
				shape.advanceFrame();
				if(!shape.isVisible())
					remove.add(shape);
			}
			
			if (!remove.isEmpty())
				cursor.removeAll(remove);
		}
		remove.clear();
		
		for (Shape shape : splash) {
			shape.advanceFrame();
			if(!shape.isVisible() || (shape.getCenter().y > getHeight()))
				remove.add(shape);
		}
		
		if (!remove.isEmpty())
			splash.removeAll(remove);
		
		if(target != null)
			target.advanceFrame();
	}
	
	private void incScore()
	{
		score.setNumber(score.getNumber() + 1);		
		if(target instanceof FaceShape)
			((FaceShape)target).sayYes();
		
		if(score.getNumber() == 20)
		{
			playFinished();
			state = STOPPING;
		}
	}

	private void decScore()
	{
		int number = score.getNumber() - 1;
		if(number < 0)
			number = 0;
		
		score.setNumber(number);
		if(target instanceof FaceShape)
			((FaceShape)target).sayNo();
	}
	
	private List<Shape> getDroplets(Shape shape)
	{
		List<Shape> droplets = new ArrayList<Shape>();
		
		Shape drop = new Droplet(Shape.Animation.SLASH);
		drop.setCenter(new Point(shape.getCenter().x - 10, shape.getCenter().y - 10));
		drop.setScale(2);
		drop.setFillColor(shape.getFillColor());
		drop.setDx(-2);
		drop.setDy(-5);
		drop.setAngle(120);
		drop.setRotateSpeed(-2);
		droplets.add(drop);
		
		drop = new Droplet(Shape.Animation.SLASH);
		drop.setCenter(new Point(shape.getCenter().x + 10, shape.getCenter().y - 10));
		drop.setScale(2);
		drop.setFillColor(shape.getFillColor());
		drop.setDx(2);
		drop.setDy(-5);
		drop.setAngle(-120);
		drop.setRotateSpeed(2);
		droplets.add(drop);
		
		drop = new Droplet(Shape.Animation.SLASH);
		drop.setCenter(new Point(shape.getCenter().x - 10, shape.getCenter().y + 10));
		drop.setScale(2);
		drop.setFillColor(shape.getFillColor());
		drop.setDx(-1);
		drop.setDy(-2);
		drop.setAngle(45);
		drop.setRotateSpeed(-1);
		droplets.add(drop);
		
		drop = new Droplet(Shape.Animation.SLASH);
		drop.setCenter(new Point(shape.getCenter().x + 10, shape.getCenter().y + 10));
		drop.setScale(2);
		drop.setFillColor(shape.getFillColor());
		drop.setDx(1);
		drop.setDy(-2);
		drop.setAngle(-45);
		drop.setRotateSpeed(1);
		droplets.add(drop);
		
		return droplets;
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
				s = getShape(p, Shape.Animation.SLASH);
				break;
			case 1:
				s = getNumber(p, Shape.Animation.SLASH);
				break;
			case 2:
			default:
				s = getLetter(p, Shape.Animation.SLASH);
				break;
			}
		} while(s == null);

		return s;
	}
	
	private Shape getTargetShape(Point p)
	{
		Shape s = getShape(p);
		
		if(target != null)
		{
			int rand = random.nextInt(2);
			if(rand > 0)
			{
				if(Settings.difficulty == 2)
				{
					if((target instanceof com.fourthwoods.firstgame.shapes.Number) || (target instanceof Letter))
					{
						while(s.getClass() != target.getClass())
						{
							s = getShape(p);
						}
					}
					else
					{
						while((s instanceof com.fourthwoods.firstgame.shapes.Number) || (s instanceof Letter))
						{
							s = getShape(p);
						}
					}
					s.setFillColor(target.getFillColor());
				}
				else if(Settings.difficulty == 3)
				{
					while(s.getClass() != target.getClass())
					{
						s = getShape(p);
					}
					
					if(s instanceof com.fourthwoods.firstgame.shapes.Number)
						((com.fourthwoods.firstgame.shapes.Number)s).setNumber(((com.fourthwoods.firstgame.shapes.Number)target).getNumber());
					else if(s instanceof Letter)
						((Letter)s).setLetter(((Letter)target).getLetter());
				}
			}
			else if(Settings.difficulty == 3)
			{
				if(target instanceof com.fourthwoods.firstgame.shapes.Number)
				{
					com.fourthwoods.firstgame.shapes.Number t = ((com.fourthwoods.firstgame.shapes.Number)target);
					while(t.getNumber() == 9 && ((com.fourthwoods.firstgame.shapes.Number)s).getNumber() == 6)
					{
						s = getNumber(p, Shape.Animation.SLASH);
					}
					while(t.getNumber() == 6 && ((com.fourthwoods.firstgame.shapes.Number)s).getNumber() == 9)
					{
						s = getNumber(p, Shape.Animation.SLASH);
					}
				}
			}
		}
		
		return s;
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public List<Shape> getCursor() {
		return cursor;
	}

	public void setCursor(List<Shape> cursor) {
		this.cursor = cursor;
	}

	public List<Shape> getSplash() {
		return splash;
	}

	public void setSplash(List<Shape> splash) {
		this.splash = splash;
	}
	
	private void playIdle()
	{
		final int rand = random.nextInt(2);
		final int touch;
		final int start;
		final int slide;
		final int screen;
		final int theState = state;
		
		switch(rand)
		{
		case 0:
			touch = R.raw.kara_touch_the_screen;
			start = R.raw.kara_to_start;
			slide = R.raw.kara_slide_your_finger;
			screen = R.raw.kara_across_the_screen;
			break;
		default:
			touch = R.raw.lexie_touch_the_screen;
			start = R.raw.lexie_to_start;
			slide = R.raw.lexie_slide_your_finger;
			screen = R.raw.lexie_across_the_screen;
			break;
		}
		
		MediaPlayer mp;
		if (theState == PAUSED)
		{
			mp = Sounds.create(getContext(), touch);
		}
		else if(theState == STARTED)
		{
			mp = Sounds.create(getContext(), slide);
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
					if(theState == PAUSED)
						mp2 = Sounds.create(getContext(), start);
					else
						mp2 = Sounds.create(getContext(), screen);
					
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
		int resid;
		switch(rand)
		{
		case 0:
			resid = R.raw.kara_pop_the;
			break;
		default:
			resid = R.raw.lexie_pop_the;
			break;
		}
		
		MediaPlayer mp = Sounds.create(getContext(), resid);
		if(mp != null)
		{
			if(Settings.difficulty == 2)
			{
				mp.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						final int color;
						final int shapes;
						final int letters;
						final int numbers;
						
						switch(rand)
						{
						case 0:
							color = Sounds.karaColors.get(target.getFillColor());
							shapes = R.raw.kara_shapes;
							letters = R.raw.kara_letters;
							numbers = R.raw.kara_numbers;
							break;
						default:
							color = Sounds.lexieColors.get(target.getFillColor());
							shapes = R.raw.lexie_shapes;
							letters = R.raw.lexie_letters;
							numbers = R.raw.lexie_numbers;
							break;
						}
						MediaPlayer s = Sounds.create(getContext(), color);
						s.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer mp)
							{
								MediaPlayer s;
								if(target instanceof com.fourthwoods.firstgame.shapes.Number)
								{
									s = Sounds.create(getContext(), numbers);
								}
								else if(target instanceof Letter)
								{
									s = Sounds.create(getContext(), letters);
								}
								else
								{
									s = Sounds.create(getContext(), shapes);
								}
								
								s.setOnCompletionListener(new OnCompletionListener(){
									public void onCompletion(MediaPlayer mp)
									{
										state = STARTED;
									}
								});
								s.start();
							}
						});
						s.start();
					}
				});
	
				mp.start();
			}
			else if(Settings.difficulty == 3)
			{
				mp.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						final int letter;
						final int number;
						final SparseIntArray numbers;
						final SparseIntArray letters;
						@SuppressWarnings("rawtypes")
						final Map<Class, Integer> shapes;
						
						switch(rand)
						{
						case 0:
							letter = R.raw.kara_letter;
							number = R.raw.kara_number;
							numbers = Sounds.karaNumbers;
							letters = Sounds.karaLetters;
							shapes = Sounds.karaShapes;
							break;
						default:
							letter = R.raw.lexie_letter;
							number = R.raw.lexie_number;
							numbers = Sounds.lexieNumbers;
							letters = Sounds.lexieLetters;
							shapes = Sounds.lexieShapes;
							break;
						}
						
						if(target instanceof com.fourthwoods.firstgame.shapes.Number)
						{
							MediaPlayer mp2 = Sounds.create(getContext(), number);
							mp2.setOnCompletionListener(new OnCompletionListener(){
								public void onCompletion(MediaPlayer mp)
								{
									int resid = numbers.get(((com.fourthwoods.firstgame.shapes.Number)target).getNumber());
									MediaPlayer mp2 = Sounds.create(getContext(), resid);
									mp2.setOnCompletionListener(new OnCompletionListener(){
										public void onCompletion(MediaPlayer mp)
										{
											state = STARTED;
										}
									});
									mp2.start();
								}
							});
							mp2.start();
						}
						else if(target instanceof Letter)
						{
							MediaPlayer mp2 = Sounds.create(getContext(), letter);
							mp2.setOnCompletionListener(new OnCompletionListener(){
								public void onCompletion(MediaPlayer mp)
								{
									int resid = letters.get(((Letter)target).getLetter());
									MediaPlayer mp2 = Sounds.create(getContext(), resid);
									mp2.setOnCompletionListener(new OnCompletionListener(){
										public void onCompletion(MediaPlayer mp)
										{
											state = STARTED;
										}
									});
									mp2.start();
								}
							});
							mp2.start();
						}
						else
						{
							int shape = shapes.get(target.getClass());

							MediaPlayer mp2 = Sounds.create(getContext(), shape);
							mp2.setOnCompletionListener(new OnCompletionListener(){
								public void onCompletion(MediaPlayer mp)
								{
									state = STARTED;
								}
							});
							mp2.start();
						}
					}
				});
	
				mp.start();
			}
		}
	}

	private void playLaunch()
	{
		if(Settings.voice_laughter)
		{
			int resid;
			do
			{
				int rand = random.nextInt(2);
				switch(rand)
				{
				case 0:
					rand = random.nextInt(Sounds.giggles.size());
					resid = Sounds.giggles.get(rand);
					break;
				default:
					rand = random.nextInt(Sounds.cheers.size());
					resid = Sounds.cheers.get(rand);
					break;
				}
				rand = random.nextInt(Sounds.pop.size());
			}
			while(resid == lastGiggle);
			lastGiggle = resid;
			
			MediaPlayer mp = Sounds.create(getContext(), resid);
			mp.setVolume(0.3f, 0.3f);
			mp.start();
		}
	}
	
	private void playFinished()
	{
		Sounds.killAllSounds();
		
		final int goodJob;
		final int youPopped;
		final int number;
		final int items;
		
		int rand = random.nextInt(2);
		switch(rand)
		{
		case 0:
			goodJob = R.raw.kara_good_job;
			youPopped = R.raw.kara_you_popped;
			number = Sounds.karaNumbers.get(score.getNumber());
			if(target instanceof com.fourthwoods.firstgame.shapes.Number)
				items = (score.getNumber() == 1 ? R.raw.kara_number : R.raw.kara_numbers);
			else if(target instanceof Letter)
				items = (score.getNumber() == 1 ? R.raw.kara_letter : R.raw.kara_letters);
			else
				items = (score.getNumber() == 1 ? R.raw.kara_shape : R.raw.kara_shapes);
			break;
		default:
			goodJob = R.raw.lexie_good_job;
			youPopped = R.raw.lexie_you_popped;
			number = Sounds.lexieNumbers.get(score.getNumber());
			if(target instanceof com.fourthwoods.firstgame.shapes.Number)
				items = (score.getNumber() == 1 ? R.raw.lexie_number : R.raw.lexie_numbers);
			else if(target instanceof Letter)
				items = (score.getNumber() == 1 ? R.raw.lexie_letter : R.raw.lexie_letters);
			else
				items = (score.getNumber() == 1 ? R.raw.lexie_shape : R.raw.lexie_shapes);
			break;
		}
		
		MediaPlayer mp = Sounds.create(getContext(), goodJob);
		mp.setOnCompletionListener(new OnCompletionListener(){
			public void onCompletion(MediaPlayer mp)
			{
				MediaPlayer mp2 = Sounds.create(getContext(), youPopped);
				mp2.setOnCompletionListener(new OnCompletionListener(){
					public void onCompletion(MediaPlayer mp)
					{
						MediaPlayer mp2 = Sounds.create(getContext(), number);
						mp2.setOnCompletionListener(new OnCompletionListener(){
							public void onCompletion(MediaPlayer mp)
							{
								MediaPlayer mp2 = Sounds.create(getContext(), items);
								mp2.setOnCompletionListener(new OnCompletionListener(){
									public void onCompletion(MediaPlayer mp)
									{
										state = PAUSED;
										idle = 400;
									}
								});
								mp2.start();
							}
						});
						mp2.start();
					}
				});
				mp2.start();
			}
		});
		mp.start();
	}
}
