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
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class Circle extends FaceShape {
	private int radius = 18;
	
	public Circle(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
		
		face.setWidth((int)(radius * .75));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.save();
		canvas.translate((float)center.x, (float)center.y);
		canvas.scale((float)scale, (float)scale);
		
		ShapeDrawable d = new ShapeDrawable(new OvalShape());
		d.setBounds(-radius, -radius, radius, radius);
		
		Paint p = d.getPaint();
		p.setColor(fillColor);
		p.setAlpha(alpha);
		p.setAntiAlias(true);

		d.draw(canvas);
		
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(borderWidth);
		p.setColor(borderColor);
		p.setAlpha(alpha);

		d.draw(canvas);
		canvas.restore();
		
		super.draw(canvas);
	}
	
	@Override
	public boolean hitTest(Point point)
	{
		return (Math.pow(point.x - center.x, 2) + Math.pow(point.y - center.y, 2) <= Math.pow(this.radius * scale, 2));
	}

	@Override
	public boolean hitTest(Line line)
	{
		if(line.distance2(center) <= radius * scale)
			return true;
		
		return false;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
}
