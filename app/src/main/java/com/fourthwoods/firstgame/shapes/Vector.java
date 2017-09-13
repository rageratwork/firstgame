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

public class Vector {
	public double x;
	public double y;

	public Vector() {
		x = 0;
		y = 0;
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	public Vector add(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y);
	}

	public Vector subtract(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y);
	}

	public Vector multiply(double d) {
		return new Vector(this.x * d, this.y * d);
	}

	double DistanceToSquared(Vector v) {
		double dx = v.x - this.x;
		double dy = v.y - this.y;

		return dx * dx + dy * dy;
	}

	double DistanceTo(Vector v) {
		return Math.sqrt(this.DistanceToSquared(v));
	}

	double DotProduct(Vector v) {
		return this.x * v.x + this.y * v.y;
	}
}
