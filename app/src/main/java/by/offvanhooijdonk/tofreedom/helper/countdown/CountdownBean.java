package by.offvanhooijdonk.tofreedom.helper.countdown;

/**
 * Created by Yahor_Fralou on 8/2/2017 6:07 PM.
 */

public class CountdownBean {
    public String year;
    public String month;
    public String day;
    public String hour;
    public String minute;
    public String second;

    public CountdownBean(String year, String month, String day, String hour, String minute, String second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public CountdownBean() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CountdownBean)) return false;

        CountdownBean cnt = (CountdownBean) obj;

        return this.year != null
                && this.month != null
                && this.day != null
                && this.hour != null
                && this.minute != null
                && this.second != null
                && this.year.equals(cnt.year)
                && this.month.equals(cnt.month)
                && this.day.equals(cnt.day)
                && this.hour.equals(cnt.hour)
                && this.minute.equals(cnt.minute)
                && this.second.equals(cnt.second);
    }

    @Override
    public String toString() {
        return "CountdownBean{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", second='" + second + '\'' +
                '}';
    }
}
