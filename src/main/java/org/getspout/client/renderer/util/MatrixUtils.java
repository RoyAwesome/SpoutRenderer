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

		float ymax = znear * (float)Math.tan(Math.toRadians(fov));
		  float ymin = -ymax;
		  float xmax = ymax * aspect;
		  float xmin = ymin * aspect;

		  float width = xmax-xmin; 
		  float height = ymax-ymin;

		  float depth = zfar - znear;
		  float q = -(zfar + znear) / depth;
		  float qn = -2 * (zfar * znear) / depth;

		  float w = 2 * znear / width;
		  float h = 2 * znear / height;
		
		
		Matrix matrix = new Matrix();
		matrix.set(0, 0, w);
		matrix.set(1, 1, h);
		matrix.set(2, 2, q);
		matrix.set(3, 2, -1);
		matrix.set(2, 3, qn);
		matrix.set(3,3, 0);

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
