package client;

import entities.Message;
import entities.MovieInfo;
import jdk.jfr.Event;
import org.greenrobot.eventbus.EventBus;

import ocsf.AbstractClient;

import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super("localhost",3000);
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
		else if(message.getMessage().startsWith("ListOfMovieInfos")){
			EventBus.getDefault().post(new MovieInfoListEvent(message));
			return;
		}
		else if(message.getMessage().startsWith("updatedtimes")){
			EventBus.getDefault().post(new TimeUpdateEvent(message));
		}
		else if(message.getMessage().startsWith("background image")){
			EventBus.getDefault().post(new BackgroundImageEvent(message));
		}
		else if(message.getMessage().startsWith("movie added")){
			EventBus.getDefault().post(new MovieAddedSuccesfullyEvent(message));
		}
		else if(message.getMessage().startsWith("movie exists")){
			EventBus.getDefault().post(new MovieAlreadyExistsEvent(message));
		}
		else if(message.getMessage().startsWith("movie removed")){
			EventBus.getDefault().post(new MovieRemovedSuccesfullyEvent(message));
		}
		else if(message.getMessage().startsWith("opening gif")){
			EventBus.getDefault().post(new OpeningGifEvent(message));
		}
		else if(message.getMessage().startsWith("ListOfUserInfos")){
			EventBus.getDefault().post(new UserInfoListEvent(message));
		}
		else {
			EventBus.getDefault().post(new MessageEvent(message));
		}

	}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}
