package client;

import entities.Message;
import entities.MovieInfo;
import org.greenrobot.eventbus.EventBus;

import ocsf.AbstractClient;

import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message message = (Message) msg;
		if(message.getMessage().startsWith("EMPTY MESSAGE")){
			EventBus.getDefault().post(new MessageEvent(message));
			return;
		}
		else if(message.getMessage().startsWith("MovieInfo")) {
			EventBus.getDefault().post(new MovieInfoEvent(message));
			return;
		}
		else if(message.getMessage().startsWith("ListOfMovies")){
			EventBus.getDefault().post(new MovieInfoListEvent(message));
			return;
		}
		else if(message.getMessage().startsWith("updatedTimes")){
			EventBus.getDefault().post(new TimeUpdateEvent(message));
		}
		else if(message.getMessage().startsWith("timealreadytaken")){
			EventBus.getDefault().post(new TimeTakenEvent(message));
		}

		/*
		if(message.getMessage().equals("update submitters IDs")){
			EventBus.getDefault().post(new UpdateMessageEvent(message));
		}else if(message.getMessage().equals("client added successfully")){
			EventBus.getDefault().post(new NewSubscriberEvent(message));
		}else if(message.getMessage().equals("Error! we got an empty message")){
			EventBus.getDefault().post(new ErrorEvent(message));
		}
		else {
			EventBus.getDefault().post(new MessageEvent(message));
		}*/
	}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
