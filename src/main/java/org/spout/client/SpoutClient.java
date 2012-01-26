package org.spout.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.spout.api.Client;
import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandSource;
import org.spout.api.command.RootCommand;
import org.spout.api.entity.Entity;
import org.spout.api.event.EventManager;
import org.spout.api.event.SimpleEventManager;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.Recipe;
import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spout.client.batcher.PrimitiveBatch;
import org.spout.client.renderer.shader.BasicShader;
import org.spout.client.renderer.shader.ClientShader;
import org.spout.client.renderer.vertexformat.PositionColor;
import org.spout.api.player.Player;
import org.spout.api.plugin.CommonPluginManager;
import org.spout.api.plugin.Platform;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginManager;
import org.spout.api.plugin.PluginStore;
import org.spout.api.plugin.security.CommonSecurityManager;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.SessionRegistry;
import org.spout.api.protocol.bootstrap.BootstrapProtocol;
import org.spout.api.render.Shader;
import org.spout.api.scheduler.Scheduler;
import org.spout.api.util.Color;

import static org.lwjgl.opengl.GL11.*;

public class SpoutClient implements Client {

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

			Matrix view = Matrix.createLookAt(new Vector3(x,y,z), Vector3.ZERO, Vector3.Up);

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
		Spout.setGame(displayExample);
		displayExample.start();
	}

	private volatile int maxPlayers = 20;

	private volatile String primaryAddress = "localhost";

	private volatile String[] allAddresses;

	private File pluginDirectory = new File("plugins");

	private File configDirectory = new File("config");

	private File updateDirectory = new File("update");

	private String logFile = "logs/log-%D.txt";

	private String name = "Spout Server";
	/**
	 * Default world generator
	 */
	private WorldGenerator defaultGenerator = null;

	/**
	 * The security manager
	 * TODO - need to integrate this
	 */
	private CommonSecurityManager securityManager = new CommonSecurityManager(0);

	/**
	 * The plugin manager for the server
	 */
	private CommonPluginManager pluginManager = new CommonPluginManager(this, securityManager, 0.0);
	/**
	 * The logger for this class.
	 */
	public static final Logger logger = Logger.getLogger("Minecraft");

	/**
	 * A group containing all of the channels.
	 */
	private final ChannelGroup group = new DefaultChannelGroup();

	/**
	 * The network executor service - Netty dispatches events to this thread
	 * pool.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * If the server has a whitelist or not.
	 */
	private volatile boolean whitelist = false;

	/**
	 * If the server allows flight.
	 */
	private volatile boolean allowFlight = false;

	/**
	 * A list of all players who can log onto this server, if using a whitelist.
	 */
	private List<String> whitelistedPlayers = new ArrayList<String>();

	/**
	 * A list of all players who can not log onto this server.
	 */
	private List<String> bannedPlayers = new ArrayList<String>();

	/**
	 * A list of all operators.
	 */
	private List<String> operators = new ArrayList<String>();

	/**
	 * A folder that holds all of the world data folders inside of it. By default, it does not exist ('.'), meant for organizational purposes.
	 */
	private File worldFolder = new File(".");

	/**
	 * The root commnd for this server.
	 */
	private final RootCommand rootCommand = new RootCommand(this);

	/**
	 * The event manager.
	 */
	private final EventManager eventManager = new SimpleEventManager();

	private final ConcurrentMap<SocketAddress, BootstrapProtocol> bootstrapProtocols = new ConcurrentHashMap<SocketAddress, BootstrapProtocol>();


	public String getName() {
		return name;
	}

	public String getVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

	public List<String> getAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Player[] getOnlinePlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getAllAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	public void broadcastMessage(String message) {
		// TODO Auto-generated method stub

	}

	public PluginManager getPluginManager() {
		return pluginManager;
	}

	public Logger getLogger() {
		return logger;
	}

	public void processCommand(CommandSource source, String commandLine) {
		// TODO Auto-generated method stub

	}

	public File getUpdateFolder() {
		return updateDirectory;
	}

	public File getConfigFolder() {
		return configDirectory;
	}

	public Player getPlayer(String name, boolean exact) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Player> matchPlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public World getWorld(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public World getWorld(UUID uid) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<World> getWorlds() {
		// TODO Auto-generated method stub
		return null;
	}

	public World loadWorld(String name, WorldGenerator generator) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(boolean worlds, boolean players) {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public File getWorldFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Command getRootCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	public EventManager getEventManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public Platform getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	public Session newSession(Channel channel) {
		// TODO Auto-generated method stub
		return null;
	}

	public ChannelGroup getChannelGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	public SessionRegistry getSessionRegistry() {
		// TODO Auto-generated method stub
		return null;
	}

	public WorldGenerator getDefaultGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDefaultGenerator(WorldGenerator generator) {
		// TODO Auto-generated method stub

	}

	public Scheduler getScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addRecipe(Recipe recipe) {
		// TODO Auto-generated method stub

	}

	public Recipe getRecipe(Plugin plugin, String recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	public Recipe removeRecipe(Plugin plugin, String recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	public BootstrapProtocol getBootstrapProtocol(SocketAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	public File getAddonFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getAudioCache() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getTemporaryCache() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getTextureCache() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getTexturePackFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getSelectedTexturePackZip() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getStatsFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity getActivePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getCamera() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCamera(Point loc) {
		// TODO Auto-generated method stub

	}

	public void detachCamera(boolean detach) {
		// TODO Auto-generated method stub

	}

	public boolean isCameraDetached() {
		// TODO Auto-generated method stub
		return false;
	}

	public PluginStore getAddonStore() {
		// TODO Auto-generated method stub
		return null;
	}
}
