package server;


import entities.Message;
import entities.MovieInfo;
import javassist.bytecode.ExceptionTable;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import ocsf.SubscribedClient;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static Session session;
	private static SessionFactory sessionFactory;
	private MovieInfo currMovieInfo;
	private Movie currMovie;
	private List<DisplayTime> currDisplayTimes=new ArrayList<>();

	private static SessionFactory getSessionFactory() throws HibernateException{
		Configuration configuration=new Configuration();

		configuration.addAnnotatedClass(Msg.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(DisplayTime.class);

		ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}


	//only use these functions if protoype database gets deleted, it should not happen.
	public static void generateMovies() throws Exception {
			Movie movie1=new Movie("Margol","1973");
			session.save(movie1);
			Movie movie2=new Movie("The Boys","2018");
			session.save(movie2);
			Movie movie3=new Movie("Scary Movie 5","2012");
			session.save(movie3);
			Movie movie4=new Movie("House of Cards","2006");
			session.save(movie4);
			Movie movie5=new Movie("Pulp Fiction","1969");
			session.save(movie5);
			Movie movie6=new Movie("Automobiles","2024");
			session.save(movie6);
            session.flush();
	}


	void addTimeToCurrentMovie(String time) {
		//check if the displaytime already exists, if yes add the movie to it's list, else, create a new displaytime and set the movie
		try {

			session.saveOrUpdate(this.currMovie);

			session.flush();

			session.getTransaction().commit();
			session.beginTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addDisplayTimeToDB(String time){
		this.currMovie=getMovieByTitleFromDB(this.currMovie.getName());
		this.currMovieInfo=this.currMovie.getMovieInfo();
		try {
			for(DisplayTime d : this.currMovie.getDisplayTimes()){
				if(time.equals(d.getDisplayTime())){
					//displaytime exists in movie so it also exists in db, nothing to do.
					return;
				}
			}
			List<DisplayTime> mylist = getDisplayTimesFromDB();
			for (DisplayTime d : mylist) {
				if (time.equals(d.getDisplayTime())) {

					//displaytime exists in DB but not in movie, so only add to movie

					d.addMovie(this.currMovie);
					this.currMovie.addDisplayTime(d);
					session.saveOrUpdate(d);
					session.saveOrUpdate(this.currMovie);
					session.flush();
					session.getTransaction().commit();
					session.beginTransaction();
					return;
				}

			}
			DisplayTime dis = new DisplayTime(time);
			dis.addMovie(this.currMovie);
			this.currMovie.addDisplayTime(dis);
			session.save(dis);
			session.saveOrUpdate(this.currMovie);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	void removeDisplayTimeFromMovieFromDB(String displayTime) {
		this.currMovie = getMovieByTitleFromDB(this.currMovie.getName());
		this.currMovieInfo = this.currMovie.getMovieInfo();
		this.currDisplayTimes = getDisplayTimesFromDB();
		boolean found = false;
		boolean found2 = false; // if displaytime is in table of displaytimes
		DisplayTime d_movie=null;
		DisplayTime d_table=null;
		try {
			for (DisplayTime d : this.currDisplayTimes) {
				if (d.getDisplayTime().equals(displayTime)) {
					found2 = true;
					d_table=d;
					break;
				}
			}
			//we found the displayDate to remove in all of displaydates in table
			//now check if our movie has it too
			for (DisplayTime d1 : this.currMovie.getDisplayTimes()) {
				//check if one of movie's displaytime is the one we look for
				if (d1.getDisplayTime().equals(displayTime)) {
					found = true; // if movie's displaytime has the displaytime
					d_movie = d1;
					break;
				}
			}
			if (found && d_movie!=null) { //movie has it
				this.currMovie.removeDisplayTime(d_movie);
				this.currMovieInfo = this.currMovie.getMovieInfo();
				d_movie.removeMovie(this.currMovie);
				if (found2){
					if(d_table.getMovies().isEmpty()){
						removeObjectWithName(currDisplayTimes, d_table.getDisplayTime());
						session.delete(d_table);
					}
					else{
						session.saveOrUpdate(d_table);
					}
					session.flush();
				}

			}
			session.saveOrUpdate(this.currMovie);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addMsgToDB(String text) throws Exception{
		Msg m=new Msg(text);
		session.save(m);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
	}



	private Movie getMovieByTitleFromDB(String title){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		Root<Movie> root=query.from(Movie.class);
		Predicate titlePredicate=builder.equal(root.get("name"),title);
		query.where(titlePredicate);
		return session.createQuery(query).getSingleResult();
	}

	private List<Movie> getMoviesFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		query.from(Movie.class);
        return session.createQuery(query).getResultList();
	}

	private List<Msg> getMsgs(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Msg> query=builder.createQuery(Msg.class);
		query.from(Msg.class);
		List<Msg> msgs=session.createQuery(query).getResultList();
		return msgs;
	}

	private DisplayTime getDisplayTimeFromDB(String displayTimeString){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<DisplayTime> query=builder.createQuery(DisplayTime.class);
		Root<DisplayTime> root=query.from(DisplayTime.class);
		Predicate titlePredicate=builder.equal(root.get("Display_Time_And_Date"),displayTimeString);
		query.where(titlePredicate);
		return session.createQuery(query).getSingleResult();
	}

	private List<DisplayTime> getDisplayTimesFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<DisplayTime> query=builder.createQuery(DisplayTime.class);
		query.from(DisplayTime.class);
		return session.createQuery(query).getResultList();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		System.out.println("Got message: "+message.getMessage());
		String request = message.getMessage();

		try {
			if (request.isBlank()) {
				message.setMessage("EMPTY MESSAGE");
				client.sendToClient(message);
			}
			else if (request.startsWith("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				message.setMessage("client added successfully");
			}
			else if(request.startsWith("getMovieInfo")){
				String[] splitted=request.split(" ",2);
				this.currMovie=getMovieByTitleFromDB(splitted[1]);
				this.currMovieInfo=this.currMovie.getMovieInfo();
				message.setMessage("MovieInfo");
				message.setMovieInfo(this.currMovie.getMovieInfo());
				client.sendToClient(message);
			}
			else if(request.startsWith("getTitles")){
				List<Movie> movies=getMoviesFromDB();
				List<MovieInfo> movieInfos=new ArrayList<MovieInfo>();
				for(Movie m: movies){
					movieInfos.add(m.getMovieInfo());
				}
				message.setList(movieInfos);
				message.setMessage("ListOfMovieInfos");
				client.sendToClient(message);
			}
			else if (request.startsWith("addtime")) {
				addDisplayTimeToDB(request.substring(8));
				//addTimeToCurrentMovie(request.substring(8));
				//this.currMovie.getDisplayTimes()=getDisplayTimesFromDB(); //this line may be moved to somewhere else
				message.setMovieInfo(this.currMovie.getMovieInfo());
				message.setMessage("updatedtimes");
				client.sendToClient(message);
			}
			else if(request.startsWith("removetime")){
				removeDisplayTimeFromMovieFromDB(request.substring(11));
				message.setMovieInfo(this.currMovie.getMovieInfo());
				message.setMessage("updatedtimes");
				client.sendToClient(message);
			}
			else {
				addMsgToDB(request);
				StringBuilder s = new StringBuilder();
				List<Msg> msgs = getMsgs();
				for (Msg msg1 : msgs) {
					s.append(msg1.getText()).append("\n");
				}
				message.setMessage(s.toString());
				client.sendToClient(message);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*
			//we got an empty message, so we will send back an error message with the error details.
			if (request.isBlank()){
				message.setMessage("Error! we got an empty message");
				client.sendToClient(message);
			}
			//we got a request to change submitters IDs with the updated IDs at the end of the string, so we save
			// the IDs at data field in Message entity and send back to all subscribed clients a request to update
			//their IDs text fields. An example of use of observer design pattern.
			//message format: "change submitters IDs: 123456789, 987654321"
			else if(request.startsWith("change submitters IDs:")){
				message.setData(request.substring(23));
				message.setMessage("update submitters IDs");
				sendToAllClients(message);
			}
			//we got a request to add a new client as a subscriber.
			else if (request.startsWith("add client")){
				SubscribedClient connection = new SubscribedClient(client);
				SubscribersList.add(connection);
				message.setMessage("client added successfully");
				client.sendToClient(message);
			}
			//we got a message from client requesting to echo Hello, so we will send back to client Hello world!
			else if(request.startsWith("echo Hello")){
				message.setMessage("Hello World!");
				client.sendToClient(message);
			}
			else if(request.startsWith("send Submitters IDs")){
				//add code here to send submitters IDs to client
				message.setMessage("316451012, 206234874");
				client.sendToClient(message);

			}
			else if (request.startsWith("send Submitters")){
				//add code here to send submitters names to client
				message.setMessage("Idan, Nir");
				client.sendToClient(message);
			}
			else if (request.contains("time")) {
				//add code here to send the time to client
				message.setMessage(message.getTimeStamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				//client.sendToClient(message);
				client.sendToClient(message);
			}
			else if (request.startsWith("multiply")){
				//add code here to multiply 2 numbers received in the message and send result back to client
				//(use substring method as shown above)
				//message format: "multiply n*m"
				int n=Integer.parseInt(request.substring(9,10));
				int m=Integer.parseInt(request.substring(11,12));
				message.setMessage(n+" * "+m+" = "+(n*m));
				client.sendToClient(message);
			}else{
				//add code here to send received message to all clients.
				//The string we received in the message is the message we will send back to all clients subscribed.
				//Example:
					// message received: "Good morning"
					// message sent: "Good morning"
				//see code for changing submitters IDs for help
				sendToAllClients(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
	}

	 	/*
	 	THIS PART WILL BE USEFUL TO ADDRESS AND UPDATE ALL CLIENTS, DATABASE STUFF FOR EXAMPLE
	 	 */

	public void stopServer(){
		try {
			if (session != null) {
				session.getTransaction().commit();
				session.close();
			}
			if (sessionFactory != null) {
				sessionFactory.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public SimpleServer(int port) {
		super(port);
		try{
			sessionFactory=getSessionFactory();
			session=sessionFactory.openSession();
			session.beginTransaction();

		} catch(Exception e) {
			if(session!=null){
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}//uncomment this section when running server for the first time
		try{
			generateMovies();
		}catch(Exception e) {
			e.printStackTrace();
		}
		session.getTransaction().commit();
		session.beginTransaction();
	}

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void removeObjectWithName(List<DisplayTime> list, String nameToRemove) {
		Iterator<DisplayTime> iterator = list.iterator();
		while (iterator.hasNext()) {
			DisplayTime obj = iterator.next();
			if (obj.getDisplayTime().equals(nameToRemove)) {
				iterator.remove();
			}
		}
	}
}
