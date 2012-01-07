package org.getspout.client.renderer.util;

import org.getspout.api.math.Matrix;
import org.getspout.api.math.Vector3;



public class MatrixUtils {
	
	public static Matrix createLookAt(Vector3 eye, Vector3 at, Vector3 up){
		Vector3 f = eye.subtract(at).normalize();
		up = up.normalize();
		
		Vector3 s = f.cross(up).normalize();
		Vector3 u = s.cross(f).normalize();

		
		Matrix mat = new Matrix(4);

		mat.set(0, 0, s.getX());
		mat.set(0, 1, s.getY());
		mat.set(0, 2, s.getZ());
		
		mat.set(1, 0, u.getX());
		mat.set(1, 1, u.getY());
		mat.set(1,2, u.getZ());
		
		mat.set(2, 0, -f.getX());
		mat.set(2, 1, -f.getY());
		mat.set(2, 2, -f.getZ());
	
		Matrix trans = Matrix.translate(eye.multiply(-1));
		mat = Matrix.multiply(trans, mat);
		return mat;		
	}
	
	public static Matrix createPerspective(float fov, float aspect, float znear, float zfar) {

		float ymax, xmax;
		float temp, temp2, temp3, temp4;
		ymax = znear * (float) Math.tan(fov * Math.PI / 360.0);
		// ymin = -ymax;
		// xmin = -ymax * aspectRatio;
		xmax = ymax * aspect;

		temp = 2.0f * znear;
		temp2 = xmax - -xmax;
		temp3 = ymax - -ymax;
		temp4 = zfar - znear;

		Matrix matrix = new Matrix();
		matrix.set(0, 0, temp / temp2);
		matrix.set(1, 1, temp / temp3);
		matrix.set(2, 0, (xmax + -xmax) / temp2);
		matrix.set(2, 1, (ymax + -ymax) / temp3);
		matrix.set(2, 2, (-zfar - znear) / temp4);
		matrix.set(2, 3, -1);
		matrix.set(3, 2, (-temp * zfar) / temp4);
		
		return matrix;
	}
	
	public static Matrix createOrthographic(float right, float left, float top, float bottom, float near, float far){
		Matrix ortho = new Matrix();
		float tx = -((right+left) / (right-left));
		float ty = -((top + bottom) / (top - bottom));
		float tz = -((far+near) / (far - near));

		ortho.set(0, 0, 2.0f / (right-left));
		ortho.set(1, 1, 2.0f / (top-bottom));
		ortho.set(2, 2, -2.0f/ (far-near));

		ortho.set(3, 0, tx);
		ortho.set(3, 1, ty);
		ortho.set(3, 2, tz);
		
		return ortho;
		
	}
	
}
