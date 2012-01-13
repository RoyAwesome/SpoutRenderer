package org.spout.client;

import java.awt.DisplayMode;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.spout.client.batcher.PrimitiveBatch;
import org.spout.client.renderer.BatchVertexRenderer;
import org.spout.client.renderer.shader.BasicShader;
import org.spout.client.renderer.util.MatrixUtils;
import org.spout.client.renderer.vertexformat.PositionColor;
import org.spout.api.util.Color;

import static org.lwjgl.opengl.GL11.*;

public class SpoutClient {
	
	public void start() throws InterruptedException {
		try {
			Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(800,600));
			
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		BatchVertexRenderer renderer = BatchVertexRenderer.constructNewBatch(GL_TRIANGLES);
		
		BasicShader shader = new BasicShader();
		
	
		PrimitiveBatch batch = new PrimitiveBatch();
		batch.getRenderer().setShader(shader);
		
		Color col = new Color(0.0f, 1.0f, 0.0f);
		Color col2 = new Color(0.0f, 0.0f, 1.0f);
		Color col3 = new Color(1.0f, 0.0f, 0.0f);
		
		PositionColor[] corners = { new PositionColor(Vector3.ZERO, col3) , new PositionColor(Vector3.UNIT_Y, col), new PositionColor(new Vector3(0,1,1), col), new PositionColor(Vector3.UNIT_Z, col),
				new PositionColor(Vector3.UNIT_X, col3), new PositionColor(new Vector3(1,1,0), col2), new PositionColor(Vector3.ONE, col2), new PositionColor(new Vector3(1, 0, 1), col2)};
		
		
		
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		int ticks = 0;
		while (!Display.isCloseRequested()) {
			long time  = System.currentTimeMillis();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			double x = 5 * Math.sin(Math.toRadians(ticks));
			double z = 5 * Math.cos(Math.toRadians(ticks));
			double y = 5 * Math.sin(Math.toRadians(ticks));
			Matrix perspective = MatrixUtils.createPerspective(60, 4.f/3.f, .1f, 100f);
			Matrix view = MatrixUtils.createLookAt(new Vector3(x,y,z), Vector3.ZERO, Vector3.Up);
			shader.setProjectionMatrix(perspective);
			shader.setViewMatrix(view);		
			renderer.setShader(shader);
			renderer.enableColors();

			
			batch.begin();
			batch.AddQuad(corners[0], corners[1], corners[2], corners[3]);
			batch.AddQuad(corners[7], corners[6], corners[5], corners[4]);
			batch.AddQuad(corners[3], corners[2], corners[6], corners[7]);
			batch.AddQuad(corners[4], corners[5], corners[1], corners[0]);
			batch.AddQuad(corners[1], corners[5], corners[6], corners[2]);
			batch.AddQuad(corners[4], corners[0], corners[3], corners[7]);
			batch.end();
			
			
			batch.draw();

			Display.update();
			ticks++;
			long dt = System.currentTimeMillis() - time;
			//run at 60fps
			Thread.sleep(16 - dt);
			
		}

		Display.destroy();
	}

	public static void main(String[] argv) throws InterruptedException {
		SpoutClient displayExample = new SpoutClient();
		displayExample.start();
	}
}
