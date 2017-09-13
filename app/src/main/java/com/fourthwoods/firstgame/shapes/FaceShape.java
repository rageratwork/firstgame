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
import android.graphics.Canvas;

public abstract class FaceShape extends Shape {
	protected Face face;
	private boolean showFace = true;
	
	public FaceShape(Animation animation)
	{
		super(animation);
		
		face = new Face(animation);
	}
	
	@Override
	public void advanceFrame()
	{
		super.advanceFrame();
		face.advanceFrame();
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if(showFace)
			face.draw(canvas);
	}
	
	@Override
	public void setAnimation(Animation animation)
	{
		super.setAnimation(animation);
		face.setAnimation(animation);
	}
	
	@Override
	public void setCenter(Point p)
	{
		super.setCenter(p);
		face.setCenter(p);
	}	
	
	@Override
	public void setScale(double scale)
	{
		super.setScale(scale);
		face.setScale(scale);
	}
	
	@Override
	public void setScreenWidth(int screenWidth) {
		super.setScreenWidth(screenWidth);
		face.setScreenWidth(screenWidth);
	}
	
	@Override
	public void setDx(double dx)
	{
		super.setDx(dx);
		face.setDx(dx);
	}
	
	@Override
	public void setDy(double dy)
	{
		super.setDy(dy);
		face.setDy(dy);
	}
	
	public void sayNo()
	{
		face.sayNo();
	}

	public void sayYes()
	{
		face.sayYes();
	}

	public void setShowFace(boolean showFace) {
		this.showFace = showFace;
	}
}
