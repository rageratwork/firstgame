package com.fourthwoods.firstgame.shapes;

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
import java.util.Date;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

import com.fourthwoods.firstgame.Settings;

public abstract class Shape {
	public enum Animation {
		SMASH,
		SLASH,
		FINDIT
	}
	
	protected static final int		SMASH_ZOOM_FRAMES = 50;				// zoom full in 50 frames
	protected static final double	SMASH_ROTATE_AMOUNT = 360.0;		// rotate once
	protected static final double	SMASH_SCALE_AMOUNT = 3.0;			// scale to 3 times original size
	protected static final int		SMASH_DISPLAY_LENGTH = 10 * 1000;	// 10 seconds before fading out
	
	// applied each frame
	protected static final int		SMASH_FADE_SPEED = 2;				// reduce opacity 2 every frame
	protected static final double	SMASH_ROTATE_SPEED = SMASH_ROTATE_AMOUNT / SMASH_ZOOM_FRAMES;
	protected static final double	SMASH_SCALE_SPEED = SMASH_SCALE_AMOUNT / SMASH_ZOOM_FRAMES;
	
	protected static final double	SLASH_DX_SCALE = 4.0;
	protected static final double	SLASH_DX_MIN = 1.0;
	protected static final double	SLASH_DY_SCALE = 4.0;
	protected static final double	SLASH_DY_MIN = 12.0;
	protected static final double	SLASH_ROTATE_SCALE = 10.0;
	
	protected static final int		FINDIT_TX_FRAMES = 30;
	
	protected static Random random = new SecureRandom();

	protected  Paint p = new Paint();

	protected Animation animation = Animation.SMASH;
	
	protected int screenWidth = 0;
	protected Point center = new Point(0, 0);
	protected int fillColor = Color.GREEN;
	protected int borderColor = Color.BLACK;
	protected int borderWidth = 2;
	
	protected int alpha = 255;		// controls fade
	protected double baseScale = 1;
	protected double scale = baseScale * Settings.scale_factor;		// controls size
	
	protected double angle = 0;		// controls rotation
	
	protected double dx = 0; 		// translate speed x
	protected double dy = 0;		// translate speed y
	
	protected double rotateSpeed = SMASH_ROTATE_SPEED;	// rotation speed degrees / frame
	protected double gravity = 0.3 * Settings.gravity_factor;	// acceleration y
	
	private double maxY = 999999;
	
	protected int frameCount = 0;
	
	protected boolean visible = true;
	
	protected long endTime;
	
	public Shape(Animation animation)
	{
		this.animation = animation;
		
		fillColor = getColor();
		p.setAntiAlias(true);
		p.setStrokeCap(Cap.SQUARE);
		p.setStrokeWidth(borderWidth);
		
		switch(animation)
		{
		case SMASH:
			endTime = new Date().getTime() + SMASH_DISPLAY_LENGTH;
			break;
		case SLASH:
			dx = random.nextFloat() * SLASH_DX_SCALE + SLASH_DX_MIN;
			dy = -random.nextFloat() * SLASH_DY_SCALE - SLASH_DY_MIN;
			rotateSpeed = random.nextFloat() * SLASH_ROTATE_SCALE;
			
			if(random.nextInt(2) == 0) // rotate left or right
				rotateSpeed = -rotateSpeed;
			
			break;
		case FINDIT:
			break;
		default:
			break;
		}
	}
	
	public abstract void draw(Canvas canvas);

	public abstract boolean hitTest(Point point);
	
	public abstract boolean hitTest(Line line);
	
	public void advanceFrame()
	{
		switch(animation)
		{
		case SMASH:
			advanceFrameSmash();
			break;
		case SLASH:
			advanceFrameSlash();
			break;
		case FINDIT:
			advanceFrameFindit();
			break;
		default:
			break;
		}
		
		frameCount++;
	}
	
	private void advanceFrameSmash()
	{
		if(frameCount < SMASH_ZOOM_FRAMES)
		{
			angle = frameCount * SMASH_ROTATE_SPEED;
			scale = baseScale * Settings.scale_factor + frameCount * SMASH_SCALE_SPEED;
		}
		else
			angle = 0;
		
		Date now = new Date();
		if(now.getTime() > endTime)
		{
			alpha -= SMASH_FADE_SPEED;
			if(alpha <= 0)
			{
				alpha = 0;
				visible = false;
			}
		}
	}
	
	private void advanceFrameSlash()
	{
		dy += gravity;
		
		center.x += dx;
		center.y += dy;
		
		if(center.y < maxY)
			maxY = center.y;
		
		angle += rotateSpeed;
	}
	
	private void advanceFrameFindit()
	{
		if(frameCount < FINDIT_TX_FRAMES)
		{
			center.x += dx;
			center.y += dy;
		}
	}
	
	/**
	 * Rather than trying to determine specific display attributes, this method
	 * will allow the game to adapt to different displays and adjust the 
	 * gravity to keep the max height of a shape to the top 2/5 of the screen.
	 */
	public void adapt()
	{
		if(animation.equals(Animation.SLASH))
		{
			if(maxY < 0) // went off the top of the screen
			{
				Settings.gravity_factor += 0.1;
			}
			else if(maxY > Settings.screen_height * 2 / 5) // didn't rise to at least 3/5 screen height
			{
				Settings.gravity_factor -= 0.1;
			}
		}
	}
	private static int getColor()
	{
		int color;
		switch(random.nextInt(8))
		{
		case 0:
			color = Color.WHITE;
			break;
		case 1:
			color = 0xff9513df; // purple
			break;
		case 2:
			color = 0xffffad54; // orange
			break;
		case 3:
			color = Color.BLUE;
			break;
		case 4:
			color = Color.RED;
			break;
		case 5:
			color = Color.YELLOW;
			break;
		case 6:
			color = Color.GREEN;
			break;
		case 7:
		default:
			color = Color.GRAY;
		}

		return color;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	/**
	 * Specific for Slash. Prevents "popped" shapes from affecting gravity
	 * adjustments when popped below the "low" threshold.
	 */
	public void adaptivePop()
	{
		maxY = 0;
		setVisible(false);
	}
	
	public Point getCenter() {
		return center;
	}
	
	public void setCenter(Point center) {
		this.center = new Point(center.x, center.y);
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public double getRotateSpeed() {
		return rotateSpeed;
	}

	public void setRotateSpeed(double rotateSpeed) {
		this.rotateSpeed = rotateSpeed;
	}
	
	public double getScale() {
		return baseScale;
	}

	public void setScale(double scale) {
		this.baseScale = scale;
		this.scale = this.baseScale * Settings.scale_factor;
	}

	public int getScreenWidth()
	{
		return screenWidth;
	}
	
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
		if(center.x > screenWidth / 2)
			dx = -dx;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
