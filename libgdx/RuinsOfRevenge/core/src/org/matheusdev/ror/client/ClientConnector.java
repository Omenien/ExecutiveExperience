package org.matheusdev.ror.client;

import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.matheusdev.ror.net.packages.*;
import org.matheusdev.ror.server.ServerMaster;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author matheusdev
 */
public class ClientConnector extends Listener implements Disposable
{

	private boolean disposed;

	private final Client client;
	private final LinkedBlockingQueue<Runnable> queue;
	private final Input newestInput;
	private final ClientMaster master;

	private long serverTickTime = 0;

	public ClientConnector(ClientMaster master, String host) throws IOException
	{
		this.master = master;
		this.client = new Client();
		this.queue = new LinkedBlockingQueue<>();
		this.newestInput = new Input();

		Register.registerAll(client.getKryo());
		new Thread(client).start();
		client.connect(5000, InetAddress.getByName(host), ServerMaster.PORT_TCP, ServerMaster.PORT_UDP);
		client.addListener(new QueuedListener(this)
		{
			@Override
			protected void queue(Runnable runnable)
			{
				queue.add(runnable);
			}
		});
	}

	public Client getClient()
	{
		return client;
	}

	public void sendUDP(NetPackage pkg)
	{
		pkg.connectionType = NetPackage.UDP;
		client.sendUDP(pkg);
	}

	public void sendTCP(NetPackage pkg)
	{
		pkg.connectionType = NetPackage.TCP;
		client.sendTCP(pkg);
	}

	public void tick(long time)
	{
		Runnable run;
		while((run = queue.poll()) != null)
		{
			run.run();
		}
	}

	@Override
	public void connected(Connection connection)
	{
		System.out.println("[CLIENT]: connected to " + connection.getRemoteAddressTCP());
	}

	@Override
	public void disconnected(Connection connection)
	{
		System.out.println("[CLIENT]: client " + connection.getRemoteAddressTCP() + " disconnected.");
	}

	@Override
	public void received(Connection connection, Object object)
	{
		// Synchronize time or remove message, if too old:
		if(object instanceof NetPackage)
		{
			NetPackage pkg = (NetPackage) object;
			if(pkg.time >= serverTickTime)
			{
				serverTickTime = pkg.time;
			}
			else if(pkg.connectionType == NetPackage.UDP)
			{
				System.out.println(
						"Package dropped (pkg.time: " + pkg.time + "/servertime: " + serverTickTime + ": " + pkg);
				return;
			}
		}

		if(object instanceof Input)
		{
			if(master.hasInitialized())
			{
				Input in = (Input) object;
				newestInput.set(in.time, in);
			}
		}
		else if(object instanceof EntityState)
		{
			if(master.hasInitialized())
			{
				EntityState state = (EntityState) object;
				master.updateEntity(state);
			}
		}
		else if(object instanceof CreateEntity)
		{
			CreateEntity create = (CreateEntity) object;
			master.addEntity(create);
		}
		else if(object instanceof FetchEntities)
		{
			FetchEntities fetch = (FetchEntities) object;
			for(CreateEntity create : fetch.creates)
			{
				master.addEntity(create);
			}
		}
		else if(object instanceof DeleteEntity)
		{
			DeleteEntity delete = (DeleteEntity) object;
			master.removeEntity(delete.id);
		}
		else if(object instanceof ChatMessage)
		{
			master.writeChat(object.toString());
		}
		else
		{
			System.out.println("Recieved strange object: " + object);
		}
	}

	@Override
	public void dispose()
	{
		if(!disposed)
		{
			disposed = true;
			client.stop();
		}
	}

	public Input getNewestInput()
	{
		return newestInput;
	}

}
