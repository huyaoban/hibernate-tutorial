package com.huyaoban.hibernate.hbm;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

public class EventManager {

	public static void main(String[] args) {
		EventManager mgr = new EventManager();
		
		if(args != null && args.length > 0) {
			if(args[0].equals("event")) {
				mgr.createAndStoreEvent("My Event", new Date());
			} else if(args[0].equals("listEvents")) {
				List<Event> events = mgr.listEvents();
				for(Event e : events) {
					System.out.println("Event: " + e.getTitle() + ", Time: " + e.getDate());
				}
			} else if(args[0].equals("person")) {
				mgr.createAndStorePerson("Jerry", "Hu", 30);
			} else if(args[0].equals("associateEvent")) {
				mgr.addPersonToEvent(1L, 1L);
			} else if(args[0].equals("associateEventDetach")) {
				mgr.addPersonToEventDetached(1L, 3L);
			} else if(args[0].equals("addEmailToPerson")) {
				Long id = mgr.createAndStorePerson("Jacky", "Fan", 35);
				mgr.addEmailToPerson(id, "jerry.hu@oceanwing.com");
				mgr.addEmailToPerson(id, "huyaoban@163.com");
			}
		}

		HibernateUtil.shudown();
	}
	
	private Long createAndStoreEvent(String title, Date theDate) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		Event event = new Event();
		event.setTitle(title);
		event.setDate(theDate);
		
		session.save(event);
		session.getTransaction().commit();
		session.close();
		
		return event.getId();
	}
	
	private List<Event> listEvents() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		List<Event> result = session.createQuery("from Event").list();
		session.getTransaction().commit();
		session.close();
		
		return result;
	}
	
	private Long createAndStorePerson(String firstname, String lastname, int age) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		Person person = new Person();
		person.setFirstname(firstname);
		person.setLastname(lastname);
		person.setAge(age);
		
		session.save(person);
		session.getTransaction().commit();
		session.close();
		
		return person.getId();
	}
	
	private void addPersonToEvent(Long personId, Long eventId) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		Person person = (Person)session.load(Person.class, personId);
		Event event = (Event)session.load(Event.class, eventId);
		
		person.getEvents().add(event);
		
		session.getTransaction().commit();
		session.close();
	}
	
	private void addPersonToEventDetached(Long personId, Long eventId) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		Person person = (Person)session.createQuery("select p from Person p left join fetch p.events where p.id = :pid")
				.setParameter("pid", personId)
				.uniqueResult();
		Event event = (Event)session.load(Event.class, eventId);
		session.getTransaction().commit();
		session.close();
		
		person.getEvents().add(event);	//person and events are detached
		
		Session session2 = HibernateUtil.openSession();
		session2.beginTransaction();
		session2.update(person);	//reattachment of person
		session2.getTransaction().commit();
		session2.close();
	}

	private void addEmailToPerson(Long personId, String emailAddress) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		
		Person person = (Person)session.load(Person.class, personId);
		person.getEmailAddresses().add(emailAddress);
		
		session.getTransaction().commit();
		session.close();
	}
}
