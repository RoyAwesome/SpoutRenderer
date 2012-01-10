package org.spout.client.renderer.util;

import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;



public class MatrixUtils {

	public static Matrix createLookAt(Vector3 eye, Vector3 at, Vector3 up){
		Vector3 f = at.subtract(eye).normalize();
		up = up.normalize();

		Vector3 s = f.cross(up).normalize();
		Vector3 u = s.cross(f).normalize();


		Matrix mat = new Matrix(4);

		mat.set(0, 0, s.getX());
		mat.set(0, 1, s.getY());
		mat.set(0, 2, s.getZ());

		mat.set(1, 0, u.getX());
		mat.set(1, 1, u.getY());
		mat.set(1, 2, u.getZ());

		mat.set(2, 0, -f.getX());
		mat.set(2, 1, -f.getY());
		mat.set(2, 2, -f.getZ());

		Matrix trans = Matrix.translate(eye.multiply(-1));
		mat = Matrix.multiply(mat, trans);
		return mat;		
	}

	public static Matrix createPerspective(float fov, float aspect, float znear, float zfar) {
		float ymax, xmax;
		ymax = znear * (float)Math.tan((fov * Math.PI) / 360.0);
		xmax = ymax * aspect;
		return createOrthographic(xmax, -xmax, ymax, -ymax, znear, zfar);
	}

	public static Matrix createOrthographic(float right, float left, float top, float bottom, float near, float far){
		Matrix ortho = new Matrix();
		float temp, temp2, temp3, temp4;
		temp = 2.0f * near;
		temp2 = right - left;
		temp3 = top - bottom;
		temp4 = far - near;

		ortho.set(0, 0, temp / temp2);
		ortho.set(1, 1, temp / temp3);

		ortho.set(0, 2, (right+left) / temp2);
		ortho.set(1, 2, (top + bottom) / temp3);
		ortho.set(2, 2, (-far - near) / temp4);
		ortho.set(3, 2, -1);

		ortho.set(2, 3, (-temp * far) / temp4);
		ortho.set(3, 3, 0);


		return ortho;

	}

}
