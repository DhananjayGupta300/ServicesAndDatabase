//declare the package name
package dgupta25.cs478.uic.client;

//import required packages
import android.os.Parcelable;
import android.os.Parcel;



//daily cash class that implements parcelable interface
public class DailyCash implements Parcelable
{
    int mDay = 25 ;
    int mMonth = 4 ;
    int mYear = 2018 ;
    int mCash = 8988 ;
    String mDayOfWeek = "Wednesday" ;



    public DailyCash(Parcel in) {
        mDay = in.readInt() ;
        mMonth = in.readInt() ;
        mYear = in.readInt() ;
        mCash = in.readInt() ;
        mDayOfWeek = in.readString() ;
    }

    public DailyCash(int day, int month, int year, int cash, String DayOfWeek) {
        mDay = day;
        mMonth = month;
        mYear = year;
        mCash = cash;
        mDayOfWeek = DayOfWeek;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mDay);
        out.writeInt(mMonth) ;
        out.writeInt(mYear) ;
        out.writeInt(mCash) ;
        out.writeString(mDayOfWeek) ;
    }

    public static final Creator<DailyCash> CREATOR
            = new Creator<DailyCash>() {

        public DailyCash createFromParcel(Parcel in) {
            return new DailyCash(in) ;
        }

        public DailyCash[] newArray(int size) {
            return new DailyCash[size];
        }
    };

    public int describeContents()  {
        return 0 ;
    }

}
