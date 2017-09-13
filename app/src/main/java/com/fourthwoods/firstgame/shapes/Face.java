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
import android.graphics.Paint.Style;
import android.graphics.RectF;

public class Face extends Shape {
	private int width = 30;

	private double yesOffset = 0.0;
	private double noOffset = 0.0;
	
	private boolean sayYes = false;
	private boolean sayNo = false;
	
	private int nodStart;
	
	public Face(Animation animation)
	{
		super(animation);
	}
	
	@Override
	public void advanceFrame()
	{
		super.advanceFrame();
		
		if(sayYes)
		{
			if(nodStart + 20 < frameCount)
			{
				sayYes = false;
				yesOffset = 0.0;
			}
			else if(yesOffset == 0.0)
				yesOffset = -2.0;
			
			if(frameCount % 5 == 0)
				yesOffset = -yesOffset;
		}
		
		if(sayNo)
		{
			if(nodStart + 20 < frameCount)
			{
				sayNo = false;
				noOffset = 0.0;
			}
			else if(noOffset == 0.0)
				noOffset = -2.0;
			
			if(frameCount % 5 == 0)
				noOffset = -noOffset;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.rotate((float)angle, (float)center.x, (float)center.y);
		canvas.translate((float)center.x, (float)center.y);
		canvas.scale((float)scale, (float)scale);
		
		p.setStyle(Style.FILL);
		p.setColor(borderColor);
		p.setAlpha(alpha);
		
		canvas.drawCircle((int)(-width / 2 + noOffset), (int)(-width / 2 + yesOffset), (int)(width * .25), p);
		canvas.drawCircle((int)(width / 2 + noOffset), (int)(-width / 2 + yesOffset), (int)(width * .25), p);
		
		RectF r = new RectF((int)(-width + noOffset), (int)((-width * .9) + yesOffset), (int)(width + noOffset), (int)((width * .9) + yesOffset));
		p.setStyle(Style.STROKE);
		
		canvas.drawArc(r, 0, 180, false, p);
		
		canvas.restore();
	}
	
	@Override
	public boolean hitTest(Point point)
	{
		return false;
	}
	
	@Override
	public boolean hitTest(Line line)
	{
		return false;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public void sayYes() {
		if(sayNo == true)
			return;
		
		this.sayYes = true;
		this.nodStart = this.frameCount;
	}

	public void sayNo() {
		if(sayYes == true)
			return;
		
		this.sayNo = true;
		this.nodStart = this.frameCount;
	}

}
