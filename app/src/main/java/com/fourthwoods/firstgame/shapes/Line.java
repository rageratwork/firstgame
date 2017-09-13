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

public class Line {
	private Point begin;
	private Point end;
	
	public Line()
	{
		begin = new Point(0, 0);
		end = new Point(0, 0);
	}
	
	public Line(Point begin, Point end)
	{
		this.begin = new Point(begin.x, begin.y);
		this.end = new Point(end.x, end.y);
	}

	public Line(Point begin, double angle, double length)
	{
		this.begin = new Point(begin.x, begin.y);
		this.end = begin.translate(angle, length);
	}
	
	public Point getBegin() {
		return begin;
	}

	public void setBegin(Point begin) {
		this.begin = begin;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	// assumes point is a valid point on the line and verifies the point
	// lies between the endpoints.
	public boolean contains(Point point)
	{
		double x1 = begin.x;
		double x2 = end.x;
		double y1 = begin.y;
		double y2 = end.y;
		
		if(x1 > x2)
		{
			x1 = x2;
			x2 = begin.x;
		}
		
		if(y1 > y2)
		{
			y1 = y2;
			y2 = begin.y;
		}
		
		return ((x1 <= point.x) && (point.x <= x2) && (y1 <= point.y) && (point.y <= y2));
	}
	
	private boolean ccw(Point A, Point B, Point C)
	{
		//(C.y-A.y)*(B.x-A.x) > (B.y-A.y)*(C.x-A.x)
		return (C.y - A.y) * (B.x - A.x) > (B.y - A.y) * (C.x - A.x);
	}
	
	public boolean intersects(Line other)
	{
		// ccw(A,C,D) != ccw(B,C,D) and ccw(A,B,C) != ccw(A,B,D)
		Point A = this.begin;
		Point B = this.end;
		Point C = other.begin;
		Point D = other.end;
		
		return (ccw(A, C, D) != ccw(B, C, D)) &&
			   (ccw(A, B, C) != ccw(A, B, D));
	}
	
	public Point intersection(Line other)
	{
	    double s1_x, s1_y, s2_x, s2_y;
	    s1_x = this.end.x - this.begin.x;
	    s1_y = this.end.y - this.begin.y;
	    s2_x = other.end.x - other.begin.x;
	    s2_y = other.end.y - other.begin.y;

	    double s, t;
	    s = (-s1_y * (this.begin.x - other.begin.x) + s1_x * (this.begin.y - other.begin.y)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = ( s2_x * (this.begin.y - other.begin.y) - s2_y * (this.begin.x - other.begin.x)) / (-s2_x * s1_y + s1_x * s2_y);

	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
	    {
	        // Collision detected
	        new Point(this.begin.x + (t * s1_x), this.begin.y + (t * s1_y));
	    }

	    return null; // No collision
	}
	
	public double distance(Point point)
	{
		// point x0, y0 line x1, y1 - x2, y2
		//|(x2 - x1)(y1 - y0) - (x1 - x0)(y2 - y1)| / sqrt((x2 - x1)^2 + (y2 - y1)^2)
		double area = Math.abs((end.x - begin.x) * (point.y - begin.y) - (point.x- begin.x ) * (end.y - begin.y));
		double lab = Math.sqrt(Math.pow((end.x - begin.x), 2) + Math.pow((end.y - begin.y), 2));
		double dist = area / lab;
		
		return dist;
	}
	
	// source: http://stackoverflow.com/a/10956601/416627
	double distance2(Point point)
	{
		Vector v = new Vector(begin);
		Vector w = new Vector(end);
		Vector p = new Vector(point);
		//Vector closest;
		
	    double distSq = v.DistanceToSquared(w); // i.e. |w-v|^2 ... avoid a sqrt
	    if ( distSq == 0.0 )
	    {
	        // v == w case
	        // closest = v;

	        return v.DistanceTo(p);
	    }

	    // consider the line extending the segment, parameterized as v + t (w - v)
	    // we find projection of point p onto the line
	    // it falls where t = [(p-v) . (w-v)] / |w-v|^2

	    double t = p.subtract(v).DotProduct(w.subtract(v)) / distSq;
	    if (t < 0.0)
	    {
	        // beyond the v end of the segment
	        // closest = v;

	        return v.DistanceTo( p );
	    }
	    else if ( t > 1.0 )
	    {
	        // beyond the w end of the segment
	        // closest = w;

	        return w.DistanceTo( p );
	    }

	    // projection falls on the segment
	    Vector projection = v.add((w.subtract(v)).multiply(t));

	    // closest = projection;

	    return p.DistanceTo(projection);
	}
}
