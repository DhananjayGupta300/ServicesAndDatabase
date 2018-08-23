// KeyGenerator.aidl
package dgupta25.cs478.uic.servicecommon;
import dgupta25.cs478.uic.service1.DailyCash;
//parcelable DailyCash;
// Declare any non-default types here with import statements

interface KeyGenerator {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //parcelable dgupta25.cs478.uic.service1.DailyCash;
    String getKey();
    boolean createDataBase();
  DailyCash[] dailyCash(String year, String month, String date, String noofdays);
}
