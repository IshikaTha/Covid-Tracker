package com.example.covidtracker.Model;

public class StateData {
    private String state, confirmed, cofirmed_new, active, death, death_new, recovered, recovered_new, lastUpdate;

    public StateData(String state, String confirmed, String cofirmed_new, String active, String death, String death_new, String recovered, String recovered_new, String lastUpdate) {
        this.state = state;
        this.confirmed = confirmed;
        this.cofirmed_new = cofirmed_new;
        this.active = active;
        this.death = death;
        this.death_new = death_new;
        this.recovered = recovered;
        this.recovered_new = recovered_new;
        this.lastUpdate = lastUpdate;
    }

    public String getState() {
        return state;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getCofirmed_new() {
        return cofirmed_new;
    }

    public String getActive() {
        return active;
    }

    public String getDeath() {
        return death;
    }

    public String getDeath_new() {
        return death_new;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getRecovered_new() {
        return recovered_new;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
