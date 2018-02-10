/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse428.snowmore.model;

// line 23 "../../../../../SnowMore.ump"
// line 47 "../../../../../SnowMore.ump"
public class Registration
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  private int id;

  //Registration Associations
  private User user;
  private Posting posting;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Registration(User aUser, Posting aPosting)
  {
    id = nextId++;
    if (!setUser(aUser))
    {
      throw new RuntimeException("Unable to create Registration due to aUser");
    }
    if (!setPosting(aPosting))
    {
      throw new RuntimeException("Unable to create Registration due to aPosting");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getId()
  {
    return id;
  }

  public User getUser()
  {
    return user;
  }

  public Posting getPosting()
  {
    return posting;
  }

  public boolean setUser(User aNewUser)
  {
    boolean wasSet = false;
    if (aNewUser != null)
    {
      user = aNewUser;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setPosting(Posting aNewPosting)
  {
    boolean wasSet = false;
    if (aNewPosting != null)
    {
      posting = aNewPosting;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    user = null;
    posting = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "posting = "+(getPosting()!=null?Integer.toHexString(System.identityHashCode(getPosting())):"null");
  }
}