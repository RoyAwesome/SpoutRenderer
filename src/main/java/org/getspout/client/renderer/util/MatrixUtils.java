package org.getspout.client.renderer.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixUtils {
	
	public static Matrix4f createLookAt(Vector3f center, Vector3f at, Vector3f up){
		Vector3f f = new Vector3f();
		Vector3f.sub(center, at, f);
		f = (Vector3f) f.normalise();
		
		up = (Vector3f) up.normalise();
		
		Vector3f s = new Vector3f();
		Vector3f u = new Vector3f();
		
		Vector3f.cross(f, up, s);
		Vector3f.cross(s, f, u);
		

		
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.m00 = s.x;
		mat.m01 = s.y;
		mat.m02 = s.z;
		mat.m03 = 0;
		
		mat.m10 = u.x;
		mat.m11 = u.y;
		mat.m12 = u.z;
		mat.m13 = 0;
		
		mat.m20 = -f.x;
		mat.m21 = -f.y;
		mat.m22 = -f.z;
		mat.m23 = 0;
		
		mat.translate(center);
		
		return mat;
		
		
		
		
		
		
	}
	
	public static Matrix4f createPerspective(float fov, float aspect, float znear, float zfar) {

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

		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.m00 = temp / temp2;
		matrix.m11 = temp / temp3;
		matrix.m20 = (xmax + -xmax) / temp2;
		matrix.m21 = (ymax + -ymax) / temp3;
		matrix.m22 = (-zfar - znear) / temp4;
		matrix.m23 = -1;
		matrix.m32 = (-temp * zfar) / temp4;

		return matrix;
	}
	
	public static Matrix4f createOrthographic(float right, float left, float top, float bottom, float near, float far){
		Matrix4f ortho = new Matrix4f();
		float tx = -((right+left) / (right-left));
		float ty = -((top + bottom) / (top - bottom));
		float tz = -((far+near) / (far - near));
		
		ortho.setIdentity();
		ortho.m00 = 2.0f / (right-left);
		ortho.m11 = 2.0f / (top-bottom);
		ortho.m22 = -2.0f/ (far-near);
		
		ortho.m30 = tx;
		ortho.m31 = ty;
		ortho.m32 = tz;
		
		return ortho;
		
	}
	
}
