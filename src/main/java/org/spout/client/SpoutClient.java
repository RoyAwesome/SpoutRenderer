package org.spout.client;

import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spout.client.batcher.PrimitiveBatch;
import org.spout.client.renderer.BatchVertexRenderer;
import org.spout.client.renderer.shader.BasicShader;
import org.spout.client.renderer.shader.ClientShader;
import org.spout.client.renderer.vertexformat.PositionColor;
import org.spout.api.render.Shader;
import org.spout.api.util.Color;

import static org.lwjgl.opengl.GL11.*;

public class SpoutClient {
	
	public void start() throws InterruptedException {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		
		//Shader shader = new BasicShader();
		Shader shader = new ClientShader("src/main/resources/vBasicLight.glsl", "src/main/resources/fBasicLight.glsl");
		
	
		PrimitiveBatch batch = new PrimitiveBatch();
		batch.getRenderer().setShader(shader);
		
		Color col = new Color(0.0f, .5f, 0.0f);
		Color col2 = new Color(0.0f, 0.0f, 0.5f);
		Color col3 = new Color(0.5f, 0.0f, 0.0f);
		
		Color ambientColor = new Color(.5f, .5f, .5f);
		
		Vector3 lightDirection = new Vector3(1.0f, 0, 0);
		Color lightColor = new Color(.9f, 0.0f, 0.0f);
		
		shader.setUniform("ambientColor", ambientColor);
		shader.setUniform("lightDirection", lightDirection);
		shader.setUniform("lightColor", lightColor);
		
	
		Matrix perspective = Matrix.createPerspective(60, 4.f/3.f, .1f, 100f);
		shader.setUniform("Projection", perspective);
		
		
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
			
			Matrix view = Matrix.createLookAt(new Vector3(x,y,z), Vector3.ZERO, Vector3.Up);
			
			shader.setUniform("View", view);		
			batch.getRenderer().setShader(shader);
			
		

			
			batch.begin();
			batch.addQuad(corners[0], corners[1], corners[2], corners[3]); //draws
			
			batch.addQuad(corners[7], corners[6], corners[5], corners[4]);
			
			batch.addQuad(corners[3], corners[2], corners[6], corners[7]);
			
			batch.addQuad(corners[4], corners[5], corners[1], corners[0]); //draws
			batch.addQuad(corners[1], corners[5], corners[6], corners[2]);
			batch.addQuad(corners[4], corners[0], corners[3], corners[7]);
			batch.end();
			
			
			batch.draw();

			Display.update();
			ticks++;			
			long dt = System.currentTimeMillis() - time;
			//run at 60fps
			if(dt < 16) Thread.sleep(16 - dt);
		
			
		}

		Display.destroy();
	}

	public static void main(String[] argv) throws InterruptedException {
		SpoutClient displayExample = new SpoutClient();
		displayExample.start();
	}
}
