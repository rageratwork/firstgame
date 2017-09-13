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

public class Star extends PathShape {
	private double innerRadius = 12;
	private double outerRadius = 25;
	
	public Star(Animation animation)
	{
		super(animation);
		face.setDx(dx);
		face.setDy(dy);
		face.setRotateSpeed(rotateSpeed);
		face.setWidth((int)(innerRadius * .70));

		buildPath();
	}
	
	@Override
	public void buildPath()
	{
		double xo = 0;
		double yo = -outerRadius;
		double xi = 0;
		double yi = -innerRadius;
		
		double x1 = xo;
		double y1 = yo;
		
		path.reset();
		path.moveTo((float)x1, (float)y1); // outer
		Point p1 = new Point(x1, y1);
		
		double angle = 36;
		double s = Math.sin(angle * Math.PI / 180);
		double c = Math.cos(angle * Math.PI / 180);
		x1 = xi * c + yi * s;
		y1 = -xi * s + yi * c;
		path.lineTo((float)x1, (float)y1); // inner
		Point p2 = new Point(x1, y1);
		lines.add(new Line(p1, p2));
		p1 = p2;
		
		for(int i = 1; i < 5; i++)
		{
			angle = 72 * i;
			s = Math.sin(angle * Math.PI / 180);
			c = Math.cos(angle * Math.PI / 180);
			x1 = xo * c + yo * s;
			y1 = -xo * s + yo * c;
			path.lineTo((float)x1, (float)y1); // outer
			p2 = new Point(x1, y1);
			lines.add(new Line(p1, p2));
			p1 = p2;

			angle += 36;
			s = Math.sin(angle * Math.PI / 180);
			c = Math.cos(angle * Math.PI / 180);
			x1 = xi * c + yi * s;
			y1 = -xi * s + yi * c;
			path.lineTo((float)x1, (float)y1); // inner
			p2 = new Point(x1, y1);
			lines.add(new Line(p1, p2));
			p1 = p2;
		}
		
		path.close();
		lines.add(new Line(lines.get(lines.size() - 1).getEnd(), lines.get(0).getBegin()));
	}
}

