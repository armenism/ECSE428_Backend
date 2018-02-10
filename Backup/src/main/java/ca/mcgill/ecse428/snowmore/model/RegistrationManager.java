/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse428.snowmore.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 30 "../../../../../SnowMore.ump"
// line 54 "../../../../../SnowMore.ump"
public class RegistrationManager
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RegistrationManager Associations
  private List<Registration> registrations;
  private List<User> users;
  private List<Posting> postings;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RegistrationManager()
  {
    registrations = new ArrayList<Registration>();
    users = new ArrayList<User>();
    postings = new ArrayList<Posting>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public Registration getRegistration(int index)
  {
    Registration aRegistration = registrations.get(index);
    return aRegistration;
  }

  public List<Registration> getRegistrations()
  {
    List<Registration> newRegistrations = Collections.unmodifiableList(registrations);
    return newRegistrations;
  }

  public int numberOfRegistrations()
  {
    int number = registrations.size();
    return number;
  }

  public boolean hasRegistrations()
  {
    boolean has = registrations.size() > 0;
    return has;
  }

  public int indexOfRegistration(Registration aRegistration)
  {
    int index = registrations.indexOf(aRegistration);
    return index;
  }

  public User getUser(int index)
  {
    User aUser = users.get(index);
    return aUser;
  }

  public List<User> getUsers()
  {
    List<User> newUsers = Collections.unmodifiableList(users);
    return newUsers;
  }

  public int numberOfUsers()
  {
    int number = users.size();
    return number;
  }

  public boolean hasUsers()
  {
    boolean has = users.size() > 0;
    return has;
  }

  public int indexOfUser(User aUser)
  {
    int index = users.indexOf(aUser);
    return index;
  }

  public Posting getPosting(int index)
  {
    Posting aPosting = postings.get(index);
    return aPosting;
  }

  public List<Posting> getPostings()
  {
    List<Posting> newPostings = Collections.unmodifiableList(postings);
    return newPostings;
  }

  public int numberOfPostings()
  {
    int number = postings.size();
    return number;
  }

  public boolean hasPostings()
  {
    boolean has = postings.size() > 0;
    return has;
  }

  public int indexOfPosting(Posting aPosting)
  {
    int index = postings.indexOf(aPosting);
    return index;
  }

  public static int minimumNumberOfRegistrations()
  {
    return 0;
  }

  public boolean addRegistration(Registration aRegistration)
  {
    boolean wasAdded = false;
    if (registrations.contains(aRegistration)) { return false; }
    registrations.add(aRegistration);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRegistration(Registration aRegistration)
  {
    boolean wasRemoved = false;
    if (registrations.contains(aRegistration))
    {
      registrations.remove(aRegistration);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addRegistrationAt(Registration aRegistration, int index)
  {  
    boolean wasAdded = false;
    if(addRegistration(aRegistration))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegistrations()) { index = numberOfRegistrations() - 1; }
      registrations.remove(aRegistration);
      registrations.add(index, aRegistration);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRegistrationAt(Registration aRegistration, int index)
  {
    boolean wasAdded = false;
    if(registrations.contains(aRegistration))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegistrations()) { index = numberOfRegistrations() - 1; }
      registrations.remove(aRegistration);
      registrations.add(index, aRegistration);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRegistrationAt(aRegistration, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfUsers()
  {
    return 0;
  }

  public boolean addUser(User aUser)
  {
    boolean wasAdded = false;
    if (users.contains(aUser)) { return false; }
    users.add(aUser);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeUser(User aUser)
  {
    boolean wasRemoved = false;
    if (users.contains(aUser))
    {
      users.remove(aUser);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addUserAt(User aUser, int index)
  {  
    boolean wasAdded = false;
    if(addUser(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveUserAt(User aUser, int index)
  {
    boolean wasAdded = false;
    if(users.contains(aUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfUsers()) { index = numberOfUsers() - 1; }
      users.remove(aUser);
      users.add(index, aUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addUserAt(aUser, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfPostings()
  {
    return 0;
  }

  public boolean addPosting(Posting aPosting)
  {
    boolean wasAdded = false;
    if (postings.contains(aPosting)) { return false; }
    postings.add(aPosting);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePosting(Posting aPosting)
  {
    boolean wasRemoved = false;
    if (postings.contains(aPosting))
    {
      postings.remove(aPosting);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addPostingAt(Posting aPosting, int index)
  {  
    boolean wasAdded = false;
    if(addPosting(aPosting))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPostings()) { index = numberOfPostings() - 1; }
      postings.remove(aPosting);
      postings.add(index, aPosting);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePostingAt(Posting aPosting, int index)
  {
    boolean wasAdded = false;
    if(postings.contains(aPosting))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPostings()) { index = numberOfPostings() - 1; }
      postings.remove(aPosting);
      postings.add(index, aPosting);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPostingAt(aPosting, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    registrations.clear();
    users.clear();
    postings.clear();
  }

}