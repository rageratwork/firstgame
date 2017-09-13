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

public abstract class RegularPolygon extends PathShape {
	protected double radius;
	protected int sides;

	public RegularPolygon(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
	}
	
	public void buildPath()
	{
		double arcLength = 360.0 / sides;
		double x = 0;
		double y = -radius;
		double rotate = 0;
		if(sides % 2 == 0)
			rotate = arcLength / 2;
		
		double s = Math.sin(rotate * Math.PI / 180);
		double c = Math.cos(rotate * Math.PI / 180);
		double x1 = (int)(x * c + y * s);
		double y1 = (int)(-x * s + y * c);
		
		path.reset();
		path.moveTo((float)x1, (float)y1);

		Point p1 = new Point(x1, y1);
		
		for(int i = 1; i < sides; i++)
		{
			double angle = arcLength * i + rotate;
			s = Math.sin(angle * Math.PI / 180);
			c = Math.cos(angle * Math.PI / 180);
			x1 = (int)(x * c + y * s);
			y1 = (int)(-x * s + y * c);
			path.lineTo((float)x1, (float)y1);
			Point p2 = new Point(x1, y1);
			lines.add(new Line(p1, p2));
			p1 = p2;
		}
		
		path.close();
		lines.add(new Line(lines.get(lines.size() - 1).getEnd(), lines.get(0).getBegin()));
	}
}
