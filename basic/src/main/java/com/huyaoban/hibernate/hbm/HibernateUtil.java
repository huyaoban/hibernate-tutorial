package com.huyaoban.hibernate.hbm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static Session session;
	
	static {
		//创建Configuration对象，用于读取hibernate.cfg.xml，并完成初始化
		Configuration config = new Configuration();
		config.configure();
		sessionFactory = config.buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Session openSession() {
		session = sessionFactory.openSession();
		return session;
	}
	
	public static void closeSession(Session session) {
		if(null != session) {
			session.close();
		}
	}
	
	public static void shudown() {
		getSessionFactory().close();
	}

}
