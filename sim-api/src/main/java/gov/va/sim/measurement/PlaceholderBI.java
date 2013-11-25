/*
 * United States Government Work
 *
 * Veterans Health Administration
 * US Department of Veterans Affairs
 *
 * US Federal Government Agencies are required
 * to release their works as Public Domain.
 *
 * http://www.copyright.gov/title17/92chap1.html#105
 */

package gov.va.sim.measurement;

/**
 * An extension of PointBI that allows the storage of known placeholders that need to be populated 
 * per patient.  The suggested implementation of this interface is to store the id_ of the placeholder
 * in the Number value field provided by the PointBI.
 * @author Dan Armbrust 
 */

public interface PlaceholderBI extends PointBI
{
   public enum PlaceholderConstant
   {
      //Don't change the order of these unless you intend to change the (auto)assigned identifiers.
      DOB("DOB"), 
      NOW("NOW"), 
      START_ACTIVE_SERVICE("start of active service"),
      END_ACTIVE_SERVICE("end of active service"), 
      PNCS_VALUE_FIELD("PNCS Value Field");
      
      private String niceName_;
      private long id_;
      
      private PlaceholderConstant(String niceName)
      {
         this.niceName_ = niceName;
         this.id_ = Long.MIN_VALUE + ordinal();
      }
      
      public long getId()
      {
         return id_;
      }
      
      public String getNiceName()
      {
         return niceName_;
      }
      
      public static PlaceholderConstant valueOf(long id)
      {
         for (PlaceholderConstant ph : PlaceholderConstant.values())
         {
            if (ph.getId() == id)
            {
               return ph;
            }
         }
         return null;
      }
   }

   public PlaceholderConstant getPlaceholder();
   public void setPlaceholder(PlaceholderConstant placeholder);
}
