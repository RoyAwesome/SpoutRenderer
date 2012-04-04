package org.spout.client;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;
import org.spout.api.render.Shader;
import org.spout.client.batcher.PrimitiveBatch;
import org.spout.client.renderer.shader.BasicShader;
import org.spout.client.renderer.vertexformat.PositionColor;

public class SpoutClient  {

	public static boolean glInfo = true;

	public void start() throws InterruptedException {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));

			Display.create();

			if(glInfo){

				System.out.println("OpenGL Information");
				System.out.println(GL11.glGetString(GL11.GL_VENDOR));
				System.out.println(GL11.glGetString(GL11.GL_RENDER));
				System.out.println(GL11.glGetString(GL11.GL_VERSION));
				System.out.println(GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
				System.out.println(GL11.glGetString(GL11.GL_EXTENSIONS));

			}

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}


		Shader shader = new BasicShader();
		//Shader shader = new ClientShader("src/main/resources/vBasicLight.glsl", "src/main/resources/fBasicLight.glsl");


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

			Matrix view = Matrix.createLookAt(new Vector3(x,y,z), Vector3.ZERO, Vector3.UP);

			if(shader instanceof BasicShader){
				((BasicShader) shader).setViewMatrix(view);
			}else{
				shader.setUniform("View", view);		
			}
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
			if(ticks % 100 == 0){
				long total = System.currentTimeMillis() - time;
				System.out.println("fps: " + (1.0/total) * 1000);
			}


		}

		Display.destroy();
	}


	public static int n = 10;

	public static void main(String[] argv) throws InterruptedException, LWJGLException {
		try {
			URI url = new URI("texture://Vanilla/path/to/texture.png");
			System.out.println(url.getHost());
			System.out.println(url.getPath());
			System.out.println(url.getScheme());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		SpoutClient displayExample = new SpoutClient();
		displayExample.start();
	}

	
}
