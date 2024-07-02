package server;


import entities.DisplayTime;
import entities.Message;
import entities.MovieInfo;
import ocsf.AbstractServer;
import ocsf.ConnectionToClient;
import ocsf.SubscribedClient;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

	private static SessionFactory getSessionFactory() throws HibernateException{
		Configuration configuration=new Configuration();

		configuration.addAnnotatedClass(Msg.class);
		configuration.addAnnotatedClass(Movie.class);

		ServiceRegistry serviceRegistry=new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}


	/* only use this function if protoype database gets deleted, it should not happen.
	public static void generateMovies() throws Exception {
			Movie movie1=new Movie("Margol","1973");
			movie1.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie1);
			Movie movie2=new Movie("The Boys","2018");
			movie2.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie2);
			Movie movie3=new Movie("Scary Movie 5","2012");
			movie3.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie3);
			Movie movie4=new Movie("House of Cards","2006");
			movie4.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie4);
			Movie movie5=new Movie("Pulp Fiction","1969");
			movie5.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie5);
			Movie movie6=new Movie("Automobiles","2024");
			movie6.getMovieInfo().setDisplayTimes(new ArrayList<DisplayTime>());
			session.save(movie6);
            session.flush();
        }
*/


	public static void addMsgToDB(String text) throws Exception{
		Msg m=new Msg(text);
		session.save(m);
		session.flush();
		session.getTransaction().commit();
		session.beginTransaction();
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
		}/*
		try{
			generateMovies();
		}catch(Exception e) {
			e.printStackTrace();
		}
		session.getTransaction().commit();
		session.beginTransaction();*/
	}

	private Movie getMovieByTitle(String title){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		Root<Movie> root= query.from(Movie.class);
		query.select(root).where(builder.equal(root.get("name"),title));
		List<Movie>result=session.createQuery(query).getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	private List<Movie> getMovies(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Movie> query=builder.createQuery(Movie.class);
		query.from(Movie.class);
		List<Movie> movies=session.createQuery(query).getResultList();
		return movies;
	}
	private List<Msg> getMsgs(){
		CriteriaBuilder builder=session.getCriteriaBuilder();
		CriteriaQuery<Msg> query=builder.createQuery(Msg.class);
		query.from(Msg.class);
		List<Msg> msgs=session.createQuery(query).getResultList();
		return msgs;
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
			else if(request.startsWith("automobiles")){
				Movie movie=getMovieByTitle("Automobiles");
				MovieInfo movieInfo=movie.getMovieInfo();
				System.out.println("I have the movie "+movieInfo.getName()+" ,will try to send to client now");
				try {
					client.sendToClient(movieInfo);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			else {
				addMsgToDB(request);
				StringBuilder s=new StringBuilder();
				List<Msg> msgs=getMsgs();
				for(Msg msg1 : msgs){
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

	private String getAllDisplayTimes(List<DisplayTime> displayTimes){
        StringBuilder sb=new StringBuilder();
        for (DisplayTime displayTime : displayTimes) {
            sb.append(displayTime.toString());
        }
        return sb.toString();
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

	public void sendToAllClients(Message message) {
		try {
			for (SubscribedClient SubscribedClient : SubscribersList) {
				SubscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
