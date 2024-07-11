package server;


import entities.Message;
import entities.MovieInfo;
import javassist.bytecode.ExceptionTable;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import ocsf.SubscribedClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private List<MovieInfo> movieInfos=new ArrayList<>();
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
	/*
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
	}*/

	private List<Movie> getMoviesFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		query.from(Movie.class);
        return session.createQuery(query).getResultList();
	}

	private Movie getMovieByTitleFromDB(String title){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		Root<Movie> root=query.from(Movie.class);
		Predicate titlePredicate=builder.equal(root.get("name"),title);
		query.where(titlePredicate);
		List<Movie> movies=session.createQuery(query).getResultList();
		if(movies!=null && !movies.isEmpty()){
			this.currMovie=movies.get(0);
			this.currMovieInfo=getMovieInfoByTitle(this.currMovie.getName());
			return this.currMovie;
		}
		return null;
	}

	private List<DisplayTime> getDisplayTimesFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<DisplayTime> query=builder.createQuery(DisplayTime.class);
		query.from(DisplayTime.class);
		return session.createQuery(query).getResultList();
	}

	private MovieInfo getMovieInfoByTitle(String title){
		for(MovieInfo m : movieInfos){
			if(m.getName().equals(title)){
				return m;
			}
		}
		return null;
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
		List<DisplayTime> dis=session.createQuery(query).getResultList();
		if(dis!=null && !dis.isEmpty()){
			return dis.get(0);
		}
		return null;
	}

	// updates display time for the current movie selected
	public void addDisplayTimeToDB(String time){
		this.currMovie=getMovieByTitleFromDB(this.currMovie.getName());
		this.currMovieInfo=getMovieInfoByTitle(this.currMovie.getName());
		try {
			for(DisplayTime d : this.currMovie.getDisplayTimes()){
				if(time.equals(d.getDisplayTime())){
					//display time exists in movie so it also exists in db, nothing to do. 123
					return;
				}
			}
			List<DisplayTime> mylist = getDisplayTimesFromDB();
			for (DisplayTime d : mylist) {
				if (time.equals(d.getDisplayTime())) {

					//displaytime exists in DB but not in movie, so only add to movie

					d.addMovie(this.currMovie);
					this.currMovie.addDisplayTime(d);
					this.currMovieInfo.addDisplayTime(d.getDisplayTime());
					session.saveOrUpdate(d);
					session.saveOrUpdate(this.currMovie);
					session.flush();
					session.getTransaction().commit();
					session.beginTransaction();
					return;
				}
			}
			//we got here so the displaytime doesnt exist in db or in movie, add both
			DisplayTime dis = new DisplayTime(time);
			dis.addMovie(this.currMovie);
			this.currMovie.addDisplayTime(dis);
			this.currMovieInfo.addDisplayTime(dis.getDisplayTime());
			session.save(dis);
			session.saveOrUpdate(this.currMovie);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
		}catch(Exception e){
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

	void removeDisplayTimeFromMovieFromDB(String displayTime) {
		this.currMovie = getMovieByTitleFromDB(this.currMovie.getName());
		this.currMovieInfo = getMovieInfoByTitle(this.currMovie.getName());
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
				this.currMovieInfo = getMovieInfoByTitle(this.currMovie.getName());
				d_movie.removeMovie(this.currMovie);
				this.currMovieInfo.removeDisplayTime(d_movie.getDisplayTime());
				if (found2){
					if(d_table.getMovies().isEmpty()){
						removeObjectWithName(currDisplayTimes, d_table.getDisplayTime());
						session.delete(d_table);
					}
					else{
						session.saveOrUpdate(d_table);
					}
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

	//BIG NOTE: TITLE MUST BE ALL LOWERCASE, WITH _ INSTEAD OF SPACES
	private byte[] getImageByTitleAsByteArray(String title){
		Path path= Paths.get("src/main/resources/"+title+".jpg");
		byte[] imageData=null;
		try {
			imageData = Files.readAllBytes(path);
		}catch(Exception e){
			e.printStackTrace();
		}
		return imageData;
	}

	private void setMovieInfos(){
		this.movieInfos=new ArrayList<>();
		List<Movie> movies=getMoviesFromDB();
		for(Movie m: movies){
			MovieInfo mi=new MovieInfo(m.getName(),m.getReleasedate());
			//add display times from movie to movieinfo
			for(DisplayTime d: m.getDisplayTimes()){
				mi.addDisplayTime(d.getDisplayTime());
			}
			//add image from files to movieinfo
			mi.setImageData(getImageByTitleAsByteArray(m.getName().replaceAll(" ","_").toLowerCase()));
			this.movieInfos.add(mi);
		}
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
			else if(request.startsWith("remove client")){
				SubscribedClient x=new SubscribedClient(client);
				SubscribersList.remove(x);
				message.setMessage("client removed succesfully");
			}
			else if(request.startsWith("getMovieInfo")){
				String[] splitted=request.split(" ",2);
				this.currMovie=getMovieByTitleFromDB(splitted[1]);
				this.currMovieInfo=getMovieInfoByTitle(this.currMovie.getName());

				message.setMessage("MovieInfo");
				message.setMovieInfo(getMovieInfoByTitle(this.currMovie.getName()));
				client.sendToClient(message);
			}
			else if(request.startsWith("getTitles")){
				setMovieInfos();
				message.setList(this.movieInfos);
				message.setMessage("ListOfMovieInfos");
				client.sendToClient(message);
			}
			else if (request.startsWith("addtime")) {
				addDisplayTimeToDB(request.substring(8));
				message.setMovieInfo(getMovieInfoByTitle(this.currMovie.getName()));
				message.setMessage("updatedtimes");
				sendToAllClients(message);
			}
			else if(request.startsWith("removetime")){
				removeDisplayTimeFromMovieFromDB(request.substring(11));
				message.setMovieInfo(getMovieInfoByTitle(this.currMovie.getName()));
				message.setMessage("updatedtimes");
				sendToAllClients(message);
			}
			else if(request.startsWith("getOpeningImage")){
				message.setImageData(getImageByTitleAsByteArray("cinemapicture"));
				message.setMessage("opening image");
				client.sendToClient(message);
			}
			else if(request.startsWith("getBackgroundImage")){
				message.setImageData(getImageByTitleAsByteArray("namal"));
				message.setMessage("background image");
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
			setMovieInfos(); //uncomment this when hibernate is on update mode
		} catch(Exception e) {
			if(session!=null){
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}//uncomment this section when running server for the first time
		/*try{
			generateMovies();
			setMovieInfos();
		}catch(Exception e) {
			e.printStackTrace();
		}
		session.getTransaction().commit();
		session.beginTransaction();*/
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
