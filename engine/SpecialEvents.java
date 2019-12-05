package engine;
import java.util.ArrayList;

public class SpecialEvents {
 private String date;
 private String showName;
 
 public SpecialEvents(String showName)
 {
   this.showName = showName;
 }
 public SpecialEvents(String showName, String date)
 {
   this.showName = showName;
   this.date = date;
 }
 public String getshowName() {
  return showName;
 }
 public void setShowName(String showName) {
  this.showName = showName;
 }
 public String getDate() {
  return date;
 }
 public void setdate(String date) {
  this.date = date;
 }
}