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
import android.graphics.Color;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;

public class Cursor extends Shape {

	private Point start;
	private Point end;

	public Cursor(Animation animation)
	{
		super(animation);
		
		borderWidth = 15;
		fillColor = Color.WHITE;
	}
	
	public void advanceFrame()
	{
		borderWidth -= 2;
		if(borderWidth <= 0)
			visible = false;
	}
	
	@Override
	public void draw(Canvas canvas) {
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(borderWidth);
		p.setStrokeCap(Cap.ROUND);
		p.setColor(fillColor);
		p.setAlpha(alpha);
		p.setAntiAlias(true);

		canvas.drawLine((float)start.x, (float)start.y, (float)end.x, (float)end.y, p);
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
	
	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
}
