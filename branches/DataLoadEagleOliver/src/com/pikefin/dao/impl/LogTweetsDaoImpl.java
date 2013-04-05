package com.pikefin.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.ErrorCode;
import com.pikefin.businessobjects.LogTweets;
import com.pikefin.dao.AbstractDao;
import com.pikefin.dao.inter.LogTweetsDao;
import com.pikefin.exceptions.GenericException;
/**
 * Have methods related to LogTweets operations
 * @author Amar_Deep_Singh
 *
 */
@Component
public class LogTweetsDaoImpl extends AbstractDao<LogTweets>
  implements LogTweetsDao {
  @Autowired
  private SessionFactory sessionFactory;
  public LogTweetsDaoImpl(SessionFactory sessionFactory) {
     super(LogTweets.class);
       this.sessionFactory=sessionFactory;
  }

  public LogTweetsDaoImpl() {
     super(LogTweets.class);
      }
  @Override
  /**
   * Saves the LogTweets information into the database, it runs into a transaction
   * @author Amar_Deep_Singh
   * @param takes the new instance of the LogTweets entity
   * @returns returns the persisted LogTweets instance
   */
  @Transactional(propagation=Propagation.REQUIRED)
  public LogTweets saveLogTweetsInfo(LogTweets columnEntity) throws GenericException {
    Session session;
    try{
      session=getOpenSession();
      columnEntity=super.save(columnEntity);

    }catch (HibernateException e) {
        throw new GenericException(ErrorCode.COULD_NOT_SAVE_LOG_TWEETS_DATA,e.getMessage(),e.getCause());
    }
    return columnEntity;
  }


  /**
   * Updates the LogTweets information into the database
   * @author Amar_Deep_Singh
   * @param takes the detached LogTweets entity
   * @returns returns the persisted LogTweets instance
   */
  @Override
  @Transactional(propagation=Propagation.REQUIRED)
  public LogTweets updateLogTweetsInfo(LogTweets columnEntity) throws GenericException {

    try{
      Session session=getOpenSession();
      super.update(columnEntity);
    }catch (HibernateException e) {
        throw new GenericException(ErrorCode.COULD_NOT_UPDATE_LOG_TWEETS_DATA,e.getMessage(),e.getCause());
    }
    return columnEntity;
  }

  @Override
  /**
   * Deleted the LogTweets information from the database
   * @author Amar_Deep_Singh
   * @param takes the detached LogTweets entity
   * @returns returns true if the LogTweets is deleted else return false
   * @throws GenericException
   */
  @Transactional(propagation=Propagation.REQUIRED)
  public Boolean deleteLogTweetsInfo(LogTweets columnEntity) throws GenericException {

    boolean result;
    try{
      Session session=getOpenSession();
     result= super.delete(columnEntity);
    }catch (Exception e) {
      throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_TWEETS_INFORMATION,e.getMessage(),e.getCause());
    }

    return result;
  }

  @Override
  /**
   * Deleted the LogTweets information from the database based on supplied LogTweetsID. It first loads the LogTweets then try to delete
   * @author Amar_Deep_Singh
   * @param takes the LogTweetsId of the LogTweets entity
   * @returns returns true if the LogTweets is deleted else return false
   * @throws GenericException
   */
  @Transactional(propagation=Propagation.REQUIRED)
  public Boolean deleteLogTweetsInfoById(Integer logTweetId) throws GenericException {
    LogTweets columnEntity=null;
    boolean result;
    try{
      Session session=getOpenSession();
      columnEntity=loadLogTweetsInfo(logTweetId);
      result=super.delete(columnEntity);

    }catch (Exception e) {
        throw new GenericException(ErrorCode.COULD_NOT_DELETE_LOG_TWEETS_INFORMATION,e.getMessage(),e.getCause());
    }
    return result;
  }

  @Override
  /**
   * Retrieve the LogTweets information from the database based on supplied LogTweetsID.
   * @author Amar_Deep_Singh
   * @param takes the LogTweetsId of the LogTweets entity
   * @returns returns LogTweets entity if the LogTweets is found else throws exception
   * @throws GenericException
   */
  @Transactional(propagation=Propagation.REQUIRED)
  public LogTweets loadLogTweetsInfo(Integer logTweetId) throws GenericException {
    LogTweets columnEntity;
    try{
      Session session=getOpenSession();
      columnEntity=super.find(logTweetId);
    }catch (Exception e) {
        throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_TWEETS_DATA_WITH_ID,e.getMessage(),e.getCause());
    }
    return columnEntity;
  }

  @Override
  /**
   * Retrieve the list of all LogTweets entities from the database.
   * @author Amar_Deep_Singh
   * @returns returns List<LogTweets>
   * @throws GenericException
   */
  @Transactional(propagation=Propagation.REQUIRED)
  public List<LogTweets> loadAllLogTweets() throws GenericException {
    List<LogTweets> logTweets=null;
    try{
      Session session=getOpenSession();
      Criteria criteria=session.createCriteria(LogTweets.class);
      logTweets=(List<LogTweets>)criteria.list();
    }catch (HibernateException e) {
        throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
    }catch (Exception e) {
        throw new GenericException(ErrorCode.COULD_NOT_LOAD_REQUIRED_DATA,e.getMessage() , e.getCause());
    }
    return logTweets;
  }

  @Override
  public SessionFactory getSessionFactory() {
    return this.sessionFactory;
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
  @Override
  public Session getOpenSession(){
    Session session;
    if(sessionFactory.getCurrentSession()!=null){
      session=sessionFactory.getCurrentSession();
    }else{
      session=sessionFactory.openSession();

    }
    return session;
  }

  @Override
  @Transactional(propagation=Propagation.REQUIRED)
  public Long getTweetCounts(Integer userId) throws GenericException {
    Long count=new Long(0);
    try{
      Session session=getOpenSession();
      Query query=session.createQuery("select count(c) from LogTweets c,Alert a where c.alert.alertId=a.alertId and  c.alert.alertUser.userId="+userId +" and (MINUTE(c.dateTime)/15)=(MINUTE(CURRENT_TIMESTAMP())/15)");
      count=(Long)query.uniqueResult();
      ApplicationSetting.getInstance().getStdoutwriter()
      .writeln("Total tweet counts for userid" + userId + " =" + count,
          Logs.STATUS1,"LTDI21");
    }catch (HibernateException e) {
        throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_TWEETS_COUNT,e.getMessage() , e.getCause());
    }catch (Exception e) {
        throw new GenericException(ErrorCode.COULD_NOT_LOAD_LOG_TWEETS_COUNT,e.getMessage() , e.getCause());
    }
    return count;
  }
}
