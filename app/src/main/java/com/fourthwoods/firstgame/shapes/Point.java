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
public class Point {
	public double x;
	public double y;
	
	public static final Point ORIGIN = new Point(0, 0);
	
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(Point other)
	{
		this.x = other.x;
		this.y = other.y;
	}
	
	public Point translate(double angle, double distance)
	{
		double s = Math.sin(angle * Math.PI / 180);
		double c = Math.cos(angle * Math.PI / 180);
		
		double x1 = distance * c + x;
		double y1 = distance * s + y;
		
		return new Point(x1, y1);
	}
	
	// specifies dx, dy
	public Point translate(Point p)
	{
		double x1 = p.x + x;
		double y1 = p.y + y;
		
		return new Point(x1, y1);
	}
	
	public Point scale(double scale)
	{
		return new Point(x * scale, y * scale);
	}
	
	public Point rotate(Point center, double angle) {
		double s = Math.sin(angle);
		double c = Math.cos(angle);

		Point newPoint = new Point(x, y);
		// translate point back to origin:
		newPoint.x -= center.x;
		newPoint.y -= center.y;

		// rotate point
		double xnew = newPoint.x * c - newPoint.y * s;
		double ynew = newPoint.x * s + newPoint.y * c;

		// translate point back:
		newPoint.x = xnew + center.x;
		newPoint.y = ynew + center.y;
		
		return newPoint;
	}
}
