package br.ufrj.caronae.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import br.ufrj.caronae.App;

public class Ride extends SugarRecord<Ride> {
    @SerializedName("myzone")
    private String zone;
    private String neighborhood;
    private String place;
    private String route;
    @SerializedName("mydate")
    private String date;
    private String slots;
    @SerializedName("mytime")
    private String time;
    private String hub;
    private String description;
    @SerializedName("week_days")
    private String weekDays;
    @SerializedName("repeats_until")
    private String repeatsUntil;
    private boolean going, routine;
    private int dbId;

    public Ride() {
    }

    public Ride(String zone, String neighborhood, String place, String route, String date, String time, String slots, String hub, String description, boolean going, boolean routine, String weekDays, String repeatsUntil) {
        this.zone = zone;
        this.neighborhood = neighborhood;
        this.place = place;
        this.route = route;
        this.date = date;
        this.time = time;
        this.slots = slots;
        this.hub = hub;
        this.description = description;
        this.going = going;
        this.routine = routine;
        this.weekDays = weekDays;
        this.repeatsUntil = repeatsUntil;
    }

    public Ride(Ride ride) {
        zone = ride.getZone();
        neighborhood = ride.getNeighborhood();
        place = ride.getPlace();
        route = ride.getRoute();
        date = App.formatBadDateWithYear(ride.getDate());
        time = ride.getTime();
        slots = ride.getSlots();
        hub = ride.getHub();
        description = ride.getDescription();
        going = ride.isGoing();
        routine = ride.isRoutine();
        weekDays = ride.getWeekDays();
        repeatsUntil = ride.getRepeatsUntil();
        dbId = ride.getId().intValue();
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGoing() {
        return going;
    }

    public void setGoing(boolean going) {
        this.going = going;
    }

    public boolean isRoutine() {
        return routine;
    }

    public void setRoutine(boolean routine) {
        this.routine = routine;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getRepeatsUntil() {
        return repeatsUntil;
    }

    public void setRepeatsUntil(String repeatsUntil) {
        this.repeatsUntil = repeatsUntil;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
}
