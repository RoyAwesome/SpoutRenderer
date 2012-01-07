package org.getspout.client;

import java.awt.DisplayMode;
import java.io.IOException;

import org.getspout.api.math.Matrix;
import org.getspout.api.math.Vector3;
import org.getspout.client.batcher.PrimitiveBatch;
import org.getspout.client.renderer.BatchVertexRenderer;
import org.getspout.client.renderer.shader.BasicShader;
import org.getspout.client.renderer.util.MatrixUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;

public class SpoutClient {
	static boolean GL30mode = true;

	public void start() {
		try {
			Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(800,600));
			
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		
		glLoadIdentity();
		
		
		BatchVertexRenderer renderer = BatchVertexRenderer.constructNewBatch(GL_TRIANGLES);
		
		BasicShader shader = new BasicShader();
		Matrix perspective = MatrixUtils.createPerspective(60, 4.f/3.f, .01f, 100f);
		Matrix view = MatrixUtils.createLookAt(new Vector3(1,0,2), Vector3.ZERO, Vector3.Up);
		shader.setProjectionMatrix(perspective);
		shader.setViewMatrix(view);
		System.out.println(perspective);
		System.out.println(view);
		
		renderer.setShader(shader);
		renderer.enableColors();

		//renderer.enableTextures();
		renderer.begin();
		renderer.AddColor(1.0f, 0.0f, 0.0f);
		renderer.AddTexCoord(1.0f, 1.0f);
		renderer.AddVertex(-0.5f, -0.5f, -1.0f);


		renderer.AddColor(0.0f, 1.0f, 0.0f);
		renderer.AddTexCoord(0.0f, 1.0f);
		renderer.AddVertex(0.5f, -0.5f, -10.0f );


		renderer.AddColor(0.0f, 0.0f, 1.0f);
		renderer.AddTexCoord(0.0f, 0.0f);
		renderer.AddVertex( 0.0f, 0.5f, -10.0f);

	//	renderer.AddColor(0.0f, 0.0f, 1.0f);
	//	renderer.AddTexCoord(1.0f, 0.0f);
	//	renderer.AddVertex(.5f, -.5f);

		renderer.end();
		
		/*
		PrimitiveBatch batch = new PrimitiveBatch();
		batch.getRenderer().setShader(shader);
		
		batch.begin();
		batch.AddQuad(Vector3.ZERO, new Vector3(1,0,0), new Vector3(0,0,1), new Vector3(1,0,1));
		batch.end();
		*/
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			renderer.render();
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective(60, 4.f/3.f, .01f, 100f);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			GLU.gluLookAt(1, 0,2, 0, 0, 0, 0, 1, 0);
			
			glBegin( GL_TRIANGLES ); 
			glVertex3f( -0.5f, -0.5f, -1.0f ); 
			glVertex3f( 0.5f, -0.5f, -10.0f ); 
			glVertex3f( 0.0f, 0.5f, -10.0f ); 
			glEnd(); 
			
			//batch.draw();

			Display.update();
		}

		Display.destroy();
	}

	public static void main(String[] argv) {
		SpoutClient displayExample = new SpoutClient();
		displayExample.start();
	}
}
