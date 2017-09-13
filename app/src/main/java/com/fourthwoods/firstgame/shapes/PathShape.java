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
import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Path;

public abstract class PathShape extends FaceShape {
	protected Path path = new Path();
	protected List<Line> lines = new ArrayList<Line>();

	public PathShape(Animation animation)
	{
		super(animation);
	}
	
	public abstract void buildPath();
	
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.rotate((float)angle, (float)center.x, (float)center.y);
		canvas.translate((float)center.x, (float)center.y);
		canvas.scale((float)scale, (float)scale);
		
		p.setStyle(Style.FILL);
		p.setColor(fillColor);
		p.setAlpha(alpha);

		canvas.drawPath(path, p);
		
		p.setStyle(Style.STROKE);
		p.setColor(borderColor);
		p.setAlpha(alpha);
		canvas.drawPath(path, p);

		canvas.restore();
		
		super.draw(canvas);
	}

	@Override
	public boolean hitTest(Point point) {
		Line line = new Line(new Point(-1, -1), point);
		
		Point begin = new Point(line.getBegin().x - center.x, line.getBegin().y - center.y);
		Point end = new Point(line.getEnd().x - center.x, line.getEnd().y - center.y);
		
		begin = begin.scale(1 / scale).rotate(Point.ORIGIN, angle);
		end = end.scale(1 / scale).rotate(Point.ORIGIN, angle);
		
		line = new Line(begin, end);
		
		int intersections = 0;
		
		for(Line l : lines)
		{
			if(l.intersects(line))
				intersections++;
		}
		
		return intersections % 2 != 0;
	}
	
	@Override
	public boolean hitTest(Line line) {

		Point begin = new Point(line.getBegin().x - center.x, line.getBegin().y - center.y);
		Point end = new Point(line.getEnd().x - center.x, line.getEnd().y - center.y);
		
		begin = begin.scale(1 / scale).rotate(Point.ORIGIN, angle);
		end = end.scale(1 / scale).rotate(Point.ORIGIN, angle);
		
		line = new Line(begin, end);
		
		for(Line l : lines)
		{
			if(l.intersects(line))
				return true;
		}
		
		return false;
	}
}
