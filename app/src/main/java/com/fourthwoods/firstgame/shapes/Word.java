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
import android.graphics.Rect;
import android.graphics.Typeface;

public class Word extends Shape {
	private String text;
	private int height = 40;
	private Rect rect = new Rect();

	public Word (String text, Animation animation)
	{
		super(animation);
		this.text = text;
		
		p.setTextSize(height);
		p.setTypeface(Typeface.DEFAULT_BOLD);
	}

	@Override
	public void draw(Canvas canvas) {
		p.setStyle(Style.FILL);
		p.setColor(fillColor);
		p.setAlpha(alpha);

		p.getTextBounds(text, 0, text.length(), rect);
		
		double x = center.x - rect.width() / 2;
		double y = center.y + rect.height() / 2;

		canvas.save();
		canvas.rotate((float)angle, (float)x, (float)y);
		canvas.translate((float)x, (float)y);
		canvas.scale((float)scale, (float)scale);
		
		canvas.drawText(text, -rect.width() / 2, rect.height() / 2, p);
		
		p.setStyle(Style.STROKE);
		p.setColor(borderColor);
		p.setAlpha(alpha);
		
		canvas.drawText(text, -rect.width() / 2, rect.height() / 2, p);
		
		canvas.restore();
	}
	
	@Override
	public boolean hitTest(Point point)
	{
		Line line = new Line(new Point(-1, -1), point);
		List<Line> lines = new ArrayList<Line>();

		p.getTextBounds(text, 0, text.length(), rect);
		Point p1 = new Point(rect.left - rect.width() / 2, rect.top + rect.height() / 2);
		Point p2 = new Point(rect.right - rect.width() / 2, rect.top + rect.height() / 2);
		lines.add(new Line(p1, p2));
		Point p3 = new Point(rect.right - rect.width() / 2, rect.bottom + rect.height() / 2);
		lines.add(new Line(p2, p3));
		Point p4 = new Point(rect.left - rect.width() / 2, rect.bottom + rect.height() / 2);
		lines.add(new Line(p3, p4));
		lines.add(new Line(p4, p1));
		
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
	public boolean hitTest(Line line)
	{
		List<Line> lines = new ArrayList<Line>();
		
		p.getTextBounds(text, 0, text.length(), rect);
		Point p1 = new Point(rect.left - rect.width() / 2, rect.top + rect.height() / 2);
		Point p2 = new Point(rect.right - rect.width() / 2, rect.top + rect.height() / 2);
		lines.add(new Line(p1, p2));
		Point p3 = new Point(rect.right - rect.width() / 2, rect.bottom + rect.height() / 2);
		lines.add(new Line(p2, p3));
		Point p4 = new Point(rect.left - rect.width() / 2, rect.bottom + rect.height() / 2);
		lines.add(new Line(p3, p4));
		lines.add(new Line(p4, p1));
		
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
