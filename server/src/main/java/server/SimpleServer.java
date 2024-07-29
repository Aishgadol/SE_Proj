package server;


import entities.*;
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
import org.hibernate.service.ServiceRegistry;


public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private static Session session;
	private static SessionFactory sessionFactory;
	private MovieInfo currMovieInfo;
	private Movie currMovie;
	private boolean gifMode = false;
	private List<Movie> movieList=new ArrayList<>();
	private List<MovieInfo> movieInfoList = new ArrayList<>(); //this list holds current movies in db at current time, keep it updated
	private List<DisplayTime> displayTimeList = new ArrayList<>();
	private List<DisplayTimeInfo> displayTimeInfoList = new ArrayList<>();
	private List<Worker> workerList = new ArrayList<>();
	private List<Customer> customerList = new ArrayList<>();
	private List<UserInfo> userInfoList = new ArrayList<>();
	private List<Ticket> ticketList=new ArrayList<>();
	private List<TicketInfo> ticketInfoList = new ArrayList<>();
	private List<Cinema> cinemaList = new ArrayList<>();
	private List<CinemaInfo> cinemaInfoList = new ArrayList<>();

	private static SessionFactory getSessionFactory() throws HibernateException {
		Configuration configuration = new Configuration();

		configuration.addAnnotatedClass(Ticket.class);
		configuration.addAnnotatedClass(Msg.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(DisplayTime.class);
		configuration.addAnnotatedClass(Worker.class);
		configuration.addAnnotatedClass(Customer.class);
		configuration.addAnnotatedClass(Cinema.class);


		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
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
			Worker w2=new Worker("000000000", "General Manager", "k","1");
			session.save(w2);
			Customer c1=new Customer("000000000","bo");
			session.save(c1);
			Cinema cinema1=new Cinema("YisPlanit",5);
			session.save(cinema1);
			Cinema cinema2=new Cinema("NoPlanit",9);
			session.save(cinema2);
			DisplayTime d=new DisplayTime("10:30, 10/10/2024",movie5,cinema1);
			movie5.addDisplayTime(d);
			cinema1.addDisplayTime(d);
			session.saveOrUpdate(movie5);
			session.saveOrUpdate(cinema1);
			session.save(d);
			Ticket ti=new Ticket(movie5,1,1,1,cinema1,c1,d);
			session.save(ti);
            d.addTicket(ti);
			session.saveOrUpdate(d);
			movie1.addTicket(ti);
			session.saveOrUpdate(movie1);
			c1.addTicket(ti);
			session.saveOrUpdate(c1);
			cinema1.addTicket(ti);
			session.saveOrUpdate(cinema1);
			session.flush();

	}


	private List<Ticket> getTicketsFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
		query.from(Ticket.class);
		return session.createQuery(query).getResultList();
	}

	private List<Cinema> getCinemasFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Cinema> query = builder.createQuery(Cinema.class);
		query.from(Cinema.class);
		return session.createQuery(query).getResultList();
	}

	private List<Worker> getWorkersFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Worker> query = builder.createQuery(Worker.class);
		query.from(Worker.class);
		return session.createQuery(query).getResultList();
	}

	private List<Customer> getCustomersFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
		query.from(Customer.class);
		return session.createQuery(query).getResultList();
	}

	private List<Movie> getMoviesFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
		query.from(Movie.class);
		return session.createQuery(query).getResultList();
	}

	private Movie getMovieByTitleFromDB(String title) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
		Root<Movie> root = query.from(Movie.class);
		Predicate titlePredicate = builder.equal(root.get("name"), title);
		query.where(titlePredicate);
		List<Movie> movies = session.createQuery(query).getResultList();
		if (movies != null && !movies.isEmpty()) {
			this.currMovie = movies.get(0);
			this.currMovieInfo = getMovieInfoByTitle(this.currMovie.getName());
			return this.currMovie;
		}
		return null;
	}
	private Cinema getCinemaByNameFromDB(String name) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Cinema> query = builder.createQuery(Cinema.class);
		Root<Cinema> root = query.from(Cinema.class);
		Predicate titlePredicate = builder.equal(root.get("name"), name);
		query.where(titlePredicate);
		List<Cinema> cinemas = session.createQuery(query).getResultList();
		if (cinemas != null && !cinemas.isEmpty()) {
			Cinema c = cinemas.get(0);
			return c;
		}
		return null;
	}
	private Ticket getTicketByToStringFromDB(String str){
		this.ticketList=getTicketsFromDB();
		for(Ticket t:this.ticketList){
			if(t.toString().equals(str)){
				return t;
			}
		}
		return null;
	}
	//BIG NOTE: TITLE MUST BE ALL LOWERCASE, WITH _ INSTEAD OF SPACES
	private byte[] getImageFromFilesByTitleAsByteArray(String title) {
		Path path;
		if (gifMode) {
			path = Paths.get("src/main/resources/" + title + ".gif");
		} else {
			path = Paths.get("src/main/resources/" + title + ".jpg");
		}
		byte[] imageData = null;
		try {
			imageData = Files.readAllBytes(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageData;
	}

	private List<DisplayTime> getDisplayTimesFromDB() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<DisplayTime> query = builder.createQuery(DisplayTime.class);
		query.from(DisplayTime.class);
		return session.createQuery(query).getResultList();
	}
	private Customer getCustomerByIdFromDB(String id){
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
		Root<Customer> root = query.from(Customer.class);
		Predicate titlePredicate = builder.equal(root.get("User_ID"), id);
		query.where(titlePredicate);
		List<Customer> dis = session.createQuery(query).getResultList();
		if (dis != null && !dis.isEmpty()) {
			return dis.get(0);
		}
		return null;
	}
	private DisplayTime getDisplayTimeFromDB(String displayTimeString) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<DisplayTime> query = builder.createQuery(DisplayTime.class);
		Root<DisplayTime> root = query.from(DisplayTime.class);
		Predicate titlePredicate = builder.equal(root.get("Time_Date_Movie_Cinema"), displayTimeString);
		query.where(titlePredicate);
		List<DisplayTime> dis = session.createQuery(query).getResultList();
		if (dis != null && !dis.isEmpty()) {
			return dis.get(0);
		}
		return null;
	}

	private MovieInfo getMovieInfoByTitle(String title) {
		setMovieInfoList();
		for (MovieInfo m : movieInfoList) {
			if (m.getName().equals(title)) {
				return m;
			}
		}
		return null;
	}
	private TicketInfo getTicketInfoByToString(String str){
		for(TicketInfo t:this.ticketInfoList){
			if (t.toString().equals(str)) {
				return t;
			}
		}
		return null;
	}
	private UserInfo getUserInfoByName(String name) {
		int count = 0;
		UserInfo i = null;
		for (UserInfo u : this.userInfoList) {
			if (u.getName().equals(name)) {
				count++;
				i = new UserInfo(u);
			}
		}
		if (count > 1) {
			System.out.println("Found more than one UserInfo with name: " + name);
		}
		return i;
	}

	private UserInfo getUserInfoByID(String id) {
		for (UserInfo u : this.userInfoList) {
			if (u.getId().equals(id)) {
				return new UserInfo((u));
			}
		}
		return null;
	}

	private CinemaInfo getCinemaInfoByName(String name) {
		for (CinemaInfo c : this.cinemaInfoList) {
			if (c.getName().equals(name)) {
				return new CinemaInfo(c);
			}
		}
		return null;
	}

	private DisplayTimeInfo getDisplayTimeInfoByDisplayTime(String displayTime) {
		for (DisplayTimeInfo d : this.displayTimeInfoList) {
			if (d.getDisplayTime().equals(displayTime)) {
				return new DisplayTimeInfo(d);
			}
		}
		return null;
	}

	private List<Msg> getMsgs() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Msg> query = builder.createQuery(Msg.class);
		query.from(Msg.class);
		List<Msg> msgs = session.createQuery(query).getResultList();
		return msgs;
	}


	private boolean connectCustomer(String id) {
		this.customerList = getCustomersFromDB();
		for (Customer c : this.customerList) {
			if (c.getId().equals(id)) {
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

	private boolean connectWorker(String name) {
		this.workerList = getWorkersFromDB();
		for (Worker w : this.workerList) {
			if (w.getName().toLowerCase().equals(name)) {
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

	private boolean disconnectCustomer(String id) {
		this.customerList = getCustomersFromDB();
		for (Customer c : this.customerList) {
			if (c.getId().equals(id)) {
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

	private boolean disconnectWorker(String name) {
		this.workerList = getWorkersFromDB();
		for (Worker w : this.workerList) {
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

	private boolean addCinemaToDB(CinemaInfo cinemaInfo) throws IOException { //THIS FUNCTION ASSUMES A NEW CINEMA, NOT A COPY OF EXISTING ONE
		setCinemaInfoList();
		for (Cinema c : this.cinemaList) {
			if (c.getName().equals(cinemaInfo.getName())) {
				return false;
			}
		}
		Cinema c = new Cinema(cinemaInfo.getName(), cinemaInfo.getNumHalls());
		this.cinemaList.add(c);
		this.cinemaInfoList.add(cinemaInfo);
		session.save(c);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		return true;
	}

	private boolean addTicketToDB(TicketInfo ticketInfo) throws IOException{
		setTicketInfoList();
		CinemaInfo cinemaInfo=null;
		MovieInfo movieInfo=null;
		Customer c1=null;
		Cinema c=null;
		Movie m=null;
		DisplayTime d=null;
		for(Ticket t:this.ticketList){
			if(t.toString().equals(ticketInfo.toString())){
				if(t.getActive()!=0)
					return false; // ticket already exists, idk if this ever should happen
				else {
					removeTicketFromDB(ticketInfo);
					setTicketInfoList();
					break;
				}
			}
		}
		for(Movie m1:this.movieList){
			if(m1.getName().equals(ticketInfo.getDisplayTimeInfo().getDisplayTime())){
				m=m1;
				break;
			}
		}
		for(MovieInfo mi:this.movieInfoList){
			if (mi.getName().equals(ticketInfo.getMovieInfo().getName())) {
				mi.addTicketInfo(ticketInfo);
				break;
			}
		}
		for(Cinema c0:this.cinemaList){
			if(c0.getName().equals(ticketInfo.getCinemaInfo().getName())){
				c=c0;
				break;
			}
		}
		for(CinemaInfo ci:this.cinemaInfoList){
			if(ci.getName().equals(ticketInfo.getCinemaInfo().getName())){
				ci.addTicketInfo(ticketInfo);
				break;
			}
		}
		for(Customer c2:this.customerList){
			if(c2.getId().equals(ticketInfo.getUserInfo().getId())){
				c1=c2;
				break;
			}
		}
		for(UserInfo ui:this.userInfoList){
			if(ui.getRole().equals("Customer") && ui.getId().equals(ticketInfo.getUserInfo().getId())){
				ui.addTicketInfo(ticketInfo);
				break;
			}
		}
		for(DisplayTime d1:this.displayTimeList){
			if(d1.getTicketList().equals(ticketInfo.getDisplayTimeInfo().getDisplayTime())){
				d=d1;
				break;
			}
		}
		for(DisplayTimeInfo di:this.displayTimeInfoList){
			if(di.getDisplayTime().equals(ticketInfo.getDisplayTimeInfo().getDisplayTime())){
				di.addTicketInfo(ticketInfo);
				break;
			}
		}
		ticketInfo.getDisplayTimeInfo().addTicketInfo(ticketInfo);
		ticketInfo.getUserInfo().addTicketInfo(ticketInfo);
		ticketInfo.getMovieInfo().addTicketInfo(ticketInfo);
		ticketInfo.getCinemaInfo().addTicketInfo(ticketInfo);
		Ticket t=new Ticket(m,ticketInfo.getSeatCol(),ticketInfo.getSeatRow(),ticketInfo.getHallNum(),c,c1,d);
		m.addTicket(t);
		c.addTicket(t);
		c1.addTicket(t);
		d.addTicket(t);
		t.setActive(ticketInfo.getActive());
		this.ticketList.add(t);
		this.ticketInfoList.add(ticketInfo);
		//need to add things here, for example create new TicketInfo and add to this.ticketInfoList, and more
		session.saveOrUpdate(m);
		session.saveOrUpdate(c);
		session.saveOrUpdate(c1);
		session.saveOrUpdate(d);
		session.save(t);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		setTicketInfoList();
		return true;
	}

	private boolean addWorkerToDB(Worker worker) throws IOException {
		for (Worker w : this.workerList) {
			if (w.getName().equals(worker.getName())) {
				return false; //name exists already in db
			}
		}
		worker.setConnected(1);
		this.workerList.add(worker);
		Worker w = new Worker(worker);
		UserInfo u = new UserInfo(w.getId(), w.getRole(), w.getName(), w.getPassword());
		u.setConnected(1);
		u.setTicketInfoList(this.ticketInfoList);
		this.userInfoList.add(u);
		session.save(w);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		return true;
	}

	private boolean addCustomerToDB(UserInfo userInfo) throws IOException {
		setUserInfoList();
		setCinemaInfoList();
		setMovieInfoList();
		setTicketInfoList();
		for (Customer c : this.customerList) {
			if (c.getId().equals(userInfo.getId())) {
				return false; //name exists already in db
			}
		}
		userInfo.setConnected(1);
		boolean found = false;
		for (CinemaInfo ci : userInfo.getCinemaInfoList()) {
			found = false;
			for (CinemaInfo cinemaInfo : this.cinemaInfoList) {
				if (cinemaInfo.getName().equals(ci.getName())) {
					found=true;
					break;
				}
			}
			if (!found) {
				this.cinemaInfoList.add(ci);
			}
			found=false;
			for(Cinema cinema:this.cinemaList){
				if(cinema.getName().equals(ci.getName())){
					found=true;
					break;
				}
			}
			if(!found){
					Cinema cx=new Cinema(ci.getName(),ci.getNumHalls());
					this.cinemaList.add(cx);
					session.save(cx);
					session.flush();
					session.getTransaction().commit();
					session.beginTransaction();
			}
		}
		for (TicketInfo ti : userInfo.getTicketInfoList()) {
			found = false;
			for (TicketInfo ticketInfo : this.ticketInfoList) {
				if (ticketInfo.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.ticketInfoList.add(ti);
			}
			found = false;
			for (Ticket ticket : this.ticketList) {
				if (ticket.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				Ticket tx=new Ticket(getMovieByTitleFromDB(ti.getMovieInfo().getName()) , ti.getSeatCol(),ti.getSeatRow(),ti.getHallNum(), getCinemaByNameFromDB(ti.getCinemaInfo().getName()), getCustomerByIdFromDB(ti.getUserInfo().getId()),getDisplayTimeFromDB(ti.getDisplayTimeInfo().getDisplayTime()));
				this.ticketList.add(tx);
				session.save(tx);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
			}


		}
		Customer c=new Customer(userInfo);
		this.customerList.add(c);
		this.userInfoList.add(userInfo);
		session.save(c);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		return true;
	}

	//this func handles adding movie object to movie table
	private boolean addMovieToDB(MovieInfo movieInfo) throws IOException {
		setUserInfoList();
		setCinemaInfoList();
		setMovieInfoList();
		setTicketInfoList();
		setDisplayTimeInfoList();
		//check if movie is already in movies, no error message sent back cuz not needed
		for (MovieInfo m : this.movieInfoList) {
			if (m.getName().equals(movieInfo.getName())) {
				//if the movie already exists in db, just dont do anything
				return false;
			}
		}
		boolean found=false;
		for (CinemaInfo ci : movieInfo.getCinemaInfoList()) {
			found = false;
			for (CinemaInfo cinemaInfo : this.cinemaInfoList) {
				if (cinemaInfo.getName().equals(ci.getName())) {
					found=true;
					break;
				}
			}
			if (!found) {
				this.cinemaInfoList.add(ci);
			}
			found=false;
			for(Cinema cinema:this.cinemaList){
				if(cinema.getName().equals(ci.getName())){
					found=true;
					break;
				}
			}
			if(!found){
					Cinema cx=new Cinema(ci.getName(),ci.getNumHalls());
					this.cinemaList.add(cx);
					session.save(cx);
					session.flush();
					session.getTransaction().commit();
					session.beginTransaction();
			}
		}
		found=false;
		for (TicketInfo ti : movieInfo.getTicketInfoList()) {
			found = false;
			for (TicketInfo ticketInfo : this.ticketInfoList) {
				if (ticketInfo.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.ticketInfoList.add(ti);
			}
			found = false;
			for (Ticket ticket : this.ticketList) {
				if (ticket.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				Ticket tx=new Ticket(getMovieByTitleFromDB(ti.getMovieInfo().getName()) , ti.getSeatCol(),ti.getSeatRow(),ti.getHallNum(), getCinemaByNameFromDB(ti.getCinemaInfo().getName()), getCustomerByIdFromDB(ti.getUserInfo().getId()),getDisplayTimeFromDB(ti.getDisplayTimeInfo().getDisplayTime()));
				this.ticketList.add(tx);
				session.save(tx);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
			}
		}
		found=false;
		for (DisplayTimeInfo di : movieInfo.getDisplayTimeInfoList()) {
			found = false;
			for (DisplayTimeInfo displayTimeInfo : this.displayTimeInfoList) {
				if (displayTimeInfo.getDisplayTime().equals(di.getDisplayTime())) {
					found=true;
					break;
				}
			}
			if (!found) {
				this.displayTimeInfoList.add(di);
			}
			found=false;
			for(DisplayTimeInfo displayTimeInfo :this.displayTimeInfoList){
				if(displayTimeInfo.getDisplayTime().equals(di.getDisplayTime())){
					found=true;
					break;
				}
			}
			if(!found){
					DisplayTime cx=new DisplayTime(getDisplayTimeFromDB(di.getDisplayTime()));
					this.displayTimeList.add(cx);
					session.save(cx);
					session.flush();
					session.getTransaction().commit();
					session.beginTransaction();
			}
		}
		found=false;
		this.movieInfoList.add(movieInfo);
		Movie movie = new Movie(movieInfo);
		movie.setImageData(movieInfo.getImageData());
		session.save(movie);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
		return true;
	}

	public boolean addDisplayTimeToDB(DisplayTimeInfo displayTimeInfo){
		setUserInfoList();
		setCinemaInfoList();
		setMovieInfoList();
		setTicketInfoList();
		setDisplayTimeInfoList();
		//check if movie is already in movies, no error message sent back cuz not needed
		for (DisplayTime d : this.displayTimeList) {
			if (d.getDisplayTime().equals(displayTimeInfo.getDisplayTime())) {
				//if the movie already exists in db, just dont do anything
				return false;
			}
		}
		try{
		boolean found=false;
		for (TicketInfo ti : displayTimeInfo.getTicketInfoList()) {
			found = false;
			for (TicketInfo ticketInfo : this.ticketInfoList) {
				if (ticketInfo.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				this.ticketInfoList.add(ti);
			}
			found = false;
			for (Ticket ticket : this.ticketList) {
				if (ticket.toString().equals(ti.toString())) {
					found = true;
					break;
				}
			}
			if (!found) {
				Ticket tx=new Ticket(getMovieByTitleFromDB(ti.getMovieInfo().getName()) , ti.getSeatCol(),ti.getSeatRow(),ti.getHallNum(), getCinemaByNameFromDB(ti.getCinemaInfo().getName()), getCustomerByIdFromDB(ti.getUserInfo().getId()),getDisplayTimeFromDB(ti.getDisplayTimeInfo().getDisplayTime()));
				this.ticketList.add(tx);
				session.save(tx);
				session.flush();
				session.getTransaction().commit();
				session.beginTransaction();
			}
		}
		found=false;

		Movie mx=getMovieByTitleFromDB(displayTimeInfo.getMovieInfo().getName());
		Cinema c = getCinemaByNameFromDB(displayTimeInfo.getCinemaInfo().getName());

		DisplayTime d = new DisplayTime(displayTimeInfo.getDisplayTime().substring(0,17),mx , c);
			this.currMovie.addDisplayTime(d);
			mx.addDisplayTime(d);
			c.addDisplayTime(d);
			session.saveOrUpdate(c);
			session.saveOrUpdate(mx);
			this.displayTimeInfoList.add(displayTimeInfo);
			session.save(d);
			session.saveOrUpdate(this.currMovie);
			session.saveOrUpdate(c);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/*
	// updates display time for the current movie selected
	public void addDisplayTimeToDB(DisplayTimeInfo displayTimeInfo) {
		this.currMovie = getMovieByTitleFromDB(displayTimeInfo.getMovieInfo().getName());
		this.currMovieInfo = displayTimeInfo.getMovieInfo();
		Cinema c = getCinemaByNameFromDB(displayTimeInfo.getCinemaInfo().getName());
		try {
			for (DisplayTime d : this.currMovie.getDisplayTimes()) {
				if (displayTimeInfo.getDisplayTime().equals(d.getDisplayTime())) {
					//display time exists in movie so it also exists in db, nothing to do. 123
					return;
				}
			}
			//update movie's movieinfo that it has new displaytime :)
			for (MovieInfo mi : movieInfoList) {
				if (mi.getName().equals(displayTimeInfo.getMovieInfo().getName())) {
					mi.addDisplayTimeInfo(displayTimeInfo);
					break;
				}
			}
			//update movie itself (update displayTimeList)
			for (DisplayTime d : this.currMovie.getDisplayTimes()) {
				if (d.getDisplayTime().equals(displayTimeInfo.getDisplayTime())) { //movie already has displaytime for that movie in that cinema
					//nothing to do
					return;
				}
			}//got here so safe to add to movie new displayTime
			DisplayTime d = new DisplayTime(displayTimeInfo.getDisplayTime().substring(0,17), this.currMovie, c);
			this.currMovie.addDisplayTime(d);
			this.displayTimeInfoList.add(displayTimeInfo);
			c.addDisplayTime(d);
			session.save(d);
			session.saveOrUpdate(this.currMovie);
			session.saveOrUpdate(c);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public static void addMsgToDB(String text) throws Exception {
		Msg m = new Msg(text);
		session.save(m);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
	}

	private boolean removeCustomerFromDB(UserInfo userInfo){
		try{
			setUserInfoList();
			Customer customerToRemove=null;
			for(Customer c:this.customerList){
				if(c.getId().equals(userInfo.getId())){
					customerToRemove=c;
					break;
				}
			}
			if(customerToRemove==null){
				return false;
			}
			//disable all his tickets
			for(Ticket t:customerToRemove.getTicketList()){
				t.setActive(0);
				t.setCustomer(null);
				session.saveOrUpdate(t);
			}
			for(Cinema c: customerToRemove.getCinemaList()){
				c.removeCustomer(customerToRemove);
				customerToRemove.removeCinema(c);
				session.saveOrUpdate(c);
			}
			this.userInfoList=removeUserInfoWithId(this.userInfoList,userInfo.getId());
			session.saveOrUpdate(customerToRemove);
			session.delete(customerToRemove);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
			setMovieInfoList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	private boolean removeMovieFromDB(MovieInfo movieInfo) {
		try {
			setMovieInfoList();
			Movie movieToRemove = getMovieByTitleFromDB(movieInfo.getName());
			if(movieToRemove==null){
				return false;
			}
			//disable all tickets related to movie
			for(DisplayTime d:movieToRemove.getDisplayTimes()){
				removeDisplayTimeFromDB(getDisplayTimeInfoByDisplayTime(d.getDisplayTime()));
			}
			for (Ticket t : movieToRemove.getTicketList()) {
				t.setActive(0);
				t.setMovie(null);
				session.saveOrUpdate(t);
			}
			for(Cinema c:movieToRemove.getCinemaList()){
				c.removeMovie(movieToRemove);
				movieToRemove.removeCinema(c);
				session.saveOrUpdate(c);
			}
			this.movieInfoList=removeMovieInfoWithName(this.movieInfoList,movieToRemove.getName());
			session.saveOrUpdate(movieToRemove);
			session.delete(movieToRemove);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
			setMovieInfoList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	private void removeTicketFromDB(TicketInfo ticketInfo) {
		//the message sent from client comes with TicketInfo obj, so we pass ticketinfo.toString()
		//get the ticket itself by the tostring (preferably from db, can from list)
		//  and remove it from all displaytimes/cinemas/customers/movies
		//and then we can delete it from db
		try{
			setTicketInfoList();
			//remove ticket from it's movie, it's cinema, it's displaytime?, it's customer

			//first remove from server's lists
			this.movieInfoList=removeMovieInfoWithName(this.movieInfoList,ticketInfo.getMovieInfo().getName());
			this.cinemaInfoList=removeCinemaInfoWithName(this.cinemaInfoList,ticketInfo.getCinemaInfo().getName());
			this.userInfoList=removeUserInfoWithId(this.userInfoList,ticketInfo.getUserInfo().getId());
			this.displayTimeInfoList=removeDisplayTimeInfoWithString(this.displayTimeInfoList,ticketInfo.getDisplayTimeInfo().getDisplayTime());

			//get the ticket itself from db
			Ticket t=getTicketByToStringFromDB(ticketInfo.toString());
			//now remove from entities in db
			Movie m=getMovieByTitleFromDB(ticketInfo.getMovieInfo().getName());
			m.removeTicket(t);
			session.saveOrUpdate(m);
			Cinema c=getCinemaByNameFromDB(ticketInfo.getCinemaInfo().getName());
			c.removeTicket(t);
			session.saveOrUpdate(c);
			Customer c1=getCustomerByIdFromDB(ticketInfo.getUserInfo().getId());
			c1.removeTicket(t);
			session.saveOrUpdate(c1);
			DisplayTime d=getDisplayTimeFromDB(ticketInfo.getDisplayTimeInfo().getDisplayTime());
			d.removeTicket(t);
			session.saveOrUpdate(d);

			//clear ticket itself before dleteing, might help
			t.clear();
			session.saveOrUpdate(t);
			session.delete(t);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void removeDisplayTimeFromDB(DisplayTimeInfo displayTimeInfo) {
		try{
			this.currMovie=getMovieByTitleFromDB(displayTimeInfo.getMovieInfo().getName());
			Cinema c = getCinemaByNameFromDB(displayTimeInfo.getCinemaInfo().getName());

			DisplayTime d=getDisplayTimeFromDB(displayTimeInfo.getDisplayTime());

			this.currMovie.removeDisplayTime(d);

			c.removeDisplayTime(d);
			//remove remove the displaytime from all movieinfos
			for(MovieInfo mi:this.movieInfoList){
				if(mi.getName().equals(displayTimeInfo.getMovieInfo().getName())){
					mi.removeDisplayTimeInfo(displayTimeInfo);
					break;
				}
			}
			//remove the display time from all cinema infos
			for(CinemaInfo ci:this.cinemaInfoList){
				if(ci.getName().equals(displayTimeInfo.getCinemaInfo().getName())){
					ci.removeDisplayTimeInfo(displayTimeInfo);
					break;
				}
			}
			//remove displaytimeinfo from the server's displayTimeInfoList
			this.displayTimeInfoList=removeDisplayTimeInfoWithString(displayTimeInfoList,displayTimeInfo.getDisplayTime());
			//remove displaytime from the server's displayTimeList
			this.displayTimeList=removeDisplayTimeWithString(displayTimeList,displayTimeInfo.getDisplayTime());

			//once tickets have no displaytimes, they can be thrown away, but instead of throwing them away, we just set them to inactive
			for(TicketInfo ti: this.ticketInfoList) {
				if (ti.getDisplayTimeInfo().equals(displayTimeInfo.getDisplayTime())) {
					ti.setDisplayTimeInfo(null);
					ti.setActive(0);
				}
			}
			for(Ticket t:this.ticketList){
				if(t.getDisplayTime().getDisplayTime().equals(displayTimeInfo.getDisplayTime())){
					t.setActive(0);
					t.setDisplayTime(null);
					d.removeTicket(t);
					session.saveOrUpdate(t);
				}
			}
			session.saveOrUpdate(c);
			session.saveOrUpdate(this.currMovie);
			d.setMovie(null);
			d.setCinema(null);
			d.setTicketList(null);
			session.saveOrUpdate(d);
			session.delete(d);
			session.flush();
			session.getTransaction().commit();
			session.beginTransaction();
		}catch(Exception e){
			e.printStackTrace();
		}
}







	private void setCinemaInfoList(){
		this.cinemaInfoList=new ArrayList<>();
		this.cinemaList=getCinemasFromDB();
		for(Cinema c: this.cinemaList){
			List<MovieInfo> milist=new ArrayList<>();
			List<UserInfo> cilist=new ArrayList<>();
			List<TicketInfo> tilist= new ArrayList<>();
			for(Ticket t: c.getTicketList()){
				tilist.add(getTicketInfoByToString(t.toString()));
			}
			for(Movie m: c.getMovieList()){
				milist.add(getMovieInfoByTitle(m.getName()));
			}
			for(Customer c1: c.getCustomerList()){
				cilist.add(getUserInfoByID(c1.getId()));
			}
			CinemaInfo ci=new CinemaInfo(c.getName(),c.getNumHalls(),milist,cilist,tilist);
			this.cinemaInfoList.add(ci);
		}
	}
	private void setMovieInfoList(){
		this.movieList=getMoviesFromDB();
		this.movieInfoList=new ArrayList<>();
		byte[] movieImageByteArray=null;
		for(Movie m: this.movieList){
			MovieInfo mi=new MovieInfo(m.getName(),m.getReleaseDate(),m.getGenre(),m.getProducer(),m.getActors(),m.getSummary(),m.getStatus());
			mi.setImageData(m.getImageData());
			//add display times from movie to movieinfo
			for(DisplayTime d: m.getDisplayTimes()){
				mi.addDisplayTimeInfo(getDisplayTimeInfoByDisplayTime(d.getDisplayTime()));
			}
			for(Ticket t:m.getTicketList()){
				mi.addTicketInfo(getTicketInfoByToString(t.toString()));
			}
			for(Cinema c:m.getCinemaList()){
				mi.addCinemaInfo(getCinemaInfoByName(c.getName()));
			}
			this.movieInfoList.add(mi);
		}
	}
	private void setTicketInfoList(){
		this.ticketList=getTicketsFromDB();
		this.ticketInfoList=new ArrayList<>();
		for(Ticket t: this.ticketList){
			TicketInfo ti=new TicketInfo(getUserInfoByID(t.getCustomer().getId()),
					getMovieInfoByTitle(t.getMovie().getName()),
					getDisplayTimeInfoByDisplayTime(t.getDisplayTime().getDisplayTime()),
					getCinemaInfoByName(t.getCinema().getName()),
					t.getHallNum(),t.getSeatRow(),t.getSeatCol(),t.getPurchaseTime());
			this.ticketInfoList.add(ti);
		}
	}
	private void setDisplayTimeInfoList(){
		this.displayTimeList=getDisplayTimesFromDB();
		this.displayTimeInfoList=new ArrayList<>();
		for(DisplayTime d: this.displayTimeList){
			List<TicketInfo> list=new ArrayList<>();
			for(Ticket t:d.getTicketList()){
				list.add(getTicketInfoByToString(t.toString()));
			}
			DisplayTimeInfo di=new DisplayTimeInfo(d.getDisplayTime().substring(0,17),
					getMovieInfoByTitle(d.getMovie().getName()),
					getCinemaInfoByName(d.getCinema().getName()),list);

			this.displayTimeInfoList.add(di);
		}
	}
	private void setUserInfoList(){
		this.customerList=getCustomersFromDB();
		this.workerList=getWorkersFromDB();
		this.userInfoList=new ArrayList<>();
		UserInfo u=null;
		for(Customer c: customerList){
			u=new UserInfo(c.getId(),c.getName());
			u.setConnected(c.getConnected());
			userInfoList.add(u);
		}
		for(Worker w: workerList){
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
				message.setMovieInfo(this.currMovieInfo);
				client.sendToClient(message);
			}
			else if(request.startsWith("getTitles")){
				setMovieInfoList();
				message.setMovieInfoList(this.movieInfoList);
				message.setMessage("ListOfMovieInfos");
				client.sendToClient(message);
			}
			else if(request.startsWith("getCinemas")){
				setCinemaInfoList();
				message.setCinemaInfoList(this.cinemaInfoList);
				message.setMessage("cinemaInfoList");
				sendToAllClients(message); //maybe change to only send to specific client
			}
			else if (request.startsWith("addtime")) { //message holds DisplayInfo ,safe to assume movie and cinema already exist
				if(addDisplayTimeToDB(message.getDisplayTimeInfo())){
					message.setMessage("displaytime added");
				}
				else{
					message.setMessage("displaytime exists");
				}
				sendToAllClients(message);
			}
			else if(request.startsWith("removetime")){
				removeDisplayTimeFromDB(message.getDisplayTimeInfo());
				message.setMovieInfoList(this.movieInfoList);
				message.setMessage("updatedtimes");
				sendToAllClients(message);
			}
			else if(request.startsWith("getDisplayTimes")){
				setDisplayTimeInfoList();
				message.setDisplayTimeInfoList(this.displayTimeInfoList);
				message.setMessage("ListOfDisplayTimes");
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
				if(removeMovieFromDB(message.getMovieInfo())){
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
				if(connectCustomer(splitted[1])){
					message.setMessage("Customer succesfully connected");
					message.setUserInfoList(this.userInfoList);
					sendToAllClients(message);
				}
			}
			else if(request.startsWith("addCustomer")){
				UserInfo u=message.getUserInfo();
				u.setConnected(1);
				//this.userInfoList.add(u);
				if(addCustomerToDB(u)){
					message.setMessage("customer added");
				}
				else{
					message.setMessage("customer wasnt added");
				}
				sendToAllClients(message);

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
				if(disconnectCustomer(splitted[1])){
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
			setMovieInfoList(); //uncomment this when hibernate is on update mode
			setUserInfoList();
			setCinemaInfoList();
			setDisplayTimeInfoList();
			setTicketInfoList();
		} catch(Exception e) {
			if(session!=null){
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}//uncomment this section when running server for the first time
		/*try{
			generateData();
			setMovieInfoList();
			setUserInfoList();
			setCinemaInfoList();
			setTicketInfoList();
			setDisplayTimeInfoList();

		}catch(Exception e) {
			e.printStackTrace();
		}*/
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

	private List<DisplayTime> removeDisplayTimeWithString(List<DisplayTime> list, String str) {
		Iterator<DisplayTime> iterator = list.iterator();
		while (iterator.hasNext()) {
			DisplayTime obj = iterator.next();
			if (obj.getDisplayTime().equals(str)) {
				iterator.remove();
			}
		}
		return list;
	}
	private List<DisplayTimeInfo> removeDisplayTimeInfoWithString(List<DisplayTimeInfo> list, String str) {
		Iterator<DisplayTimeInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			DisplayTimeInfo obj = iterator.next();
			if (obj.getDisplayTime().equals(str)) {
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
	private List<UserInfo> removeUserInfoWithId(List<UserInfo> list, String idToRemove) {
		Iterator<UserInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			UserInfo obj = iterator.next();
			if (obj.getId().equals(idToRemove) && obj.getRole().equals("Customer")) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<UserInfo> removeUserInfoWithName(List<UserInfo> list, String nameToRemove) {
		Iterator<UserInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			UserInfo obj = iterator.next();
			if (obj.getName().equals(nameToRemove) && !obj.getRole().equals("Customer")) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<Ticket> removeTicketWithToString(List<Ticket> list, String toStringToRemove) {
		Iterator<Ticket> iterator = list.iterator();
		while (iterator.hasNext()) {
			Ticket obj = iterator.next();
			if (obj.toString().equals(toStringToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<TicketInfo> removeTicketInfoWithToString(List<TicketInfo> list, String toStringToRemove) {
		Iterator<TicketInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			TicketInfo obj = iterator.next();
			if (obj.toString().equals(toStringToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<Cinema> removeCinemaWithName(List<Cinema> list, String nameToRemove) {
		Iterator<Cinema> iterator = list.iterator();
		while (iterator.hasNext()) {
			Cinema obj = iterator.next();
			if (obj.getName().equals(nameToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	private List<CinemaInfo> removeCinemaInfoWithName(List<CinemaInfo> list, String nameToRemove) {
		Iterator<CinemaInfo> iterator = list.iterator();
		while (iterator.hasNext()) {
			CinemaInfo obj = iterator.next();
			if (obj.getName().equals(nameToRemove)) {
				iterator.remove();
			}
		}
	return list;
	}
	/*
	private MovieInfo convertMovieToMovieInfo(Movie movie){

	}
	private CinemaInfo convertCinemaToCinemaInfo(Cinema cinema){

	}
	private TicketInfo convertTicketToTicketInfo(Ticket ticket){

	}
	private UserInfo convertCustomerToUserInfo(Customer customer){

	}
	private UserInfo convertWorkerToUserInfo(Worker worker){

	}
	private DisplayTimeInfo convertDisplayTimeToDisplayTimeInfo(DisplayTime displayTime){

	}*/
}
