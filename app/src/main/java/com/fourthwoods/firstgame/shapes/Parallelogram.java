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

public class Parallelogram extends PathShape {
	private double width = 50;
	private double height = 30;
	private static final int OFFSET = 5;

	public Parallelogram(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
		face.setWidth((int)(height * .4));

		buildPath();
	}
	
	@Override
	public void buildPath()
	{
		double l = -width / 2;
		double t = -height / 2;
		double r = width / 2;
		double b = height / 2;

		Point p1 = new Point(l + OFFSET, t);
		Point p2 = new Point(r + OFFSET, t);
		lines.add(new Line(p1, p2));
		p1 = p2;
		p2 = new Point(r - OFFSET, b);
		lines.add(new Line(p1, p2));
		p1 = p2;
		p2 = new Point(l - OFFSET, b);
		lines.add(new Line(p1, p2));
		lines.add(new Line(lines.get(lines.size() - 1).getEnd(), lines.get(0).getBegin()));

		path.reset();
		path.moveTo((float)l + OFFSET, (float)t);
		path.lineTo((float)r + OFFSET, (float)t);
		path.lineTo((float)r - OFFSET, (float)b);
		path.lineTo((float)l - OFFSET, (float)b);
		path.close();
	}
}
