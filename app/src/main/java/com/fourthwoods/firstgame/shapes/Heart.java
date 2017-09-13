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

public class Heart extends PathShape {
	private int points = 360;

	public Heart(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
		face.setWidth(10);

		buildPath();
	}
	
	// x = 16sin^3(t)	
	// y = 13cos(t) - 5cos(2t) - 2cos(3t) - cos(4t)
	// http://mathworld.wolfram.com/HeartCurve.html
	@Override
	public void buildPath()
	{
		double x = 0;
		double y = 0;
		
		path.moveTo((float)x, (float)y);

		Point p1 = new Point(x, y);
		
		for(int i = 0; i < points; i += 5) // only every 5th point
		{
			double rad = Math.toRadians(i);
			x =	(16 * Math.pow(Math.sin(rad), 3.0)) * 1.3;
			y =	-(13 * Math.cos(rad) - 5 * Math.cos(2 * rad) - 2 * Math.cos(3 * rad) - Math.cos(4 * rad)) * 1.3;
			path.lineTo((float)x, (float)y);
			Point p2 = new Point(x, y);
			lines.add(new Line(p1, p2));
			p1 = p2;
		}
		
		path.close();
		lines.add(new Line(lines.get(lines.size() - 1).getEnd(), lines.get(0).getBegin()));
	}
}
