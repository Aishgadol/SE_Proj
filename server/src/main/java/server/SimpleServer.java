package server;


import entities.Message;
import entities.MovieInfo;
import entities.UserInfo;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import ocsf.SubscribedClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static Session session;
	private static SessionFactory sessionFactory;
	private MovieInfo currMovieInfo;
	private Movie currMovie;
	private boolean gifMode=false;
	private List<MovieInfo> movieInfoList=new ArrayList<>(); //this list holds current movies in db at current time, keep it updated
	private List<DisplayTime> currDisplayTimes=new ArrayList<>();
	private List<Worker> workersList=new ArrayList<>();
	private List<Customer> customersList=new ArrayList<>();
	private List<UserInfo> userInfoList=new ArrayList<>();


	private static SessionFactory getSessionFactory() throws HibernateException{
		Configuration configuration=new Configuration();

		configuration.addAnnotatedClass(Msg.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(DisplayTime.class);
		configuration.addAnnotatedClass(Worker.class);
		configuration.addAnnotatedClass(Customer.class);

		ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}

	//only use these functions if protoype database gets deleted, it should not happen.

	public void generateData() throws Exception {
			Movie movie1=new Movie("Margol","1973","Romance","Zohar Argov","Margalit Tzanany, Eyal Golan, Shimi Tavory","MARGOL!","Upcoming");
			movie1.setImageData(getImageFromFilesByTitleAsByteArray("margol"));
			session.save(movie1);
			Movie movie2=new Movie("The Boys","2018","Action","Billy Butcher","God","A bunch of no-good people trying to steal the country, will homelander be able to stop them?","Available");
			movie2.setImageData(getImageFromFilesByTitleAsByteArray("the_boys"));
			session.save(movie2);
			Movie movie3=new Movie("Scary Movie 5","2012","Comedy","Snoop Dogg","Terry Crews, Charlie Sheen", "Funny movie you should watch, its the 5th one","Available");
			movie3.setImageData(getImageFromFilesByTitleAsByteArray("scary_movie_5"));
			session.save(movie3);
			Movie movie4=new Movie("House of Cards","2006","Drama","Boujee Herzog","Bibi Netanyahu, Simha Rotman, Yoav Gallant, Tali Gotlib","Welcome to the house of corruption!","Available");
			movie4.setImageData(getImageFromFilesByTitleAsByteArray("house_of_cards"));
			session.save(movie4);
			Movie movie5=new Movie("Pulp Fiction","1969","Action","Quentin Tarantino","John Travolta, Samuel L. Jackson, Uma Thurman","Cool gangsters, bruce willis punching a mafia boss, drugs and dancing","Available");
			movie5.setImageData(getImageFromFilesByTitleAsByteArray("pulp_fiction"));
			session.save(movie5);
			Movie movie6=new Movie("Automobiles","2024","Comedy","Steven Spielberg","Dwayne Johnson, Kevin Hart, Kobi82","Parody movie about the movie Cars, much funnier to my opinion","Available");
			movie6.setImageData(getImageFromFilesByTitleAsByteArray("automobiles"));
			session.save(movie6);
			Worker w1=new Worker("111111111","General Manager","Kobi Kobi","123456");
			session.save(w1);
			Customer c1=new Customer("000000000","bo");
			session.save(c1);
            session.flush();
	}




	private List<Worker> getWorkersFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Worker> query=builder.createQuery(Worker.class);
		query.from(Worker.class);
        return session.createQuery(query).getResultList();
	}
	private List<Customer> getCustomersFromDB(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Customer> query=builder.createQuery(Customer.class);
		query.from(Customer.class);
        return session.createQuery(query).getResultList();
	}

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
		for(MovieInfo m : movieInfoList){
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

	//this func handles adding movie object to movie table
	private boolean addMovieToDB(MovieInfo movieInfo) throws IOException {
		//check if movie is already in movies, no error message sent back cuz not needed
		for(MovieInfo m:this.movieInfoList){
			if(m.getName().equals(movieInfo.getName())){
				//if the movie already exists in db, just dont do anything
				return false;
			}
		}
		this.movieInfoList.add(movieInfo);
		Movie movie=new Movie(movieInfo);
		movie.setImageData(movieInfo.getImageData());
		session.save(movie);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		return true;
	}
	private boolean connectCustomer(String name){
		this.customersList=getCustomersFromDB();
		for(Customer c:this.customersList){
			if(c.getName().toLowerCase().equals(name)){
				c.setConnected(1);
				session.saveOrUpdate(c);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
				setUserInfoList();
				return true;
			}
		}
		return false;
	}
	private boolean connectWorker(String name){
		this.workersList=getWorkersFromDB();
		for(Worker w:this.workersList){
			if(w.getName().toLowerCase().equals(name)){
				w.setConnected(1);
				session.saveOrUpdate(w);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
				setUserInfoList();
				return true;
			}
		}
		return false;
	}
	private boolean disconnectCustomer(String name){
		this.customersList=getCustomersFromDB();
		for(Customer c:this.customersList){
			if (c.getName().toLowerCase().equals(name)) {
				c.setConnected(0);
				session.saveOrUpdate(c);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
				setUserInfoList();
				return true;
			}
		}
		return false;
	}
	private boolean disconnectWorker(String name){
		this.workersList=getWorkersFromDB();
		for(Worker w:this.workersList){
			if (w.getName().toLowerCase().equals(name)) {
				w.setConnected(0);
				session.saveOrUpdate(w);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
				setUserInfoList();
				return true;
			}
		}
		return false;
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


	//this func handles both removing movie object from movie table and movieimage object from movie_images table
	private boolean removeMovieFromDB(String name){
		Movie movieToDelete=getMovieByTitleFromDB(name);
		try {
			//make sure all movie's displaytimes know the movie is gone
			//remove all display times from movie to delete before deletding movie
			/*for(DisplayTime d: movieToDelete.getDisplayTimes()){
				this.currMovie=movieToDelete;
				removeDisplayTimeFromMovieFromDB(d.getDisplayTime());
			}*/
			for(DisplayTime d:getDisplayTimesFromDB()){
				removeDisplayTimeFromMovieFromDB(d.getDisplayTime());
			}
			this.movieInfoList = removeMovieInfoWithName(this.movieInfoList, movieToDelete.getName());
			session.saveOrUpdate(movieToDelete);
			session.delete(movieToDelete);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
			setMovieInfoList();
		}catch(Exception e){
			e.printStackTrace();
		}
			return true;

	}


	private void removeDisplayTimeFromMovieFromDB(String displayTime) {
		if(this.currMovie!=null){
			this.currMovie = getMovieByTitleFromDB(this.currMovie.getName());
			this.currMovieInfo = getMovieInfoByTitle(this.currMovie.getName());
		}
		else{
			return;
		}
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
						this.currDisplayTimes=removeDisplayTimeWithName(currDisplayTimes, d_table.getDisplayTime());
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
	private byte[] getImageFromFilesByTitleAsByteArray(String title){
		Path path;
		if(gifMode){
			path=Paths.get("src/main/resources/"+title+".gif");
		}
		else {
			path = Paths.get("src/main/resources/" + title + ".jpg");
		}
		byte[] imageData=null;
		try {
			imageData = Files.readAllBytes(path);
		}catch(Exception e){
			e.printStackTrace();
		}
		return imageData;
	}

	private void setMovieInfoList(){
		this.movieInfoList=new ArrayList<>();
		byte[] movieImageByteArray=null;
		List<Movie> movies=getMoviesFromDB();
		for(Movie m: movies){
			MovieInfo mi=new MovieInfo(m.getName(),m.getReleasedate(),m.getGenre(),m.getProducer(),m.getActors(),m.getSummary(),m.getStatus());
			mi.setImageData(m.getImageData());
			//add display times from movie to movieinfo
			for(DisplayTime d: m.getDisplayTimes()){
				mi.addDisplayTime(d.getDisplayTime());
			}
			this.movieInfoList.add(mi);
		}
	}
	private void setUserInfoList(){
		this.customersList=getCustomersFromDB();
		this.workersList=getWorkersFromDB();
		this.userInfoList=new ArrayList<>();
		UserInfo u=null;
		for(Customer c: customersList){
			u=new UserInfo(c.getId(),c.getName());
			u.setConnected(c.getConnected());
			userInfoList.add(u);
		}
		for(Worker w: workersList){
			u=new UserInfo(w.getId(),w.getRole(),w.getName(),w.getPassword());
			u.setConnected(w.getConnected());
			userInfoList.add(u);
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
				this.currMovieInfo=getMovieInfoByTitle(splitted[1]);

				message.setMessage("MovieInfo");
				message.setMovieInfo(getMovieInfoByTitle(splitted[1]));
				client.sendToClient(message);
			}
			else if(request.startsWith("getTitles")){
				setMovieInfoList();
				message.setMovieInfoList(this.movieInfoList);
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
			else if(request.startsWith("getBackgroundImage")){
				message.setImageData(getImageFromFilesByTitleAsByteArray("namal"));
				message.setMessage("background image");
				client.sendToClient(message);
			}
			else if(request.startsWith("getOpeningGif")){
				gifMode=true;
				message.setImageData(getImageFromFilesByTitleAsByteArray("popCorn"));
				gifMode=false;
				message.setMessage("opening gif");
				client.sendToClient(message);
			}
			else if(request.startsWith("addMovie")){
				if(addMovieToDB(message.getMovieInfo())){
					message.setMessage("movie added");
				}
				else{
					message.setMessage("movie exists");
				}
				sendToAllClients(message);
			}
			else if(request.startsWith("removeMovie")){
				if(removeMovieFromDB(message.getMovieInfo().getName())){
					message.setMessage("movie removed succesfully");
				}
				sendToAllClients(message);

			}
			else if(request.startsWith("getUsers")){
				setUserInfoList();
				message.setUserInfoList(this.userInfoList);
				message.setMessage("ListOfUserInfos");
				client.sendToClient(message);
			}
			else if(request.startsWith("connectCustomer")){
				String[] splitted=request.split(" ",2);
				if(connectCustomer(splitted[1].toLowerCase())){
					message.setMessage("Customer succesfully connected");
					message.setUserInfoList(this.userInfoList);
					sendToAllClients(message);
				}
			}
			else if(request.startsWith("connectWorker")){
				String[] splitted=request.split(" ",2);
				if(connectWorker(splitted[1].toLowerCase())){
					message.setMessage("Worker succesfully connected");
					message.setUserInfoList(this.userInfoList);
					sendToAllClients(message);
				}
			}
			else if(request.startsWith("disconnectCustomer")){
				String[] splitted=request.split(" ",2);
				if(disconnectCustomer(splitted[1].toLowerCase())){
					message.setMessage("Customer succesfully disconnected");
					message.setUserInfoList(this.userInfoList);
					sendToAllClients(message);
				}
			}
			else if(request.startsWith("disconnectWorker")){
				String[] splitted=request.split(" ",2);
				if(disconnectWorker(splitted[1].toLowerCase())){
					message.setMessage("Worker succesfully disconnected");
					message.setUserInfoList(this.userInfoList);
					sendToAllClients(message);
				}
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
			//setMovieInfoList(); //uncomment this when hibernate is on update mode
			//setUserInfoList();
		} catch(Exception e) {
			if(session!=null){
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}//uncomment this section when running server for the first time
		try{
			generateData();
			setMovieInfoList();
			setUserInfoList();
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

	private List<DisplayTime> removeDisplayTimeWithName(List<DisplayTime> list, String nameToRemove) {
		Iterator<DisplayTime> iterator = list.iterator();
		while (iterator.hasNext()) {
			DisplayTime obj = iterator.next();
			if (obj.getDisplayTime().equals(nameToRemove)) {
				iterator.remove();
			}
		}
		return list;
	}
	private List<Movie> removeMovieWithName(List<Movie> list, String nameToRemove) {
		Iterator<Movie> iterator = list.iterator();
		while (iterator.hasNext()) {
			Movie obj = iterator.next();
			if (obj.getName().equals(nameToRemove)) {
				iterator.remove();
			}
		}
		return list;
	}
	private List<MovieInfo> removeMovieInfoWithName(List<MovieInfo> list, String nameToRemove) {
		Iterator<MovieInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			MovieInfo obj = iterator.next();
			if (obj.getName().equals(nameToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<Customer> removeCustomerWithId(List<Customer> list, String idToRemove) {
		Iterator<Customer> iterator = list.iterator();
		while (iterator.hasNext()) {
			Customer obj = iterator.next();
			if (obj.getId().equals(idToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<Worker> removeWorkerWithId(List<Worker> list, String idToRemove) {
		Iterator<Worker> iterator = list.iterator();
		while (iterator.hasNext()) {
			Worker obj = iterator.next();
			if (obj.getId().equals(idToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
}
